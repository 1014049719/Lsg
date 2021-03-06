package com.talenton.lsg.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Permission;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.app.Activity;
import android.content.ClipData;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

import com.talenton.lsg.base.R;
import com.talenton.lsg.base.XltApplication;
import com.talenton.lsg.base.server.bean.FirstIssueData;

public class SystemUtil {
    public static int CPU_COUNT = 0;
    public static int CPU_FREQ = 800000;
    public static int mWidthPixels = 0;

    private static float sDensityFactor = -1.0f;

    public static final int ORIENTATION_HYSTERESIS = 5;

    public static final String getDeviceUUID(Context context) {

        if (!checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE, R.string.permission_info)){
            return "0";
        }

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();

        return uniqueId;
    }

    public static boolean isWifiConnected() {
        ConnectivityManager conMan = (ConnectivityManager) XltApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (State.CONNECTED.equals(wifi)) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) XltApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    public static boolean isSDExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static Object sLock = new Object();
    private static String sVersionName;
    private static int sVersionCode;

    public static String getVersionName() {
        if (sVersionName == null) {
            synchronized (sLock) {
                sVersionName = getPackageInfo().versionName;
            }
        }
        return sVersionName;
    }

    public static int getVersionCode() {
        if (sVersionCode == 0) {
            synchronized (sLock) {
                sVersionCode = getPackageInfo().versionCode;
            }
        }
        return sVersionCode;
    }

    public static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = XltApplication.getInstance().getPackageManager().getPackageInfo(
                    XltApplication.getInstance().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    public static Object getMetaData(String name) {
        Object value = null;
        ApplicationInfo appInfo = null;
        try {
            appInfo = XltApplication.getAppContext().getPackageManager().getApplicationInfo(
                    XltApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                value = appInfo.metaData.get(name);
            }
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }

    public static FirstIssueData getFirstIssueData(){
        String channel = ChannelUtil.getChannel();
        if(channel.equals("360")){
            return new FirstIssueData(R.mipmap.load_image_failed_200, "http://openbox.mobilem.360.cn/channel/getUrl?src=cp&app=360box");
        }
        else if(channel.equals("huawei")){
            return new FirstIssueData(R.mipmap.load_image_failed_200, null);
        }
        return null;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static List<Camera.Size> getResolutionList(Camera camera)
    {
        Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        return previewSizes;
    }

    public static class ResolutionComparator implements Comparator<Camera.Size>{

        @Override
        public int compare(Size lhs, Size rhs) {
            if(lhs.height!=rhs.height)
                return lhs.height-rhs.height;
            else
                return lhs.width-rhs.width;
        }
    }

    public static void apkInstall(Context context, String filePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(new File(filePath)), type);
        context.startActivity(intent);
    }

    public static float densityFactor(Context context) {
        if (sDensityFactor < 0) {
            WindowManager wMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wMgr.getDefaultDisplay().getMetrics(metrics);
            sDensityFactor = metrics.density;
        }
        return sDensityFactor;
    }

    public static float roundRadiusFactor(Context context, int photoSize) {
		/*
		 * float densityDpi = OsUtil.densityFactor(context); float roundRadius =
		 * 0.0f; if (photoSize <= densityDpi * 45.0f) { roundRadius = 2.0f *
		 * densityDpi; } else if (photoSize <= densityDpi * 60.0f) { roundRadius
		 * = 4.0f * densityDpi; } else if (photoSize > densityDpi * 60.0f){
		 * roundRadius = 4.0f * densityDpi; } return roundRadius;
		 */
        return photoSize * 10.0f / 96; // 按照logo的圆角比例，亦即96x96的图片，圆角半径为10
    }

    public static float cropImageScaleSize(Context context, int photoSize) {
        float densityDpi = SystemUtil.densityFactor(context);
        float size = 1.0f;
        if (densityDpi >= 3) {
            size = 1.0f;
        } else if (densityDpi >= 1.5 && densityDpi < 3) {
            size = 2.0f;
        } else {
            size = 4.0f;
        }
        return size;
    }

    public static int getDisplayWidthPixels(){
        if(mWidthPixels<=0){
            mWidthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        return mWidthPixels;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static float spToPt(int sp) {
        return sp * Resources.getSystem().getDisplayMetrics().density;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getDisplayRotation(Activity activity) {
        int rotation;
        if (Build.VERSION.SDK_INT > 7) {
            rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        } else {
            rotation = activity.getWindowManager().getDefaultDisplay().getOrientation();
        }

        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    @SuppressLint("NewApi")
    public static int getJpegRotation(int cameraId, int orientation) {
        // See android.hardware.Camera.Parameters.setRotation for
        // documentation.
        if (Build.VERSION.SDK_INT < 9) {
            return orientation;
        }
        int rotation = 0;
        if (orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(cameraId, info);

            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else { // back-facing camera
                rotation = (info.orientation + orientation) % 360;
            }
        }
        return rotation;
    }

    public static void initCpuInfo() {
        CPU_FREQ = getMaxCpuFreq();
        if (isSingleCpu()) {
            CPU_COUNT = 1;
        } else if (isQuadCpu()) {
            CPU_COUNT = 4;
        } else {
            CPU_COUNT = 2;
        }
    }

	/*
	 * public static boolean isMobileNO(String mobiles) { Pattern p =
	 * Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); Matcher
	 * m = p.matcher(mobiles); return m.matches(); }
	 */

    public static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation = false;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    public static int versionName2Code(String versionName) {
        if (versionName.endsWith("-SNAPSHOT")) {
            versionName = versionName.substring(0, 5);
        }
        String[] sp = versionName.split("\\.");
        int s1 = Integer.valueOf(sp[0].trim());
        int s2 = Integer.valueOf(sp[1].trim());
        int s3 = Integer.valueOf(sp[2].trim());
        int versionCode = /* (Integer.valueOf(sp[0])) << 24 + */(s1 << 24) + (s2 << 16) + (s3 << 8);
        return versionCode;
    }

    private static int getMaxCpuFreq() {
        String result = "";
        BufferedReader cpuReader = null;
        try {
            cpuReader = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
            result = cpuReader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (cpuReader != null) {
                try {
                    cpuReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (pattern.matcher(result).matches()) {
            if (result.length() > 32) {
                return 0xFFFFFFFF;
            }
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    private static boolean isQuadCpu() {
        try {
            // check whether cpu1 exists.
            return (new File("/sys/devices/system/cpu/cpu3").exists());
        } catch (Exception e) {
            // Default to return true
            return false;
        }
    }

    private static boolean isSingleCpu() {
        try {
            // check whether cpu1 exists.
            return !(new File("/sys/devices/system/cpu/cpu1").exists());
        } catch (Exception e) {
            // Default to return true
            return true;
        }
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static boolean isSimExsit(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (manager.getSimState() == TelephonyManager.SIM_STATE_READY);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static boolean copyToClipBoard(Context context, String text) {
        try {
            int apiLevel = android.os.Build.VERSION.SDK_INT;
            Object service = context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (apiLevel < 11) {
                android.text.ClipboardManager cb = (android.text.ClipboardManager) service;
                cb.setText(text);
            } else {
                android.content.ClipboardManager cb = (android.content.ClipboardManager) service;
                cb.setText(text);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getClipboardsContent(Context context) {
        String string;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            string = getClipboardsContentV11(context);
        } else {
            string = getClipboardsContentV9(context);
        }
        return string;
    }

    public static String splitPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        Pattern pattern = Pattern.compile("[^0-9+*#]");
        String[] strings = pattern.split(phone);
        if (strings != null && strings.length > 0) {
            return strings[strings.length - 1];
        }
        return phone;
    }

    @SuppressLint("NewApi")
    private static String getClipboardsContentV11(Context context) {
        android.content.ClipboardManager cm = (android.content.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            ClipData cd = cm.getPrimaryClip();
            if (cd != null && cd.getItemCount() > 0) {
                if (cd.getItemAt(0) != null && cd.getItemAt(0).getText() != null) {
                    return cd.getItemAt(0).getText().toString();
                }
            }
        }
        return null;
    }

    private static String getClipboardsContentV9(Context context) {
        android.text.ClipboardManager cm = (android.text.ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null && cm.getText() != null) {
            return cm.getText().toString();
        }
        return null;
    }

    public static byte[] objectToByte(java.lang.Object obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static Object byteToObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();

            bi.close();
            oi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获取系统版本
     * @return
     */
    public static String getSystemVersion(){
        return android.os.Build.VERSION.SDK;
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getSystemType(){
        return android.os.Build.VERSION.RELEASE;
    }


    public static boolean isRunningForeground (Context context)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))
        {
            return true ;
        }
        return false ;
    }

    public static boolean checkSelfPermission(@NonNull Context context, @NonNull String permission, int resId){
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            //ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            XLTToast.makeText(context, resId).show();
            return false;
        }
        return true;
    }

    public boolean checkSelfPermission(@NonNull Activity activity, @NonNull String permission, int requestCode){
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }
}
