package com.talenton.lsg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.XltApplication;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.ui.user.LoginMainActivity;
import com.talenton.lsg.widget.HomeAdvertisementDialog;
import com.xlt.library.LibHelp;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final int REQUEST_LOGIN_INIT = 105;
    private static final int REQUEST_WELCOME_INIT = 106;
    private static final int REQUEST_PERSONAL_INIT = 107;

    private XLTResponseCallback<Boolean> mInitCallback = new XLTResponseCallback<Boolean>() {

        @Override
        public void onResponse(Boolean data, XLTError error) {
            if (error != null) {
                XLTToast.makeText(XltApplication.getAppContext(), error.getMesssage()).show();
            }
            doEnter();
        }

    };

    public static void startForReLogin() {
        Intent intent = new Intent(XltApplication.getAppContext(), LoginMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        XltApplication.getInstance().exit();
        XltApplication.getAppContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //MineServer.getRegions(0, null);
        HomeAdvertisementDialog.getHomeAdvertisement(HomeAdvertisementDialog.HOME_AD_INDEX);
        initNativeLib();


    }

    /**
     * 检查是否需要初始化SO,是进行初始化,否则进入相应页面
     */
    private void initNativeLib() {
        if (!LibHelp.getInstance().checkHasLibs(this)) {
            checkLibs(new LibHelp.OnInitListener() {
                @Override
                public void onSuccess() {
                    doInit();
                }

                @Override
                public void onError() {
                    doInit();
                }
            });
        } else {
            doInit();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    private void doInit() {
        if (AppInitController.firstLaunchInit(WelcomeActivity.this, REQUEST_WELCOME_INIT)
                && AppInitController.loginInit(WelcomeActivity.this, REQUEST_LOGIN_INIT, mInitCallback)
                ) {
            doEnter();
        }
    }

    private void doEnter() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_in:
                doInit();
                break;
            case R.id.iv_advertisement:
                doInit();
                break;
        }
    }

    /* 重写的结果返回方法，获取返回结果 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_WELCOME_INIT:
                doInit();
                break;
            case REQUEST_PERSONAL_INIT:
            case REQUEST_LOGIN_INIT:
                if (resultCode != Activity.RESULT_OK) {
                    Intent intent = new Intent(this, LoginMainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    doInit();
                }
                break;
        }
    }


    /**
     * 检测所需的库是否存在
     */
    private void checkLibs(final LibHelp.OnInitListener onInitListener) {
        LibHelp.getInstance().initializeLib(this, new LibHelp.OnInitListener() {
            @Override
            public void onSuccess() {
                AppLogger.d("初始化插件成功");
                if (onInitListener != null) {
                    onInitListener.onSuccess();
                }
                hideProgress();
            }

            @Override
            public void onError() {
                showShortToast("初始化失败");
                if (onInitListener != null) {
                    onInitListener.onError();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }
}
