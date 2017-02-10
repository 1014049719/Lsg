package com.talenton.lsg.ui.school.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.school.CatalogData;
import com.talenton.lsg.widget.PinnedSectionListView;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @date 2016/4/15
 */
public class CatalogAdapter extends BaseCatalogAdapter implements PinnedSectionListView.PinnedSectionListAdapter{

    public CatalogAdapter(Context context,ArrayList<CatalogData> datas){
        super(context,datas);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getCtype() >= 1 ? 1 : 0;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 0;
    }

    @Override
    protected int getConvertViewId() {
        return R.layout.item_school_catalog;
    }
}
