package com.talenton.lsg.receiver;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.base.util.SystemUtil;
import com.talenton.lsg.event.PushActionEvent;
import com.talenton.lsg.event.PushMessageEvent;
import com.talenton.lsg.server.bean.message.CustomMessageData;
import com.talenton.lsg.server.bean.user.NotificationBean;
import com.talenton.lsg.util.PushUtil;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class LSGPushReceiver extends XGPushBaseReceiver {

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
	}



	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
	}

	// 通知展示
	@SuppressLint("DefaultLocale")
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		AppLogger.d("push onNotifactionShowedResult ==> " + notifiShowedRlt.getCustomContent());
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult registerMessage) {
		if (context == null || registerMessage == null) {
			return;
		}
		if (errorCode == XGPushBaseReceiver.SUCCESS) {

		}
	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
	}

	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		if (context == null || message == null) {
			return;
		}
		AppLogger.d("push onTextMessage ==> " + message.getContent());
		AppLogger.d("push onTextMessage ==> " + message.getCustomContent());
		Preference.getInstance().setPushMsg(true);
		CustomMessageData data = new CustomMessageData();
		if (SystemUtil.isRunningForeground(context)){
			PushMessageEvent event = new PushMessageEvent(data);
			EventBus.getDefault().post(event);
			return;
		}
		String customContentString = message.getCustomContent();
		if (!TextUtils.isEmpty(customContentString)) {
			try{
				JSONObject json = new JSONObject(customContentString);
				if(json != null){
					int op = json.optInt("op");
					int notificationId = (int) (System.currentTimeMillis());
					data.op = op;
					data.url = json.optString("url");
					NotificationBean notificationBean = new NotificationBean(notificationId,message.getTitle(),message.getContent());
					data.opNotic(context,notificationBean);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
	}




}
