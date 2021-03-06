package com.talenton.lsg.server.bean.school;

import com.talenton.lsg.base.util.JsonObjUtil;

/**
 * @author zjh
 * @date 2016/4/19
 */
public class ReqClassification implements IBaseReq{
    private final static String URL_CLASS_TYPE = "classroom.php?mod=catlist&cmdcode=30"; //获取课程分类
    public static final int LSG_SCHOOL = 1; //乐思构
    public static final int PUBLIC_SCHOOL = 2; //实验校
    public static final int FID_0 = 0;

    public ReqClassification(int cattype) {
        this.cattype = cattype;
        fid = FID_0;
    }

    private long fid = -1; //父节点id
    private int cattype = -1; //0-乐思构（默认）、1-公共校

    @Override
    public String getReqParams() {
        JsonObjUtil jsonObjUtil = JsonObjUtil.getInstance();
        if (fid != -1){
            jsonObjUtil.addParam("fid",fid);
        }
        if (cattype != -1){
            jsonObjUtil.addParam("cattype",cattype);
        }
        return jsonObjUtil.toJsonString();
    }

    @Override
    public String getReqUrl() {
        return URL_CLASS_TYPE;
    }

    public static String getUrlClassType() {
        return URL_CLASS_TYPE;
    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public int getCattype() {
        return cattype;
    }

    public void setCattype(int cattype) {
        this.cattype = cattype;
    }
}
