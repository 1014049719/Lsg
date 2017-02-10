package com.talenton.lsg.server.bean.school;

import com.talenton.lsg.base.util.JsonObjUtil;

/**
 * @author zjh
 * @date 2016/4/28
 */
public class ReqPraise implements IBaseReq{
    private static final String PRAISE_URL = "classroom.php?mod=courselike&cmdcode=33";
    private long aid;
    private long cid;

    public ReqPraise(long aid) {
        this.aid = aid;
    }

    @Override
    public String getReqParams() {
        return JsonObjUtil.getInstance().addParam("aid",aid).addParam("cid",cid).toJsonString();
    }

    @Override
    public String getReqUrl() {
        return PRAISE_URL;
    }
}