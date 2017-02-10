package com.talenton.lsg.event;

/**
 * Created by ttt on 2016/5/23.
 */
public class ModifyRelationEvent {
    public long memberId;
    public int atteintionType;
    public int gxid;
    public String gxname;

    public ModifyRelationEvent(long memberId, int atteintionType, int gxid, String gxname){
        this.memberId = memberId;
        this.atteintionType = atteintionType;
        this.gxid = gxid;
        this.gxname = gxname;
    }
}