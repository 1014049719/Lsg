package com.talenton.lsg.ui.discovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.server.DiscoveryServer;
import com.talenton.lsg.server.bean.discovery.LocatoinParam;
import com.talenton.lsg.server.bean.discovery.RspShopListType;
import com.talenton.lsg.server.bean.discovery.ShopList;
import com.talenton.lsg.ui.discovery.adapter.ShopListAdapter;
import com.talenton.lsg.util.UIHelper;
import com.talenton.lsg.widget.RegionSelectFragment;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2016/4/8.
 */
public class ShopListActivity extends BaseCompatActivity implements View.OnClickListener {

    //传入参数
    private String typeStr;

    //声明对象
    private ListView listView2;
    //适配器
    private ShopListAdapter adapter;
    //显示地区
    MenuItem item1;
    //已存储的位置
//    private String provinceStr;
//    private String cityStr;
//    private String areaStr;

    //位置
    private String provinceStr;
    private String cityStr;
    private String areaStr;
    //数据源
    private List<ShopList> list;
    //刷新控件
    private PullToRefreshListView mPullRefreshListView;
    private LoadingViewHolder mLoading;


    //外部进入接口
    public static void startShopListActivity(Context context, String typeStr){

        Intent intent=new Intent(context,ShopListActivity.class);
        intent.putExtra("typeStr",typeStr);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_list);
        super.onCreate(savedInstanceState);

        //传入参数
        typeStr=getIntent().getStringExtra("typeStr");
        if (mActionBarTitle!=null){
            mActionBarTitle.setText(typeStr);
        }

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        listView2 = mPullRefreshListView.getRefreshableView();

        //设置下拉刷新
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

                //下拉刷新
                loadNewData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {

            }
        });

        View loadingView = findViewById(R.id.loading_container);
        mLoading = new LoadingViewHolder(mPullRefreshListView, loadingView, this, this);
        //装载适配器
        adapter = new ShopListAdapter(LsgApplication.getAppContext(), new LinkedList<ShopList>());
        listView2.setAdapter(adapter);


        //取 已存的地区
//        provinceStr = Preference.getInstance().getShopLocation_province();
//        cityStr = Preference.getInstance().getShopLocation_city();
//        areaStr = Preference.getInstance().getShopLocation_area();
//        Log.d("存储的门店位置", String.format("%s%s%s", provinceStr, cityStr, areaStr));


        //实例化地区显示控件
        item1 = mToolbar.getMenu().getItem(0);
        item1.setTitle("全部地区");

//        if (provinceStr.equals("") && cityStr.equals("") && areaStr.equals("")) {
//
//            item1.setTitle("全部地区");
//        } else if (!areaStr.equals("")) {
//            item1.setTitle(areaStr);
//        } else if (areaStr.equals("") && !cityStr.equals("")) {
//            item1.setTitle(cityStr);
//        } else {
//            item1.setTitle(provinceStr);
//        }

        //设置item点击事件
        setItemClick();
        //请求网络数据
        getShopListData();
    }

    //设置item点击事件
    private void setItemClick() {

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AppLogger.d("点击了" + position);
                Intent intent = new Intent(LsgApplication.getAppContext(), com.talenton.lsg.ui.WebViewActivity.class);
                intent.putExtra("load_url",String.format("discover.php?mod=info&blogid=%s&type=%s", list.get(position-1).blogid,list.get(position-1).type ));
                startActivity(intent);
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_reload:
            case R.id.empty_action:
                getShopListData();
                break;
        }
    }

    //头部标题
    @Override
    protected int getTitleResourceId() {
        return R.string.shop_list_title;
    }

    //设置导航栏右边 可点击图标和文字
    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_select_location;
    }

    @Override
    protected void onRightClick(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_select_location:

                RegionSelectFragment fragment = RegionSelectFragment.newInstance(
                        new RegionSelectFragment.OnClickReginSelectListener() {

                            @Override
                            public void onData(String province, String city, String area) {

                                if (mToolbar != null && mToolbar.getMenu().size() > 0) {
                                    if (area.equals("") && city.equals("")) {

                                        Log.d("市区都为空", String.format("%s%s%s", province, city, area));
                                        item1.setTitle(province);
                                        //存位置 省
//                                        Preference.getInstance().setShopLocation_province(province);
//                                        Preference.getInstance().setShopLocation_city("");
//                                        Preference.getInstance().setShopLocation_area("");
                                        provinceStr=province;
                                        cityStr="";
                                        areaStr="";
                                    } else if (area.equals("")) {

                                        Log.d("区为空", String.format("%s%s%s", province, city, area));
                                        item1.setTitle(city);
                                        //存位置 省市
//                                        Preference.getInstance().setShopLocation_province(province);
//                                        Preference.getInstance().setShopLocation_city(city);
//                                        Preference.getInstance().setShopLocation_area("");
                                        provinceStr=province;
                                        cityStr=city;
                                        areaStr="";
                                    } else if (!area.equals("")) {
                                        Log.d("详细地区", String.format("%s%s%s", province, city, area));
                                        item1.setTitle(area);
                                        //存位置 省市区
//                                        Preference.getInstance().setShopLocation_province(province);
//                                        Preference.getInstance().setShopLocation_city(city);
//                                        Preference.getInstance().setShopLocation_area(area);
                                        provinceStr=province;
                                        cityStr=city;
                                        areaStr=area;
                                    }
                                }
                                //刷新数据
                                getShopListData();
                            }
                        });
                UIHelper.showDialog(this, fragment, "RegionSelectFragment");

                break;
        }
    }

    //获取门店列表
    private void getShopListData() {

        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);

        LocatoinParam locatoinParam = new LocatoinParam();
//        locatoinParam.resideprovince = Preference.getInstance().getShopLocation_province();
//        locatoinParam.residecity = Preference.getInstance().getShopLocation_city();
//        locatoinParam.residedist = Preference.getInstance().getShopLocation_area();
        locatoinParam.resideprovince =provinceStr;
        locatoinParam.residecity =cityStr;
        locatoinParam.residedist =areaStr;

        DiscoveryServer.getShopListData(locatoinParam,typeStr ,new XLTResponseCallback<RspShopListType>() {
            @Override
            public void onResponse(RspShopListType data, XLTError error) {

                if (error == null && data != null && data.list.size()>0) {
                   //填充数据
                    initData(data);
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_DATA);
                } else if (error != null) {
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
                } else {
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA);
                }
            }
        });
    }

    //下拉刷新
    private void loadNewData(){

        LocatoinParam locatoinParam = new LocatoinParam();
//        locatoinParam.resideprovince = Preference.getInstance().getShopLocation_province();
//        locatoinParam.residecity = Preference.getInstance().getShopLocation_city();
//        locatoinParam.residedist = Preference.getInstance().getShopLocation_area();
        locatoinParam.resideprovince =provinceStr;
        locatoinParam.residecity =cityStr;
        locatoinParam.residedist =areaStr;

        DiscoveryServer.getShopListData(locatoinParam,typeStr ,new XLTResponseCallback<RspShopListType>() {
            @Override
            public void onResponse(RspShopListType data, XLTError error) {

                mPullRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshListView.onRefreshComplete();
                    }
                },1000);
                if (error == null && data != null) {
                    initData(data);
                }
            }
        });
    }

    //填充数据
   private void initData(RspShopListType data){

       //先清空，再加载，通知刷新
       adapter.clear();
       adapter.setDatas(data.list);
       list = data.list;
       adapter.notifyDataSetChanged();
   }
}

