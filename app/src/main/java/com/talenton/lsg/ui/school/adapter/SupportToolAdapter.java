package com.talenton.lsg.ui.school.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.school.SupportToolData;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

/**
 * @author zjh
 * @date 2016/4/11
 */
public class SupportToolAdapter extends LSGBaseAdapter<SupportToolData>{
    public SupportToolAdapter(Context context, List<SupportToolData> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_school_support_tool,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_discount_price = (TextView) convertView.findViewById(R.id.tv_discount_price);
            viewHolder.tv_original_price = (TextView) convertView.findViewById(R.id.tv_original_price);
            viewHolder.tv_sales_number = (TextView) convertView.findViewById(R.id.tv_sales_number);
            viewHolder.tv_praise_number = (TextView) convertView.findViewById(R.id.tv_praise_number);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SupportToolData supportToolData = getItem(position);
        ImageLoaderManager.getInstance().displayImage(supportToolData.getShopimg(), viewHolder.iv_left, ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER_200, null, null);
        viewHolder.tv_title.setText(supportToolData.getShopname());
        viewHolder.tv_discount_price.setText(String.valueOf(supportToolData.getShopprice()));
        viewHolder.tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tv_original_price.setText(String.valueOf(supportToolData.getMarket_price()));
        viewHolder.tv_sales_number.setText(String.valueOf(supportToolData.getShopsales()));
        viewHolder.tv_praise_number.setText(supportToolData.getShoppraise()+"%");
        return convertView;
    }

    static class ViewHolder{
        private ImageView iv_left;
        private TextView tv_title;
        private TextView tv_discount_price; //折扣价
        private TextView tv_original_price; //原价
        private TextView tv_sales_number; //销量
        private TextView tv_praise_number; //好评数
    }
}