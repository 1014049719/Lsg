package com.talenton.lsg.ui.school.adapter;

import android.content.Context;

import com.talenton.lsg.server.bean.school.ClassData;

import java.util.List;

/**
 * @author zjh
 * @date 2016/4/7
 */
public class ExperimentalAdapterMy extends BaseMyClassListAdapter {

    public ExperimentalAdapterMy(Context context, List<ClassData> datas) {
        super(context, datas);
    }

    @Override
    public boolean isMyUploadClass() {
        return true;
    }
}
