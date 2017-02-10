package com.talenton.lsg;

import com.talenton.lsg.base.XltApplication;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.ui.WelcomeActivity;
import com.talenton.lsg.util.CacheManager;

/**
 * Created by ttt on 2016/3/28.
 */
public class LsgApplication extends XltApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        UserServer.setReLoginListener(new UserServer.OnReLoginListener() {
            @Override
            public void OnSwitchUser() {
                CacheManager.getInstance().reset();
            }

            @Override
            public void OnReLoginUi() {
                WelcomeActivity.startForReLogin();
            }
        });
    }
}