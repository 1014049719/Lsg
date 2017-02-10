package com.talenton.lsg.ui.shop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.shop.GoodsInfo;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @date 2016/4/8
 */
public class GoodsAdapter extends LSGBaseAdapter<GoodsInfo>{

    public int type;
    public GoodsAdapter(Context context, ArrayList<GoodsInfo> datas,int type) {
        super(context, datas);
        this.type=type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_goods, null);
            viewHolder = new ViewHolder();
            viewHolder.img=(ImageView)convertView.findViewById(R.id.shop_goods_picture);
            viewHolder.imgItem  = (ImageView) convertView.findViewById(R.id.shop_picture_item);
            viewHolder.goods_name = (TextView) convertView.findViewById(R.id.shop_goods_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.salenum = (TextView) convertView.findViewById(R.id.sale_number);
            viewHolder.haol=(TextView) convertView.findViewById(R.id.favourable_comment);
            viewHolder.market_price=(TextView) convertView.findViewById(R.id.market_price);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GoodsInfo mGoodsInfo = getItem(position);

        ImageLoaderManager.getInstance().displayImage(mGoodsInfo.thumb, viewHolder.img, ImageLoaderManager.DEFAULT_IMAGE_GRAY_LOADING_DISPLAYER, null, null);

        viewHolder.goods_name.setText(mGoodsInfo.name);

        viewHolder.price.setText("￥"+mGoodsInfo.shop_price);

        viewHolder.salenum.setText(mGoodsInfo.salesnum+"件");
        viewHolder.market_price.setText(mGoodsInfo.market_price);
        viewHolder.haol.setText(mGoodsInfo.haol);
        viewHolder.market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if(type==0){
            viewHolder.imgItem.setImageResource(R.mipmap.icon_shop_discount_item);
       //     viewHolder.price.setText("￥"+mGoodsInfo.promote_price);
        }
        if(type==1){
            viewHolder.imgItem.setImageResource(R.mipmap.icon_shop_recommend_item);
       //     viewHolder.market_price.setVisibility(View.INVISIBLE);
        }
        if(type==2){
            viewHolder.imgItem.setImageResource(R.mipmap.icon_shop_hot_item);
       //     viewHolder.market_price.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

    static class ViewHolder{
        public ImageView img;
        public ImageView imgItem;
        public TextView goods_name;
        public TextView price;
        public TextView salenum;
        public TextView haol;
        public TextView market_price;
    }
}