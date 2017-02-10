package com.talenton.lsg.ui.user.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.DateUtil;
import com.talenton.lsg.server.bean.user.MyTopic;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

/**
 * Created by Wang.'''' on 2016/5/5.
 */
public class MyTopicAdapter extends LSGBaseAdapter{

    public MyTopicAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyTopic myTopic= (MyTopic) getItem(position);
        ViewHolder viewHolder=null;
        if (viewHolder==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.mytopic_cell,null);
            viewHolder.titleTV= (TextView) convertView.findViewById(R.id.title_tv);
            viewHolder.cnameTV= (TextView) convertView.findViewById(R.id.cname_tv);
            viewHolder.commentcountTV= (TextView) convertView.findViewById(R.id.commentcount_tv);
            viewHolder.ctimeTV= (TextView) convertView.findViewById(R.id.ctime_tv);
            viewHolder.attachinfoImageView= (ImageView) convertView.findViewById(R.id.attachinfo_imageview);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.titleTV.setText(myTopic.title);
        viewHolder.cnameTV.setText(myTopic.cname);
        viewHolder.commentcountTV.setText(myTopic.commentcount);
        viewHolder.ctimeTV.setText(DateUtil.parseTime(context, myTopic.ctime));

        AppLogger.d("myTopic.attachinfo="+myTopic.attachinfo);
        if (myTopic.attachinfo==null || myTopic.attachinfo.equals("null") || TextUtils.isEmpty(myTopic.attachinfo)){
            viewHolder.attachinfoImageView.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.attachinfoImageView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder{

        TextView titleTV;
        TextView cnameTV;
        TextView commentcountTV;
        TextView ctimeTV;
        ImageView attachinfoImageView;
    }
}
