package com.talenton.lsg.ui.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.talenton.lsg.R;
import com.talenton.lsg.base.util.BitmapUtil;
import com.talenton.lsg.base.util.FileUtil;
import com.talenton.lsg.base.util.IntentUtil;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.base.widget.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaptureAndPickActivity extends Activity implements View.OnClickListener{

    private final static int REQUEST_CAPTURE = 801;
    private final static int REQUEST_PICK = 802;
    public final static int CAPTURE_PICK_COVER = 1; // 修改封面
    private static final float MAX_ZOOM_FACTOR = 4f;
    private static final int PIC_QUALITY = 100;
    private LinearLayout mCapture;
    private LinearLayout mPick;
    private LinearLayout mCacnel;
    private LinearLayout mLayoutStart;
    private LinearLayout mLayoutCrop;
    private Button mTextCropOk;
    private Button mTextCancel;
    private CropImageView mCropImage;
    private File mCaptureFile;
    private int mCapturePick = 0;//0 --默认裁剪图片 1--修改宝宝封面
    private int mBitmapWidth = 480;
    private int mBitmapHeight = 800;
    private Map<String, Boolean> mPermissions = new HashMap<>();
    public static final int REQ_PERMISSION = 5001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_and_pick);

        initViews();
    }

    private void initViews() {
        mCapture = (LinearLayout) findViewById(R.id.layout_capture);
        mPick = (LinearLayout) findViewById(R.id.layout_pick);
        mCacnel = (LinearLayout) findViewById(R.id.layout_cancel);
        mLayoutStart = (LinearLayout) findViewById(R.id.layout_start);
        mLayoutCrop = (LinearLayout) findViewById(R.id.layout_crop);
        mTextCropOk = (Button) findViewById(R.id.crop_ok);
        mTextCancel = (Button) findViewById(R.id.crop_cancel);
        mCropImage = (CropImageView) findViewById(R.id.crop_image);

        mCapture.setOnClickListener(this);
        mPick.setOnClickListener(this);
        mCacnel.setOnClickListener(this);
        mTextCropOk.setOnClickListener(this);
        mTextCancel.setOnClickListener(this);
        findViewById(R.id.root).setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = dm.heightPixels; //p.height = (int) (d.getHeight() * 1.0);   //高度设置为屏幕的1.0
        p.width = dm.widthPixels; //p.width = (int) (d.getWidth() * 0.7);    //宽度设置为屏幕的0.8

        //p.alpha = 1.0f;      //设置本身透明度
        //p.dimAmount = 0.0f;      //设置黑暗度
        getWindow().setAttributes(p);

        Intent intent = this.getIntent();
        if(intent != null){
            mCapturePick = intent.getIntExtra("capturepick", 0);
        }
        mCropImage.setMaxZoom(MAX_ZOOM_FACTOR);
        if(mCapturePick == CAPTURE_PICK_COVER){
            int w = dm.widthPixels - 10;
            //int h = (dm.widthPixels * 4)/3;
            mCropImage.setCropSize(w, w);
        }else{
            int h = (dm.widthPixels * 2)/3;
            h = h < 320 ? 320 : h;
            mCropImage.setCropSize(h, h);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_capture:
                if(!checkPermission(Manifest.permission.CAMERA
                        ,Manifest.permission.READ_EXTERNAL_STORAGE
                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    return;
                }
                mCaptureFile = FileUtil.getCaptureFile(this);
                Intent intent = IntentUtil.getCameraIntent(mCaptureFile);
                startActivityForResult(intent, REQUEST_CAPTURE);
                break;
            case R.id.layout_pick:
                mCaptureFile = null;
                Intent intent2 = IntentUtil.getChooseImageIntent();
                startActivityForResult(intent2, REQUEST_PICK);
                break;
            case R.id.layout_cancel:
            case R.id.root:
                finish();
                break;
            case R.id.crop_ok:
                onPhotoCroppedOk();
                break;
            case R.id.crop_cancel:
                mLayoutStart.setVisibility(View.VISIBLE);
                mLayoutCrop.setVisibility(View.GONE);
                break;
        }
    }

    public boolean checkPermission(@NonNull String... permission){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        List<String> denyPermissions = new ArrayList<>();
        for (String per : permission){
            if (ContextCompat.checkSelfPermission(this, per) == PackageManager.PERMISSION_GRANTED){
                mPermissions.put(per, true);
            }else {
                mPermissions.put(per, false);
                denyPermissions.add(per);
            }
        }

        if (denyPermissions.size() > 0){
            ActivityCompat.requestPermissions(this, denyPermissions.toArray(new String[denyPermissions.size()]), REQ_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case REQ_PERMISSION:
                for (int i = 0; i< grantResults.length; i++){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions.length > i){
                        mPermissions.put(permissions[i], true);
                    }
                }
                onPermissionsResult(mPermissions);
                break;
        }
    }

    public void onPermissionsResult(Map<String, Boolean> permissions){
        if (permissions == null || permissions.size() == 0){
            return;
        }
        Boolean object = permissions.get(Manifest.permission.CAMERA);
        if(object != null && !object) {
            XLTToast.makeText(this, R.string.permission_camera_info, Toast.LENGTH_LONG).show();
            return;
        }
        int i = 0;
        for (Boolean b : permissions.values()){
            if(b== null || !b) break;
            i++;
        }
        if (i != permissions.size()){
            XLTToast.makeText(this, R.string.permission_storage_info, Toast.LENGTH_LONG).show();
            return;
        }
        mCaptureFile = FileUtil.getCaptureFile(this);
        Intent intent = IntentUtil.getCameraIntent(mCaptureFile);
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    private void onPhotoCroppedOk() {
        // TODO Auto-generated method stub
        File file = FileUtil.getUploadFile();
        if(file.exists())
            file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bitmap = mCropImage.getCropRectBitmap();
        BitmapUtil.storeBitmapToFile(bitmap, file, PIC_QUALITY);
        Intent intent = new Intent();
        intent.putExtra("filepath", file.getAbsolutePath());
        //intent.putExtra("cropimage", bitmap);// FAILED BINDER TRANSACTION Widget使用的是remoteViews。 Intent传输的bytes不能超过40k。
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CAPTURE){
                onPhotoCropped();
            }
            else if(requestCode == REQUEST_PICK){
                if(data == null){
                    onPhotoCroppedError();
                    return;
                }
                String path = FileUtil.getSmartFilePath(this, data.getData());
                if(!TextUtils.isEmpty(path)){
                    mCaptureFile = new File(path);
                    onPhotoCropped();
                }else{
                    onPhotoCroppedError();
                }
            }
        }
    }

    private void onPhotoCropped() {
        if(mCaptureFile == null || !mCaptureFile.exists()
                || mCaptureFile.length() <= 0){
            onPhotoCroppedError();
            return;
        }
        mLayoutStart.setVisibility(View.GONE);
        mLayoutCrop.setVisibility(View.VISIBLE);

        Bitmap bitmap = BitmapUtil.loadBitmapFromFile(mCaptureFile, mBitmapWidth, mBitmapHeight, BitmapUtil.ResizeMode.Fit);
        if (bitmap != null) {
            mCropImage.setImageBitmap(bitmap);
        }
    }

    private void onPhotoCroppedError(){
        XLTToast.makeText(this, "数据有问题，请重新尝试").show();
        finish();
    }
}