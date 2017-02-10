package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.bean.BabyData;
import com.talenton.lsg.ui.user.JumpType;
import com.talenton.lsg.base.server.task.TaskBase;
import com.talenton.lsg.base.util.DateUtil;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.util.SystemUtil;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.base.widget.TimePopupWindow;
import com.talenton.lsg.event.AddCommentEvent;
import com.talenton.lsg.event.AttentionAgeEvent;
import com.talenton.lsg.event.CommentingEvent;
import com.talenton.lsg.event.DeleteCommentEvent;
import com.talenton.lsg.event.DeleteFeedsEvent;
import com.talenton.lsg.event.FeedsTaskEvent;
import com.talenton.lsg.event.ModifyCircleEvent;
import com.talenton.lsg.event.SelectedDateEvent;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.feed.CircleInfo;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.MineCircle;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.server.bean.feed.ReqFeedsList;
import com.talenton.lsg.server.bean.feed.RspFeedsList;
import com.talenton.lsg.ui.WebViewActivity;
import com.talenton.lsg.ui.feed.adapter.BabyFeedsAdapter;
import com.talenton.lsg.ui.user.FamilyActivity;
import com.talenton.lsg.util.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class BabyFeedsActivity extends BaseCompatActivity implements View.OnClickListener{

    private BabyFeedsAdapter mAdapter;
    private int mCurPage, mSumCount;
    private FrameLayout frameContent;
    private LoadingViewHolder mLoading;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private View mEmptyHint, mPhotoTimeContainer, mCover;
    private TextView mEmptyMonth, mEmptyDay;
    private TextView mProgress, mPhotoDay, mPhotoMonth;
    private int mfirstVisibleItem = -1;
    private int mHeaderCount = 2;
    private PostToParam mPostToParam;
    private boolean mIsAttention = false;
    private TimePopupWindow mPwTime = null;
    private long mGraphtime;
    private View mInviteFriend;
    private ImageView mCircleCover;
    private CircleInfo mCircleInfo;

    public static void startBabyFeedsActivity(Context context, PostToParam postToParam){
        Intent intent = new Intent(context, BabyFeedsActivity.class);
        intent.putExtra("key_post_to", postToParam);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mPostToParam = (PostToParam) intent.getParcelableExtra("key_post_to");
        }
        if (mPostToParam == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_baby_feeds);

        EventBus.getDefault().register(this);
        mPostToParam.name = TextUtils.isEmpty(mPostToParam.name) ? "宝宝圈" : mPostToParam.name;
        if(mActionBarTitle != null){
            mActionBarTitle.setText(mPostToParam.name);
        }

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

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
        mPhotoTimeContainer = findViewById(R.id.photo_time_container);
        mPhotoTimeContainer.setOnClickListener(this);
        mPhotoTimeContainer.setVisibility(View.INVISIBLE);
        mPhotoDay = (TextView) findViewById(R.id.photo_day);
        mPhotoMonth = (TextView) findViewById(R.id.photo_month);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnScrollListener(
                new PauseOnScrollListener(ImageLoader.getInstance(), true, true, new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                         int totalItemCount) {
                        if (firstVisibleItem != mfirstVisibleItem && firstVisibleItem >= mHeaderCount) {
                            mPhotoTimeContainer.setVisibility(View.VISIBLE);
                            String photoTime = "";
                            {
                                View v = mListView.getChildAt(0);
                                if (v != null) {
                                    View timeContainer = v.findViewById(R.id.photo_time_container);
                                    Feeds f = (Feeds) mListView.getAdapter().getItem(firstVisibleItem);
                                    if (timeContainer != null && f != null) {
                                        mfirstVisibleItem = firstVisibleItem;
                                        timeContainer.setVisibility(View.INVISIBLE);
                                        //mAdapter.mFirstFeedsId = f.guid;
                                        photoTime = f.getPhotoTime();
                                        mPhotoDay.setText(photoTime.substring(8));
                                        mPhotoMonth.setText(photoTime.substring(0, 7));
                                    }
                                }
                            }
                            {
                                View v = mListView.getChildAt(1);
                                if (v != null) {
                                    View timeContainer = v.findViewById(R.id.photo_time_container);
                                    Feeds f = (Feeds) mListView.getAdapter().getItem(firstVisibleItem + 1);
                                    if (timeContainer != null && f != null) {
                                        if (!photoTime.equals(f.getPhotoTime())) {
                                            timeContainer.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }
                        } else if (firstVisibleItem < mHeaderCount && mfirstVisibleItem > (mHeaderCount - 1)) {
                            mfirstVisibleItem = firstVisibleItem;
                            mPhotoTimeContainer.setVisibility(View.INVISIBLE);
                            View v = mListView.getChildAt(2);
                            if (v != null) {
                                View timeContainer = v.findViewById(R.id.photo_time_container);
                                // Feeds f = (Feeds)
                                // mListView.getAdapter().getItem(firstVisibleItem
                                // + 2);
                                if (timeContainer != null) {
                                    timeContainer.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                }));

        addBabyCoverView();
        addFeedsEmptyView();

        mAdapter = new BabyFeedsAdapter(this, mPostToParam);
        mListView.setAdapter(mAdapter);
        mProgress = (TextView) findViewById(R.id.post_progress);
        updatePostProgress();
        mProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FeedsTaskActivity.startFeedsTaskActivity(BabyFeedsActivity.this, mPostToParam.clone());
                updatePostProgress();
            }

        });
        mfirstVisibleItem = -1;

        frameContent = (FrameLayout)findViewById(R.id.frame_content);
        View loading = findViewById(R.id.loading_container);
        mLoading = new LoadingViewHolder(frameContent, loading, this, this);

        mGraphtime = System.currentTimeMillis() / 1000;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteFeedsEvent event) {
        mIsAttention = true;
        if (mAdapter != null && event != null && mPostToParam != null
                && mPostToParam.circleId == event.circleId) {
            mAdapter.deleteFeeds(event.guid, event.tid);
            if (mAdapter.getCount() == 0) {
                mEmptyHint.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FeedsTaskEvent event){

        updatePostProgress();
        if(mAdapter == null || event == null || event.feeds == null || mPostToParam == null
                || event.feeds.circle_id  != mPostToParam.circleId) return;
        mIsAttention = true;
        Feeds feeds = event.feeds;
        mPhotoTimeContainer.setVisibility(View.INVISIBLE);
        mfirstVisibleItem = -1;
        int anchor = mAdapter.replaceTask(feeds);

        if (mAdapter.getCount() > 0) {
            mEmptyHint.setVisibility(View.GONE);
        }
        if (anchor >= 0) {
            mListView.setSelection(anchor + mHeaderCount);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SelectedDateEvent event){
        if(event == null || mPhotoTimeContainer == null) return;
        mPhotoTimeContainer.performClick();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommentingEvent event) {
        if (mAdapter != null && event != null && mPostToParam != null
                && mPostToParam.circleId == event.circleId) {
            int[] lp = new int[2];
            mListView.getLocationOnScreen(lp);
            int s = mListView.getFirstVisiblePosition();
            int e = mListView.getLastVisiblePosition() + 1;
            for (int i = s; i < e; ++i) {
                Feeds f = (Feeds) mListView.getAdapter().getItem(i);
                if (f != null && f.guid.equals(event.guid)) {
                    mListView.setSelectionFromTop(i, event.h - lp[1]);
                    return;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AddCommentEvent event) {
        if (mAdapter != null && event != null && event.comment != null
                && mPostToParam != null && mPostToParam.circleId == event.comment.circle_id) {
            mAdapter.addComment(event.comment);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteCommentEvent event) {
        if (mAdapter != null && event != null
                && mPostToParam != null && mPostToParam.circleId == event.circleId) {
            mAdapter.deleteComment(event.tid, event.ctid);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ModifyCircleEvent event){
        if (event == null && mPostToParam == null && mPostToParam.circleId != event.circleId) return;
        if (!TextUtils.isEmpty(event.description)){

            mCircleInfo.description = event.description;
        }
        if (!TextUtils.isEmpty(event.photo)){
            mCircleInfo.circle_photo = event.photo;
        }
        if (!TextUtils.isEmpty(event.cover)){
            mCircleInfo.circle_bg = event.cover;
        }
        if (mCover != null){
            if(!TextUtils.isEmpty(event.cover)) {
                //ImageView cover = (ImageView) mCover.findViewById(R.id.cover);
                ImageLoader.getInstance().displayImage(event.cover, mCircleCover, ImageLoaderManager.DEFAULT_DISPLAYER);
            }

            if (!TextUtils.isEmpty(event.photo)){
                ImageView logo = (ImageView) mCover.findViewById(R.id.user_logo);
                ImageLoader.getInstance().displayImage(event.photo, logo, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadData();
        loadCircleInfo();
    }

    private void updatePostProgress() {

        LinkedList<Feeds> feeds = FeedServer.listAllFeedsTask();
        int pending = 0;
        for (Feeds f : feeds) {
            if (f.taskStatus != TaskBase.STATUS_SUCCESS ) {
                ++pending;
            }
        }
        if (pending == 0) {
            mProgress.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setText(getString(R.string.post_feeds_progress, pending));
        }
    }

    private void addBabyCoverView(){
        FrameLayout footerParent = new FrameLayout(this);
        footerParent.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (SystemUtil.getDisplayWidthPixels() * 0.75)));
        mCover = getLayoutInflater().inflate(R.layout.item_feeds_baby_cover, null);
        //setCoverView();
        footerParent.addView(mCover);
        mListView.addHeaderView(footerParent);
        mHeaderCount = 3;
    }

    private void addFeedsEmptyView(){
        FrameLayout footerParent = new FrameLayout(this);
        footerParent
                .setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mEmptyHint = getLayoutInflater().inflate(R.layout.item_feeds_empty, null);
        mEmptyHint.findViewById(R.id.photo_time_container).setOnClickListener(this);
        TextView tv = (TextView) mEmptyHint.findViewById(R.id.feeds_text_content);
        tv.setText(R.string.circle_feeds_item_empty);
        mEmptyDay = (TextView) mEmptyHint.findViewById(R.id.photo_day);
        mEmptyMonth = (TextView) mEmptyHint.findViewById(R.id.photo_month);
        String photoTime = DateUtil.parseTimeToYMD(System.currentTimeMillis() / 1000);
        mEmptyDay.setText(photoTime.substring(8));
        mEmptyMonth.setText(photoTime.substring(0, 7));
        footerParent.addView(mEmptyHint);
        mListView.addHeaderView(footerParent);
    }

    private void setCoverView(CircleInfo data) {
        if (mCover == null) {
            return;
        }
        mCircleInfo = data;

        if(TextUtils.isEmpty(data.circle_name) || "宝宝圈".equals(data.circle_name)) {
            mPostToParam.name = data.circle_name;
            if (mActionBarTitle != null)
                mActionBarTitle.setText(mPostToParam.name);
        }

        ImageView logo = (ImageView) mCover.findViewById(R.id.user_logo);
        ImageLoader.getInstance().displayImage(data.circle_photo, logo, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
        //logo.setOnClickListener(this);

        mCircleCover = (ImageView) mCover.findViewById(R.id.cover);
        if (!TextUtils.isEmpty(data.circle_bg))
            ImageLoader.getInstance().displayImage(data.circle_bg, mCircleCover, ImageLoaderManager.DEFAULT_DISPLAYER);

        if (data.ext_baobao != null)
        {
            BabyData bb = data.ext_baobao;
            TextView tvName = (TextView) mCover.findViewById(R.id.baby_name);
            tvName.setText(bb.name);
            TextView tvAage = (TextView) mCover.findViewById(R.id.baby_age);
            tvAage.setText(bb.getAge());

            //TextView tvSchool = (TextView) mCover.findViewById(R.id.school);
            //tvSchool.setText(bb.school_name);
        }

        mCover.findViewById(R.id.layout_grow_book).setOnClickListener(this);
        mCover.findViewById(R.id.layout_family).setOnClickListener(this);
        mInviteFriend = mCover.findViewById(R.id.group_request);
        if(mPostToParam.attentionType == MineCircle.ATTENTION_TYPE_CREATE
                || mPostToParam.attentionType == MineCircle.ATTENTION_TYPE_ADMIN) {
            mInviteFriend.setVisibility(View.VISIBLE);
            mInviteFriend.setOnClickListener(this);
            mCircleCover.setOnClickListener(this);
        } else {
            mInviteFriend.setVisibility(View.INVISIBLE);
            mInviteFriend.setOnClickListener(null);
            mCircleCover.setOnClickListener(null);
        }
        if (JumpType.jump(JumpType.JUMP_TYPE_NO_ACTION, this)){
            mInviteFriend.setVisibility(View.INVISIBLE);
            mInviteFriend.setOnClickListener(null);
            mCircleCover.setOnClickListener(null);
        }
    }

    private void loadData(){
        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
        ReqFeedsList reqFeedsList = new ReqFeedsList();
        reqFeedsList.circle_id = mPostToParam.circleId;
        reqFeedsList.circle_type = mPostToParam.circleType;
        reqFeedsList.type = ReqFeedsList.LIST_LATEST;
        long time = getGraphTime();
        reqFeedsList.graphtime = time;
        reqFeedsList.dateline = time;
        reqFeedsList.dbdateline = time;

        FeedServer.getFeedsList(reqFeedsList, new XLTResponseCallback<RspFeedsList>() {
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

    private void loadCircleInfo() {
        FeedServer.getCircleInfo(mPostToParam.circleId, new XLTResponseCallback<CircleInfo>() {
            @Override
            public void onResponse(CircleInfo data, XLTError error) {
                if (error == null && data != null && mCover != null) {
                    setCoverView(data);
                }
            }
        });
    }

    private void loadNewData(){
        loadCircleInfo();

        ReqFeedsList reqFeedsList = new ReqFeedsList();
        reqFeedsList.circle_id = mPostToParam.circleId;
        reqFeedsList.circle_type = mPostToParam.circleType;
        reqFeedsList.type = ReqFeedsList.LIST_NEWER;
        long time = getGraphTime();
        reqFeedsList.graphtime = time;
        reqFeedsList.dateline = time;
        reqFeedsList.dbdateline = time;

        FeedServer.getFeedsList(reqFeedsList, new XLTResponseCallback<RspFeedsList>() {
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
        reqFeedsList.circle_id = mPostToParam.circleId;
        reqFeedsList.circle_type = mPostToParam.circleType;
        reqFeedsList.query_pager = String.valueOf(mCurPage+1);
        reqFeedsList.type = ReqFeedsList.LIST_OLDER;
        long time = getGraphTime();
        reqFeedsList.graphtime = time;
        reqFeedsList.dateline = mAdapter.dateline;
        reqFeedsList.dbdateline = mAdapter.dbdateline;

        FeedServer.getFeedsList(reqFeedsList, new XLTResponseCallback<RspFeedsList>() {
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
        if(data.list != null && data.list.size() > 0){
            Feeds f = data.list.get(0);
            if(mInviteFriend != null) {
                if (mPostToParam.attentionType == MineCircle.ATTENTION_TYPE_CREATE
                        || mPostToParam.attentionType == MineCircle.ATTENTION_TYPE_ADMIN) {
                    mInviteFriend.setVisibility(View.VISIBLE);
                    mInviteFriend.setOnClickListener(this);
                    mCircleCover.setOnClickListener(this);
                } else {
                    mInviteFriend.setVisibility(View.INVISIBLE);
                    mInviteFriend.setOnClickListener(null);
                    mCircleCover.setOnClickListener(null);
                }
            }
            mPostToParam.ispajs = data.list.size() > 0 ? 1 : 0;
        }
        mAdapter.setFeeds(data);
        mPhotoTimeContainer.setVisibility(View.INVISIBLE);
        mfirstVisibleItem = -1;

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
    protected int getTitleResourceId() {
        return R.string.circle_title_mine_baby;
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_feeds_post;
    }

    @Override
    protected void onRightClick(MenuItem item){
        if (JumpType.jump(JumpType.JUMP_TYPE_LOGIN, this)){
            return;
        }
        PostTypeDialog dialog = PostTypeDialog.newInstance(mPostToParam.clone());
        UIHelper.showDialog(this, dialog, "PostTypeDialog");
    }

    @Override
    public void finish() {
        if (mIsAttention)
            EventBus.getDefault().post(new AttentionAgeEvent(mIsAttention));
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reload:
            case R.id.empty_action:
                loadData();
                break;
            case R.id.group_request:
                InviteAttentionActivity.startInviteAttentionActivity(this, mPostToParam.circleId);
                break;
            case R.id.layout_family:
                if (JumpType.jump(JumpType.JUMP_TYPE_NO_ACTION, this)){
                    return;
                }
                if (mCircleInfo != null && mCircleInfo.ext_baobao != null) {
                    FamilyActivity.startFamilyActivity(this, mPostToParam.circleId,  mCircleInfo.ext_baobao.baobaouid);
                }
                break;
            case R.id.layout_grow_book:
                if (mCircleInfo != null && mCircleInfo.ext_baobao != null){
                    PostToParam param = mPostToParam.clone();
                    param.tid = mCircleInfo.ext_baobao.baobaouid;
                    String url = String.format("h5.php?mod=book_index&baobaouid=%d", param.tid);
                    WebViewActivity.startWebViewActivity(this, url, param);
                }
                break;
            case R.id.cover:
                if (mCircleInfo != null)
                    ShowCircleActivity.startShowCircleActivity(this, mCircleInfo);
                break;
            case R.id.photo_time_container:
                if (mPwTime == null) {
                    mPwTime = new TimePopupWindow(this);
                    // 时间选择后回调
                    mPwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

                        @Override
                        public void onTimeSelect(Date date) {
                            long time = date.getTime() / 1000;
                            long now = System.currentTimeMillis() / 1000;
                            if (time > now) {
                                time = now;
                            }

                            String selectTime = DateUtil.parseTimeToYMD(time);
                            /*
                            String photoTime = DateUtil.parseTimeToYMD(mGraphtime);
                            if(selectTime.equals(photoTime)){
                                return;
                            }
                            */
                            mGraphtime = time;
                            mEmptyDay.setText(selectTime.substring(8));
                            mEmptyMonth.setText(selectTime.substring(0, 7));
                            mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            mPullRefreshListView.getFooterLoadingView().setVisibility(View.GONE);
                            mPullRefreshListView.setRefreshing(true);
                            //loadData();
                        }
                    });
                    mPwTime.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                        }
                    });
                }
                mPwTime.setDate(new Date(mGraphtime * 1000));
                mPwTime.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    private long getGraphTime(){

        DateFormat df = DateFormat.getDateInstance();//日期格式，精确到日
        Date date = new Date(mGraphtime * 1000);
        Date d;
        try {
            d = df.parse(df.format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            d = date;
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis() / 1000;
    }
}
