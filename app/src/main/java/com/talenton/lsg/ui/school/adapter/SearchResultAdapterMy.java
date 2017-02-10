package com.talenton.lsg.ui.school.adapter;

import android.content.Context;

import com.talenton.lsg.server.bean.school.ClassData;

import java.util.List;

/**
 * @author zjh
 * @date 2016/5/4
 */
public class SearchResultAdapterMy extends BaseClassListAdapter {
    public SearchResultAdapterMy(Context context, List<ClassData> datas) {
        super(context, datas);
    }
}
