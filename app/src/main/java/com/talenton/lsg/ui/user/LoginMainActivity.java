package com.talenton.lsg.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.bean.Authority;
import com.talenton.lsg.base.server.bean.RspLogin;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.event.LoginEvent;
import com.talenton.lsg.event.WXLoginEvent;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.ui.MainActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class LoginMainActivity extends BaseCompatActivity implements View.OnClickListener{

    private static final int REQUEST_BIND_PHONE = 201;
    private static final int REQUEST_LOGIN = 202;
    private static final int REQUEST_PERSONAL_INIT = 203;

    public static final int REQUEST_RELOGIN = 1;

    // Wechat
    private Tencent mTencent;
    private IWXAPI api;
    private SendAuth.Req req;
    private ImageView mBack;
    private boolean mIsEventPost;
    LinearLayout mLayoutQQ, mLayoutWeChat, mLayoutPhone;

    public static void startLoginMainActivity(Context context, boolean isEventPost){
        Intent intent = new Intent(context, LoginMainActivity.class);
        intent.putExtra("isEventPost", isEventPost);
        //if (isEventPost){
         //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //}
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        mIsEventPost = this.getIntent().getBooleanExtra("isEventPost", false);
        mBack = (ImageView) findViewById(R.id.back);
        mBack.setOnClickListener(this);
        mLayoutQQ = (LinearLayout) findViewById(R.id.login_qq);
        mLayoutQQ.setOnClickListener(this);
        mLayoutWeChat = (LinearLayout) findViewById(R.id.login_weixin);
        mLayoutWeChat.setOnClickListener(this);
        mLayoutPhone = (LinearLayout) findViewById(R.id.login_phone);
        mLayoutPhone.setOnClickListener(this);
        findViewById(R.id.layout_stroll).setOnClickListener(this);
        if(mIsEventPost){
            mBack.setVisibility(View.VISIBLE);
        }else {
            mBack.setVisibility(View.GONE);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        /*
        if(UserServer.mNeedLogIn && !UserServer.getCurrentUser().checkUserInfo()) {
            Intent intent = new Intent(this, PerfectPersonalInfoActivity.class);
            startActivityForResult(intent, REQUEST_PERSONAL_INIT);
        }
        */
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loginQQ(){
        mTencent = Tencent.createInstance(UserServer.QQ_APP_ID, LsgApplication.getAppContext());
        String SCOPE = "get_user_info";
        mTencent.login(this, SCOPE, loginListener);
    }

    private void loginQQCallBack(String error){
        hideProgress();
        if(TextUtils.isEmpty(error)){
            if (BindPhoneActivity.startBindPhoneActivity(LoginMainActivity.this, REQUEST_BIND_PHONE)) {
                doEnter();
            }
        }else {
            XLTToast.makeText(this, error).show();
        }
    }

    private void loginWeChat(){
        api = WXAPIFactory.createWXAPI(LsgApplication.getAppContext(), UserServer.WX_APP_ID, false);
        if (api != null && api.isWXAppInstalled()) {
            api.registerApp(UserServer.WX_APP_ID);
            req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";// 用于请求用户信息的作用域;
            req.state = "login_state"; // 自定义
            api.sendReq(req);
        }else{
            mLayoutWeChat.setEnabled(true);
            XLTToast.makeText(this, "您还未安装微信").show();
        }
    }

    @Override
    public void onClick(View v) {
        hideSoftInput(v);
        switch (v.getId()){
            case R.id.login_qq:
                mLayoutQQ.setEnabled(false);
                loginQQ();
                break;
            case R.id.login_weixin:
                mLayoutWeChat.setEnabled(false);
                loginWeChat();
                break;
            case R.id.login_phone:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.layout_stroll:
                Preference.getInstance().setGuideDone("mNeedLogIn");
                UserServer.mNeedLogIn = false;
                doEnter();
                break;
        }

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutQQ.setEnabled(true);
                mLayoutWeChat.setEnabled(true);
            }
        }, 2000);
    }

    private void doEnter() {

        if(UserServer.mNeedLogIn && !UserServer.getCurrentUser().checkUserInfo()) {
            Intent intent = new Intent(this, PerfectPersonalInfoActivity.class);
            startActivityForResult(intent, REQUEST_PERSONAL_INIT);
        }else {
            MineServer.postPushToken(Preference.getInstance().getPushToken(),"",UserServer.APP_ID);
            if(mIsEventPost){
                EventBus.getDefault().post(new LoginEvent(true));
            }else {
                Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

    private void reLogin(){
        showProgress(R.string.login_logining);
        UserServer.reLogin(false, new XLTResponseCallback<RspLogin>() {
            @Override
            public void onResponse(RspLogin data, XLTError error) {
                // TODO Auto-generated mthod stub
                if(error == null && data != null){
                    doEnter();
                }else{

                }
                hideProgress();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_BIND_PHONE:
            case REQUEST_PERSONAL_INIT:
                if (resultCode == Activity.RESULT_OK){
                    doEnter();
                }
                break;
            case REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_OK){
                    if(data != null && data.getIntExtra("relogin", 0) == REQUEST_RELOGIN) {
                        reLogin();
                    }else{
                        doEnter();
                    }
                }
                break;
            case Constants.REQUEST_LOGIN:
            case Constants.REQUEST_APPBAR:
                Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WXLoginEvent event) {
        if (event == null)
            return;
        if ((event.ret) && (event.authtype == Authority.TYPE_WEIXIN)) {
            try {
                // 无论是否获得头像昵称，都可以继续登录
                UserServer.logIn(event.authtype, event.openid, event.nickname, event.figureurl, event.access_token,
                        new XLTResponseCallback<RspLogin>() {

                            @Override
                            public void onResponse(RspLogin data, XLTError error) {
                                if (error == null) {
                                    if (BindPhoneActivity.startBindPhoneActivity(LoginMainActivity.this, REQUEST_BIND_PHONE)) {
                                        doEnter();
                                    }
                                } else {
                                    XLTToast.makeText(LoginMainActivity.this, error.getMesssage()).show();
                                }
                            }
                        });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                XLTToast.makeText(LoginMainActivity.this, event.error).show();
            }
            //

        } else if (event.error != null && !TextUtils.isEmpty(event.error)) {
            XLTToast.makeText(LoginMainActivity.this, event.error).show();
        }
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                loginQQCallBack("QQ登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                loginQQCallBack("QQ登录失败");
                return;
            }
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            loginQQCallBack("QQ登录取消");
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            loginQQCallBack("QQ登录出错");
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
                    showProgress(R.string.main_processing, true);
                    getQQUserInfo(token, openId);
                }
            } catch(Exception e) {
                e.printStackTrace();
                loginQQCallBack("QQ登录失败");
            }
        }
    };

    private void getQQUserInfo(final String token, final String openId){
        if (mTencent != null && mTencent.isSessionValid()){
            UserInfo info = new UserInfo(LsgApplication.getAppContext(), mTencent.getQQToken());

            info.getUserInfo(new BaseUiListener() {
                public void onComplete(final Object response) {
                    String qq_nickname = "", qq_figureurl = "";
                    try {
                        JSONObject jsonObject = (JSONObject) response;
                        int iret = jsonObject.getInt("ret");
                        if (iret == 0) {
                            qq_nickname = jsonObject.getString("nickname");
                            qq_figureurl = jsonObject.getString("figureurl_qq_2");
                        }
                    } catch (Exception e) {

                    }
                    UserServer.logIn(Authority.TYPE_QQ, openId, qq_nickname, qq_figureurl, token, new XLTResponseCallback<RspLogin>() {
                        @Override
                        public void onResponse(RspLogin data, XLTError error) {
                            if (error == null) {
                                loginQQCallBack(null);
                            } else {
                                loginQQCallBack(error.getMesssage());
                            }
                        }
                    });

                }
            });
        }
    }
}
