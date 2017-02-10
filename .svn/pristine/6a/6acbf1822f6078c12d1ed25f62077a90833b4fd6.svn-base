package com.talenton.lsg.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.user.MyPointsParam;
import com.talenton.lsg.server.bean.user.MyTopic;
import com.talenton.lsg.server.bean.user.MyTopicParam;
import com.talenton.lsg.server.bean.user.RspMyPoints;
import com.talenton.lsg.server.bean.user.RspMyTopic;
import com.talenton.lsg.ui.user.adapter.MyTopicAdapter;

import java.util.LinkedList;

public class MyTopicFragment extends BaseCompatFragment {

    public static MyTopicFragment newInstance() {
        MyTopicFragment fragment = new MyTopicFragment();
        return fragment;
    }

    //声明
    ListView listView;
    //适配器
    MyTopicAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_topic, container, false);
        //请求数据
        getMyTopicData();
        //实例化
        listView = (ListView) view.findViewById(R.id.listView);

        adapter = new MyTopicAdapter(LsgApplication.getAppContext(), new LinkedList<MyTopic>());
        listView.setAdapter(adapter);

        return view;
    }

    //请求数据
    protected void getMyTopicData(){

        MyTopicParam myTopicParam=new MyTopicParam();
        MineServer.getMyTopicData(myTopicParam, new XLTResponseCallback<RspMyTopic>() {
            @Override
            public void onResponse(RspMyTopic data, XLTError error) {
                if (error==null && data!=null){
                    //先清空，再刷新
                    adapter.clear();
                    adapter.setDatas(data.list);
                    adapter.notifyDataSetChanged();
                }else {
                    AppLogger.d("收藏话题列表数据请求错误");
                }
            }
        });
    }

}
