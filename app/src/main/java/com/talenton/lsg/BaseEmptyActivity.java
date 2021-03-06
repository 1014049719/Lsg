package com.talenton.lsg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.talenton.lsg.base.widget.LoadingViewHolder;

/**
 * @author zjh
 * @date 2016/4/15
 */
public abstract class BaseEmptyActivity extends BaseCompatActivity{
    private LoadingViewHolder loadingViewHolder;

    public void addEmptyViewToContain(View v){
        if(loadingViewHolder == null){
            View emptyView = LayoutInflater.from(this).inflate(R.layout.load_fail_view,null);
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

//            ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
//            if(rootView != null && rootView.getChildCount() > 0){
//                ViewGroup parentView = (ViewGroup) rootView.getChildAt(0);
//                parentView.addView(emptyView);
//            }
            ViewGroup parentView = (ViewGroup) v.getParent();
            if (parentView != null){
                parentView.addView(emptyView);
            }
        }
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
