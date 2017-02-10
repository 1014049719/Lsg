package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.UploadCompletionHandler;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.bean.ObjectCode;
import com.talenton.lsg.base.util.FileUtil;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.event.ModifyCircleEvent;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.feed.MediaBean;
import com.talenton.lsg.server.bean.feed.QNPicInfo;
import com.talenton.lsg.ui.user.CaptureAndPickActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;

public class ModifyCircleActivity extends BaseCompatActivity implements View.OnClickListener {

    private static final int RELATION_CAPTURE_PICK = 301;
    ImageView mUserLogo;
    TextView mDescription;

    long mCircleId;
    String  mDes, mPhoto;
    private boolean isUploadPhoto = false;

    public static void startShowCircleActivity(Context context, long circleId, String des, String photo){
        Intent intent = new Intent(context, ModifyCircleActivity.class);
        intent.putExtra("circleId", circleId);
        //intent.putExtra("name", circle.circle_name);
        intent.putExtra("des", des);
        intent.putExtra("photo", photo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_circle);
        Intent intent = getIntent();
        mCircleId = intent.getLongExtra("circleId", 0);
        mDes = intent.getStringExtra("des");
        mPhoto = intent.getStringExtra("photo");

        mUserLogo = (ImageView)findViewById(R.id.user_logo);
        mDescription = (TextView)findViewById(R.id.description);
        mUserLogo.setOnClickListener(this);
        findViewById(R.id.save_ok).setOnClickListener(this);

        ImageLoader.getInstance().displayImage(mPhoto, mUserLogo, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
        mDescription.setText(mDes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_logo:
                Intent intent = new Intent(this, CaptureAndPickActivity.class);
                startActivityForResult(intent, RELATION_CAPTURE_PICK);
                break;
            case R.id.save_ok:
                uploadCover();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RELATION_CAPTURE_PICK:
                if (resultCode == RESULT_OK && data != null) {
                    File file = FileUtil.getUploadFile();
                    if(!file.exists() || !file.isFile() || file.length() <= 0){
                        showShortToast("图片加载失败，请重新尝试。");
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (bitmap != null) {
                        isUploadPhoto = true;
                        mUserLogo.setImageBitmap(bitmap);
                    }
                }
                break;
        }
    }

    private void uploadCover(){
        File file = FileUtil.getUploadFile();
        if(!isUploadPhoto || !file.exists() || !file.isFile() || file.length() <= 0){
            showProgress(R.string.main_processing);
            saveCover("");
            return;
        }
        showProgress(R.string.main_processing);
        FeedServer.upload(file.getAbsolutePath(), new UploadCompletionHandler(new UploadCompletionHandler.OnUpCompletionListener() {
            @Override
            public void error(int statusCode, ResponseInfo info) {
                // TODO Auto-generated method stub
                hideProgress();
                XLTToast.makeText(LsgApplication.getAppContext(), info.error).show();
            }

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                // TODO Auto-generated method stub
                hideProgress();
                QNPicInfo qnPic = OkHttpClientManager.getInstance().getmGson().fromJson(response.toString(),
                        QNPicInfo.class);

                String coverurl = MediaBean.genQNFullUrl(qnPic.key);
                saveCover(coverurl);
            }
        }));
    }

    private void saveCover(final String url){
        isUploadPhoto = false;
        if (!TextUtils.isEmpty(url))
            ImageLoader.getInstance().displayImage(url, mUserLogo, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
        final String description = mDescription.getText().toString().trim();
        if (TextUtils.isEmpty(description)){
            hideProgress();
            XLTToast.makeText(this, "圈子描述不能为空").show();
            return;
        }
        if (description.length() > 20){
            hideProgress();
            XLTToast.makeText(this, "圈子描述不能超过20个字符").show();
            return;
        }
        FeedServer.modifyCircle(mCircleId, description, url, null, new XLTResponseCallback<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode data, XLTError error) {
                hideProgress();
                if (error != null) {
                    XLTToast.makeText(LsgApplication.getAppContext(), "修改圈子信息失败，请重试").show();
                } else if (data != null) {
                    EventBus.getDefault().post(new ModifyCircleEvent(mCircleId, description, url, null));
                    finish();
                }
            }
        });
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.circle_modify_info;
    }
}
