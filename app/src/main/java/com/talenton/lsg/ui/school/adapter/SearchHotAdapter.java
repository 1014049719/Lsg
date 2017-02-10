package com.talenton.lsg.ui.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.util.RandomUtils;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

/**
 * @author zjh
 * @date 2016/4/14
 */
public class SearchHotAdapter extends LSGBaseAdapter<String>{
    private RandomUtils randomUtils;
    public SearchHotAdapter(Context context, List<String> datas) {
        super(context, datas);
        randomUtils = new RandomUtils();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_school_search_hot,null);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String name = getItem(position);

        viewHolder.tv.setText(name);
        viewHolder.tv.setTextColor(randomUtils.randomColor());
        return convertView;
    }


    static class ViewHolder{
        public TextView tv;
    }
}