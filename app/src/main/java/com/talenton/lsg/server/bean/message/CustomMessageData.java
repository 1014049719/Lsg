package com.talenton.lsg.server.bean.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.base.AppConfig;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.bean.UserInfo;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.server.bean.user.NotificationBean;
import com.talenton.lsg.ui.WebViewActivity;
import com.talenton.lsg.ui.feed.AttentionRecordActivity;
import com.talenton.lsg.ui.feed.BabyFeedsActivity;
import com.talenton.lsg.ui.feed.CircleFeedsActivity;
import com.talenton.lsg.ui.feed.CircleFeedsDetailActivity;
import com.talenton.lsg.ui.feed.FeedsDetailActivity;
import com.talenton.lsg.ui.message.MsgActivity;
import com.talenton.lsg.ui.school.ClassDetailActivity;
import com.talenton.lsg.ui.shop.GoodsDetailActivity;
import com.talenton.lsg.ui.user.ApprovalActivity;
import com.talenton.lsg.ui.user.FamilyActivity;
import com.talenton.lsg.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Iterator;


public class CustomMessageData {
    /*
    填content string 内容
    type  int      消息类型 (根据推送表获得,通知或消息)
    style Style类型   (填1)
    action ClickAction类型  (ClickAtion::TYPE_ACTIVITY)
    custom MAP类型 ，"op":1,"url":"xxxxxx"   (读取omode字段和url字段)
    (op用于指示APP做相应的处理，关联URL中包含对应的处理
    op 和omode 处理方式 (用于指示APP做相应的处理，关联url中包含对应的处理 )
        1：打开URL页面 (url域内是url)
        2: 打开动态详情页面(url域内是动态guid)
        3: 不用处理
        4:打开亲友团页面(没用到url)
        5:跳转我[我的]TAB (没用到url)
        6.跳转到每日报告录入(url内是早餐、中餐、学习等8项内容) (后台推送任务生成)
        7.跳转到拍照提醒(没用到url)                            (后台推送任务生成)
        8.跳转到我的积分明细页面(没用到url)
        9.跳转到我的乐豆明细页面(没用到url)
        10.跳转到每日报告详情页面(url是宝宝UID和日期,格式为{“uid”:”xxx”,”d”:”yyyymmdd”})
     (url对应op需要的内容,如动态guid/URL/亲友ID等)
         11.下线通知，弹出提示框（url内容是 {"id":"最后登录设备号","os":"类型(1:iphone 2:android)","lt":"登录时间"} ）
         12.跳转到礼物商店页面（baobaouid 选填，后端提供时，圈主就跳自己宝宝，非圈主跳当前宝宝）
        13．跳转到亲友团页面(url域内是{“baobaouid”:”宝宝ID”,”uid”:”亲友ID”,”realname”:”亲友名称”})
        14．跳转到宝宝圈-宝宝页（url内容是:{"dtype":"动态类别","baobaouid":"","classuid":"","schoolid":""}dtype必填，各种ID选填。）
        15．跳转到成长书首页（baobaouid 选填，后端提供时，圈主就跳自己宝宝，非圈主跳当前宝宝）
        16．跳转到成长书预览（电子版）（baobaouid 选填，后端提供时，圈主就跳自己宝宝，非圈主跳当前宝宝）
        17．跳转到家园-育儿（育儿原生页面）
        18. 跳转到邀请亲页面（url内容是空）
        19. 跳转到邀请管理页面
        20. 跳转到【我要制作未来的你】页面（baobaouid 选填，后端不提供时，圈主就跳自己宝宝，非圈主跳当前宝宝）
        21. 跳转到【我的等级】（url内容是空）
        22. 跳转到【关注列表】（url内容是空）
        23. 跳转到【上传照片】（baobaouid 选填，后端不提供时，圈主就跳自己宝宝，非圈主跳当前宝宝）
        24. 跳转到【上传视频】（baobaouid 选填，后端不提供时，圈主就跳自己宝宝，非圈主跳当前宝宝)
        25. 跳转到【批量导入相片】（baobaouid 选填，后端不提供时，圈主就跳自己宝宝，非圈主跳当前宝宝)
        26.跳转到指定课程(URL内容为：{“aid”:1}必填，跳转到的课程ID)
        27.跳转到指定商品(URL内容为：{“shopid”:1}必填，跳转到的商品ID)
        28.跳转到指定圈子(URL内容为：{“circle_id”:1}必填，跳转到的圈子ID)
    */
    public static final int MSG_OPEN_URL = 1;
    public static final int MSG_OPEN_DYNAMIC = 2;
    public static final int MSG_NOT_DISPOSE = 3;
    public static final int MSG_OPEN_XX = 4;
    public static final int MSG_OPEN_MINE = 5;
    public static final int MSG_OPEN_DAILY_REPORT = 6;
    public static final int MSG_OPEN_PHOTO_REMINED = 7;
    public static final int MSG_OPEN_INTEGRAL = 8;
    public static final int MSG_OPEN_BEANFUN = 9;
    public static final int MSG_OPEN_DAILY_REPORT_DETAIL = 10;
    public static final int MSG_LOGOFF_NOTIFICATION = 11;
    public static final int MSG_OPEN_GIFT = 12;
    public static final int MSG_OPEN_FAMILY = 13;
    public static final int MSG_OPEN_BABY_DYNAMIC = 14;
    public static final int MSG_OPEN_EBOOK = 15;
    public static final int MSG_OPEN_PREVIEW_EBOOK = 16;
    public static final int MSG_OPEN_INVITED = 18;
    public static final int MSG_OPEN_INVITED_MANAGE = 19;
    public static final int MSG_OPEN_FUTURE = 20;
    public static final int MSG_OPEN_GROWTHINFO = 21;
    public static final int MSG_OPEN_MINE_ATTENTIONLIST = 22;
    public static final int MSG_OPEN_UPLOAD_IMAGE = 23;
    public static final int MSG_OPEN_UPLOAD_VIDEO = 24;
    public static final int MSG_OPEN_UPLOAD_IMAGE_GALLERY = 25;
    public static final int MSG_OPEN_CLASS = 26;
    public static final int MSG_OPEN_GOODS = 27;
    public static final int MSG_OPEN_CIRCLE = 28;
    public static final int MSG_OPEN_APPROVAL = 29; //申请加入学校列表
    public static final int MSG_OPEN_LIST_MSG = 30;

    private static final SimpleDateFormat mDateFormatYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public int op;
    public String url;
    //public String jsonData;

    public void opAction(final Context context) {
        if (op == 0 || op == MSG_NOT_DISPOSE) return;
        Intent intent;
        switch (op) {
            case MSG_OPEN_URL:
                if (!TextUtils.isEmpty(url)) {
                    if (!url.startsWith("http"))
                        url = AppConfig.HOME_URL + url;
                    com.talenton.lsg.ui.WebViewActivity.startWebViewActivity(context, url, false);
                }
                break;
            case MSG_OPEN_DYNAMIC:
                if (TextUtils.isEmpty(url)) return;
                goDynamic(context,null);
                break;
            case MSG_OPEN_FAMILY:
                if (TextUtils.isEmpty(url)) return;
                goFamily(context,null);
                break;
            case MSG_OPEN_MINE:
                break;
            case MSG_OPEN_DAILY_REPORT:
                break;
            case MSG_OPEN_PHOTO_REMINED:
                break;
            case MSG_OPEN_INTEGRAL:
                break;
            case MSG_OPEN_BEANFUN:
                break;
            case MSG_OPEN_DAILY_REPORT_DETAIL:
                break;
            case MSG_LOGOFF_NOTIFICATION:
                break;
            case MSG_OPEN_GIFT:
                break;
            case MSG_OPEN_BABY_DYNAMIC:
                break;
            case MSG_OPEN_EBOOK:
                break;
            case MSG_OPEN_PREVIEW_EBOOK:
                //if (!TextUtils.isEmpty(url))

                break;
            case MSG_OPEN_INVITED_MANAGE:
                AttentionRecordActivity.startAttentionRecordActivity(context, -1);
                break;
            case MSG_OPEN_GROWTHINFO:
                break;
            case MSG_OPEN_MINE_ATTENTIONLIST:
                break;
            case MSG_OPEN_UPLOAD_IMAGE:
            case MSG_OPEN_UPLOAD_VIDEO:
                break;
            case MSG_OPEN_UPLOAD_IMAGE_GALLERY:
                break;
            case MSG_OPEN_CLASS:
                if (TextUtils.isEmpty(url)) return;
                goClassDetail(context,null);
                break;
            case MSG_OPEN_GOODS:
                if (TextUtils.isEmpty(url)) return;
                goShopDetail(context,null);
                break;
            case MSG_OPEN_CIRCLE:
                if (TextUtils.isEmpty(url)) return;
                goBabyCircle(context,null);
                break;
            case MSG_OPEN_APPROVAL:
                goApproval(context,null);
                break;
            case MSG_OPEN_LIST_MSG:
                goListMsg(context,null);
                break;
        }
    }

    public void opNotic(final Context context,NotificationBean notificationBean){

        if (op == 0 || op == MSG_NOT_DISPOSE) return;
        switch (op) {
            case CustomMessageData.MSG_OPEN_URL:
                if (!TextUtils.isEmpty(url)) {
                    if (!url.startsWith("http://"))
                        url = AppConfig.HOME_URL + url;
                    Bundle bundle = new Bundle();
                    bundle.putString(WebViewActivity.LOAD_URL,url);
                    notificationBean.setIntentClass(WebViewActivity.class);
                    notificationBean.setIntentExtr(bundle);
                    NotificationUtils.createNotification(context, notificationBean);
                }
                break;
            case MSG_OPEN_DYNAMIC:
                if (TextUtils.isEmpty(url)) return;
                goDynamic(context,notificationBean);
                break;
            case MSG_OPEN_FAMILY:
                goFamily(context,notificationBean);
                break;
            case MSG_OPEN_MINE:
            case MSG_OPEN_DAILY_REPORT:
                break;
            case MSG_OPEN_PHOTO_REMINED:
                break;
            case MSG_OPEN_INTEGRAL:
                break;
            case MSG_OPEN_BEANFUN:
                break;
            case MSG_OPEN_DAILY_REPORT_DETAIL:
                break;
            case MSG_LOGOFF_NOTIFICATION:
                break;
            case MSG_OPEN_GIFT:
                break;
            case MSG_OPEN_BABY_DYNAMIC:
                break;
            case MSG_OPEN_EBOOK:
                break;
            case MSG_OPEN_PREVIEW_EBOOK:
                break;
            case MSG_OPEN_INVITED:
                break;
            case MSG_OPEN_INVITED_MANAGE:
                break;
            case MSG_OPEN_FUTURE:
                break;
            case MSG_OPEN_GROWTHINFO:
                break;
            case MSG_OPEN_MINE_ATTENTIONLIST:
                break;
            case MSG_OPEN_UPLOAD_IMAGE:
            case MSG_OPEN_UPLOAD_VIDEO:
                break;
            case MSG_OPEN_UPLOAD_IMAGE_GALLERY:
                break;
            case MSG_OPEN_CLASS:
                if (TextUtils.isEmpty(url)) return;
                goClassDetail(context, notificationBean);
                break;
            case MSG_OPEN_GOODS:
                if (TextUtils.isEmpty(url)) return;
                goShopDetail(context, notificationBean);
                break;
            case MSG_OPEN_CIRCLE:
                if (TextUtils.isEmpty(url)) return;
                goBabyCircle(context, notificationBean);
                break;
            case MSG_OPEN_APPROVAL:
                goApproval(context, notificationBean);
                break;
            case MSG_OPEN_LIST_MSG:
                goListMsg(context, notificationBean);
                break;
        }
    }


    private void goListMsg(Context context,NotificationBean notificationBean) {
        if (notificationBean == null){
            MsgActivity.startMsgActvity(context);
        }else {
            notificationBean.setIntentClass(ApprovalActivity.class);
            NotificationUtils.createNotification(context, notificationBean);
        }
    }

    private void goApproval(Context context,NotificationBean notificationBean) {
        try{
            JSONObject json = new JSONObject(url);
            long schoolId = json.optLong("schoolid", 0);
            if (schoolId == 0) return;
            if (notificationBean == null){
                ApprovalActivity.startApprovalActivity(context, schoolId);
            }else {
                Bundle bundle = new Bundle();
                bundle.putLong(ApprovalActivity.SCHOOL_ID,schoolId);
                notificationBean.setIntentExtr(bundle);
                notificationBean.setIntentClass(ApprovalActivity.class);
                NotificationUtils.createNotification(context,notificationBean);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void goFamily(Context context,NotificationBean notificationBean) {
        try{
            JSONObject json = new JSONObject(url);
            long id = json.optLong("circle_id", 0);
            if (id == 0) return;
            if (notificationBean == null){
                FamilyActivity.startFamilyActivity(context, id, 0);
            }else {
                Bundle bundle = new Bundle();
                bundle.putLong("circleId",id);
                notificationBean.setIntentExtr(bundle);
                notificationBean.setIntentClass(FamilyActivity.class);
                NotificationUtils.createNotification(context, notificationBean);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    private void goDynamic(Context context,NotificationBean  notificationBean) {
        try{
            JSONObject json = new JSONObject(url);
            long tid = json.optLong("tid", 0);
            int type = json.optInt("type", 0);
            if (tid == 0) return;
            PostToParam param = new PostToParam();
            Bundle bundle = new Bundle();
            param.tid = tid;
            if (type == 0){
                if (notificationBean == null){
                    FeedsDetailActivity.startFeedsDetailActivity(context, param);
                }else {
                    bundle.putParcelable("key_post_to",param);
                    notificationBean.setIntentExtr(bundle);
                    notificationBean.setIntentClass(FeedsDetailActivity.class);
                    NotificationUtils.createNotification(context,notificationBean);
                }
            }else if (type == 1){
                if (notificationBean == null){
                    CircleFeedsDetailActivity.startCircleFeedsDetailActivity(context, param);
                }else if (notificationBean != null){
                    bundle.putParcelable("key_post_to",param);
                    notificationBean.setIntentExtr(bundle);
                    notificationBean.setIntentClass(CircleFeedsDetailActivity.class);
                    NotificationUtils.createNotification(context,notificationBean);
                }
            }
        }catch (JSONException e){

        }
    }

    private void goBabyCircle(Context context,NotificationBean notificationBean) {
        if (TextUtils.isEmpty(url)) return;
        try{
            JSONObject json = new JSONObject(url);
            long id = json.optLong("circle_id", 0);
            int type = json.optInt("type", 0);
            long schoolId = 0;
            String schoolName = "";
            if (json.has("schoolid")){
                schoolId = json.getLong("schoolid");
            }
            if (json.has("schoolname")){
                schoolName = json.getString("schoolname");
            }
            if (schoolId != 0 && !TextUtils.isEmpty(schoolName)){
                if (UserServer.getCurrentUser().getBaobaodata() != null
                        && UserServer.getCurrentUser().getBaobaodata().baobaouid > 0){
                    //更新本地孩子学校信息
                    UserServer.getCurrentUser().getBaobaodata().school_name = schoolName;
                    UserServer.getCurrentUser().getBaobaodata().schoolid = schoolId;
                    UserServer.setRspLogin(UserServer.getRspLogin());
                }
            }
            if (id == 0) return;
            PostToParam param = new PostToParam();
            Bundle bundle = new Bundle();
            param.circleId = id;
            if (type == 0){
                param.circleType =  CircleListData.CIRCLE_TYPE_BABY;
                if (notificationBean == null){
                    BabyFeedsActivity.startBabyFeedsActivity(context, param);
                }else {
                    bundle.putParcelable("key_post_to",param);
                    notificationBean.setIntentClass(BabyFeedsActivity.class);
                    notificationBean.setIntentExtr(bundle);
                    NotificationUtils.createNotification(context,notificationBean);
                }
            }else if (type == 1){
                param.circleType = CircleListData.CIRCLE_TYPE_AGE;
                if (notificationBean == null){
                    CircleFeedsActivity.startCircleFeedsActivity(context, param);
                }else {
                    bundle.putParcelable("key_post_to",param);
                    notificationBean.setIntentClass(CircleFeedsActivity.class);
                    notificationBean.setIntentExtr(bundle);
                    NotificationUtils.createNotification(context,notificationBean);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * 跳转到课程详情页面
     * @param context
     */
    private void goClassDetail(Context context,NotificationBean notificationBean){
        try {
            long aid = 0;
            JSONObject jsonObject = new JSONObject(url);
            if (jsonObject.has("aid")){
                aid = jsonObject.getLong("aid");
                if (notificationBean == null){
                    ClassDetailActivity.startClassDetailActivity(context,aid,-1);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("aid",aid);
                    notificationBean.setIntentExtr(bundle);
                    notificationBean.setIntentClass(ClassDetailActivity.class);
                    NotificationUtils.createNotification(context,notificationBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goShopDetail(Context context,NotificationBean notificationBean){
        try {
            long shopId = 0;
            JSONObject jsonObject = new JSONObject(url);
            if (jsonObject.has("shopid")){
                shopId = jsonObject.getLong("shopid");
                if (notificationBean == null){
                    GoodsDetailActivity.startGoodsDetailActivity(context, shopId);
                }else {
                    notificationBean.setIntentClass(GoodsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("shopid", shopId);
                    notificationBean.setIntentExtr(bundle);
                    NotificationUtils.createNotification(context, notificationBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static WindowManager.LayoutParams getParam() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSPARENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;

        return params;
    }
}
