package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.ui.feed.FeedFragment;

public class MyTopicActivity extends BaseCompatActivity {

    public static void startMyTopicActivity(Context context){
        context.startActivity(new Intent(context,MyTopicActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_topic);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_topic_container, FeedFragment.newInstance(CircleListData.CIRCLE_TYPE_MINE));
        transaction.commit();
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.mine_send_topice_title;
    }
}
