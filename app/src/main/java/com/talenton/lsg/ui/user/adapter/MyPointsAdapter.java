package com.talenton.lsg.ui.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.DateUtil;
import com.talenton.lsg.server.bean.user.MyPoints;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;
import java.util.List;

/**
 * Created by Wang.'''' on 2016/5/4.
 */
public class MyPointsAdapter extends LSGBaseAdapter<MyPoints>{

    public MyPointsAdapter(Context context, List<MyPoints> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyPoints myPoints=getItem(position);
        ViewHolder viewHolder=null;
        if (viewHolder==null){

            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.mypoints_item,null);
            viewHolder.descTextView= (TextView) convertView.findViewById(R.id.desc_tv);
            viewHolder.datelineTextView= (TextView) convertView.findViewById(R.id.dateline_tv);
            viewHolder.pointsTextView= (TextView) convertView.findViewById(R.id.points_tv);
            viewHolder.logisticsstatusTextView= (TextView) convertView.findViewById(R.id.logisticsstatus_tv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.descTextView.setText(myPoints.desc);
        viewHolder.datelineTextView.setText(DateUtil.parseTime(context, myPoints.dateline));

        if (myPoints.flag ==1 ){
            viewHolder.pointsTextView.setText("-" + myPoints.points);
        }else {
            viewHolder.pointsTextView.setText("+"+myPoints.points);
            viewHolder.pointsTextView.setTextColor(context.getResources().getColor(R.color.point_blue));
        }
        viewHolder.logisticsstatusTextView.setText(myPoints.logisticsstatus);

        return convertView;
    }

    class ViewHolder{

        TextView descTextView;
        TextView datelineTextView;
        TextView pointsTextView;
        TextView logisticsstatusTextView;
    }
}
