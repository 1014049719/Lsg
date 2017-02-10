package com.talenton.lsg.ui.school.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.base.util.DateUtil;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.widget.CircleImageView;
import com.talenton.lsg.server.bean.school.ClassEvaluateData;
import com.talenton.lsg.util.QiNiuUtil;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

/**
 * @author zjh
 * @date 2016/4/11
 */
public class ClassEvaluateAdapter extends LSGBaseAdapter<ClassEvaluateData>{

    public ClassEvaluateAdapter(Context context, List<ClassEvaluateData> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_school_class_evaluate,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_header = (CircleImageView) convertView.findViewById(R.id.iv_header);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_create_time = (TextView) convertView.findViewById(R.id.tv_create_time);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.rating_bar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            viewHolder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        ClassEvaluateData classEvaluateData = getItem(position);
        ImageLoaderManager.getInstance().displayImage(QiNiuUtil.getThumbailUrl(classEvaluateData.getAvartar(), viewHolder.iv_header), viewHolder.iv_header, ImageLoaderManager.DEFAULT_IMAGE_GRAY_LOADING_DISPLAYER, null, null);
//        ImageLoaderManager.getInstance().displayImage(classEvaluateData.getAvartar(), viewHolder.iv_header, ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER4, null, null);
        viewHolder.tv_name.setText(classEvaluateData.getRealname());
        viewHolder.tv_create_time.setText(DateUtil.parseTime(context, classEvaluateData.getDateline()));
        viewHolder.tv_content.setText(Html.fromHtml(classEvaluateData.getMessage()));
        viewHolder.rating_bar.setRating(classEvaluateData.getStarcount());
        viewHolder.tv_level.setText(getEvaluateLevel(classEvaluateData.getStarcount()));


        return convertView;
    }

    private String getEvaluateLevel(float rating) {
        String level;
        if (rating == 0){
            level = "";
        }else if (rating <= 1){
            level = "差评";
        }else if (rating <= 4){
            level = "中评";
        }else{
            level = "好评";
        }
        return level;
    }


    static class ViewHolder{
        public CircleImageView iv_header; //头像
        public TextView tv_name; //名字
        public TextView tv_create_time; //评价创建时间
        public TextView tv_content; //内容
        public RatingBar rating_bar; //星评
        public TextView tv_level;
    }
}
