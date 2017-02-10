package com.talenton.lsg.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.bean.BabyData;
import com.talenton.lsg.base.server.bean.RspLogin;
import com.talenton.lsg.base.server.bean.UserInfo;
import com.talenton.lsg.base.util.DateUtil;
import com.talenton.lsg.base.util.FileUtil;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.base.widget.CircleImageView;
import com.talenton.lsg.base.widget.TimePopupWindow;
import com.talenton.lsg.server.MineServer;
import com.talenton.lsg.server.bean.user.RspObject;
import com.talenton.lsg.server.bean.user.UploadFile;
import com.talenton.lsg.util.UIHelper;
import com.talenton.lsg.widget.RegionSelectFragment;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;

public class PerfectPersonalInfoActivity extends BaseCompatActivity implements View.OnClickListener {

    private static final int RELATION_CAPTURE_PICK = 501;
    private static final int REQUEST_SELECT_SCHOOL = 502;

    private EditText mName, mBabyName;
    private TextView mBabyBirthDate, mBabySchool, mRegion;
    private CircleImageView mCropImage;
    private TimePopupWindow mPwTime = null;
    private long mGraphtime;
    private boolean isModifyPhoto = false;
    private long schoolid = 0;
    private String mProvince, mCity, mArea;
    private boolean isFromChildInfo;

    public static void startPerfectPersonalInfoActivity(Context context,boolean isFromChildInfo){
        Intent intent = new Intent(context,PerfectPersonalInfoActivity.class);
        intent.putExtra("is_from_child_info",isFromChildInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_personal_info);

        mName = (EditText) findViewById(R.id.et_name);
        mBabyName = (EditText) findViewById(R.id.et_babyname);
        mBabyBirthDate = (TextView) findViewById(R.id.tv_baby_birth_date);
        mBabySchool = (TextView) findViewById(R.id.tv_baby_school);
        mBabySchool.setOnClickListener(this);
        mRegion = (TextView) findViewById(R.id.tv_region);
        mCropImage = (CircleImageView) findViewById(R.id.crop_image);
        mCropImage.setOnClickListener(this);
        findViewById(R.id.layout_baby_birth_date).setOnClickListener(this);
        findViewById(R.id.layout_region).setOnClickListener(this);
        findViewById(R.id.tv_login_next).setOnClickListener(this);

        mGraphtime = System.currentTimeMillis() / 1000;
        isFromChildInfo = getIntent().getBooleanExtra("is_from_child_info",false);

        initDatas();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        mPwTime = null;
    }

    private void initDatas() {
        UserInfo userInfo = UserServer.getCurrentUser();
        if (userInfo == null || userInfo.uid <= 0) return;
        if (!TextUtils.isEmpty(userInfo.realname))
            mName.setText(userInfo.realname);

        if (!TextUtils.isEmpty(userInfo.avartar))
            ImageLoader.getInstance().displayImage(userInfo.avartar, mCropImage, ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER_200);

        BabyData babyData = userInfo.getBaobaodata();
        if (babyData != null && babyData.baobaouid > 0) {
            mGraphtime = babyData.birthday;
            schoolid = babyData.schoolid;
            mBabyName.setText(babyData.name);

            if (babyData.schooldata != null && !TextUtils.isEmpty(babyData.schooldata.name)) {
                mBabySchool.setText(babyData.schooldata.name);
            }
        }
        if (mGraphtime <= 0) {
            mGraphtime = System.currentTimeMillis() / 1000;
        } else if (mGraphtime >= 2051197261L) {
            // 2035-01-01 01:01:01
            mGraphtime = System.currentTimeMillis() / 1000;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crop_image:
                Intent intent = new Intent(this, CaptureAndPickActivity.class);
                startActivityForResult(intent, RELATION_CAPTURE_PICK);
                break;
            case R.id.tv_baby_school:
                if (TextUtils.isEmpty(mProvince)) {
                    XLTToast.makeText(this, "请先选择所在地区").show();
                    return;
                }
                Intent intent2 = new Intent(this, SelectSchoolActivity.class);
                intent2.putExtra("province", mProvince);
                intent2.putExtra("city", mCity);
                intent2.putExtra("area", mArea);
                startActivityForResult(intent2, REQUEST_SELECT_SCHOOL);
                break;
            case R.id.layout_region:
                RegionSelectFragment fragment = RegionSelectFragment.newInstance(
                    new RegionSelectFragment.OnClickReginSelectListener() {

                        @Override
                        public void onData(String province, String city, String area) {
                            mProvince = province;
                            mCity = city;
                            mArea = area;
                            mRegion.setText(String.format("%s%s%s", province, city, area));
                        }
                    });
                UIHelper.showDialog(this, fragment, "RegionSelectFragment");
                break;
            case R.id.layout_baby_birth_date:

                if (mPwTime == null) {
                    mPwTime = new TimePopupWindow(this);
                    // 时间选择后回调
                    mPwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

                        @Override
                        public void onTimeSelect(Date date) {
                            long time = date.getTime() / 1000;
                            long now = System.currentTimeMillis() / 1000;
                            if (time > now) {
                                return;
                            }
                            mGraphtime = time;
                            mBabyBirthDate.setText(DateUtil.parseTimeToYMD(mGraphtime));
                        }
                    });
                    mPwTime.setOnDismissListener(new PopupWindow.OnDismissListener() {

                        @Override
                        public void onDismiss() {
                        }
                    });
                }
                mPwTime.setDate(new Date(mGraphtime * 1000));
                mPwTime.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_login_next:
                savePersonalInfo();
                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.user_perfect_personal;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RELATION_CAPTURE_PICK:
                if (resultCode == RESULT_OK && data != null) {
                    File file = FileUtil.getUploadFile();
                    if (file.exists() && file.length() > 0) {
                        isModifyPhoto = true;
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        if (bitmap != null) {
                            mCropImage.setImageBitmap(bitmap);
                        }
                    }
                }
                break;
            case REQUEST_SELECT_SCHOOL:
                if (resultCode == RESULT_OK && data != null) {
                    String name = data.getStringExtra("schoolname");
                    if (!TextUtils.isEmpty(name)) {
                        mBabySchool.setText(name);
                        schoolid = data.getLongExtra("schoolid", 0);
                    }
                }
                break;
        }
    }

    private void savePersonalInfo() {
        final String relname = mName.getText().toString().trim();
        if (TextUtils.isEmpty(relname)) {
            XLTToast.makeText(this, "姓名或昵称不能为空").show();
            return;
        }
        if (TextUtils.isEmpty(mBabyBirthDate.getText().toString())) {
            XLTToast.makeText(this, "宝宝出生日期不能为空").show();
            return;
        }
        String schoolname = mBabySchool.getText().toString();
        if (TextUtils.isEmpty(schoolname)) {
            XLTToast.makeText(this, "宝宝所在学校不能为空").show();
            return;
        }
        if (mGraphtime <= 0) {
            mGraphtime = System.currentTimeMillis() / 1000;
        } else if (mGraphtime >= 2051197261L) {
            // 2035-01-01 01:01:01
            mGraphtime = System.currentTimeMillis() / 1000;
        }

        showProgress(R.string.main_processing);
        if (isModifyPhoto) {
            MineServer.uploadFile(UserServer.getCurrentUser().getUid(), FileUtil.getUploadFile(),
                    new XLTResponseCallback<UploadFile>() {

                        @Override
                        public void onResponse(UploadFile data, XLTError error) {
                            hideProgress();
                            if (error == null && data != null) {
                                isModifyPhoto = false;
                                //XLTToast.makeText(PerfectPersonalInfoActivity.this, "头像上传成功").show();
                            } else {
                                XLTToast.makeText(PerfectPersonalInfoActivity.this, error.getMesssage()).show();
                            }
                        }
                    });
        }

        final UserInfo userInfo = new UserInfo();
        userInfo.realname = relname;
        BabyData baby =userInfo.getBaobaodata();
        baby.name = mBabyName.getText().toString().trim();
        baby.resideprovince = mProvince;
        baby.residecity = mCity;
        baby.residedist = mArea;
        baby.schoolid = schoolid;
        baby.classname = schoolname;
        baby.birthday = mGraphtime;

        MineServer.modifiyPersonal(relname, baby, new XLTResponseCallback<RspObject>() {
            @Override
            public void onResponse(RspObject data, XLTError error) {
                hideProgress();
                if (error == null && data != null) {
                    if (data.baobaodata != null) {
                        RspLogin sRspLogin = UserServer.getRspLogin();
                        if (sRspLogin != null && sRspLogin.member != null) {
                            sRspLogin.member.realname = relname;
                            sRspLogin.member.getBaobaodataList().addAll(data.baobaodata);
                            UserServer.setRspLogin(sRspLogin);
                            if (!isFromChildInfo){
                                XLTToast.makeText(PerfectPersonalInfoActivity.this, R.string.main_save_success).show();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                            }else {
                                ChildInfoActivity.startChildInfoActivity(PerfectPersonalInfoActivity.this);
                            }
                            finish();
                            return;
                        }
                    }
                    XLTToast.makeText(PerfectPersonalInfoActivity.this, "保存失败").show();
                } else {
                    XLTToast.makeText(PerfectPersonalInfoActivity.this, error.getMesssage()).show();
                }
            }
        });
    }
}
