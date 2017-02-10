package com.talenton.lsg.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.bean.RspLogin;
import com.talenton.lsg.base.server.bean.ObjectCode;

/**
 * @modify 2016/05/18 zjh 添加是否为修改手机号绑定判断
 */
public class BindPhoneActivity extends BaseCompatActivity implements View.OnClickListener{

    public final static int BINDPHONE_WECHAT = 1;
    public final static int BINDPHONE_OTHER = 2;

    public final static String IS_SKIP = "is_skip";

    //private CommonAlertDialog mAlertDlg;
    private int mBindPhoneType = BINDPHONE_WECHAT;
    private int mTimers = 60;
    private Handler mHandler = new Handler();

    private EditText mPhone, mVerificationCode;
    private TextView mGetVerificationCode;
    //private TextView mPrompt;

    private boolean isChangePhone; //是否为修改绑定手机号

    public static boolean startBindPhoneActivity(Activity context, int requestCode) {
        if (!UserServer.needBindPhone()) {
            return true;
        }
        Intent intent = new Intent(context, BindPhoneActivity.class);
        context.startActivityForResult(intent, requestCode);
        return false;
    }

    public static void startBindPhoneActivity(Activity context, int requestCode,boolean isChangePhone) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra("is_change_phone",isChangePhone);
        intent.putExtra("bindphone",BINDPHONE_OTHER);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() != null){
            mBindPhoneType = getIntent().getIntExtra("bindphone", BINDPHONE_WECHAT);
            isChangePhone = getIntent().getBooleanExtra("is_change_phone", false);
        }

        setContentView(R.layout.activity_bind_phone);
        mPhone = (EditText)findViewById(R.id.et_phone);
        mVerificationCode = (EditText)findViewById(R.id.et_verification_code);
        mGetVerificationCode = (TextView)findViewById(R.id.tv_verification_code);
        mGetVerificationCode.setOnClickListener(this);
        findViewById(R.id.tv_login_next).setOnClickListener(this);
        if (isChangePhone){
            findViewById(R.id.tv_prompt2).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mHandler != null){
            mHandler.removeCallbacks(timerRunnable);
        }
    }

    @Override
    public void onClick(View v) {
        final String phone = mPhone.getText().toString().trim();
        String code = mVerificationCode.getText().toString().trim();
        switch (v.getId()){
            case R.id.tv_verification_code:
                if (phone.length() != 11){
                    XLTToast.makeText(this, R.string.login_error_invalid_phone).show();
                }
                /*
                if (!SystemUtil.isMobileNO(phone)){
                    XLTToast.makeText(this, R.string.login_invalid_phone_prompt, Toast.LENGTH_LONG).show();
                    return;
                }
                */
                mGetVerificationCode.setEnabled(false);
                mTimers = 60;
                mHandler.postDelayed(timerRunnable, 1000);

                UserServer.getSmscode(phone, UserServer.SMS_CODE_BIND_PHONE, new XLTResponseCallback<ObjectCode>() {
                    @Override
                    public void onResponse(ObjectCode data, XLTError error) {
                        if (error != null){
                            mHandler.removeCallbacks(timerRunnable);
                            mGetVerificationCode.setEnabled(true);
                            mGetVerificationCode.setText(R.string.login_get_sms_code);
                            XLTToast.makeText(LsgApplication.getAppContext(), error.getMesssage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.tv_login_next:
                if (TextUtils.isEmpty(phone)) {
                    XLTToast.makeText(this, R.string.login_error_empty_phone).show();
                    return;
                } else if (phone.length() != 11) {
                    XLTToast.makeText(this, R.string.login_error_invalid_phone).show();
                    return;
                }
                if (code.length() < 4) {
                    XLTToast.makeText(this, R.string.login_prompt_sms_code).show();
                    return;
                }
                if (phone.equals(UserServer.getCurrentUser().username)) {
                    XLTToast.makeText(this, R.string.login_phone_has_binded).show();
                    return;
                }
                hideSoftInput(v);
                if (mBindPhoneType == BINDPHONE_WECHAT){
                    showProgress(R.string.login_phone_binding);
                    UserServer.registBind(phone, code, new XLTResponseCallback<RspLogin>() {
                        @Override
                        public void onResponse(RspLogin data, XLTError error) {
                            hideProgress();
                            if (error == null) {
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                XLTToast.makeText(LsgApplication.getAppContext(), error.getMesssage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }else {
                    showProgress(R.string.login_phone_binding);
                    UserServer.ChangeBind(phone, code, new XLTResponseCallback<ObjectCode>() {
                        @Override
                        public void onResponse(ObjectCode data, XLTError error) {
                            hideProgress();
                            if (error == null) {
                                UserServer.sRspLogin.member.username = phone;
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                XLTToast.makeText(LsgApplication.getAppContext(), error.getMesssage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        if (isChangePhone){
            return R.string.mine_change_bind_phone_title;
        }
        return R.string.login_bind_number;
    }

    @Override
    protected int getMenuResourceId() {
        if (isChangePhone){
            return 0;
        }
        return R.menu.menu_user_bind_phone;
    }

    @Override
    protected void onRightClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_user_go:
                showProgress("");
                UserServer.registBind("", "", new XLTResponseCallback<RspLogin>() {
                    @Override
                    public void onResponse(RspLogin data, XLTError error) {
                        hideProgress();
                        if (error == null) {
                            Intent intent = new Intent();
                            intent.putExtra(IS_SKIP,true);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            XLTToast.makeText(LsgApplication.getAppContext(), error.getMesssage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
                break;
        }
    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            mTimers--;
            if (mTimers <= 0){
                mGetVerificationCode.setEnabled(true);
                mGetVerificationCode.setText(R.string.login_get_sms_code);
            }else {
                mGetVerificationCode.setText(String.format("%d%s", mTimers, getString(R.string.login_count_down)));
                mHandler.postDelayed(this, 1000);
            }
        }
    };
}
