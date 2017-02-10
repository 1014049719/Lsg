package com.talenton.lsg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talenton.lsg.base.widget.LoadingViewHolder;

/**
 * @author zjh
 * @date 2016/4/15
 */
public abstract class BaseEmptyFragment extends BaseCompatFragment {
    private LoadingViewHolder loadingViewHolder;
    private ViewGroup container;


    public void addEmptyViewToContain(View v){
        if(loadingViewHolder == null){
            View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.load_fail_view,null);
            emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            loadingViewHolder = new LoadingViewHolder(v, emptyView, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReload(v);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReload(v);
                }
            });

            if (container != null){
                container.addView(emptyView);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 显示正在加载中
     */
    public void showLoadingView(){
        loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_LOADING);
    }

    /**
     * 数据加载完成,隐藏emptyview
     */
    public void showData(){
        loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_DATA);
    }

    /**
     * 显示数据加载失败
     */
    public void showLoadErrorView(){
        loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_NO_NOTWORK);
    }

    /**
     * 显示没有数据
     */
    public void showNoDataView(){
        loadingViewHolder.showView(LoadingViewHolder.VIEW_TYPE_NO_DATA);
    }

    /**
     * VIEW_TYPE_NO_DATA = 1;
     * VIEW_TYPE_NO_NOTWORK = 2;
     * VIEW_TYPE_DATA = 3;
     * VIEW_TYPE_LOADING = 4;
     * VIEW_TYPE_RECOMMEND = 5;
     * @param viewType
     */
    public void showEmptyViewByType(int viewType){
        loadingViewHolder.showView(viewType);
    }

    public abstract void onReload(View v);
}
