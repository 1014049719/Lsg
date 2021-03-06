package com.talenton.lsg.ui.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.user.HelperData;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

/**
 * @author zjh
 * @date 2016/5/17
 */
public class HelperAdapter extends LSGBaseAdapter<HelperData.HelperArticle>{

    public HelperAdapter(Context context, List<HelperData.HelperArticle> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_helper_article,parent,false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HelperData.HelperArticle helperArticle = getItem(position);
        viewHolder.tv_title.setText(helperArticle.getTitle());

        return convertView;
    }

    static class ViewHolder{
        public TextView tv_title;
    }
}
