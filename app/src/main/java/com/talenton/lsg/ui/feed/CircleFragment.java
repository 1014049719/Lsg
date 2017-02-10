package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.event.AttentionAgeEvent;
import com.talenton.lsg.event.LoginEvent;
import com.talenton.lsg.event.ModifyCircleEvent;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.feed.CircleHomeData;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.server.bean.feed.CircleMember;
import com.talenton.lsg.server.bean.feed.MineCircle;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.server.bean.feed.RspMineCircle;
import com.talenton.lsg.ui.feed.adapter.CircleHomeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;

/**
 * Created by ttt on 2016/4/1.
 */
public class CircleFragment extends BaseCompatFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private LoadingViewHolder mLoading;

    private CircleHomeAdapter mAdapter;

    private LinkedList<CircleHomeData> mDatas = new LinkedList<CircleHomeData>();

    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        if (context instanceof View.OnClickListener) {

        } else {

        }
    }

    static public CircleFragment newInstance() {
        CircleFragment f = new CircleFragment();
        f.isLazyMode = true;
        return f;
    }

    public CircleFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AttentionAgeEvent event){
        if (event == null || !event.isAttention || mPullRefreshListView == null) return;
        mPullRefreshListView.setRefreshing(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ModifyCircleEvent event){
        if (event == null || mAdapter == null) return;
        for (CircleHomeData data : mDatas){
            if (data.circleId == event.circleId){
                if (!TextUtils.isEmpty(event.description)){
                    data.circleDescription = event.description;
                }
                if (!TextUtils.isEmpty(event.photo)){
                    data.imageUrl = event.photo;
                }
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginEvent event){
        if (event == null) return;
        loadData();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // image_detail_fragment.xml contains just an ImageView
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_circle, container, false);

        mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                RefreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            }

        });
        mListView = mPullRefreshListView.getRefreshableView();

        mAdapter = new CircleHomeAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        View loading = v.findViewById(R.id.loading_container);
        mLoading = new LoadingViewHolder(mPullRefreshListView, loading, this, this);
        mListView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void loadData(){
        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
        if(UserServer.mNeedLogIn) {
            FeedServer.getMineCircle(0, MineCircle.ATTENTION_TYPE_EMPTY, new XLTResponseCallback<RspMineCircle>() {

                @Override
                public void onResponse(RspMineCircle data, XLTError error) {
                    if (error == null && data != null && data.list != null && data.list.size() > 0) {
                        initData(data);
                        mLoading.showView(LoadingViewHolder.VIEW_TYPE_DATA);
                    } else if (error != null) {
                        mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
                    } else {
                        mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA);
                    }
                }
            });
        }else{
            FeedServer.getDefaultCircle(MineCircle.SETTING_TYPE_HOME, new XLTResponseCallback<RspMineCircle>() {

                @Override
                public void onResponse(RspMineCircle data, XLTError error) {
                    if (error == null && data != null && data.list != null && data.list.size() > 0) {
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
    }

    private void RefreshData(){
        if(UserServer.mNeedLogIn) {
            FeedServer.getMineCircle(0, MineCircle.ATTENTION_TYPE_EMPTY, new XLTResponseCallback<RspMineCircle>() {

                @Override
                public void onResponse(RspMineCircle data, XLTError error) {
                    mPullRefreshListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    }, 1000);
                    if (error == null && data != null) {
                        initData(data);
                    } else if (error != null) {
                    } else {
                    }
                }
            });
        }else{
            FeedServer.getDefaultCircle(MineCircle.SETTING_TYPE_HOME, new XLTResponseCallback<RspMineCircle>() {

                @Override
                public void onResponse(RspMineCircle data, XLTError error) {
                    mPullRefreshListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    }, 1000);
                    if (error == null && data != null) {
                        initData(data);
                    } else if (error != null) {

                    } else {
                    }
                }
            });
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.main_menu_feed;
    }

    @Override
    protected int getLeftImageResourceId() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reload:
            case R.id.empty_action:
                loadData();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CircleHomeData circle = (CircleHomeData)parent.getAdapter().getItem(position);
        if (circle == null || circle.circleId == 0) return;
        PostToParam param = new PostToParam(CircleListData.CIRCLE_TYPE_BABY, circle.circleId, circle.circleName);
        param.circleType = circle.circleType;
        param.attentionType = circle.attentionType;
        if (circle.circleType == CircleListData.CIRCLE_TYPE_BABY){
            BabyFeedsActivity.startBabyFeedsActivity(getActivity(), param);
        }
        else {
            CircleFeedsActivity.startCircleFeedsActivity(getActivity(), param);
        }
    }

    private void initData(RspMineCircle data){
        if(data.list == null || data.list.size() == 0) return;
        mDatas.clear();
        LinkedList<MineCircle> babys = new LinkedList<MineCircle>();
        LinkedList<MineCircle> attinions = new LinkedList<MineCircle>();
        //boolean isShowAttentionFamily = false;
        for(MineCircle circle : data.list) {
            CircleListData circleListData = circle.getCircleListData();
            if (circleListData.circle_type == CircleListData.CIRCLE_TYPE_BABY){
                UserServer.getCurrentUser().attentionType.put((int)circle.circle_id, circle.attention_type);
                babys.add(circle);
                continue;
            }
            if(circleListData.circle_type == CircleListData.CIRCLE_TYPE_SCHOOL
                   && circleListData.is_public == 1){
                UserServer.getCurrentUser().attentionType.put((int)circle.circle_id, 99);
            }
            /*
            if(circle.attention_type == MineCircle.ATTENTION_TYPE_CREATE
                || circle.attention_type == MineCircle.ATTENTION_TYPE_ADMIN){
                if (circleListData.circle_type == CircleListData.CIRCLE_TYPE_BABY){
                    babys.add(circle);
                    continue;
                }
            }
            */
            attinions.add(circle);
        }
        long babyId = babys.size() == 0 ? 0 : babys.get(babys.size() - 1).circle_id;
        long attId = attinions.size() == 0 ? 0 : attinions.get(attinions.size() - 1).circle_id;

        if (attinions.size() == 0){
            MineCircle mineCircle = new MineCircle();
            mineCircle.circle_id = 0;
            mineCircle.ext_circle = new CircleListData();
            mineCircle.ext_circle.circle_type = CircleListData.CIRCLE_TYPE_SCHOOL;

            attinions.add(mineCircle);
        }

        addCircleHomeData(babys, R.string.circle_title_mine_baby, R.string.circle_title_new_family);
        addCircleHomeData(attinions,  R.string.circle_title_mine_attention,  R.string.circle_attention_more);

        mAdapter.addDatas(mDatas, babyId, attId);
    }

    private void addCircleHomeData(LinkedList<MineCircle> datas, int resTitleId, int resAttentionId){
        if (datas == null || datas.size() == 0) return;
        int i = 0;
        for (MineCircle d : datas){
            //CircleMember member = d.getCircleMember();
            CircleListData circleListData = d.getCircleListData();
            CircleHomeData data = new CircleHomeData();
            data.circleId = d.circle_id;
            data.circleType = circleListData.circle_type;
            data.attentionType = d.attention_type;
            data.isPublic = circleListData.is_public == 1;
            data.circleTitleResId = i== 0 ? resTitleId : 0;
            data.attentionResId = resAttentionId;
            data.imageUrl = circleListData.circle_photo;
            data.circleName = circleListData.circle_name;
            data.circleDayTopicsNum = circleListData.day_topics_count;
            data.circleDescription = circleListData.description;
            i++;
            mDatas.add(data);
        }

    }
}
