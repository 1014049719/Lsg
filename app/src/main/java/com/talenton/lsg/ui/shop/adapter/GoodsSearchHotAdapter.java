package com.talenton.lsg.ui.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.server.bean.shop.GoodsCartInfo;
import com.talenton.lsg.util.RandomUtils;

import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/5/20.
 */
public class GoodsSearchHotAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<String> mGoodsSearchHot;

    private RandomUtils randomUtils;
    public View.OnClickListener mListener;
    private Context mContext;


    public GoodsSearchHotAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListener = (View.OnClickListener)context;
        mGoodsSearchHot = new ArrayList<String>();
        randomUtils = new RandomUtils();
        mContext=context;


    }

    public int getCount() {
        return mGoodsSearchHot.size();
    }

    public void setData(ArrayList<String> paths) {
        mGoodsSearchHot.clear();
        mGoodsSearchHot.addAll(paths);
        notifyDataSetChanged();
    }


    public String getItem(int position) {
        return mGoodsSearchHot.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_shop_search_hot, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.shop_search_hot_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;
        final String item = getItem(position);

        holder.name.setText(item);
        holder.name.setTextColor(randomUtils.randomColor());

        return convertView;
    }

    public class ViewHolder {

        TextView name;

        public int position;
    }

}