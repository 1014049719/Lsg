package com.talenton.lsg.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.server.bean.feed.MineCircle;

public class AttentionTypeActivity extends BaseCompatActivity implements View.OnClickListener{

    private LinearLayout layout_admin;
    private TextView tv_admin;
    private ImageView iv_admin;
    private LinearLayout layout_audience;
    private TextView tv_audience;
    private ImageView iv_audience;

    private int attentionType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_type);

        initViews();
        initDatas();
    }

    private void initViews() {
        // TODO Auto-generated method stub
        layout_admin = (LinearLayout)findViewById(R.id.layout_admin);
        tv_admin = (TextView)findViewById(R.id.tv_admin);
        iv_admin = (ImageView)findViewById(R.id.iv_admin);
        layout_audience = (LinearLayout)findViewById(R.id.layout_audience);
        tv_audience = (TextView)findViewById(R.id.tv_audience);
        iv_audience = (ImageView)findViewById(R.id.iv_audience);

        layout_admin.setOnClickListener(this);
        layout_audience.setOnClickListener(this);
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        Intent intent = this.getIntent();
        if (intent != null) {
            attentionType = intent.getIntExtra("attentionType", 0);
        }
        initAuthorityInfo(attentionType == MineCircle.ATTENTION_TYPE_ADMIN);

    }

    private void initAuthorityInfo(boolean isAdmin){

        if(isAdmin){
            tv_admin.setTextColor(getResources().getColor(R.color.text_main));
            iv_admin.setVisibility(View.VISIBLE);
            tv_audience.setTextColor(getResources().getColor(R.color.gray1));
            iv_audience.setVisibility(View.GONE);
        }else{
            tv_audience.setTextColor(getResources().getColor(R.color.text_main));
            iv_audience.setVisibility(View.VISIBLE);
            tv_admin.setTextColor(getResources().getColor(R.color.gray1));
            iv_admin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String authorityName = "";
        int id = v.getId();
        if(id==R.id.layout_admin){
            attentionType = MineCircle.ATTENTION_TYPE_ADMIN;
            authorityName = "管理员";
        }else if (id==R.id.layout_audience){
            attentionType = MineCircle.ATTENTION_TYPE;
            authorityName  = "普通亲友";
        }
        initAuthorityInfo(attentionType == MineCircle.ATTENTION_TYPE_ADMIN);
        Intent intent = new Intent();
        intent.putExtra("attentionType", attentionType);
        intent.putExtra("authorityName",authorityName);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.fix_family_authority_set;
    }
}
