package com.talenton.lsg.ui.school;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.server.SchoolServer;
import com.talenton.lsg.server.bean.school.ClassData;
import com.talenton.lsg.server.bean.school.ReqClassList;
import com.talenton.lsg.server.bean.school.RspListClass;
import com.talenton.lsg.BaseListFragment;
import com.talenton.lsg.server.bean.school.event.JoinClassEvent;
import com.talenton.lsg.ui.school.adapter.ClassAdapter;
import com.talenton.lsg.ui.school.adapter.MyJoinClassAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @date 2016/4/22
 */
public class MyFavoritesFragment extends BaseListFragment implements AdapterView.OnItemClickListener {
    private List<ClassData> myFavoriteDatas;
    private int currentClickPosition;

    public MyFavoritesFragment() {
    }

    public static MyFavoritesFragment newInstance() {
        MyFavoritesFragment fragment = new MyFavoritesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_my_favorites, container, false);
        super.onCreateView(inflater, v, savedInstanceState);
        initActionBar(v);
        fillData();
        EventBus.getDefault().register(this);
        return v;
    }

    private void fillData() {
        myFavoriteDatas = new ArrayList<>();
        setIsShowNoDataView(true);
        SchoolServer.SchoolClassListServer classListServer = new SchoolServer.SchoolClassListServer(new ReqClassList(ReqClassList.ClassListType.MY_COURSE));
        startGetData(classListServer, new ListResponseCallback<RspListClass>() {
            @Override
            public void onSuccess(RspListClass data) {
                myFavoriteDatas.addAll(data.getList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(XLTError error) {

            }
        });
        mAdapter = new MyJoinClassAdapter(getContext(),myFavoriteDatas);
        mPullRefreshListView.setAdapter(mAdapter);
        mPullRefreshListView.getRefreshableView().setOnItemClickListener(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getPullRefreshListViewResId() {
        return R.id.list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - mPullRefreshListView.getRefreshableView().getHeaderViewsCount();
        currentClickPosition = position;
        ClassDetailActivity.startClassDetailActivity(getContext(),((MyJoinClassAdapter)mAdapter).getItem(position).getAid(),position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JoinClassEvent joinClassEvent) {
        if (joinClassEvent == null){
            return;
        }
        int position = joinClassEvent.getPosition();
        if (position != -1 && position == currentClickPosition){
            int currentJoincount = ((ClassAdapter)mAdapter).getDatas().get(position).getJoincount();
            ((ClassAdapter)mAdapter).getDatas().get(position).setJoincount(currentJoincount + 1);
            mAdapter.notifyDataSetChanged();
        }
    }
}
