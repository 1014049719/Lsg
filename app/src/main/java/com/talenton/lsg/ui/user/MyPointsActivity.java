package com.talenton.lsg.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.user.MyPoints;
import com.talenton.lsg.server.bean.user.MyPointsParam;
import com.talenton.lsg.server.bean.user.RspMyPoints;
import com.talenton.lsg.ui.user.adapter.MyPointsAdapter;

import java.util.LinkedList;

public class MyPointsActivity extends BaseCompatActivity implements View.OnClickListener{

    //声明对象
    private ListView listView;
    //头部积分
    private TextView pointscountTV;
    //头部规则问号

    //头部视图
    LinearLayout headViewLayout;
    //兑换按钮
    Button exchangeButton;
    //适配器
    private MyPointsAdapter adapter;

    //刷新相关
    private PullToRefreshListView pullToRefreshListView;
    private LoadingViewHolder loadingViewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);


        //实例化
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        listView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                //刷新
                loadNewData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        View view=findViewById(R.id.loading_container);
        loadingViewHolder=new LoadingViewHolder(pullToRefreshListView,view,this,this);

        //头部积分控件
        headViewLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.mypoints_head_cell, null);
        //兑换按钮
        exchangeButton = (Button) headViewLayout.findViewById(R.id.exchangeBT);
        exchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //跳转到兑换
                AppLogger.d("点击了兑换");
                Intent intent = new Intent(LsgApplication.getAppContext(), ShopPointsActivity.class);
                intent.putExtra("pointscount",pointscountTV.getText());
                startActivity(intent);
            }

        });
        listView.addHeaderView(headViewLayout);

        pointscountTV= (TextView) findViewById(R.id.pointscount_tv);
        //规则问号
        ImageView imageView= (ImageView) findViewById(R.id.guize_imageview);
        imageView.setOnClickListener(this);

        //装载适配器
        adapter = new MyPointsAdapter(LsgApplication.getAppContext(), new LinkedList<MyPoints>());
        listView.setAdapter(adapter);

        //请求数据
        getMyPointsData();
    }

    //请求数据
    protected void getMyPointsData() {

        loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_LOADING);

        MyPointsParam myPointsParam = new MyPointsParam();
        MineServer.getMyPointsData(myPointsParam, new XLTResponseCallback<RspMyPoints>() {
            @Override
            public void onResponse(RspMyPoints data, XLTError error) {

                if (error == null && data != null) {
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

    //刷新
    protected void loadNewData(){

        MyPointsParam myPointsParam = new MyPointsParam();
        MineServer.getMyPointsData(myPointsParam, new XLTResponseCallback<RspMyPoints>() {
            @Override
            public void onResponse(RspMyPoints data, XLTError error) {
                pullToRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pullToRefreshListView.onRefreshComplete();
                    }
                }, 1000);

                if (error == null && data != null) {
                    if (data.list.size() >= 0) {

                        //填充数据
                        initData(data);
                    }
                }
            }
        });

    }

    //填充数据
    protected void initData(RspMyPoints data){

        adapter.clear();
        adapter.setDatas(data.list);
        adapter.notifyDataSetChanged();
        pointscountTV.setText(data.pointscount);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reload:
            case R.id.empty_action:
                getMyPointsData();
                break;

            case R.id.guize_imageview:{
                HelperListActivity.startHelperListActivity(this);
            }break;
        }
    }

    //头部导航
    @Override
    protected int getTitleResourceId() {
        return R.string.point_title;
    }
}
