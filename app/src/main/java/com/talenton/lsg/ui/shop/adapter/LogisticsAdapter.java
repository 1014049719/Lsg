package com.talenton.lsg.ui.shop.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;

import android.widget.TextView;

import com.talenton.lsg.R;

import com.talenton.lsg.server.bean.shop.LogisticsQuery;

import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/5/30.
 */
public class LogisticsAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<LogisticsQuery> mLogisticsInfo;


        public LogisticsAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLogisticsInfo = new ArrayList<LogisticsQuery>();


        }

        public int getCount() {
            return mLogisticsInfo.size();
        }


        public void setData(ArrayList<LogisticsQuery> paths) {
            mLogisticsInfo.clear();
            mLogisticsInfo.addAll(paths);
            notifyDataSetChanged();
        }


        public LogisticsQuery getItem(int position) {
            return mLogisticsInfo.get(position);
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_shop_logistics, parent, false);

                holder.datetime = (TextView) convertView.findViewById(R.id.shop_logistics_datatime);
                holder.remark = (TextView) convertView.findViewById(R.id.shop_logistics_remark);
                holder.img=(ImageView)convertView.findViewById(R.id.shop_logistics_picture);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.position = position;
            if(holder.position==0){
                holder.img.setImageResource(R.mipmap.icon_shop_logistics_arrave);
            }else{
                holder.img.setImageResource(R.mipmap.icon_shop_logistics);
            }
            final LogisticsQuery item = getItem(position);

            holder.datetime.setText(item.datetime);
            holder.remark.setText(item.remark);
            return convertView;
        }

        public class ViewHolder {

            ImageView img;
            TextView datetime;
            TextView remark;
            public int position;
        }




}