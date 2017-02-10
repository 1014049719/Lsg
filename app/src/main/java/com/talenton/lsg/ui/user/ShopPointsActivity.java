package com.talenton.lsg.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.user.RspMyPoints;
import com.talenton.lsg.server.bean.user.RspShopPoints;
import com.talenton.lsg.server.bean.user.ShopPoints;
import com.talenton.lsg.server.bean.user.ShopPointsParam;
import com.talenton.lsg.ui.user.adapter.ShopPointsAdapter;

import java.util.LinkedList;

public class ShopPointsActivity extends BaseCompatActivity implements View.OnClickListener{

    //声明
    GridView gridView;
    //适配器
    ShopPointsAdapter adapter;

    //刷新相关
    private PullToRefreshGridView pullToRefreshGridView;
    private LoadingViewHolder loadingViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_points);


        //实例化
        pullToRefreshGridView= (PullToRefreshGridView) findViewById(R.id.pull_refresh_GridView);
        gridView = pullToRefreshGridView.getRefreshableView();
        //列数2
        gridView.setNumColumns(2);
        pullToRefreshGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                //下拉刷新
                loadNewData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }
        });
        //转圈控件
        View view=findViewById(R.id.loading_container);
        loadingViewHolder=new LoadingViewHolder(pullToRefreshGridView,view,this,this);

        //装载适配器
        adapter = new ShopPointsAdapter(LsgApplication.getAppContext(), new LinkedList<ShopPoints>());
        gridView.setAdapter(adapter);

        //头部积分
        TextView pointscountTV= (TextView) findViewById(R.id.pointscount_textView);
        pointscountTV.setText(getIntent().getStringExtra("pointscount"));
        //请求网络
        getShopPointsData();
    }

    //请求网络
    protected void getShopPointsData() {

        ShopPointsParam shopPointsParam = new ShopPointsParam();
        MineServer.getShopPointsData(shopPointsParam, new XLTResponseCallback<RspShopPoints>() {
            @Override
            public void onResponse(RspShopPoints data, XLTError error) {

                if (error == null && data != null && data.list.size()>0) {
                    //填充数据
                    initData(data);
                    loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_DATA);
                } else if (error != null) {
                    loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
                } else {
                    loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA);
                }
            }
        });
    }

    //下拉刷新
    protected void loadNewData(){

        ShopPointsParam shopPointsParam = new ShopPointsParam();
        MineServer.getShopPointsData(shopPointsParam, new XLTResponseCallback<RspShopPoints>() {
            @Override
            public void onResponse(RspShopPoints data, XLTError error) {

                pullToRefreshGridView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshGridView.onRefreshComplete();
                    }
                },1000);

                if (error == null && data != null && data.list.size()>0) {
                    //填充数据
                    initData(data);
                }
            }
        });
    }

    //填充数据
    protected void initData(RspShopPoints data){

        //先清空，再刷新
        adapter.clear();
        adapter.setDatas(data.list);
        //积分总值 赋值
        adapter.setPointscount(data.pointscount);
        AppLogger.d("积分值:" + data.pointscount);
        adapter.notifyDataSetChanged();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reload:
            case R.id.empty_action:
                getShopPointsData();
                break;
        }
    }

    //设置导航栏
    @Override
    protected int getTitleResourceId() {
        return R.string.shoppoints_title;
    }

}
