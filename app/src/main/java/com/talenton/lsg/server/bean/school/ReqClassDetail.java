package com.talenton.lsg.server.bean.school;

import com.talenton.lsg.base.AppConfig;
import com.talenton.lsg.base.util.JsonObjUtil;

/**
 * @author zjh
 * @date 2016/4/22
 */
public class ReqClassDetail implements IBaseReq{
    private final static String CLASS_DETAIL_URL = "classroom.php?mod=courselist&cmdcode=31";
    private final static String CLASS_SHARE_URL = "h5.php?mod=share_course&cmdcode=133&aid=";
    private long aid;

    public ReqClassDetail(long aid){
        this.aid = aid;
    }

    @Override
    public String getReqParams() {
        return JsonObjUtil.getInstance().addParam("aid",aid).toJsonString();
    }

    @Override
    public String getReqUrl() {
        return CLASS_DETAIL_URL;
    }

    public static String getShareUrl(long aid){
        return AppConfig.HOME_URL + CLASS_SHARE_URL + aid;
    }
}
