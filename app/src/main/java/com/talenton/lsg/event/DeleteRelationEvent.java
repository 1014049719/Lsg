package com.talenton.lsg.event;

/**
 * Created by ttt on 2016/5/31.
 */
public class DeleteRelationEvent {
    public long memberId;

    public DeleteRelationEvent(long memberId){
        this.memberId = memberId;
    }
}
