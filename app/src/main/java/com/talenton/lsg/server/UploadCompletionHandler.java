package com.talenton.lsg.server;

//import com.qiniu.android.http.ResponseInfo;
//import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

/**
 * Created by ttt on 2016/4/15.
 */
/*
public class UploadCompletionHandler implements UpCompletionHandler {

    OnUpCompletionListener mOnUpCompletionListener;

    public UploadCompletionHandler(OnUpCompletionListener listener){
        mOnUpCompletionListener = listener;
    }

    @Override
    public void complete(String key, ResponseInfo info, JSONObject response) {
        // TODO Auto-generated method stub
        if (info.isOK()) {
            mOnUpCompletionListener.complete(key, info, response);
        }else{
            if (info.statusCode == 401) { // token 过期，自动登陆
                UserServer.reLogin(false);
                return;
            }
            mOnUpCompletionListener.error(info.statusCode, info);
        }
    }

    public interface OnUpCompletionListener{
        public void complete(String key, ResponseInfo info, JSONObject response);
        public void error(int statusCode, ResponseInfo info);
    }
}
*/
