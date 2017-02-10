package com.talenton.lsg.server.bean.school;

import com.talenton.lsg.base.util.JsonObjUtil;

/**
 * @author zjh
 * @date 2016/4/22
 */
public class ReqEvalute implements IBaseReq{
    public enum ReqEvaluteType{
        CREATE,LIST
    }

    private static final String CREATE_EVALUTE_URL = "classroom.php?mod=addcomment&cmdcode=34";
    private static final String LIST_EVALUTE_URL = "classroom.php?mod=getcomment&cmdcode=35";
    private long aid;
    private String message;
    private ReqEvaluteType reqEvaluteType;
    private int getcount;
    private int page;
    private float starcount;


    public ReqEvalute(ReqEvaluteType reqEvaluteType,long aid){
        this.aid = aid;
        this.reqEvaluteType = reqEvaluteType;
    }

    @Override
    public String getReqParams() {
        JsonObjUtil jsonObjUtil = JsonObjUtil.getInstance();
        switch (reqEvaluteType){
            case CREATE:
                jsonObjUtil.addParam("aid",aid);
                jsonObjUtil.addParam("message",message);
                jsonObjUtil.addParam("starcount",starcount);
                return jsonObjUtil.toJsonString();
            case LIST:
                jsonObjUtil.addParam("aid",aid);
                jsonObjUtil.addParam("getcount",getcount);
                jsonObjUtil.addParam("page",page);
                return jsonObjUtil.toJsonString();
        }
        return "";
    }

    @Override
    public String getReqUrl() {
        switch (reqEvaluteType){
            case CREATE:
                return CREATE_EVALUTE_URL;
            case LIST:
                return LIST_EVALUTE_URL;
        }
        return "";
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getGetcount() {
        return getcount;
    }

    public void setGetcount(int getcount) {
        this.getcount = getcount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public float getStarcount() {
        return starcount;
    }

    public void setStarcount(float starcount) {
        this.starcount = starcount;
    }
}
