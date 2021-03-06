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
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import android.os.Process;

/**
 * Provides a thread for performing cache triage on a queue of requests.
 *
 * Requests added to the specified cache queue are resolved from cache. Any
 * deliverable response is posted back to the caller via a
 *  Cache misses and responses that require refresh are
 * enqueued on the specified network queue for processing by a
 *
 */
public class TimerDispatcher extends Thread {

	/** 等待重试的任务. */
	private final ArrayList<TaskBase> mWatingRetryQueue;

	/** The queue of requests going out to the network. */
	private final BlockingQueue<TaskBase> mNetworkQueue;

	/** Used for telling us to die. */
	private volatile boolean mQuit = false;

	/**
	 * Creates a new cache triage dispatcher thread. You must call
	 * {@link #start()} in order to begin processing.
	 *
	 * @param cacheQueue
	 *            Queue of incoming requests for triage
	 * @param networkQueue
	 *            Queue to post requests that require network to
	 *
	 */
	public TimerDispatcher(ArrayList<TaskBase> cacheQueue, BlockingQueue<TaskBase> networkQueue) {
		mWatingRetryQueue = cacheQueue;
		mNetworkQueue = networkQueue;
	}

	/**
	 * Forces this dispatcher to quit immediately. If any requests are still in
	 * the queue, they are not guaranteed to be processed.
	 */
	public void quit() {
		mQuit = true;
		interrupt();
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while (true) {
			try {
				Thread.sleep(TaskBase.RETRYS_INTERVAL);
				TaskBase task = null;
				Long now = System.currentTimeMillis();
				synchronized (mWatingRetryQueue) {
					Iterator<TaskBase> it = mWatingRetryQueue.iterator();
					while (it.hasNext()) {
						task = it.next();
						if (task.reTryTime <= now) {
							it.remove();
							break;
						}
					}
				}
				if (task != null && task.reTryTime <= now) {
					// 告诉执行线程执行去

					mNetworkQueue.put(task);
				}
			} catch (InterruptedException e) {
				// We may have been interrupted because it was time to quit.
				if (mQuit) {
					return;
				}
				continue;
			}
		}
	}
}
