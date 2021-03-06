package com.talenton.lsg.ui.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.base.dao.model.SearchCacheBean;
import com.talenton.lsg.server.SchoolCacheServer;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @date 2016/4/14
 */
public class SearchHistoryAdapter extends LSGBaseAdapter<SearchCacheBean> {
    private SearchFilter searchFilter;
    private onFilterResultListener onFilterResultListener;
    private List<SearchCacheBean> searchCacheBeans;
    private View clean_divider_line;

    public SearchHistoryAdapter(Context context, List<SearchCacheBean> datas) {
        super(context, datas);
        searchFilter = new SearchFilter();
        searchCacheBeans = new ArrayList<>();
        searchCacheBeans.addAll(datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_school_search_history,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String result = getItem(position).getSearchText();
        viewHolder.tv_name.setText(result);
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = getItem(position).getSearchText();
                if (key != null){
                    SchoolCacheServer.getInstance().deleteSearchCacheData(key);
                    getDatas().remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        if (clean_divider_line != null){
            if (!getDatas().isEmpty()){
                clean_divider_line.setVisibility(View.GONE);
            }else {
                clean_divider_line.setVisibility(View.VISIBLE);
            }
        }


        return convertView;
    }

    static class ViewHolder{
        public TextView tv_name;
        private ImageView iv_delete;
    }

    public void filterHistroyText(CharSequence text){
        searchFilter.filter(text);
    }

    class SearchFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<SearchCacheBean> resultList = new ArrayList<>();
            for (SearchCacheBean searchCacheBean : searchCacheBeans){
                String searchText = searchCacheBean.getSearchText();
                if (searchText.contains(constraint)){
                    resultList.add(searchCacheBean);
                }
            }
            filterResults.values = resultList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<SearchCacheBean> resultList = (List<SearchCacheBean>) results.values;
            if (resultList != null && !resultList.isEmpty()){
                setDatas(resultList);
                notifyDataSetChanged();
            }
            if (onFilterResultListener != null){
                onFilterResultListener.filterResult(resultList);
            }
        }
    }

    public interface onFilterResultListener{
        void filterResult(List<SearchCacheBean> result);
    }

    public SearchHistoryAdapter.onFilterResultListener getOnFilterResultListener() {
        return onFilterResultListener;
    }

    public void setOnFilterResultListener(SearchHistoryAdapter.onFilterResultListener onFilterResultListener) {
        this.onFilterResultListener = onFilterResultListener;
    }


    public void setCleanDividerLine(View view) {
        clean_divider_line = view;
    }
}
