package com.talenton.lsg.server.bean.school;

/**
 * @author zjh
 * @date 2016/5/9
 */
public class ReqAgeList implements IBaseReq{
    public static final String URL_AGE_LIST = "classroom.php?mod=agelist&cmdcode=110";

    @Override
    public String getReqParams() {
        return null;
    }

    @Override
    public String getReqUrl() {
        return URL_AGE_LIST;
    }
}
