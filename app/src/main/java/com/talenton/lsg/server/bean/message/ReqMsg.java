package com.talenton.lsg.server.bean.message;

import com.talenton.lsg.base.util.JsonObjUtil;
import com.talenton.lsg.server.bean.school.IBaseReq;

/**
 * @author zjh
 * @date 2016/5/16
 */
public class ReqMsg implements IBaseReq{
    private final String URL_MSG = "user.php?mod=messagelist&cmdcode=113";
    private long dateline;
    private int type; //(0-最新10条，1-大于时间点的最新10条，2-小于时间点的最新10条)

    public static final int TYPE_LAST = 0;
    public static final int TYPE_GT = 1; //大于
    public static final int TYPE_LT = 2; //小于


    public ReqMsg() {
    }

    public ReqMsg(int type) {
        this.type = type;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getReqParams() {
        JsonObjUtil jsonObjUtil = JsonObjUtil.getInstance();
        if (dateline != 0){
            jsonObjUtil.addParam("dateline", dateline);
        }
        jsonObjUtil.addParam("type", type);
        return jsonObjUtil.toJsonString();
    }

    @Override
    public String getReqUrl() {
        return URL_MSG;
    }
}
