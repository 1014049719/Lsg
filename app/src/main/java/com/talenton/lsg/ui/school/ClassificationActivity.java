package com.talenton.lsg.ui.school;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.widget.NoScrollGridView;
import com.talenton.lsg.server.SchoolServer;
import com.talenton.lsg.server.bean.school.ClassFilterAge;
import com.talenton.lsg.server.bean.school.ClassFilterData;
import com.talenton.lsg.server.bean.school.ClassificationData;
import com.talenton.lsg.server.bean.school.ReqClassList;
import com.talenton.lsg.server.bean.school.ReqClassification;
import com.talenton.lsg.server.bean.school.RspListClassType;
import com.talenton.lsg.ui.school.adapter.ClassFilterAdapter;
import com.talenton.lsg.ui.school.adapter.ClassFilterAgeAdapter;

import java.util.ArrayList;

/**
 * @author zjh
 * @date 2016/4/5
 */
public class ClassificationActivity extends BaseCompatActivity implements View.OnClickListener {
    private TextView tv_all; //全部课程
    private LinearLayout ll_filter_content;
    private ArrayList<ClassFilterData> classFilterDatas;
    private ArrayList<ClassFilterAge> classFilterAges;
//    private AdvertisementView ad_view;
    private NoScrollGridView gv_age_filter;

    public static void startClassFilterActivty(Context context){
        Intent intent = new Intent(context,ClassificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_classification);
        super.onCreate(savedInstanceState);

        initView();
//        fillAdViewData();
        getNetworkData();
    }

    private void getNetworkData() {
        final SchoolServer schoolServer = new SchoolServer();
        schoolServer.getClassTypeData(this, new ReqClassification(ReqClassification.LSG_SCHOOL), new XLTResponseCallback<RspListClassType>() {
            @Override
            public void onResponse(RspListClassType data, XLTError error) {
                if (error == null) {
                    classFilterDatas = schoolServer.parseToClassFilterDatas(data.getList());
                    classFilterAges = (ArrayList<ClassFilterAge>) data.getAgelist();
                    fillTypeData();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
//        if (ad_view != null){
//            ad_view.onDestroy();
//        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
//        if (ad_view != null){
//            ad_view.onResume();
//        }
        super.onResume();
    }

    @Override
    public void onPause() {
//        if (ad_view != null){
//            ad_view.onPause();
//        }
        super.onPause();
    }

    /**
     * 填充广告view数据
     */
//    private void fillAdViewData() {
//        ad_view.setData(new ReqAdvertisement(ReqAdvertisement.AD_SCHOOL_ALL));
//    }

    private void fillTypeData() {
        if (classFilterDatas != null && classFilterAges != null){
            for (final ClassFilterData classFilterData : classFilterDatas){
                View view = LayoutInflater.from(this).inflate(R.layout.item_school_inflater_class_filter,null);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                NoScrollGridView gv_filter = (NoScrollGridView) view.findViewById(R.id.gv_filter);
                tv_title.setText(classFilterData.getTitle());
                final ClassFilterAdapter filterAdapter = new ClassFilterAdapter(this,classFilterData.getClassificationDatas());
                gv_filter.setAdapter(filterAdapter);
                gv_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ClassificationData classificationData = filterAdapter.getItem(position);
                        ReqClassList reqClassList = new ReqClassList(ReqClassList.ClassListType.LSG);
                        reqClassList.setCatid(classificationData.getCatid());
                        ClassListActivity.startClassListActivity(ClassificationActivity.this, classificationData.getName(),reqClassList,classFilterAges,-1);
                    }
                });
                ll_filter_content.addView(view);
            }

            final ClassFilterAgeAdapter filterAgeAdapter = new ClassFilterAgeAdapter(this,classFilterAges);
            gv_age_filter.setAdapter(filterAgeAdapter);
            gv_age_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ClassFilterAge classFilterAge = filterAgeAdapter.getItem(position);
                    ReqClassList reqClassList = new ReqClassList(ReqClassList.ClassListType.LSG);
                    reqClassList.setAge((int) classFilterAge.getAgeid());
                    ClassListActivity.startClassListActivity(ClassificationActivity.this,classFilterAge.getTitle(),reqClassList,classFilterAges,position);
                }
            });
        }
    }

    private void initView() {
//        ad_view = (AdvertisementView) findViewById(R.id.ad_view);
        ll_filter_content = (LinearLayout) findViewById(R.id.ll_filter_content);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_all.setOnClickListener(this);
        gv_age_filter = (NoScrollGridView) findViewById(R.id.gv_age_filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_all:
                ClassListActivity.startClassListActivity(ClassificationActivity.this, getString(R.string.school_class_filter_text_age)
                        ,new ReqClassList(ReqClassList.ClassListType.LSG,true));
                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.school_my_class_filter_title;
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.school_class_search;
    }

    @Override
    protected void onRightClick(MenuItem item) {
        if (item.getItemId() == R.id.search){
            SearchActivity.startSearchActivity(this);
        }
    }
}
