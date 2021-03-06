/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.talenton.lsg.base.server.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A request dispatch queue with a thread pool of dispatchers.
 *
 * Calling  will enqueue the given Request for dispatch,
 * resolving from either cache or network on a worker thread, and then
 * delivering a parsed response on the main thread.
 */
public class TaskQueue {

	/**
	 * The set of all requests currently being processed by this RequestQueue. A
	 * Request will be in this set if it is waiting in any queue or currently
	 * being processed by any dispatcher.
	 */
	private final Set<Long> mCurrentRequests = new HashSet<Long>();

	private final ArrayList<TaskBase> mWatingRetryQueue = new ArrayList<TaskBase>();
	/** The queue of requests that are actually going out to the network. */
	private final PriorityBlockingQueue<TaskBase> mNetworkQueue = new PriorityBlockingQueue<TaskBase>();

	/** Number of network request dispatcher threads to start. */
	private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 2;

	/** The network dispatchers. */
	private TaskDispatcher[] mDispatchers;

	/** The cache dispatcher. */
	private TimerDispatcher mCacheDispatcher;
	
	public long mUid;

	public TaskQueue(long uid) {
		this(DEFAULT_NETWORK_THREAD_POOL_SIZE);
		mUid = uid;
	}

	/**
	 * Creates the worker pool. Processing will not begin until {@link #start()}
	 * is called.
	 * @param threadPoolSize
	 *            Number of network dispatcher threads to create
	 */
	public TaskQueue(int threadPoolSize) {
		mDispatchers = new TaskDispatcher[threadPoolSize];
	}

	/**
	 * Starts the dispatchers in this queue.
	 */
	public void start() {
		stop(); // Make sure any currently running dispatchers are stopped.
		// Create the cache dispatcher and start it.
		mCacheDispatcher = new TimerDispatcher(mWatingRetryQueue, mNetworkQueue);
		mCacheDispatcher.start();

		// Create network dispatchers (and corresponding threads) up to the pool
		// size.
		for (int i = 0; i < mDispatchers.length; i++) {
			TaskDispatcher networkDispatcher = new TaskDispatcher(mNetworkQueue);
			mDispatchers[i] = networkDispatcher;
			networkDispatcher.start();
		}
	}

	/**
	 * Stops the cache and network dispatchers.
	 */
	public void stop() {
		if (mCacheDispatcher != null) {
			mCacheDispatcher.quit();
		}
		for (int i = 0; i < mDispatchers.length; i++) {
			if (mDispatchers[i] != null) {
				mDispatchers[i].quit();
			}
		}
	}

	/**
	 * Adds a Request to the dispatch queue.
	 * 
	 * @param request
	 *            The request to service
	 * @return The passed-in request
	 */
	TaskBase add(TaskBase request) {
		boolean modify = false;
		synchronized (mCurrentRequests) {
			modify = mCurrentRequests.add(request.id);
		}

		if (modify) {
			mNetworkQueue.add(request);
		}
		return request;
	}

	/**
	 * Adds a Request to the dispatch queue.
	 * 
	 * @param request
	 *            The request to continue
	 * @return
	 */
	void continueTask(TaskBase request) {
		synchronized (mCurrentRequests) {
			if (!mCurrentRequests.contains(request.id)) {
				return;
			}
		}
		mNetworkQueue.add(request);
		return;
	}

	/**
	 * Adds a Request to the dispatch queue.
	 * 
	 * @param request
	 *            The request to service
	 * @return
	 */
	void retry(TaskBase request) {
		synchronized (mCurrentRequests) {
			if (!mCurrentRequests.contains(request.id)) {
				return;
			}
		}
		synchronized (mWatingRetryQueue) {

			mWatingRetryQueue.add(request);
		}
		return;
	}

	void finish(TaskBase request) {
		// Remove from the set of requests currently being processed.
		if (request.status == TaskBase.STATUS_CANCELED) {
			synchronized (mWatingRetryQueue) {
				Iterator<TaskBase> it = mWatingRetryQueue.iterator();
				while (it.hasNext()) {
					TaskBase task = it.next();
					if (task.id == request.id) {
						it.remove();
						break;
					}
				}
			}
		}
		synchronized (mCurrentRequests) {
			mCurrentRequests.remove(request.id);
		}
	}
}
