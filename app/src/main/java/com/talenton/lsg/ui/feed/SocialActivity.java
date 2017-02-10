package com.talenton.lsg.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.talenton.lsg.BaseCompatActivity;
import com.talenton.lsg.R;
import com.talenton.lsg.base.AppConfig;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.bean.ObjectCode;
import com.talenton.lsg.base.util.AppLogger;
import com.talenton.lsg.base.util.BitmapUtil;
import com.talenton.lsg.server.DiscoveryServer;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.discovery.ShareParam;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SocialActivity extends BaseCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public final static int SOCIAL_ACTION_BABY = 0; //宝宝圈分享
    public final static int SOCIAL_ACTION_TOPIC = 1;//话题分享
    public final static int SOCIAL_ACTION_BABY_DETAIL = 2;//宝宝圈详情分享

    private final static int SOCIAL_TYPE_CIRCLE = 1;
    private final static int SOCIAL_TYPE_WECHAT = 2;
    private final static int SOCIAL_TYPE_WECHAT_CIRCLE = 3;
    private final static int SOCIAL_TYPE_QQ = 4;
    private final static int SOCIAL_TYPE_QQ_ZONE = 5;

    public static final String KEY_FEEDS_ID = "feeds_id";
    public static final String KEY_FEEDS_TID = "feeds_tid";
    public static final String KEY_FEEDS_CIRCLE_ID = "feeds_circle_id";
    public static final String KEY_ARTICLE_CONTENT = "article_content";
    public static final String KEY_ARTICLE_IMAGE = "article_image";
    public static final String KEY_SHARED_IMAGE = "shared_image";
    public static final String KEY_SHARED_TITLE = "shared_title";
    public static final String KEY_SHARED_CONTENT = "shared_content";
    public static final String KEY_SHARED_URL = "shared_url";
    public static final String KEY_SHARED_FROM = "shared_from";
    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final String KEY_ACTION_ID = "key_action_id";
    private static final int THUMB_SIZE = 80;
    private String mSharedFrom;
    private String mFeedsId;
    private long mTid, mCircleId;
    private String mSharedUrl;
    private String mSharedTitle;
    private String mSharedContent;
    private String mSharedImage;
    private int mActionId;

    private int sharetype;
    private int shareid;

    private GridView mList;
    private GridAdapter mListAdapter;
    private ArrayList<SocialItem> mSocialItem;

    private IWXAPI api;
    private Tencent mTencent;

    public static void startSocialActivity(Context context, Feeds feeds, int actionId) {
        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(KEY_FEEDS_ID, feeds.guid);
        intent.putExtra(KEY_FEEDS_TID, feeds.tid);
        intent.putExtra(KEY_FEEDS_CIRCLE_ID, feeds.circle_id);
        intent.putExtra(KEY_ARTICLE_CONTENT, feeds.content);
        intent.putExtra(KEY_ACTION_ID, actionId);
        if ((feeds.attachinfo == null) || (feeds.attachinfo.isEmpty())) {
            intent.putExtra(KEY_ARTICLE_IMAGE, "");
        } else {
            intent.putExtra(KEY_ARTICLE_IMAGE, feeds.attachinfo.get(0).filepath);
        }
//		Log.d("20150813", "KEY_ARTICLE_IMAGE="+intent.getStringExtra(KEY_ARTICLE_IMAGE));
        intent.putExtra(KEY_SHARED_TITLE, TextUtils.isEmpty(feeds.title) ? feeds.content : feeds.title);
        intent.putExtra(KEY_SHARED_URL, AppConfig.HOME_URL + "h5.php?mod=share_topic&cmdcode=132&tid=" + feeds.tid);
        context.startActivity(intent);
    }

    public static void startSocialActivity(Context context, String title, String content, String url, int actionId){
        startSocialActivity(context, title, content, url, actionId, 0, 0);
    }

    public static void startSocialActivity(Context context, String title, String content, String url, int actionId,int sharetype,int shareid) {
        if (TextUtils.isEmpty(url)) return;

        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(KEY_FEEDS_ID, "1");
        intent.putExtra(KEY_ARTICLE_CONTENT, content);

        //分享活动id
        intent.putExtra("sharetype",sharetype);
        intent.putExtra("shareid",shareid);

//		Log.d("20150813", "KEY_ARTICLE_IMAGE="+intent.getStringExtra(KEY_ARTICLE_IMAGE));
        intent.putExtra(KEY_SHARED_TITLE, title);
        intent.putExtra(KEY_ACTION_ID, actionId);
        if (!url.startsWith("http://")) {
            url = AppConfig.HOME_URL + url;
        }
        intent.putExtra(KEY_SHARED_URL, url);
        context.startActivity(intent);
    }


    public static void startSocialActivity(Context context, String title, String content, String imgUrl, String url) {
        if (TextUtils.isEmpty(url)) return;

        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(KEY_FEEDS_ID, "1");
        intent.putExtra(KEY_ARTICLE_CONTENT, content);
//		Log.d("20150813", "KEY_ARTICLE_IMAGE="+intent.getStringExtra(KEY_ARTICLE_IMAGE));
        intent.putExtra(KEY_SHARED_TITLE, title);
        intent.putExtra(KEY_ACTION_ID, 0);
        intent.putExtra(KEY_ARTICLE_IMAGE, imgUrl);
        if (!url.startsWith("http://")) {
            url = AppConfig.HOME_URL + url;
        }
        intent.putExtra(KEY_SHARED_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_social);
        Intent intent = getIntent();
        if (intent != null) {
            mFeedsId = intent.getStringExtra(KEY_FEEDS_ID);
            mSharedTitle = intent.getStringExtra(KEY_SHARED_TITLE);
            mSharedContent = intent.getStringExtra(KEY_ARTICLE_CONTENT);
            mSharedUrl = intent.getStringExtra(KEY_SHARED_URL);
            mSharedImage = intent.getStringExtra(KEY_ARTICLE_IMAGE);
            mTid = intent.getLongExtra(KEY_FEEDS_TID, 0);
            mCircleId = intent.getLongExtra(KEY_FEEDS_CIRCLE_ID, 0);
            mActionId = intent.getIntExtra(KEY_ACTION_ID, SOCIAL_ACTION_BABY);
            sharetype=intent.getIntExtra("sharetype", 0);
            shareid=intent.getIntExtra("shareid",0);
            if ((mSharedTitle == null) || (mSharedTitle.length() == 0))
                mSharedTitle = getString(R.string.app_name);
            if ((mSharedUrl == null) || (mSharedUrl.length() == 0))
                mSharedUrl = AppConfig.HOME_URL;
            if ((mSharedImage == null) || (mSharedImage.length() == 0))
                mSharedImage = AppConfig.HOME_URL + getString(R.string.circle_logo_icon);
        }
        if (TextUtils.isEmpty(mFeedsId)) {
            finish();
            overridePendingTransition(0, 0);
        }
        api = WXAPIFactory.createWXAPI(SocialActivity.this, UserServer.WX_APP_ID, true);
        boolean br = api.registerApp(UserServer.WX_APP_ID);
//		Log.d("20150813", "WX_APP_ID="+UserService.WX_APP_ID+ " bf="+br+" api="+api.toString());
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(UserServer.QQ_APP_ID, getApplicationContext());
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);


        mList = (GridView) findViewById(R.id.grid);
        mList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mListAdapter = new GridAdapter(SocialActivity.this, R.layout.item_social);


        mSocialItem = new ArrayList<SocialItem>(5);
        if (mActionId == SOCIAL_ACTION_TOPIC && UserServer.getCurrentUser().getUid() > 0) {
            mList.setNumColumns(5);
            mSocialItem.add(new SocialItem(SOCIAL_TYPE_CIRCLE, R.mipmap.social_circle, R.string.social_circle));
        }
        mSocialItem.add(new SocialItem(SOCIAL_TYPE_WECHAT, R.mipmap.social_weixin, R.string.social_weixin));
        mSocialItem.add(new SocialItem(SOCIAL_TYPE_WECHAT_CIRCLE, R.mipmap.social_friend, R.string.social_friend));
        mSocialItem.add(new SocialItem(SOCIAL_TYPE_QQ, R.mipmap.social_qq, R.string.social_qq));
        mSocialItem.add(new SocialItem(SOCIAL_TYPE_QQ_ZONE, R.mipmap.social_qzone, R.string.social_qzone));
        mListAdapter.setData(mSocialItem);
        mList.setAdapter(mListAdapter);
        mList.setOnItemClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SocialItem item = (SocialItem) parent.getAdapter().getItem(position);
        social(item);
        if(shareid > 0)
            reportShareData();
    }

    private class GridAdapter extends ArrayAdapter<SocialItem> {
        private LayoutInflater mInflater;
        private int mResource;

        public GridAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResource = textViewResourceId;

        }

        public void setData(List<SocialItem> data) {
            setNotifyOnChange(false);
            if (data != null) {
                for (SocialItem item : data) {
                    add(item);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = mInflater.inflate(mResource, parent, false);
            } else {
                view = convertView;
            }
            ImageView logo = (ImageView) view.findViewById(R.id.social_logo);
            TextView name = (TextView) view.findViewById(R.id.social_name);
            SocialItem item = getItem(position);
            logo.setImageResource(item.logo);
            name.setText(item.name);
            view.setTag(item.name);
            return view;
        }
    }

    private class SocialItem {
        public SocialItem(int type, int logo, int name) {
            this.type = type;
            this.logo = logo;
            this.name = name;
        }

        int type;
        int logo;
        int name;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back) {
            finish();
            overridePendingTransition(0, 0);
        }
        if (id == R.id.cancel) {
            finish();
            overridePendingTransition(0, 0);
        }

    }

    private void social(SocialItem item) {
        if (mActionId == SOCIAL_ACTION_BABY && mTid > 0) {
            FeedServer.incrementBrowswerNum(mTid);
        }
        switch (item.type) {
            case SOCIAL_TYPE_CIRCLE:
                SocialCircleActivity.startSocialCircleActivity(this, mTid, mCircleId);
                finish();
                overridePendingTransition(0, 0);
                break;
            case SOCIAL_TYPE_WECHAT:
                try {
                    ImageLoader.getInstance().loadImage(mSharedImage, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = mSharedUrl;//"http://www.baidu.com";
                            WXMediaMessage msg = new WXMediaMessage();
                            msg.mediaObject = webpage;
//							Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(loadedImage, THUMB_SIZE, THUMB_SIZE, true);
//							loadedImage.recycle();
                            msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
//							msg.thumbData = BitmapUtil.bmpToByteArray(loadedImage, true);
                            // 发送文本类型的消息时，title字段不起作用
                            msg.title = mSharedTitle;
                            if(!TextUtils.isEmpty(mSharedContent) && mSharedContent.length() > 500){
                                mSharedContent = mSharedContent.substring(0, 500);
                            }
                            msg.description = mSharedContent;
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("img");
                            req.message = msg;
                            req.scene = SendMessageToWX.Req.WXSceneSession;
                            boolean br = api.sendReq(req);
                            finish();
                            overridePendingTransition(0, 0);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = mSharedUrl;//"http://www.baidu.com";
                            WXMediaMessage msg = new WXMediaMessage();
                            msg.mediaObject = webpage;
                            Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//加载失败，选本地图标代替
                            msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
                            msg.title = mSharedTitle;
                            if(!TextUtils.isEmpty(mSharedContent) && mSharedContent.length() > 500){
                                mSharedContent = mSharedContent.substring(0, 500);
                            }
                            msg.description = mSharedContent;
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("img");
                            req.message = msg;
                            req.scene = SendMessageToWX.Req.WXSceneSession;
                            boolean br = api.sendReq(req);
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SOCIAL_TYPE_WECHAT_CIRCLE:
                try {
                    ImageLoader.getInstance().loadImage(mSharedImage, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = mSharedUrl;//"http://www.baidu.com";
                            WXMediaMessage msg = new WXMediaMessage();
                            msg.mediaObject = webpage;
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(loadedImage, THUMB_SIZE, THUMB_SIZE, true);
                            msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
                            // 发送文本类型的消息时，title字段不起作用
                            msg.title = mSharedTitle;
                            msg.description = mSharedContent;
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("img");
                            req.message = msg;
                            req.scene = SendMessageToWX.Req.WXSceneTimeline;
                            api.sendReq(req);
                            finish();
                            overridePendingTransition(0, 0);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            // TODO Auto-generated method stub
                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = mSharedUrl;//"http://www.baidu.com";
                            WXMediaMessage msg = new WXMediaMessage();
                            msg.mediaObject = webpage;
                            Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//加载失败，选本地图标代替
                            msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
                            // 发送文本类型的消息时，title字段不起作用
                            msg.title = mSharedTitle;
                            msg.description = mSharedContent;
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("img");
                            req.message = msg;
                            req.scene = SendMessageToWX.Req.WXSceneTimeline;
                            api.sendReq(req);
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SOCIAL_TYPE_QQ:
                try {
//				 Log.d("20150813", "2 mSharedImage="+mSharedImage);
                    Bundle params = new Bundle();
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, mSharedTitle);
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mSharedContent);
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mSharedUrl);
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mSharedImage);
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
                    mTencent.shareToQQ(this, params, new BaseUiListener());
                    finish();
                    overridePendingTransition(0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SOCIAL_TYPE_QQ_ZONE:
                try {
//				 Log.d("20150813", "3 mSharedImage="+mSharedImage);
                    Bundle params = new Bundle();
                    //分享类型
                    ArrayList<String> image_url = new ArrayList<String>();
                    image_url.add(mSharedImage);
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mSharedTitle);
                    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mSharedContent);
                    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, mSharedUrl);
                    params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, mSharedImage);
                    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, image_url);
                    mTencent.shareToQzone(this, params, new BaseUiListener());
                    finish();
                    overridePendingTransition(0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onComplete(Object arg0) {
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //上报分享
    protected void reportShareData(){

        ShareParam shareParam=new ShareParam();
        shareParam.type=sharetype;
        shareParam.blogid=shareid;
        DiscoveryServer.reportShareData(shareParam, new XLTResponseCallback<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode data, XLTError error) {
            }
        });

    }
}
