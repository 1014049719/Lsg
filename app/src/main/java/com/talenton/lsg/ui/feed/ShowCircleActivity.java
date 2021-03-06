package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
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
import com.talenton.lsg.server.bean.feed.CircleInfo;
import com.talenton.lsg.server.bean.feed.MediaBean;
import com.talenton.lsg.server.bean.feed.QNPicInfo;
import com.talenton.lsg.ui.user.CaptureAndPickActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;

public class ShowCircleActivity extends BaseCompatActivity implements View.OnClickListener{

    private static final int RELATION_CAPTURE_PICK = 301;

    ImageView mCover, mUserLogo, mChnageBackground;
    TextView mDescription;

    long mCircleId;
    String mName, mDes, mPhoto, mBg;

    public static void startShowCircleActivity(Context context, CircleInfo circle){
        Intent intent = new Intent(context, ShowCircleActivity.class);
        intent.putExtra("circleId", circle.circle_id);
        intent.putExtra("name", circle.circle_name);
        intent.putExtra("des", circle.description);
        intent.putExtra("photo", circle.circle_photo);
        intent.putExtra("bg", circle.circle_bg);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_circle);
        Intent intent = getIntent();
        mCircleId = intent.getLongExtra("circleId", 0);
        mName = intent.getStringExtra("name");
        mDes = intent.getStringExtra("des");
        mPhoto = intent.getStringExtra("photo");
        mBg = intent.getStringExtra("bg");

        mCover = (ImageView)findViewById(R.id.cover);
        mUserLogo = (ImageView)findViewById(R.id.user_logo);
        mChnageBackground = (ImageView)findViewById(R.id.iv_change_background);
        mDescription = (TextView)findViewById(R.id.tv_description);

        mChnageBackground.setOnClickListener(this);

        if (mActionBarTitle != null && !TextUtils.isEmpty(mName)){
            mActionBarTitle.setText(mName);
        }
        if (!TextUtils.isEmpty(mBg)){
            ImageLoader.getInstance().displayImage(mBg, mCover, ImageLoaderManager.DEFAULT_DISPLAYER);
        }
        ImageLoader.getInstance().displayImage(mPhoto, mUserLogo, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
        mDescription.setText(mDes);

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ModifyCircleEvent event){
        if (event == null || mDescription == null) return;
        if (!TextUtils.isEmpty(event.description)){
            mDescription.setText(event.description);
            mDes = event.description;
        }
        if (!TextUtils.isEmpty(event.photo)){
            mPhoto = event.photo;
            ImageLoader.getInstance().displayImage(event.photo, mUserLogo, ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_change_background:
                Intent intent = new Intent(this, CaptureAndPickActivity.class);
                intent.putExtra("capturepick", CaptureAndPickActivity.CAPTURE_PICK_COVER);
                startActivityForResult(intent, RELATION_CAPTURE_PICK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RELATION_CAPTURE_PICK:
                if (resultCode == RESULT_OK && data != null) {
                    uploadCover();
                }
                break;
        }
    }

    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_modify_circle;
    }

    @Override
    protected void onRightClick(MenuItem item){
        ModifyCircleActivity.startShowCircleActivity(this, mCircleId, mDes, mPhoto);
    }

    private void uploadCover(){
        File file = FileUtil.getUploadFile();
        if(!file.exists() || !file.isFile() || file.length() <= 0){
            showShortToast("图片加载失败，请重新尝试。");
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
        if(TextUtils.isEmpty(url)) return;

        ImageLoader.getInstance().displayImage(url, mCover, ImageLoaderManager.DEFAULT_DISPLAYER);
        FeedServer.modifyCircle(mCircleId, null, null, url, new XLTResponseCallback<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode data, XLTError error) {
                if (error != null){
                    XLTToast.makeText(LsgApplication.getAppContext(), "修改背景失败，请重试").show();
                }else if(data != null){
                    EventBus.getDefault().post(new ModifyCircleEvent(mCircleId, null, null, url));
                }
            }
        });
    }
}
