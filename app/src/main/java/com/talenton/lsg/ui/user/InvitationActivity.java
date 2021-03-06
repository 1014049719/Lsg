package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;

public class InvitationActivity extends BaseCompatActivity implements View.OnClickListener {
    private LinearLayout ll_invite_phone;
    private LinearLayout ll_invite_QQ;
    private LinearLayout ll_invite_wechat;
    private LinearLayout ll_invite_weibo;


    public static void startInvitationActivity(Context context){
        context.startActivity(new Intent(context,InvitationActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        initView();
    }

    private void initView() {
        ll_invite_phone = (LinearLayout) findViewById(R.id.ll_invite_phone);
        ll_invite_QQ = (LinearLayout) findViewById(R.id.ll_invite_QQ);
        ll_invite_wechat = (LinearLayout) findViewById(R.id.ll_invite_wechat);
        ll_invite_weibo = (LinearLayout) findViewById(R.id.ll_invite_weibo);

        ll_invite_phone.setOnClickListener(this);
        ll_invite_QQ.setOnClickListener(this);
        ll_invite_wechat.setOnClickListener(this);
        ll_invite_weibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_invite_phone:

                break;
            case R.id.ll_invite_QQ:

                break;
            case R.id.ll_invite_wechat:

                break;
            case R.id.ll_invite_weibo:

                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.mine_invite_title;
    }
}
