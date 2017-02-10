package com.talenton.lsg.ui.discovery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.R;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.discovery.RspShopListType;
import com.talenton.lsg.server.bean.discovery.ShopList;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ShopListAdapter extends LSGBaseAdapter<ShopList> {

    public ShopListAdapter(Context context, List<ShopList> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ShopList shopList =(ShopList)getItem(position);
        ViewHolder viewHolder = null;

        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.shoplist_cell, null);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.shopName_textView);
            viewHolder.shopImageView = (ImageView) convertView.findViewById(R.id.shop_image);
            viewHolder.addressTextView= (TextView) convertView.findViewById(R.id.locate_textView);
            viewHolder.describeTextView= (TextView) convertView.findViewById(R.id.shop_describe_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //加载网络图片
        ImageLoader.getInstance().displayImage(shopList.attachment, viewHolder.shopImageView, ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER_200);

        viewHolder.nameTextView.setText(shopList.subject);
        viewHolder.addressTextView.setText(shopList.mendian_addr);
        viewHolder.describeTextView.setText(shopList.describe);
        AppLogger.d("描述："+shopList.describe);

        return convertView;
    }

    class ViewHolder {

        public TextView nameTextView;
        public ImageView shopImageView;
        public TextView addressTextView;
        public TextView describeTextView;

    }
}