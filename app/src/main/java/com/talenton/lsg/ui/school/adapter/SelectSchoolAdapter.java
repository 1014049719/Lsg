package com.talenton.lsg.ui.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talenton.lsg.R;
import com.talenton.lsg.base.server.bean.SchoolData;
import com.talenton.lsg.widget.adapter.LSGBaseAdapter;

import java.util.List;

public class SelectSchoolAdapter extends LSGBaseAdapter<SchoolData> {

    public SelectSchoolAdapter(Context context, List<SchoolData> datas) {
        super(context, datas);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_select_school, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_school_name = (TextView) convertView.findViewById(R.id.tv_school_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SchoolData schoolData = getItem(position);
        viewHolder.tv_school_name.setText(schoolData.name);
        if (schoolData.isShow) {
            viewHolder.tv_school_name.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_school_name.setVisibility(View.GONE);
        }
        return convertView;
    }


    class ViewHolder {
        public TextView tv_school_name;
    }

    /**
     * 匹配数据
     *
     * @param schoolName
     */
    public void filter(String schoolName) {
        if (schoolName == null || schoolName.equals("")) {
            initSchoolDataStatus(true);
        } else {
            initSchoolDataStatus(false);
        }
        for (SchoolData schoolData : getDatas()) {
            if (schoolData.name.contains(schoolName)) {
                schoolData.isShow = true;
            }
        }
        notifyDataSetChanged();
    }

    private void initSchoolDataStatus(boolean isShow) {
        for (SchoolData schoolData : getDatas()) {
            schoolData.isShow = isShow;
        }
    }

}