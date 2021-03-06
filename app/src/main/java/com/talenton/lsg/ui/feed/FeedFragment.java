package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.event.AttentionAgeEvent;
import com.talenton.lsg.event.DeleteFeedsEvent;
import com.talenton.lsg.event.FeedsTaskEvent;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.feed.CircleHomeData;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.server.bean.feed.CircleMember;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.MineCircle;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.server.bean.feed.ReqFeedsList;
import com.talenton.lsg.server.bean.feed.RspFeedsList;
import com.talenton.lsg.server.bean.feed.RspMineCircle;
import com.talenton.lsg.ui.feed.adapter.CircleFeedsAdapter;
import com.talenton.lsg.ui.feed.adapter.CircleHomeAdapter;
import com.talenton.lsg.ui.feed.adapter.TopicFeedsAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by ttt on 2016/4/1.
 */
public class FeedFragment extends BaseCompatFragment implements View.OnClickListener{

    private TopicFeedsAdapter mAdapter;
    private static final int PAGE_SIZE = 20;
    private int mCurPage, mSumCount;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private View mEmptyHint;
    private LoadingViewHolder mLoading;
    private PostToParam mPostToParam;
    private int mType = CircleListData.CIRCLE_TYPE_TOPIC;
    private long mCreUID;

    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        if (context instanceof View.OnClickListener) {

        } else {

        }
    }

    static public FeedFragment newInstance(int type) {
        return newInstance(type, 0);
    }

    static public FeedFragment newInstance(int type, long creuid){
        FeedFragment f = new FeedFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putLong("creuid", creuid);
        f.setArguments(args);
        f.isLazyMode = true;
        return f;
    }

    public FeedFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    /*
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FeedsTaskEvent event){

        if(mAdapter == null || event == null || event.feeds == null || mPostToParam == null
                || event.feeds.circle_id  != mPostToParam.circleId) return;

        Feeds feeds = event.feeds;
        int anchor = mAdapter.replaceTask(feeds);

        if (mAdapter.getCount() > 0) {
            mEmptyHint.setVisibility(View.GONE);
        }
        if (anchor >= 0) {
            mListView.setSelection(anchor + 2);
        }
    }
    */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteFeedsEvent event) {
        if (mAdapter != null && event != null && mPostToParam != null
                && mPostToParam.circleId == event.circleId) {
            mAdapter.deleteFeeds(event.guid, event.tid);
            if (mAdapter.getCount() == 0) {
                mEmptyHint.setVisibility(View.VISIBLE);
            }
        }
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
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        if(getArguments() != null) {
            mType = getArguments().getInt("type");
            mCreUID = getArguments().getLong("creuid");
        }
        mPostToParam = new PostToParam(CircleListData.CIRCLE_TYPE_TOPIC, 1, "");

        mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                loadNewData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                loadOlderData();
            }

        });

        // 取得真实的View
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        mAdapter = new TopicFeedsAdapter(getActivity(), mType, mPostToParam);
        mListView.setAdapter(mAdapter);

        addFeedsEmptyView(inflater);

        View loading = v.findViewById(R.id.loading_container);
        mLoading = new LoadingViewHolder(mPullRefreshListView, loading, this, this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private void addFeedsEmptyView(LayoutInflater inflater){
        FrameLayout footerParent = new FrameLayout(getActivity());
        footerParent
                .setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mEmptyHint = inflater.inflate(R.layout.item_feeds_empty, null);
        mEmptyHint.findViewById(R.id.photo_time_container).setOnClickListener(this);
        TextView tv = (TextView) mEmptyHint.findViewById(R.id.feeds_text_content);
        tv.setText(R.string.circle_feeds_item_empty);
        LinearLayout layout = (LinearLayout) mEmptyHint.findViewById(R.id.layout_empty);
        int size = getResources().getDimensionPixelSize(R.dimen.space_15_0);
        layout.setPadding(size, size, 0, 0);
        mEmptyHint.findViewById(R.id.photo_time_container).setVisibility(View.GONE);
        /*
        mEmptyDay = (TextView) mEmptyHint.findViewById(R.id.photo_day);
        mEmptyMonth = (TextView) mEmptyHint.findViewById(R.id.photo_month);
        String photoTime = DateUtil.parseTimeToYMD(System.currentTimeMillis() / 1000);
        mEmptyDay.setText(photoTime.substring(8));
        mEmptyMonth.setText(photoTime.substring(0, 7));
        */
        footerParent.addView(mEmptyHint);
        mListView.addHeaderView(footerParent);
    }

    private void loadData(){
        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
        ReqFeedsList reqFeedsList = new ReqFeedsList();
        reqFeedsList.type = mType;
        reqFeedsList.creuid = mCreUID;
        FeedServer.getTopicList(reqFeedsList, new XLTResponseCallback<RspFeedsList>() {
            @Override
            public void onResponse(RspFeedsList data, XLTError error) {
                if (error == null && data != null) {
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

    private void loadNewData(){
        //loadCircleInfo();

        ReqFeedsList reqFeedsList = new ReqFeedsList();
        reqFeedsList.type = mType;
        reqFeedsList.creuid = mCreUID;

        FeedServer.getTopicList(reqFeedsList, new XLTResponseCallback<RspFeedsList>() {
            @Override
            public void onResponse(RspFeedsList data, XLTError error) {
                mPullRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshListView.onRefreshComplete();
                        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                }, 1000);
                if (error == null && data != null) {
                    initData(data);
                }
            }
        });
    }

    private void loadOlderData(){

        if(mSumCount <= 0 || (mCurPage * 20) >= mSumCount) {

            showShortToast(getString(R.string.toast_text_no_data));
            mPullRefreshListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPullRefreshListView.onRefreshComplete();
                }
            }, 1000);
            //mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            //mPullRefreshListView.getFooterLoadingView().setVisibility(View.GONE);
            return;
        }

        ReqFeedsList reqFeedsList = new ReqFeedsList();
        reqFeedsList.type = mType;
        reqFeedsList.creuid = mCreUID;
        reqFeedsList.query_pager = String.valueOf(mCurPage+1);

        FeedServer.getTopicList(reqFeedsList, new XLTResponseCallback<RspFeedsList>() {
            @Override
            public void onResponse(RspFeedsList data, XLTError error) {
                mPullRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshListView.onRefreshComplete();
                    }
                }, 1000);
                if (error == null && data != null && data.list != null) {
                    mCurPage++;
                    mAdapter.addOldFeeds(data);
                }
            }
        });
    }


    private void initData(RspFeedsList data){
        mCurPage = 1;
        mSumCount = data.count;
        mAdapter.setFeeds(data);

        if (mAdapter.getCount() == 0) {
            mEmptyHint.setVisibility(View.VISIBLE);
        } else {
            mEmptyHint.setVisibility(View.GONE);
        }
        /*
        if(data.count < PAGE_SIZE){
            mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            mPullRefreshListView.getFooterLoadingView().setVisibility(View.GONE);
        }else {
            mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        */

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
}
