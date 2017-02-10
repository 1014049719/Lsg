package com.talenton.lsg.ui.school;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;

import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseListener;
import com.talenton.lsg.server.SchoolServer;
import com.talenton.lsg.server.bean.school.ClassEvaluateData;
import com.talenton.lsg.server.bean.school.ReqEvalute;

public class CreateEvaluateActivity extends BaseCompatActivity {
    private EditText et_content;
    private RatingBar rating_bar;
    private SchoolServer.SchoolClassEvaluateServer classEvaluateServer;
    private static final String AID = "aid";
    private long aid;
    private ReqEvalute reqEvalute;
    private ProgressDialog progressDialog;

    public static void startCreateEvaluateActivity(Context context,long aid){
        Intent intent = new Intent(context,CreateEvaluateActivity.class);
        intent.putExtra(AID,aid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_school_create_evaluate);
        super.onCreate(savedInstanceState);

        aid = getIntent().getLongExtra(AID,0);
        et_content = (EditText) findViewById(R.id.et_content);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        reqEvalute = new ReqEvalute(ReqEvalute.ReqEvaluteType.CREATE,aid);
        classEvaluateServer = new SchoolServer.SchoolClassEvaluateServer(reqEvalute);
        progressDialog = new ProgressDialog(this);
    }


    @Override
    protected int getTitleResourceId() {
        return R.string.school_class_create_evaluate_title;
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.school_class_evaluate;
    }

    @Override
    protected void onRightClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.submit_evaluate:
                String content = et_content.getText().toString();
                if(TextUtils.isEmpty(content)){
                    et_content.setError("评价内容不能为空");
                    return;
                }
                if (rating_bar.getRating() <= 0){
                    showShortToast("请给予好评度");
                    return;
                }
                reqEvalute.setMessage(content);
                reqEvalute.setStarcount(rating_bar.getRating());

                progressDialog.setMessage(getString(R.string.main_processing));
                progressDialog.show();

                classEvaluateServer.createEvalute(new XLTResponseListener<ClassEvaluateData>() {
                    @Override
                    public void onResponse(ClassEvaluateData responseData, XLTError errorData) {
                        if (errorData == null && responseData != null) {
                            finish();
                        } else {
                            showShortToast(errorData.getMesssage());
                        }
                        progressDialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    protected int getLeftImageResourceId() {
        return R.mipmap.x;
    }
}