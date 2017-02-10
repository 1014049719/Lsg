package com.talenton.lsg.ui.discovery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.Preference;
import com.talenton.lsg.server.DiscoveryServer;
import com.talenton.lsg.server.bean.discovery.Discovery;
import com.talenton.lsg.server.bean.discovery.NumData;
import com.talenton.lsg.server.bean.discovery.RspDiscoverNumType;
import com.talenton.lsg.ui.discovery.adapter.MyAdapter;
import com.talenton.lsg.ui.user.MyCollectionActivity;
import com.talenton.lsg.ui.user.MyOrderActivity;
import com.talenton.lsg.ui.user.MyPointsActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/4/6.
 */
public class DiscoveryFragment extends BaseCompatFragment {


    ListView listView;
    //声明适配器
    private MyAdapter adapter;
    //数据
    private String data[]={"门店","活动","资讯","实验校"};
    //图片
    private int imageData[]={R.mipmap.shop,R.mipmap.activity,R.mipmap.globe,R.mipmap.school};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //请求数据
        getNumData();

        View v = inflater.inflate(R.layout.fragment_discovery, container, false);
        initActionBar(v);

        listView=(ListView)v.findViewById(R.id.listView);

        List<Discovery> list = new ArrayList<>();
        for (int i=0;i<data.length;i++){
            list.add(new Discovery(imageData[i],data[i]));
        }

        //加载adapter
        adapter=new MyAdapter(getContext(),list);
        listView.setAdapter(adapter);

        //设置item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AppLogger.d("点击了cell", "第" + position);
                switch (position){
                    case 0:{
                        ShopListActivity.startShopListActivity(LsgApplication.getAppContext(),"门店");
                    }break;
                    case 1:{
                        ActionActivity.startActionActivity(LsgApplication.getAppContext(),"活动");
                        adapter.clearRedActionnum(0);
                    }break;
                    case 2:{
                        ActionActivity.startActionActivity(LsgApplication.getAppContext(),"资讯");
                        adapter.clearRedMessagenum(0);
                    }break;
                    case 3:{
                        ShopListActivity.startShopListActivity(LsgApplication.getAppContext(), "实验校");
                    }break;
                    default:

                }
            }
        });

        return v;
    }


    /**
     * 获取活动/资讯的未读数
     */
    private void getNumData() {
        NumData num =  new NumData();
        //取已存的时间戳
        num.datetime = Preference.getInstance().getDiscoverNum();

        final long timer = System.currentTimeMillis()/1000;
        Log.d("时间", "" + Preference.getInstance().getDiscoverNum());

        DiscoveryServer.getNumData(num, new XLTResponseCallback<RspDiscoverNumType>() {
            @Override
            public void onResponse(RspDiscoverNumType data, XLTError error) {

                if (error == null && data != null) {
                    //请求成功

                    Preference.getInstance().setDiscoverNum(timer);
                    AppLogger.d("发现消息数请求成功", "" + data.actionnum);
                    adapter.updateNum(data);

                } else {
                    AppLogger.d("发现未读数请求错误", error.getMesssage());
                }

            }
        });
    }

    //设置导航栏和返回
    @Override
    protected int getTitleResourceId() {
        return R.string.discovery_title;
    }
    @Override
    protected int getLeftImageResourceId() {
        return 0;
    }
}

