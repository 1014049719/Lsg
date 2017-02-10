package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.bean.ObjectCode;

public class RegisterActivity extends BaseCompatActivity implements View.OnClickListener{

    private EditText mPhone, mVerificationCode, mPassword, mConfirmPassowrd;
    private TextView mGetVerificationCode;

    private ImageView mEye, mConfirmEye;
    private boolean isOpenEye = false, isOpenConfirmEye = true;

    private Handler mHandler = new Handler();
    private int mTimers = 60;

    public static void startRegisterActivity(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPhone = (EditText)findViewById(R.id.et_phone);
        mVerificationCode = (EditText)findViewById(R.id.et_verification_code);
        mPassword = (EditText)findViewById(R.id.et_password);
        mConfirmPassowrd = (EditText)findViewById(R.id.et_confirm_password);
        mGetVerificationCode = (TextView)findViewById(R.id.tv_verification_code);
        mEye = (ImageView)findViewById(R.id.iv_eye);
        mConfirmEye = (ImageView)findViewById(R.id.iv_eye_confirm);
        mGetVerificationCode.setOnClickListener(this);
        findViewById(R.id.tv_login_next).setOnClickListener(this);
        findViewById(R.id.layout_eye).setOnClickListener(this);
        findViewById(R.id.layout_eye_confirm).setOnClickListener(this);
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
        String password = mPassword.getText().toString().trim();
        switch (v.getId()){
            case R.id.tv_verification_code:
                if (phone.length() != 11){
                    XLTToast.makeText(this, R.string.login_error_invalid_phone).show();
                    mPhone.requestFocus();
                    return;
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
                        if (error != null) {
                            mHandler.removeCallbacks(timerRunnable);
                            mGetVerificationCode.setEnabled(true);
                            mGetVerificationCode.setText(R.string.login_get_sms_code);
                            XLTToast.makeText(RegisterActivity.this, error.getMesssage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.tv_login_next:

                if (TextUtils.isEmpty(phone)) {
                    XLTToast.makeText(this, R.string.login_error_empty_phone).show();
                    mPhone.requestFocus();
                    return;
                }else if (phone.length() != 11){
                    XLTToast.makeText(this, R.string.login_error_invalid_phone).show();
                    mPhone.requestFocus();
                    return;
                }


                if (code.length() < 4) {
                    XLTToast.makeText(this, R.string.login_prompt_sms_code).show();
                    mVerificationCode.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    XLTToast.makeText(this, R.string.login_error_empty_password).show();
                    mPassword.requestFocus();
                    return;
                }else if (password.length() < 6 || password.length() > 16){
                    XLTToast.makeText(this, R.string.login_error_invalid_password).show();
                    mPassword.requestFocus();
                    return;
                }

                if(!password.equals(mConfirmPassowrd.getText().toString().trim())){
                    XLTToast.makeText(this, R.string.login_error_different_password).show();
                    mConfirmPassowrd.requestFocus();
                    return;
                }

                hideSoftInput(v);
                showProgress(R.string.main_processing);
                UserServer.register(phone, password, code, new XLTResponseCallback<ObjectCode>() {
                    @Override
                    public void onResponse(ObjectCode data, XLTError error) {
                        hideProgress();
                        if (error == null){
                            //UserServer.reLogin(false);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            XLTToast.makeText(RegisterActivity.this, error.getMesssage()).show();
                        }
                    }
                });

                break;
            case R.id.layout_eye:
                isOpenEye = !isOpenEye;
                if(isOpenEye){
                    mEye.setImageResource(R.mipmap.eye_open);
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    mPassword.setSelection(mPassword.getText().length());
                }else{
                    mEye.setImageResource(R.mipmap.eye_close);
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPassword.setSelection(mPassword.getText().length());
                }
                break;
            case R.id.layout_eye_confirm:
                isOpenConfirmEye = !isOpenConfirmEye;
                if(isOpenConfirmEye){
                    mConfirmEye.setImageResource(R.mipmap.eye_open);
                    mConfirmPassowrd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    mConfirmPassowrd.setSelection(mConfirmPassowrd.getText().length());
                }else{
                    mConfirmEye.setImageResource(R.mipmap.eye_close);
                    mConfirmPassowrd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mConfirmPassowrd.setSelection(mConfirmPassowrd.getText().length());
                }
                break;

        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.register;
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
