package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.XLTResponseListener;
import com.talenton.lsg.base.util.NetWorkUtils;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.user.ReqSuggestion;
import com.talenton.lsg.server.bean.user.RspCustomerServiceInfo;

import org.json.JSONObject;

/**
 * 意见反馈
 */
public class SuggestionActivity extends BaseCompatActivity implements View.OnClickListener {
    private EditText et_content;
    private EditText et_phone;
    private LinearLayout ll_send; //发送
    private LinearLayout ll_link; //联系客服
    private TextView tv_cs_tel; //客服电话;
    private TextView tv_cs_qq; //QQ群
    private String linkQQ; //联系客服QQ

    public static void startSuggestionActivity(Context context){
        context.startActivity(new Intent(context,SuggestionActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        et_content = (EditText) findViewById(R.id.et_content);
        et_phone = (EditText) findViewById(R.id.et_phone);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        ll_link = (LinearLayout) findViewById(R.id.ll_link);
        tv_cs_tel = (TextView) findViewById(R.id.tv_cs_tel);
        tv_cs_qq = (TextView) findViewById(R.id.tv_cs_qq);

        ll_send.setOnClickListener(this);
        ll_link.setOnClickListener(this);

        MineServer.getCustomerServiceInfo(new XLTResponseCallback<RspCustomerServiceInfo>() {
            @Override
            public void onResponse(RspCustomerServiceInfo data, XLTError error) {
                if (error == null && data != null){
                    tv_cs_tel.setText(data.getServicetel());
                    tv_cs_qq.setText(data.getServicegroup());
                    linkQQ = data.getServiceqq();
                }else {
                    showShortToast(error.getMesssage());
                }
            }
        });
    }


    @Override
    protected int getTitleResourceId() {
        return R.string.suggestion_title;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_send:
                String content = et_content.getText().toString();
                String phone = et_phone.getText().toString();
                if (TextUtils.isEmpty(content)){
                    et_content.setError(getString(R.string.mine_text_suggestion_error));
                    return;
                }
                showProgress("提交数据中");
                MineServer.postSuggestion(new ReqSuggestion(this, phone, content), new XLTResponseListener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseData, XLTError errorData) {
                        if (errorData == null){
                            showShortToast("提交成功");
                            finish();
                        }else {
                            showShortToast(errorData.getMesssage());
                        }
                        hideProgress();
                    }
                });

                break;
            case R.id.ll_link:
                //联系客服
                if ("".equals(linkQQ)){
                    showProgress("未能成功获取到客服QQ号");
                    return;
                }
                startQQLink(linkQQ);
                break;
        }
    }


    private void startQQLink(String qq){
        if(TextUtils.isEmpty(qq)) return;
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq
                + "&version=1&src_type=web&web_src=oicqzone.com";
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            XLTToast.makeText(getApplication(),"您还没安装手机QQ，请电话联系"+tv_cs_tel.getText().toString());
            e.printStackTrace();
        }
    }
}
