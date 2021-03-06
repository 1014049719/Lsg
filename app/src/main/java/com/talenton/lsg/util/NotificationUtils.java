package com.talenton.lsg.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;

import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.user.NotificationBean;
import android.graphics.BitmapFactory;

/**
 * @author zjh
 * @date 2016/6/6
 */
public class NotificationUtils {
    public static void createNotification(Context context,NotificationBean notificationBean){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                        .setContentTitle(notificationBean.getTitle())
                        .setTicker(notificationBean.getTicker())
                        .setContentText(notificationBean.getContent());
// Creates an explicit intent for an Activity in your app
        Intent intent = null;
        if (notificationBean.getIntentClass() != null){
            intent = new Intent(context, notificationBean.getIntentClass());
        }
        if (notificationBean.getIntentExtr() != null){
            intent.putExtras(notificationBean.getIntentExtr());
        }

        AudioManager mAudioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
        if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            //处于静音
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }else {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationBean.getNotificationId(), intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationBean.getNotificationId(),mBuilder.build());
    }


    /**
     * 移除当个通知
     * @param context
     * @param notificationId
     */
    public static void removeNotification(Context context,int notificationId){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationId);
    }


    /**
     * 移除所有通知
     * @param context
     */
    public static void removeAll(Context context){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }


}
