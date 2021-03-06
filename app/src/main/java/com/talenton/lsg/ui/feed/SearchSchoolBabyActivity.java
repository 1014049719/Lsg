package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.dao.model.SearchCacheBean;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.bean.BabyData;
import com.talenton.lsg.base.util.NetWorkUtils;
import com.talenton.lsg.base.widget.LoadingViewHolder;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.SchoolCacheServer;
import com.talenton.lsg.server.bean.user.RspSchoolBaby;
import com.talenton.lsg.server.bean.user.SchoolBabyData;
import com.talenton.lsg.ui.feed.adapter.SchoolBabyAdapter;
import com.talenton.lsg.ui.school.adapter.SearchHistoryAdapter;
import com.talenton.lsg.widget.SearchView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchSchoolBabyActivity extends BaseCompatActivity implements View.OnClickListener{

    private ListView lv_history;
    private TextView tv_clean;
    private SearchView search_view;
    private LinearLayout ll_search_content, layoutContent;
    private ArrayList<SearchCacheBean> searchCacheBeans; //历史搜索数据
    private SearchHistoryAdapter searchHistoryAdapter;
    private ListView mListView;
    private LoadingViewHolder mLoading;
    private SchoolBabyAdapter mAdapter;
    BabyData mBabyData;
    private LinkedList<SchoolBabyData> mDatas = new LinkedList<>();
    private LinkedList<SchoolBabyData> mSearchDatas = new LinkedList<>();

    public static void startSearchSchoolBabyActivity(Context context){
        Intent intent = new Intent(context, SearchSchoolBabyActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_school_baby);
        mBabyData = UserServer.getCurrentUser().getBaobaodata();

        initView();
        fillHistroyList();
        loadData();
    }

    private void loadData(){
        String value = String.valueOf(mBabyData.baobaouid);
        mLoading.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
        MineServer.getSchoolBaby(value, AttentionSchoolBabyActivity.TYPE_SCHOOL, new XLTResponseCallback<RspSchoolBaby>() {
            @Override
            public void onResponse(RspSchoolBaby data, XLTError error) {
                if (error == null && data != null && data.list != null && data.list.size() > 0) {
                    mDatas.clear();
                    mDatas.addAll(data.list);
                    mAdapter.setDatas(data.list);
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_DATA);
                } else if (error != null) {
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
                } else {
                    mLoading.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA);
                }
            }
        });
    }

    /**
     * 填充搜索历史数据
     */
    private void fillHistroyList() {
        View footerView = LayoutInflater.from(this).inflate(R.layout.item_school_search_footer,null);
        tv_clean = (TextView) footerView.findViewById(R.id.tv_clean);
        tv_clean.setOnClickListener(this);
        lv_history.addFooterView(footerView);

        List<SearchCacheBean> cacheData = SchoolCacheServer.getInstance().getSearchCache(SearchCacheBean.TYPE_SCHOOL_BABY_SEARCH);
        if (cacheData != null && !cacheData.isEmpty()){
            searchCacheBeans = (ArrayList<SearchCacheBean>) cacheData;
        }
        if (searchCacheBeans == null){
            searchCacheBeans = new ArrayList<>();
        }

        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchCacheBean searchCacheBean = searchHistoryAdapter.getItem(position);
                search_view.setSearchText(searchCacheBean.getSearchText());
            }
        });
        searchHistoryAdapter = new SearchHistoryAdapter(this,searchCacheBeans);
        search_view.setHistroyAdapter(searchHistoryAdapter);
        lv_history.setAdapter(searchHistoryAdapter);
    }

    /**
     * 是否显示搜索结果view
     * @param isShow
     */
    public void showResultView(boolean isShow){
        if (isShow){
            ll_search_content.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }else {
            ll_search_content.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }

    private void initView() {
        layoutContent = (LinearLayout) findViewById(R.id.ll_content);
        ll_search_content = (LinearLayout) findViewById(R.id.ll_search_content);
        lv_history = (ListView) findViewById(R.id.list);
        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setIsShowHistroy(true);
        search_view.setSearchType(SearchCacheBean.TYPE_CIRCLE_SEARCH);
        search_view.setSearchHint(R.string.feeds_search_school_baby_hint);
        search_view.setOnClickHistroyItemListener(new SearchView.OnClickHistroyItemListener() {
            @Override
            public void ClickHistroyItem(String searchText, int position) {
                //reqFeedsList.title = searchText;
                startSearchData(searchText);
            }
        });
        search_view.setOnSearchCallback(new SearchView.OnSearchCallback() {
            @Override
            public void searchCallback(String searchText) {
                if (!NetWorkUtils.isNetworkAvailable(SearchSchoolBabyActivity.this)) {
                    showShortToast(getString(R.string.main_disable_network));
                    return;
                }
                if (!searchText.isEmpty()) {
                    showResultView(true);
                    //reqFeedsList.title = searchText;
                    startSearchData(searchText);
                } else {
                    showShortToast("搜索内容不能为空");
                }
            }
        });
        search_view.setOnTextChanged(new SearchView.OnTextChanged() {
            @Override
            public void textChange(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    showResultView(false);
                }
            }
        });

        mListView = (ListView) findViewById(R.id.list_content);
        mAdapter = new SchoolBabyAdapter(this, AttentionSchoolBabyActivity.TYPE_SCHOOL);
        mListView.setAdapter(mAdapter);

        View loading = findViewById(R.id.loading_container);
        mLoading = new LoadingViewHolder(layoutContent, loading, this, this);
    }

    private void startSearchData(String name) {
        if (mDatas.size() == 0){
            showShortToast("没有符合条件的结果");
            return;
        }
        mSearchDatas.clear();
        for (SchoolBabyData data : mDatas){
            if (!TextUtils.isEmpty(data.realname) && data.realname.indexOf(name) != -1){
                mSearchDatas.add(data);
            }
        }
        if (mSearchDatas.size() == 0){
            showShortToast("没有符合条件的结果");
            return;
        }
        mAdapter.setDatas(mSearchDatas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clean:
                //清除历史记录
                SchoolCacheServer.getInstance().clearnSearchCacheData(SearchCacheBean.TYPE_SCHOOL_BABY_SEARCH);
                searchHistoryAdapter.clear();
                searchHistoryAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_reload:
            case R.id.empty_action:
                loadData();
                break;
        }
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.school_class_cancel;
    }

    @Override
    protected void onRightClick(MenuItem item) {
        if (item.getItemId() == R.id.cancel_action){
            finish();
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.school_text_search;
    }

    @Override
    protected int getLeftImageResourceId() {
        return 0;
    }
}
