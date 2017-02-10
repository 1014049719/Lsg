package com.talenton.lsg.event;

public class DeleteFeedsEvent {
	public String guid;
	public long tid;
	public long circleId;

	public DeleteFeedsEvent(long circleId, String guid, long tid){
		this.circleId = circleId;
		this.guid = guid;
		this.tid = tid;
	}
}