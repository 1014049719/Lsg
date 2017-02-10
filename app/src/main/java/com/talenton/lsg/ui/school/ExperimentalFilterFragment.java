package com.talenton.lsg.ui.school;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.talenton.lsg.BaseCompatFragment;
import com.talenton.lsg.R;
import com.talenton.lsg.base.widget.NoScrollGridView;
import com.talenton.lsg.server.bean.school.ClassificationData;
import com.talenton.lsg.server.bean.school.ReqClassList;
import com.talenton.lsg.ui.school.adapter.ClassFilterAdapter;

import java.util.ArrayList;

/**
 * @author zjh
 * @date 2016/4/7
 */
public class ExperimentalFilterFragment extends BaseCompatFragment implements AdapterView.OnItemClickListener {
    private NoScrollGridView gv_filter;
    private ClassFilterAdapter filterAdapter;
    private ArrayList<ClassificationData> datas;
    private static final String DATA = "data";

    public static ExperimentalFilterFragment newInstance(ArrayList<ClassificationData> datas){
        ExperimentalFilterFragment f = new ExperimentalFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DATA, datas);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        datas = bundle.getParcelableArrayList(DATA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_experimental_filter, null);
        gv_filter = (NoScrollGridView) view.findViewById(R.id.gv_filter);
        if(datas != null){
            filterAdapter = new ClassFilterAdapter(getContext(),datas);
            gv_filter.setAdapter(filterAdapter);
            gv_filter.setOnItemClickListener(this);
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClassificationData classificationData = datas.get(position);
        ReqClassList reqClassList = new ReqClassList(ReqClassList.ClassListType.EXPERIMENTAL);
        reqClassList.setCatid(classificationData.getCatid());
        ClassListActivity.startClassListActivity(getContext(), classificationData.getName(),reqClassList,false);
    }
}
