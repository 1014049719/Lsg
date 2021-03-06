package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.DateUtil;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.user.BrowserData;
import com.talenton.lsg.server.bean.user.RspBrowser;

import java.util.LinkedList;

public class BrowserActivity extends BaseCompatActivity implements View.OnClickListener {

    private int mCurPage, mSumCount;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private LoadingViewHolder mLoading;
    private PagerAdapter mAdapter;
    private long circleId;
    private TextView mBrowserNum;

    public static void startBrowserActivity(Context context, long circleId){
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("circleId", circleId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        circleId = this.getIntent().getLongExtra("circleId", 0);

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                loadNewData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                loadMoreData();
            }

        });
        mListView = mPullRefreshListView.getRefreshableView();

        mAdapter = new PagerAdapter(this);
        mListView.setAdapter(mAdapter);

        View loading = findViewById(R.id.loading_container);
        mLoading = new LoadingViewHolder(mPullRefreshListView, loading, this, this);

        mBrowserNum = (TextView)findViewById(R.id.tv_browser_sum);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        loadData();
    }

    private void loadData(){
        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
        MineServer.getBrowserList(circleId, "", "", new XLTResponseCallback<RspBrowser>() {
            @Override
            public void onResponse(RspBrowser data, XLTError error) {
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
        MineServer.getBrowserList(circleId, "", "", new XLTResponseCallback<RspBrowser>() {
            @Override
            public void onResponse(RspBrowser data, XLTError error) {
                mPullRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshListView.onRefreshComplete();
                    }
                }, 1000);
                if (error == null && data != null) {
                    initData(data);
                }
            }
        });
    }

    private void initData(RspBrowser data){
        mCurPage = 1;
        mSumCount = data.count;
        if (data.count > 0){
            mBrowserNum.setText(String.format("已浏览 (%d)", data.count));
        }
        mAdapter.setDatas(data.list);
    }

    private void loadMoreData() {
        if (mSumCount <= 0 || (mCurPage * 20) >= mSumCount) {
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

        MineServer.getBrowserList(circleId, "", String.valueOf(mCurPage + 1), new XLTResponseCallback<RspBrowser>() {
            @Override
            public void onResponse(RspBrowser data, XLTError error) {
                mPullRefreshListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshListView.onRefreshComplete();
                    }
                }, 1000);
                if (error == null && data != null) {
                    mCurPage++;
                    mAdapter.addDatas(data.list);
                }
            }
        });
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.family_title;
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

    private class PagerAdapter extends BaseAdapter {

        LayoutInflater mInflater;
        LinkedList<BrowserData> mDatas;
        Context mContext;

        public PagerAdapter(Context context){
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDatas = new LinkedList<>();
        }

        public void setDatas(LinkedList<BrowserData> datas){
            mDatas.clear();
            if (datas != null && datas.size() > 0){
                mDatas.addAll(datas);
            }
            notifyDataSetChanged();
        }

        public void addDatas(LinkedList<BrowserData> datas){
            if (datas != null && datas.size() > 0){
                mDatas.addAll(datas);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public BrowserData getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder;
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.item_user_family, parent, false);
                holder = new HolderView();
                holder.mPhoto = (ImageView)convertView.findViewById(R.id.user_image);
                holder.mArrow = (ImageView)convertView.findViewById(R.id.iv_arrow);
                holder.mName = (TextView)convertView.findViewById(R.id.tv_name);
                holder.mDescription = (TextView)convertView.findViewById(R.id.tv_info);
                holder.mBrowser = (TextView)convertView.findViewById(R.id.tv_browser);
                holder.mArrow.setVisibility(View.GONE);
                holder.mBrowser.setVisibility(View.VISIBLE);
                convertView.setTag(holder);
            }else {
                holder = (HolderView)convertView.getTag();
            }
            BrowserData data = getItem(position);

            ImageLoader.getInstance().displayImage(data.getCircleMember().avatar, holder.mPhoto, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
            holder.mName.setText(data.getCircleMember().realname);
            if (data.create_time > 0){
                holder.mDescription.setText(DateUtil.parseTimeToYMDHMS(data.create_time));
            }
            if (data.browse_count > 0){
                holder.mBrowser.setText(String.format("浏览 %d 次", data.browse_count));
            }
            return convertView;
        }
    }

    private class HolderView{
        public ImageView mPhoto, mArrow;
        public TextView mName, mDescription, mBrowser;
    }

    /**
     * Created by Wang.'''' on 2016/5/23.
     */
    public static class ConfirmDuihuanPointActivity extends BaseCompatActivity{

    }
}
