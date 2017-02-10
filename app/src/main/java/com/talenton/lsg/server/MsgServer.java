package com.talenton.lsg.server;

import android.content.Context;

import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.XLTResponseListener;
import com.talenton.lsg.base.util.NetWorkUtils;
import com.talenton.lsg.server.bean.message.MsgData;
import com.talenton.lsg.server.bean.message.ReqMsg;
import com.talenton.lsg.server.bean.message.RspMsgList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @date 2016/5/11
 */
public class MsgServer{
    public static void getMsgList(final Context context,ReqMsg reqMsg, final XLTResponseListener<RspMsgList> listener){
        if (reqMsg == null){
            return;
        }
        final String url = reqMsg.getReqUrl();
        final String param = reqMsg.getReqParams();
        OkHttpClientManager.getInstance().addGsonRequest(url, RspMsgList.class, new XLTResponseListener<RspMsgList>() {
            @Override
            public void onResponse(RspMsgList responseData, XLTError errorData) {
                if (errorData == null){
                    //请求成功
                    BaseCacheServer.getInstance().saveOrReplaceHttpCacheData(url,param,OkHttpClientManager.getInstance().getmGson().toJson(responseData));
                }else {
                    if (!NetWorkUtils.isNetworkAvailable(context)){
                        RspMsgList cacheData = BaseCacheServer.getInstance().getRspCacheData(url,param,RspMsgList.class);
                        responseData = cacheData;
                        errorData = null;
                    }
                }
                if (listener != null){
                    listener.onResponse(responseData,errorData);
                }
            }
        },param);
    }
}
