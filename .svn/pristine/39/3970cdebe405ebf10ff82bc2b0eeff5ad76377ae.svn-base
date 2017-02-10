package com.talenton.lsg.ui.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.school.ClassFilterAge;
import com.talenton.lsg.util.RandomUtils;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

/**
 * @author zjh
 * @date 2016/5/3
 */
public class ClassFilterAgeAdapter extends LSGBaseAdapter<ClassFilterAge>{
    private RandomUtils randomUtils;

    public ClassFilterAgeAdapter(Context context, List<ClassFilterAge> datas) {
        super(context, datas);
        randomUtils = new RandomUtils();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassFilterAge classFilterAge =  getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_school_class_filter,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name.setText(classFilterAge.getText());
        viewHolder.tv_name.setTextColor(randomUtils.randomColor());
        return convertView;
    }

    static class ViewHolder{
        public TextView tv_name;
    }
}
