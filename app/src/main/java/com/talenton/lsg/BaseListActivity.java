package com.talenton.lsg;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.NetWorkUtils;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.server.IListServer;
import com.talenton.lsg.server.bean.school.BaseRspList;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

/**
 * @author zjh
 * @date 2016/4/1
 */
public abstract class BaseListActivity<T extends BaseRspList> extends BaseEmptyActivity {
    private static final int PAGE_SIZE = 10; //一页加载条数
    private int currentPage = 1;  //当前页数
    protected PullToRefreshListView mPullRefreshListView;
    private IListServer<T> listServer;
    protected LSGBaseAdapter<T> mAdapter;
    private boolean isShowNoDataView = false; //是否显示没有数据时的view
    private ListResponseCallback callback;
    private boolean isCacheByDb = true; //无网络时是否从数据库获取数据返回


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mPullRefreshListView = (PullToRefreshListView) findViewById(getPullRefreshListViewResId());
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });
        addEmptyViewToContain(mPullRefreshListView);
    }

    /**
     * 开始请求数据
     *
     * @param listServer 数据服务
     */
    public void startGetData(IListServer<T> listServer, final ListResponseCallback callback) {
        this.listServer = listServer;
        this.callback = callback;
        loadFirstData();
    }

    /**
     * 第一次加载数据
     */
    private void loadFirstData() {
        showEmptyViewByType(LoadingViewHolder.VIEW_TYPE_LOADING);
        listServer.getData(currentPage, PAGE_SIZE, new XLTResponseCallback<T>() {
            @Override
            public void onResponse(T data, XLTError error) {
                if (error == null) {
                    if (data != null && data.getList() != null && !data.getList().isEmpty()) {
                        showEmptyViewByType(LoadingViewHolder.VIEW_TYPE_DATA);
                        if (data.getList().size() < PAGE_SIZE) {
                            hideLoadMoreView(false);
                        }
                    } else {
                        if (isShowNoDataView) {
                            showEmptyViewByType(LoadingViewHolder.VIEW_TYPE_NO_DATA);
                        } else {
                            showEmptyViewByType(LoadingViewHolder.VIEW_TYPE_DATA);
                        }
                    }
                    if (callback != null) {
                        callback.onSuccess(data);
                    }
                } else {
                    if (isCacheByDb && !NetWorkUtils.isNetworkAvailable(BaseListActivity.this)) {
                        T t = listServer.getCacheData();
                        if (t != null && t.getList() != null && !t.getList().isEmpty()) {
                            showData();
                            callback.onSuccess(t);
                            return;
                        }
                    }
                    showEmptyViewByType(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
                    if (callback != null) {
                        callback.onError(error);
                    }
                }
            }
        });
    }


    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        currentPage++;
        listServer.getData(currentPage, PAGE_SIZE, new XLTResponseCallback<T>() {
            @Override
            public void onResponse(T data, XLTError error) {
                onRefreshComplete();
                if (error != null) {
                    // 数据加载失败
//                    if (isCacheByDb && !NetWorkUtils.isNetworkAvailable(BaseListActivity.this)) {
//                        T t = listServer.getCacheData();
//                        if (t != null && t.getList() != null && !t.getList().isEmpty()) {
//                            loadMoreAdapterData(t);
//                            return;
//                        }
//                    }
                    currentPage--;
                } else {
                    if (data != null && data.getList() != null) {
                        if (data.getList().size() < PAGE_SIZE) {
                            hideLoadMoreView(true);
                        }
                        loadMoreAdapterData(data);
                    }
                }
                onLoadMore(data,error);
            }
        });
    }

    private void loadMoreAdapterData(T data) {
        mAdapter.addAll(data.getList());
        mAdapter.notifyDataSetChanged();
    }

    private void onRefreshComplete() {
        mPullRefreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullRefreshListView.onRefreshComplete();
            }
        }, 1000);
    }

    /**
     * 设置listview不允许加载更多
     */
    private void hideLoadMoreView(boolean isShowTips) {
        if (isShowTips) {
            showShortToast(getString(R.string.toast_text_no_data));
        }
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullRefreshListView.getFooterLoadingView().setVisibility(View.GONE);
    }

    private void showLoadMoreView() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }


    /**
     * 刷新数据
     */
    private void refreshData() {
        final int oldCurrentPage = currentPage;
        currentPage = 1;
        listServer.getData(currentPage, PAGE_SIZE, new XLTResponseCallback<T>() {
            @Override
            public void onResponse(T data, XLTError error) {
                onRefreshComplete();
                if (error != null) {
                    //TODO 数据刷新失败
                    if (isCacheByDb && !NetWorkUtils.isNetworkAvailable(BaseListActivity.this)) {
                        T t = listServer.getCacheData();
                        if (t != null && t.getList() != null && !t.getList().isEmpty()) {
                            refreshAdapterData(t);
                            return;
                        }
                    }
                    currentPage = oldCurrentPage;
                } else {
                    if (mAdapter == null) {
                        return;
                    }
                    if (data.getList().size() < PAGE_SIZE) {
                        hideLoadMoreView(false);
                    } else {
                        showLoadMoreView();
                    }
                    refreshAdapterData(data);
                    onRefresh(data,error);
                }
            }
        });
    }

    private void refreshAdapterData(T data) {
        mAdapter.clear();
        mAdapter.addAll(data.getList());
        mAdapter.notifyDataSetChanged();
    }

    public void setListServer(IListServer<T> listServer) {
        this.listServer = listServer;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void onReload(View v) {
        //重新加载数据
        loadFirstData();
    }

    protected abstract int getPullRefreshListViewResId();

    public interface ListResponseCallback<E extends BaseRspList> {
        void onSuccess(E data);

        void onError(XLTError error);
    }

    protected void onLoadMore(T data, XLTError error) {
    }

    protected void onRefresh(T data, XLTError error) {
    }

    protected void setMode(PullToRefreshBase.Mode mode){
        mPullRefreshListView.setMode(mode);
    }

    public void setIsShowNoDataView(boolean isShowNoDataView) {
        this.isShowNoDataView = isShowNoDataView;
    }

}
