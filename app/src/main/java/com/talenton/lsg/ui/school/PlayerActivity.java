package com.talenton.lsg.ui.school;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.AppLogger;
import com.xlt.library.GiraffePlayer;
import com.xlt.library.LibHelp;

public class PlayerActivity extends BaseCompatActivity {
    public static final String PATH = "path";
    private GiraffePlayer player;

    /**
     *
     * @param context
     * @param path 播放地址
     */
    public static void startPlayerActivity(Context context,String path){
        Intent intent = new Intent(context,PlayerActivity.class);
        intent.putExtra(PATH,path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        findViewById(R.id.app_video_box).setVisibility(View.VISIBLE);
        initGiraffePlayerView();
        playVidieo();
    }

    private void playVidieo() {
        String path = getIntent().getStringExtra(PATH);
        AppLogger.d("path==>"+path);
        player.play(path);
    }

    /**
     * 初始化播放控件
     */
    private void initGiraffePlayerView() {
        player = new GiraffePlayer(this);
        player.setDefaultRetryTime(5 * 1000);
        player.setFullScreenOnly(false);
        player.setScaleType(GiraffePlayer.SCALETYPE_FITPARENT);
        player.setShowNavIcon(true);
        player.setIsShowTopInPortrait(true);
        player.setToggleScreenListen(new GiraffePlayer.ToggleScreenListen() {
            @Override
            public void onScreenLandscape() {

            }

            @Override
            public void onScreenPortrait() {

            }
        });
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                finish();
                //showShortToast(getString(R.string.player_text_complete_tips));
            }
        }).onError(new GiraffePlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                showShortToast(getString(R.string.player_text_error_tips));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (player != null){
            player.onStop();
        }
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }


    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

}
