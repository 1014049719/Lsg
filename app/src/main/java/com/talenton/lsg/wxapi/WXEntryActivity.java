package com.talenton.lsg.wxapi;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.okhttp.callback.StringCallback;
import com.talenton.lsg.event.WXLoginEvent;
import com.talenton.lsg.base.server.UserServer;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import okhttp3.Call;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

//	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
//	private Button gotoBtn, regBtn, launchBtn, checkBtn;
	// IWXAPI 是第三方app和微信通信的openapi接口
	public final static int LOGIN_WEIXIN = 1;
    private IWXAPI api;
	///
    private String access_token = "";
    private String openid = "";
//    private String get_access_token="";
    private int authtype = 1;
    private String wx_nickname = "";
    private String wx_figureurl = "";
//    private String get_nick_name="";
    ///
	private final static int WECHAT_GET_TOKEN = 1;
	private final static int WECHAT_GET_UID = 2;

	private ProgressDialog mProgressDlg;
	protected boolean isFinished = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
    	api = WXAPIFactory.createWXAPI(this, UserServer.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case 0://ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
	
			break;
		case 1://ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:

			if(resp instanceof SendAuth.Resp) {
				SendAuth.Resp sendAuthResp = (SendAuth.Resp) resp;// 用于分享时不要有这个，不能强转
				String code = sendAuthResp.code;
				getResult(code);
			}else {
				finish();
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			EventBus.getDefault().post(new WXLoginEvent(false,"微信登录取消",LOGIN_WEIXIN,"","","",""));
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			EventBus.getDefault().post(new WXLoginEvent(false,"微信登录拒绝",LOGIN_WEIXIN,"","","",""));
			finish();
			break;
		default:
			EventBus.getDefault().post(new WXLoginEvent(false,"微信登录不成功",LOGIN_WEIXIN,"","","",""));
			finish();
			break;
		}
	}
	/**
	 * 获取openid accessToken值用于后期操作
	 * @param code 请求码
	 */
	private void getResult(final String code) {

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ UserServer.WX_APP_ID
				+ "&secret="
				+ UserServer.WX_APP_SECRET
				+ "&code="
				+ code
				+ "&grant_type=authorization_code";
		showProgress("正在处理中...");
		OkHttpClientManager.getInstance().GetHttpsRequest(url, new MyStringCallBack(WECHAT_GET_TOKEN));

	}
	/**
	 * 获取用户唯一标识
	 * @param openId
	 * @param accessToken
	 */
	private void getUID(final String openId, final String accessToken) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ accessToken + "&openid=" + openId;
		OkHttpClientManager.getInstance().GetHttpsRequest(url, new MyStringCallBack(WECHAT_GET_UID));
	}

	private class MyStringCallBack extends StringCallback{

		int type;

		public MyStringCallBack(int type){
			this.type = type;
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			weChatCallBack(false,"微信登录不成功");
		}

		@Override
		public void onResponse(String response, int id) {
			if (TextUtils.isEmpty(response)){
				weChatCallBack(false, "微信登录不成功");
			}
			try{
				JSONObject jsonObject = new JSONObject(response);
				if(type == WECHAT_GET_TOKEN){
					openid = jsonObject.getString("openid").toString().trim();
					access_token = jsonObject.getString("access_token").toString().trim();
					getUID(openid, access_token);
				}else{
					wx_nickname = jsonObject.getString("nickname");
					wx_figureurl = jsonObject.getString("headimgurl");
					weChatCallBack(true,"微信登录成功");
				}

			}catch (Exception e) {
				e.printStackTrace();
				weChatCallBack(false,"微信登录不成功");
			}

		}
	}

	private void weChatCallBack(boolean isSuccess, String msg){
		hideProgress();
		if (isSuccess){
			EventBus.getDefault().post(new WXLoginEvent(true,"微信登录成功",LOGIN_WEIXIN,openid,access_token,wx_nickname,wx_figureurl));
		}else{
			EventBus.getDefault().post(new WXLoginEvent(false, "微信登录不成功", LOGIN_WEIXIN, "", "", "", ""));
		}
		finish();
	}

	private ProgressDialog progressDlg() {
		if (mProgressDlg == null) {
			mProgressDlg = new ProgressDialog(this);
			mProgressDlg.setCancelable(false);
		}

		return mProgressDlg;
	}

	public void showProgress(String msg){
		if (isFinished) {
			return;
		}
		progressDlg().setCancelable(true);
		progressDlg().setMessage(msg);
		progressDlg().show();
	}

	public void hideProgress() {
		if (isFinished) {
			return;
		}

		if (mProgressDlg != null) {
			if (mProgressDlg.isShowing()) {
				mProgressDlg.dismiss();
				mProgressDlg.setProgress(0);
			}
			mProgressDlg = null;
		}
	}

	@Override
	public void onDestroy() {
		isFinished = true;
		super.onDestroy();
	}

	@Override
	public void finish() {
		isFinished = true;
		super.finish();
	}
}