package com.talenton.lsg.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.bean.RspLogin;
import com.talenton.lsg.server.bean.operation.HomeAdvertisement;
import com.talenton.lsg.ui.user.LoginMainActivity;
import com.talenton.lsg.ui.user.PerfectPersonalInfoActivity;
import com.talenton.lsg.util.CacheManager;
import com.talenton.lsg.util.UIHelper;
import com.talenton.lsg.widget.HomeAdvertisementDialog;
import com.talenton.lsg.widget.SplashDialog;

import java.util.LinkedList;

/**
 * Created by ttt on 2016/4/5.
 * 打开app关注模块初始化controler
 */
public class AppInitController {

    /**
     * 第一次打开
     *
     * @param context
     * @param requestCode
     * @return false:不能进入下一步 true:允许进入下一步
     */
    public static boolean firstLaunchInit(AppCompatActivity context,int requestCode) {
        if (Preference.getInstance().isNeedGuide("splash")) {
            DialogFragment fr = SplashDialog.newInstance();
            UIHelper.showDialog(context, fr, "SplashDialog");
            Preference.getInstance().setGuideDone("splash");
            HomeAdvertisementDialog.getHomeAdvertisement(HomeAdvertisementDialog.HOME_AD_INDEX);
            return false;
        }
        return true;
    }

    /**
     * 登录模块初始化
     *
     * @param context
     * @param requestCode
     * @param InitCallback
     * @return false:不能进入下一步 true:允许进入下一步
     */
    public static boolean loginInit(AppCompatActivity context, int requestCode,
                                    final XLTResponseCallback<Boolean> InitCallback) {

        if (!UserServer.needLogIn()) {
            return true;
        }
        if (UserServer.needManuallyLogIn()) {
            // 需要手动登录
            LoginMainActivity.startLoginMainActivity(context, false);
            context.finish();
        } else {
            UserServer.reLogin(true, new XLTResponseCallback<RspLogin>() {
                @Override
                public void onResponse(RspLogin data, XLTError error) {
                    // TODO Auto-generated mthod stub
                    if (InitCallback == null) return;

                    if (!UserServer.needLogIn()) {
                        InitCallback.onResponse(true, null);
                    } else {
                        //UserServer.enfoceManuallyLogIn(true);
                        InitCallback.onResponse(false, error);
                    }
                }
            });
        }
        return false;
    }

    /**
     * 首页广告
     * @param context
     * @return false:不能进入下一步 true:允许进入下一步
     */
    public static boolean homeAdvertisementInit(AppCompatActivity context){
        if(Preference.getInstance().isNeedGuide("homead")){
            Preference.getInstance().setGuideDone("homead");
            return true;
        }
        LinkedList<HomeAdvertisement> ha = CacheManager.getInstance().getHomeAdvertisement();
        if(ha == null || ha.size() == 0 || ha.get(0) == null || ha.get(0).id == 0) return true;

        if(UserServer.mFromXgStart){
            UserServer.mFromXgStart = false;
            return true;
        }
        if(context.isFinishing()){
            return true;
        }
        DialogFragment fr = HomeAdvertisementDialog.newInstance();
        UIHelper.showDialogAllowingStateLoss(context, fr, "HomeAdvertisementDialog");
        return false;
    }

    /**
     * 个人信息完善初始化
     *
     * @param context
     * @param requestCode
     * @return false:不能进入下一步 true:允许进入下一步
     */
    public static boolean personalInit(Activity context, int requestCode) {
        if(!UserServer.getCurrentUser().checkUserInfo()) {
            Intent intent = new Intent(context, PerfectPersonalInfoActivity.class);
            context.startActivityForResult(intent, requestCode);
            return false;
        }
        return true;
    }
}
