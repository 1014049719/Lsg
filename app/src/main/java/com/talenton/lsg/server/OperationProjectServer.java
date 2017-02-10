package com.talenton.lsg.server;

import com.google.gson.JsonObject;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.XLTResponseListener;
import com.talenton.lsg.server.bean.operation.RspHomeAdvertisement;

/**
 * Created by ttt on 2016/4/5.
 */
public class OperationProjectServer {
    /* 获得手机端启动广告数据接口 */
    public final static String HOME_ADVERTISEMENT_URL="user.php?mod=ad&cmdcode=15";

    public static void getHomeAdvertisement(int type, final XLTResponseCallback<RspHomeAdvertisement> listener) {
        JsonObject jo = new JsonObject();
        jo.addProperty("advPosition", type);
        OkHttpClientManager.getInstance().addGsonRequest(HOME_ADVERTISEMENT_URL, RspHomeAdvertisement.class,
          new XLTResponseListener<RspHomeAdvertisement>() {

            @Override
            public void onResponse(RspHomeAdvertisement responseData, XLTError errorData) {

                listener.onResponse(responseData, errorData);

            }
         }, jo.toString());
    }
}