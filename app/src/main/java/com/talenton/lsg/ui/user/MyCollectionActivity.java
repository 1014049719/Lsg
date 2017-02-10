package com.talenton.lsg.ui.user;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.widget.IndicationViewPager;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.ui.feed.FeedFragment;
import com.talenton.lsg.ui.school.MyFavoritesFragment;

public class MyCollectionActivity extends BaseCompatActivity {

    private IndicationViewPager indication_view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        indication_view_pager = (IndicationViewPager) findViewById(R.id.indication_view_pager);
        indication_view_pager.setData(getSupportFragmentManager()
                ,new String[]{"课程","话题"}
                ,new Fragment[]{MyFavoritesFragment.newInstance(), FeedFragment.newInstance(CircleListData.CIRCLE_TYPE_COLLECT)
        });

        //设置颜色
        indication_view_pager.getTabs().setIndicatorColor(Color.parseColor("#3983cc"));
        indication_view_pager.getTabs().setSelectedTextColor(Color.parseColor("#3983cc"));
    }

    //头部导航
    @Override
    protected int getTitleResourceId() {
        return R.string.collection_title;
    }
}
