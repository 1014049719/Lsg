package com.talenton.lsg.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.okhttp.OkHttpUtils;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.FileUtil;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.event.LoginEvent;
import com.talenton.lsg.util.NotificationUtils;
import com.talenton.lsg.util.PushUtil;
import com.talenton.lsg.widget.SwitchView;
import com.talenton.lsg.widget.dialog.TipsDialog;

import org.greenrobot.eventbus.EventBus;

public class SettingActivity extends BaseCompatActivity implements View.OnClickListener {
    private LinearLayout ll_clear_data;
    private TextView tv_cache_size;
    private LinearLayout ll_about;
    private LinearLayout ll_help_center;
    private LinearLayout ll_exit;
    private SwitchView wifi_switch;
    private ClearTask clearTask;
    private TipsDialog exitDialog;

    public static void startSettingActivity(Context context){
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
        initView();
        fillData();
    }

    private void fillData() {
        tv_cache_size.setText(getCacheSize());
    }

    /**
     * 获取缓存大小
     * @return
     */
    private String getCacheSize(){
       return FileUtil.getFileSizeString(ImageLoaderManager.getInstance().getDiskCacheSize());
    }

    private void initView() {
        ll_clear_data = (LinearLayout) findViewById(R.id.ll_clear_data);
        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        ll_about = (LinearLayout) findViewById(R.id.ll_about);
        ll_help_center = (LinearLayout) findViewById(R.id.ll_help_center);
        ll_exit = (LinearLayout) findViewById(R.id.ll_exit);

        wifi_switch = (SwitchView) findViewById(R.id.wifi_switch);
        Boolean isOnlyWifi = Preference.getInstance().isOnlyWifi();
        if (isOnlyWifi){
            wifi_switch.setStatus(SwitchView.OPEN);
        }else {
            wifi_switch.setStatus(SwitchView.CLOSE);
        }
        wifi_switch.setOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void open() {
                AppLogger.d("open");
                Preference.getInstance().saveOnlyUserWifi(true);
            }

            @Override
            public void close() {
                AppLogger.d("close");
                Preference.getInstance().saveOnlyUserWifi(false);
            }
        });

        ll_clear_data.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_help_center.setOnClickListener(this);
        ll_exit.setOnClickListener(this);

        if (UserServer.getCurrentUser().getUid() == 0){
            ll_exit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_clear_data:
                //清除缓存 ==> 图片缓存
                if (ImageLoaderManager.getInstance().getDiskCacheSize() == 0){
                    showShortToast("未有缓存数据");
                    return;
                }else {
                    if (clearTask == null){
                        clearTask = new ClearTask(this);
                    }
                    clearTask.execute();
                }
                break;
            case R.id.ll_about:
                //关于
                AboutActivity.startAboutActivity(this);
                break;
            case R.id.ll_help_center:
                HelperListActivity.startHelperListActivity(this);
                break;
            case R.id.ll_exit:
                // 退出
                if (exitDialog == null){
                    exitDialog = new TipsDialog();
                    exitDialog.setMsg("确定退出登录?");
                    exitDialog.setOnClickRightBtnListener(new TipsDialog.OnClickRightBtnListener() {
                        @Override
                        public void onClick(View v) {
                            doExit();
                        }
                    });
                 }
                exitDialog.show(getSupportFragmentManager(),"exit");
                break;
        }
    }

    public void doExit(){
        PushUtil.stop(this); //退出推送
        NotificationUtils.removeAll(this); //清除所有通知
        UserServer.clearRspInfo(); //清楚用户信息
        OkHttpClientManager.getInstance().removeCookie(); //清除用户cookie信息
        Preference.getInstance().setGuideDone("mNeedLogIn");
        UserServer.mNeedLogIn = false;
        EventBus.getDefault().post(new LoginEvent(false));
        finish();
        //UserServer.reLogin(true);
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.mine_setting_title;
    }

    class ClearTask extends AsyncTask<Object,Integer,Boolean>{
        private ProgressDialog progressDialog;
        public ClearTask(Context context){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("清楚缓存中...");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true){
                showShortToast("清除完成");
                tv_cache_size.setText(getCacheSize());
            }
            progressDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            try{
                ImageLoaderManager.getInstance().removeDiskAndMemoryCache();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }


    }
}
