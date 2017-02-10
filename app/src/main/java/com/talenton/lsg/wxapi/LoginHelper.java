package com.talenton.lsg.wxapi;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.base.XltApplication;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.ChannelUtil;
import com.talenton.lsg.base.util.NetWorkUtils;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.base.util.SystemUtil;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.bean.RspLogin;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;

/**
 * Created by ttt on 2016/4/7.
 */
public class LoginHelper {
    public final static int LOGIN_QQ = 2, LOGIN_WEIXIN = 1;
    public final static int ONLY_LOGIN_QQ = 12, ONLY_LOGIN_WEIXIN = 11;//只获取QQ微信登录参数，不用login
    protected static final int Timer1s = 60; //
    private int authtype = 2;
    private Tencent mTencent;
    private IWXAPI api;
    private SendAuth.Req req;
    private String access_token = "";
    private String openid = "";
    private String get_access_token = "";
    private String qq_nickname = "";
    private String qq_figureurl = "";
    private SoftReference<Activity> mContext;
    private XLTResponseCallback<Boolean> mLoginCallback;
    private IUiListener listener;

    public static LoginHelper getInstance(Activity context, int from) {
        LoginHelper helper = new LoginHelper();
        helper.authtype = from;
        helper.mContext = new SoftReference<Activity>(context);
        return helper;
    }

    public void login(XLTResponseCallback<Boolean> callback) {
        if (mContext.get() == null) {
            return;
        }
        mLoginCallback = callback;
        if ((authtype == LOGIN_QQ)||(authtype == ONLY_LOGIN_QQ)) {
            mTencent = Tencent.createInstance(UserServer.QQ_APP_ID, LsgApplication.getAppContext());
            doQQLogin();
        } else if ((authtype == LOGIN_WEIXIN)||(authtype == ONLY_LOGIN_WEIXIN)) {
            api = WXAPIFactory.createWXAPI(LsgApplication.getAppContext(), UserServer.WX_APP_ID, false);
            if (api != null && api.isWXAppInstalled()) {
                api.registerApp(UserServer.WX_APP_ID);
                doWXLogin();
            }else{
                XLTError errorData = new XLTError(1004, "您还未安装微信");
                mLoginCallback.onResponse(false, errorData);
            }
        }
    }

    private void doQQLogin() {
        if (mContext.get() == null) {
            return;
        }
        //。。。直接给调试数据
        listener = new IUiListener() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                XLTError errorData = new XLTError(1001, "QQ登录取消");
                mLoginCallback.onResponse(false, errorData);
            }

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
//                XLTToast.makeText(LsgApplication.getAppContext(), arg0.toString()).show();
                JSONObject bundle1 = (JSONObject) arg0;
                try {
                    openid = bundle1.getString("openid");
                    access_token = bundle1.getString("access_token");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    XLTError errorData = new XLTError(1001, "QQ登录失败");
                    mLoginCallback.onResponse(false, errorData);
                }
                getQQUserInfo();

            }

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                XLTError errorData = new XLTError(1002, "QQ登录出错");
                mLoginCallback.onResponse(false, errorData);
            }
        };
        String SCOPE = "get_user_info";
        mTencent.login(mContext.get(), SCOPE, listener);
    }

    private void doWXLogin() {

        req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";// 用于请求用户信息的作用域;
        req.state = "login_state"; // 自定义
        api.sendReq(req);
    }

    /**
     * 到此已经获得OpneID以及其他你想获得的内容了 QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
     * sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
     */
    void getQQUserInfo() {
        QQToken qqToken = mTencent.getQQToken();
        UserInfo info = new UserInfo(LsgApplication.getAppContext(), qqToken);
        // 这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
        info.getUserInfo(new BaseUiListener() {
            public void onComplete(final Object response) {
                // TODO Auto-generated method stub
                JSONObject bundle2 = (JSONObject) response;
                try {
                    int iret = bundle2.getInt("ret");
                    if (iret == 0) {
                        qq_nickname = bundle2.getString("nickname");
                        qq_figureurl = bundle2.getString("figureurl_qq_2");
                    }
                    //无论是否获得头像昵称，都可以继续登录
                    if(authtype == LOGIN_QQ){
                        UserServer.logIn(authtype, openid, qq_nickname, qq_figureurl, access_token, new XLTResponseCallback<RspLogin>() {
                            @Override
                            public void onResponse(RspLogin data, XLTError error) {
                                if (error == null) {
                                    mLoginCallback.onResponse(true, null);
                                } else {
                                    mLoginCallback.onResponse(false, error);
                                }
                            }
                        });
                    }
                    else if(authtype == ONLY_LOGIN_QQ){
                        JsonObject jo = new JsonObject();
                        jo.addProperty("authtype", LOGIN_QQ);
                        jo.addProperty("openid", openid);
                        jo.addProperty("nickname", qq_nickname);
                        jo.addProperty("avartarurl", qq_figureurl);
                        jo.addProperty("access_token", access_token);
                        jo.addProperty("mobile", UserServer.getCurrentUser().username);
                        jo.addProperty("appver", SystemUtil.getVersionName());
                        jo.addProperty("ostype", "android");
                        jo.addProperty("osver", android.os.Build.VERSION.RELEASE);
                        jo.addProperty("phonetype", android.os.Build.MODEL);
                        jo.addProperty("appid", UserServer.ANDROID_APP_ID);
                        jo.addProperty("appname", LsgApplication.getAppContext().getApplicationInfo()
                                .loadLabel(LsgApplication.getAppContext().getPackageManager()).toString());// "乐思购";
                        //jo.addProperty("token", Preference.getInstance().getToken(XltbgApplication.getAppContext()));
                        jo.addProperty("network", NetWorkUtils.getNetworkTypeName(XltApplication.getAppContext()));
                        jo.addProperty("appsource", ChannelUtil.getChannel());// AppConfig.CHANNEL_NAME;
                        XLTError errorData = new XLTError(1000, jo.toString());
                        mLoginCallback.onResponse(true, errorData);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    XLTError errorData = new XLTError(1003, "QQ登录失败");
                    mLoginCallback.onResponse(false, errorData);
                }

            }
        });
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                XLTError errorData = new XLTError(1001, "QQ登录失败");
                mLoginCallback.onResponse(false, errorData);
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                XLTError errorData = new XLTError(1001, "QQ登录失败");
                mLoginCallback.onResponse(false, errorData);
                return;
            }
            XLTToast.makeText(LsgApplication.getAppContext(), response.toString()).show();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            XLTError errorData = new XLTError(1001, "QQ登录取消");
            mLoginCallback.onResponse(false, errorData);
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            XLTError errorData = new XLTError(1002, "QQ登录出错");
            mLoginCallback.onResponse(false, errorData);
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject jsonObject) {
            //XLTToast.makeText()
            try {
                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                        && !TextUtils.isEmpty(openId)) {
                    mTencent.setAccessToken(token, expires);
                    mTencent.setOpenId(openId);

                    getQQUserInfo();
                }
            } catch(Exception e) {
                e.printStackTrace();
                XLTError errorData = new XLTError(1001, "QQ登录失败");
                mLoginCallback.onResponse(false, errorData);
            }
        }
    };

    public IUiListener getListener() {
        return listener;
    }
}