package com.talenton.lsg.server.bean.school;

import com.talenton.lsg.base.util.JsonObjUtil;

/**
 * @author zjh
 * @date 2016/4/28
 */
public class ReqClassJoin implements IBaseReq{
    private static final String CLASS_JOIN_URL = "classroom.php?mod=join&cmdcode=90";
    private static final String CLASS_CANCEL_JOIN_URL = "classroom.php?mod=unjoin&cmdcode=111";
    private long aid;
    private int paytype = -1;
    private boolean isJoin; //是否为参加课程,true为是,false为取消参加

    public ReqClassJoin(long aid, boolean isJoin) {
        this.aid = aid;
        this.isJoin = isJoin;
    }

    public ReqClassJoin(long aid, int paytype, boolean isJoin) {
        this.aid = aid;
        this.paytype = paytype;
        this.isJoin = isJoin;
    }

    @Override
    public String getReqParams() {
        JsonObjUtil jsonObjUtil = JsonObjUtil.getInstance();
        jsonObjUtil.addParam("aid",aid);
        if (paytype != -1){
            jsonObjUtil.addParam("paytype",paytype);
        }
        return jsonObjUtil.toJsonString();
    }

    @Override
    public String getReqUrl() {
        if (isJoin){
            return CLASS_JOIN_URL;
        }else {
            return CLASS_CANCEL_JOIN_URL;
        }

    }
}
