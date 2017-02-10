package com.talenton.lsg.server.bean.user;

import com.talenton.lsg.base.util.JsonObjUtil;
import com.talenton.lsg.server.bean.school.IBaseReq;

/**
 * @author zjh
 * @date 2016/5/24
 */
public class ReqPostApproval implements IBaseReq{
    private static final String URL = "user.php?mod=examineschool&cmdcode=129";
    private long examineid;
    private int flag;

    public ReqPostApproval(int flag, long examineid) {
        this.flag = flag;
        this.examineid = examineid;
    }

    @Override
    public String getReqParams() {
        return JsonObjUtil.getInstance().addParam("examineid",examineid).addParam("flag",flag).toJsonString();
    }

    @Override
    public String getReqUrl() {
        return URL;
    }
}
