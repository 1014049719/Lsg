package com.talenton.lsg;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.talenton.lsg.base.XltApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ttt on 2016/3/29.
 */
@Deprecated
public class BaseFragmentActivity extends AppCompatActivity {
    protected boolean isFinished = false;
    private ProgressDialog mProgressDlg;
    protected TextView mActionBarTitle, mActionBarRightText;
    protected ImageView mActionBarRightImage;
    protected View mActionBar;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        initActionBar();
        XltApplication.getInstance().addActivity(this);
    }

    protected void initActionBar() {
        ViewStub stub = (ViewStub) findViewById(R.id.vs_action_bar);
        if (stub == null) {
            return;
        }
        stub.setLayoutResource(getActionBarResourceId());
        mActionBar = stub.inflate();
        initActionBarContent(mActionBar);
    }

    protected void initActionBarContent(View actionBar) {
        if (actionBar != null) {
            int resourceId = getTitleResourceId();
            if (resourceId > 0) {
                mActionBarTitle = (TextView) actionBar.findViewById(R.id.tv_actionbar_title);
                mActionBarTitle.setText(resourceId);
            }
            resourceId = getRightTextResourceId();
            mActionBarRightText = (TextView) actionBar.findViewById(R.id.tv_actionbar_right);
            mActionBarRightImage = (ImageView) actionBar.findViewById(R.id.iv_actionbar_right);
            if (resourceId > 0) {
                mActionBarRightImage.setVisibility(View.INVISIBLE);
                mActionBarRightText.setVisibility(View.VISIBLE);
                mActionBarRightText.setText(resourceId);
            } else {
                resourceId = getRightImageResourceId();
                if (resourceId > 0) {
                    mActionBarRightImage.setVisibility(View.VISIBLE);
                    mActionBarRightText.setVisibility(View.INVISIBLE);
                    mActionBarRightImage.setImageResource(resourceId);
                }
            }

            if (resourceId > 0) {
                View actionBarRight = actionBar.findViewById(R.id.fl_actionbar_right);
                actionBarRight.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onRightClick(v);
                    }

                });
            }

            resourceId = getLeftImageResourceId();

            if (resourceId > 0) {
                ImageView actionBarLeft = (ImageView) actionBar.findViewById(R.id.iv_actionbar_back);
                actionBarLeft.setImageResource(resourceId);
                actionBarLeft.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onLeftClick();
                    }

                });
            } else {
                actionBar.findViewById(R.id.iv_actionbar_back).setVisibility(View.INVISIBLE);
            }

        }
    }

    protected int getActionBarResourceId() {
        return R.layout.global_action_bar;
    }

    protected int getTitleResourceId() {
        return R.string.app_name;
    }

    protected int getRightTextResourceId() {
        return 0;
    }

    protected int getRightImageResourceId() {
        return 0;
    }

    protected void onRightClick(View v) {
    }

    protected void onLeftClick() {
        finish();
    }

    protected int getLeftImageResourceId() {
        return R.drawable.actionbar_back_btn;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isFinished = true;
        XltApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        isFinished = true;
        super.finish();
    }

    public boolean isFinished() {
        return isFinished;
    }

    private ProgressDialog progressDlg() {
        if (mProgressDlg == null) {
            mProgressDlg = new ProgressDialog(this);
            mProgressDlg.setCancelable(false);
        }

        return mProgressDlg;
    }

    public void showProgress(int msgResId) {
        if (isFinished()) {
            return;
        }

        progressDlg().setCancelable(false);
        progressDlg().setMessage(getText(msgResId));
        progressDlg().show();
    }

    public void showProgress(String msg) {
        if (isFinished()) {
            return;
        }

        progressDlg().setCancelable(false);
        progressDlg().setMessage(msg);
        progressDlg().show();
    }

    public void hideProgress() {
        if (isFinished()) {
            return;
        }

        if (mProgressDlg != null) {
            if (mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
                mProgressDlg.setProgress(0);
            }
            mProgressDlg = null;
        }
    }

    protected void showSoftInput(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 100);
    }

    protected void hideSoftInput(View view) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
