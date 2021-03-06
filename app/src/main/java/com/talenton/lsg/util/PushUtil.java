package com.talenton.lsg.util;

import android.text.TextUtils;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.talenton.lsg.base.AppConfig;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.receiver.LSGPushReceiver;
import com.talenton.lsg.server.MineServer;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushActivity;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushReceiver;
import com.tencent.android.tpush.service.XGPushService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PushUtil {
	// 信鸽推送（add）
	public static final String ADD_XINGE_PUSH_DATA = "add_xinge_push_data";

	// 信鸽推送（delete）
	public static final String DELETE_XINGE_PUSH_DATA = "delete_xinge_push_data";
	
	private static int connFailCount = 0;

	private static long trytime = 10 * 1000; //注册失败尝试重试时间

	private static ExecutorService threadPool = Executors.newSingleThreadExecutor();
	private static boolean isNeedTry = true;

	private static void initPush(Context context){
		// registerPush之前调用
		// 防止被安全软件等禁止掉组件，导致广播接收不了或service无法启动
		enableComponentIfNeeded(context, XGPushService.class.getName());
		enableComponentIfNeeded(context, XGPushReceiver.class.getName());
		// 2.30及以上版本
		enableComponentIfNeeded(context, XGPushActivity.class.getName());
		// CustomPushReceiver改为自己继承XGPushBaseReceiver的类，若有的话
		enableComponentIfNeeded(context, LSGPushReceiver.class.getName());
	}
	
	public static void start(Context context) {
		initPush(context);
		XGPushConfig.enableDebug(context, AppConfig.DEBUG);
		XGPushManager.registerPush(context);

	}
	
	//绑定账号（别名）注册
	public static void start(final Context context, final String account){
		if(TextUtils.isEmpty(account)){
			start(context);
			return;
		}
		isNeedTry = true;
		initPush(context);
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				while (isNeedTry) {
					registerPush(context, account);
					try {
						Thread.sleep(trytime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}
	
	private static void registerPush(final Context context, final String account){
		XGPushManager.registerPush(context, account, new XGIOperateCallback() {

			@Override
			public void onFail(Object data, int errCode, String msg) {
				// TODO Auto-generated method stub
				if (AppConfig.DEBUG){
					Toast.makeText(context, "信鸽推送注册失败", Toast.LENGTH_SHORT).show();
				}
				String err = String.format("register push fail. token:%s, errCode:%d ,msg:%s, account:%s", data, errCode, msg, account);
				Log.d("registerPush", err);
				connFailCount++;
				if (connFailCount >= 3) {
					trytime = 10 * 60 * 1000;
				}
				//XLTToast.makeText(context, err).show();
//				count++;
//				if(count++ < 3)
//					registerPush(context, account);
//				else
//					count = 0;
			}

			@Override
			public void onSuccess(Object data, int flag) {
				// TODO Auto-generated method stub
				if (AppConfig.DEBUG){
					Toast.makeText(context, "信鸽推送注册成功", Toast.LENGTH_LONG).show();
				}
				String message = String.format("register push sucess. token:%s, flag:%d, account:%s", data, flag, account);
				saveAndPostToken(data.toString());
				Log.d("registerPush", message);
				connFailCount = 0;
				isNeedTry = false;
			}
		});
	}

	private static void saveAndPostToken(String token) {
		String newToken = token;
		String oldToken = Preference.getInstance().getPushToken();
		MineServer.postPushToken(oldToken,newToken, UserServer.APP_ID);
		Preference.getInstance().savePushToken(token); //更新sharePreference的token
	}

	//取消绑定账号（别名）
	public static void stopAlias(Context context){
		isNeedTry = false;
		XGPushManager.registerPush(context, "*");//解绑后，该针对该账号的推送将失效
	}

	public static void stop(Context context) {
		isNeedTry = false;
		XGPushManager.unregisterPush(context);
	}

	public static void unReceiverPush(){
		isNeedTry = false;
	}

	// 启用被禁用组件方法
	private static void enableComponentIfNeeded(Context context,
			String componentName) {
		PackageManager pmManager = context.getPackageManager();
		if (pmManager != null) {
			ComponentName cnComponentName = new ComponentName(
					context.getPackageName(), componentName);
			int status = pmManager.getComponentEnabledSetting(cnComponentName);
			if (status != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
				pmManager.setComponentEnabledSetting(cnComponentName,
						PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
						PackageManager.DONT_KILL_APP);
			}
		}
	}
}
