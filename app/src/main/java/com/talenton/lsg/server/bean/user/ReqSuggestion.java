package com.talenton.lsg.server.bean.user;

import android.content.Context;

import com.talenton.lsg.R;
import com.talenton.lsg.base.util.JsonObjUtil;
import com.talenton.lsg.base.util.NetWorkUtils;
import com.talenton.lsg.base.util.SystemUtil;
import com.talenton.lsg.server.bean.school.IBaseReq;

/**
 * @author zjh
 * @date 2016/5/13
 */
public class ReqSuggestion implements IBaseReq{
    private static final String URL = "my.php?mod=feedback&cmdcode=38";
    private String content; //反馈内容
    private String tel; //电话号码
    private Context context;


    public ReqSuggestion(Context context,String tel, String content) {
        this.context = context;
        this.tel = tel;
        this.content = content;
    }

    @Override
    public String getReqParams() {
        JsonObjUtil jsonObjUtil = JsonObjUtil.getInstance();
//        jsonObjUtil.addParam("appid","");
//        jsonObjUtil.addParam("appsource","360");
        jsonObjUtil.addParam("appname",context.getString(R.string.app_name));
        jsonObjUtil.addParam("ostype","Android");
        jsonObjUtil.addParam("osver",SystemUtil.getSystemVersion());
        jsonObjUtil.addParam("phonetype",SystemUtil.getSystemType());
        jsonObjUtil.addParam("token",SystemUtil.getDeviceUUID(context));
        jsonObjUtil.addParam("netWork", NetWorkUtils.getNetworkTypeName(context));
        jsonObjUtil.addParam("appver",SystemUtil.getVersionCode());
        jsonObjUtil.addParam("content",content);
        jsonObjUtil.addParam("tel",tel);
        return jsonObjUtil.toJsonString();
    }

    @Override
    public String getReqUrl() {
        return URL;
    }
}
