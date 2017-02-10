package com.talenton.lsg.ui.school.adapter;

import android.content.Context;

import com.talenton.lsg.server.bean.school.ClassData;

import java.util.List;

/**
 * @author zjh
 * @date 2016/4/7
 */
public class MyJoinClassAdapter extends BaseMyClassListAdapter {

    public MyJoinClassAdapter(Context context, List<ClassData> datas) {
        super(context, datas);
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
