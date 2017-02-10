package com.talenton.lsg.event;

import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.task.TaskFeeds;

public class FeedsTaskEvent {
	public Feeds feeds;
	
	public FeedsTaskEvent(TaskFeeds data) {
		feeds = data.feeds;
		feeds.taskStatus = data.status;
		feeds.taskMessage = data.message;
		feeds.from = Feeds.FROM_TASK;
	}
}