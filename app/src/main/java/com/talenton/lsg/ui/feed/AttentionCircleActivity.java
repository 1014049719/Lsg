package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.feed.CircleListData;

public class AttentionCircleActivity extends BaseCompatActivity implements View.OnClickListener{

    public static void startAttentionCircleActivity(Context context){
        Intent intent = new Intent(context, AttentionCircleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_circle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_input_code:
                AttentionByOtherActivity.startAttentionByOtherActivity(this, AttentionByOtherActivity.VERIFY_TYPE_CODE);
                break;
            case R.id.layout_input_phone:
                AttentionSchoolBabyActivity.startAttentionSchoolBabyActivity(this, AttentionSchoolBabyActivity.TYPE_PHONE);
                break;
            case R.id.layout_school_baby:
                AttentionSchoolBabyActivity.startAttentionSchoolBabyActivity(this, AttentionSchoolBabyActivity.TYPE_SCHOOL);
                break;
            case R.id.layout_age:
                AttentionAgeActivity.startAttentionAgeActivity(this, CircleListData.CIRCLE_TYPE_AGE);
                break;
            case R.id.layout_subject:
                AttentionAgeActivity.startAttentionAgeActivity(this, CircleListData.CIRCLE_TYPE_THEME);
                break;
            case R.id.layout_school:
                AttentionAgeActivity.startAttentionAgeActivity(this, CircleListData.CIRCLE_TYPE_SCHOOL);
                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.circle_title_attention;
    }
}
