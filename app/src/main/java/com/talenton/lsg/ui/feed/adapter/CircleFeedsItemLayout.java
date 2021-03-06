package com.talenton.lsg.ui.feed.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.talenton.lsg.R;
import com.talenton.lsg.base.server.task.TaskBase;
import com.talenton.lsg.base.util.DateUtil;
import com.talenton.lsg.base.util.ImageLoaderManager;
import com.talenton.lsg.base.widget.CommentTextView;
import com.talenton.lsg.base.widget.OptimizeGridView;
import com.talenton.lsg.server.FeedServer;
import com.talenton.lsg.server.bean.feed.CircleMember;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.MediaBean;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.ui.ImageDetailViewerActivity;
import com.talenton.lsg.ui.feed.OnFeedsClickListener;
import com.talenton.lsg.ui.school.PlayerActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/14.
 */
public class CircleFeedsItemLayout {
    public Context mContext;
    public Feeds feeds;
    public ItemViewHolder holder;
    private static int imageWidth[] = null;
    private static int gridViewWidth = 0;
    private View.OnClickListener basicClickListener;
    private OnFeedsClickListener mOnFeedsClickListener;
    private LayoutInflater inflater;
    //private PostToParam mPostToParam;

    private static AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object tag = parent.getTag();
            if (tag instanceof MediaBean) {
                MediaBean media = (MediaBean) tag;
                if (media != null) {
                    //VideoPlayerActivity.startVideoPlayerActivity(parent.getContext(), media);
                    PlayerActivity.startPlayerActivity(parent.getContext(), media.getVideoUrl());
                }
            } else if (tag instanceof ArrayList) {

                ArrayList<String> urls = (ArrayList<String>) tag;
                if (urls != null && urls.size() > 0) {
                    ImageDetailViewerActivity.startViewImage(parent.getContext(), urls, position);
                }
            }

        }
    };
    private static OptimizeGridView.OnTouchItemListener mOnTouchItemListener = new OptimizeGridView.OnTouchItemListener() {

        @Override
        public boolean onTouchItem(View parent, int motionPosition, int motionEvent) {
            if (motionEvent == MotionEvent.ACTION_UP) {
                Object tag = parent.getTag();
                if (tag instanceof MediaBean) {
                    MediaBean media = (MediaBean) tag;
                    if (media != null) {
                        //VideoPlayerActivity.startVideoPlayerActivity(parent.getContext(), media);
                        PlayerActivity.startPlayerActivity(parent.getContext(), media.getVideoUrl());
                    }
                } else if (tag instanceof ArrayList) {
                    ArrayList<String> urls = (ArrayList<String>) tag;
                    if (urls != null && motionPosition < urls.size()) {
                        ImageDetailViewerActivity.startViewImage(parent.getContext(), urls, motionPosition);
                    }
                }
                return false; // 不终止路由事件让父级控件处理事件
            } else {
                return true;
            }

        }
    };

    public static class ItemViewHolder {
        ImageView mAuthorPhoto;
        TextView mAuthorName;
        TextView mNickName;
        TextView mPublishTime;
        TextView mTextContent;

        View mSendProgressbar;
        TextView mSendFeedFailedView;
        OptimizeGridView mPhotoGridView;
        View mItemContainer;
        TextView mTitle, mCircleName, mLikeNum, mReplyNum, mBrowserNum;
        public View mRoot, mLayoutInfo;
    }

    /**
     * Feeds view holder
     *            view type BbGroupFeedsFragment.FEEDS_BBG or
     *            BbGroupFeedsFragment.FEEDS_CLASS
     */
    public CircleFeedsItemLayout(Context context, View convertView, OnFeedsClickListener onFeedsClickListener) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mOnFeedsClickListener = onFeedsClickListener;
        initViewItems(convertView);
    }

    public ItemViewHolder getHolder() {
        return holder;
    }

    /**
     * @param feed
     *  hidenFeeds
     *            界面外部当前隐藏的feedsid
     *  param
     *            上一行的拍照时间
     */
    public void setFeeds(Feeds feed) {
        this.feeds = feed;
        fillData();
    }

    private void initViewListeners() {
        basicClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnFeedsClickListener != null) {
                    mOnFeedsClickListener.onCircleFeedsClick(CircleFeedsItemLayout.this, view);
                    return;
                }
            }
        };
        holder.mItemContainer.setOnClickListener(basicClickListener);

        if (holder.mLayoutInfo != null) {
            holder.mLayoutInfo.setOnClickListener(basicClickListener);
        }
        if (holder.mTitle != null) {
            holder.mTitle.setOnClickListener(basicClickListener);
        }
        initGridViewListener(holder.mPhotoGridView);
        holder.mAuthorPhoto.setOnClickListener(basicClickListener);
        if (holder.mSendFeedFailedView != null) {
            holder.mSendFeedFailedView.setOnClickListener(basicClickListener);
        }
    }

    private void initGridViewListener(OptimizeGridView gridView) {

        gridView.setOnItemClickListener(mOnItemClickListener);

        gridView.setOnTouchInvalidPositionListener(OptimizeGridView.defaultOnTouchInvalidPositionListener);

        gridView.setOnTouchItemListener(mOnTouchItemListener);
    }

    public void initViewItems(View mainLayout) {
        holder = new ItemViewHolder();
        holder.mRoot = mainLayout;
        holder.mAuthorPhoto = (ImageView) mainLayout.findViewById(R.id.user_logo);
        holder.mAuthorName = (TextView) mainLayout.findViewById(R.id.user_name);
        holder.mNickName = (TextView) mainLayout.findViewById(R.id.nick_name);
        holder.mPublishTime = (TextView) mainLayout.findViewById(R.id.publish_time);
        holder.mTextContent = (TextView) mainLayout.findViewById(R.id.feeds_text_content);


        holder.mPhotoGridView = (OptimizeGridView) mainLayout.findViewById(R.id.feeds_content_gridview);
        holder.mSendFeedFailedView = (TextView) mainLayout.findViewById(R.id.feeds_send_failed_view);
        holder.mSendProgressbar = mainLayout.findViewById(R.id.feeds_send_progressbar);

        holder.mItemContainer = mainLayout.findViewById(R.id.feeds_container);

        holder.mLayoutInfo = mainLayout.findViewById(R.id.layout_info);
        holder.mTitle = (TextView)mainLayout.findViewById(R.id.tv_title);
        holder.mReplyNum = (TextView)mainLayout.findViewById(R.id.reply_num);
        holder.mBrowserNum = (TextView)mainLayout.findViewById(R.id.browser_num);
        holder.mLikeNum = (TextView)mainLayout.findViewById(R.id.like_num);
        holder.mCircleName = (TextView)mainLayout.findViewById(R.id.circle_name);

        initViewListeners();
    }

    private void fillData() {
        fillAuthorBasicInfo();
        fillTextContent();
        fillPhotoContent(feeds, holder.mPhotoGridView);
    }

    private void setImageWidth() {
        if (imageWidth == null || imageWidth.length == 0) {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            //int margin = mContext.getResources().getDimensionPixelSize(R.dimen.space_47_5);
            int margin = 0;
            margin += mContext.getResources().getDimensionPixelSize(R.dimen.space_15_0) * 2;
            margin += mContext.getResources().getDimensionPixelSize(R.dimen.space_7_5) * 2;
            int space = mContext.getResources().getDimensionPixelSize(R.dimen.space_5_0);
            imageWidth = new int[3];
            for (int i = 0; i < 3; ++i) {
                imageWidth[i] = (dm.widthPixels - margin - space * i) / (i + 1);
            }
            imageWidth[0] = imageWidth[0] > 500 ? 500 : imageWidth[0];
            //imageWidth[1] = imageWidth[1] > 200 ? 200 : imageWidth[1];
            imageWidth[1] = imageWidth[2] = imageWidth[2] > 200 ? 200 : imageWidth[2];
            gridViewWidth = imageWidth[2] * 3 + space*2;
        }
    }

    private void fillTextContent() {
        // String content = String.format("%d\n(%s,local:%d)\n%s\n%s",
        // feeds.from, feeds.guid, feeds.localid, feeds.content,
        // TimeUtil.parseTimeToYMDHMS(new Date(feeds.graphtime * 1000)));
        String content = feeds.content;
        if (TextUtils.isEmpty(content)) {
            holder.mTextContent.setVisibility(View.GONE);
        } else {
            if (content.length() > 43){
                content = FeedServer.subContext(feeds.content, FeedServer.endTopicContent);
            }
            holder.mTextContent.setVisibility(View.VISIBLE);
            holder.mTextContent.setText(content);
        }


        if(TextUtils.isEmpty(feeds.title)){
            holder.mTitle.setVisibility(View.GONE);
        }else {
            holder.mTitle.setVisibility(View.VISIBLE);
            holder.mTitle.setText(feeds.title);
        }
        if(feeds.commentcount > 0){
            holder.mReplyNum.setVisibility(View.VISIBLE);
            holder.mReplyNum.setText(String.format("%d回复", feeds.commentcount));
        }else {
            holder.mReplyNum.setVisibility(View.GONE);
        }
    }

    private void fillPhotoContent(Feeds feeds, OptimizeGridView photoGridView) {
        ArrayList<MediaBean> pics = feeds.attachinfo;
        holder.mPublishTime.setText(DateUtil.parseTime(mContext, feeds.modify_time));

        boolean hasMedia = false;
        int resourceId = R.layout.item_feeds_photo_gridview;
        ArrayList<String> urls = new ArrayList<String>();
        if (pics != null) {
            photoGridView.setTag(urls);
            for (MediaBean pic : pics) {
                if (pic == null) {
                    break;
                }
                hasMedia = true;
                if (pic.itype == MediaBean.TYPE_VIDEO) {
                    resourceId = R.layout.item_feeds_video_gridview;
                    photoGridView.setTag(pic);
                    break;
                } else {
                    urls.add(pic.genUrl());
                }
            }
        }

        if (hasMedia) {
            setImageWidth();
            PhotoGridViewAdapter photoAdapter = new PhotoGridViewAdapter(null, resourceId);
            photoGridView.setAdapter(photoAdapter);
            photoGridView.setVisibility(View.VISIBLE);
            if(pics.size() > 1){
                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams.width = gridViewWidth;
                photoGridView.setLayoutParams(linearParams);
            }

            photoGridView.setNumColumns((pics.size() < 2) ? pics.size() : 3);

            photoAdapter.setPicinfos(pics);
            photoAdapter.notifyDataSetChanged();
        } else {
            photoGridView.setTag(null);
            photoGridView.setVisibility(View.GONE);
        }
    }

    private void fillAuthorBasicInfo() {

        ImageLoader.getInstance().displayImage(feeds.getCircleMember().avatar, holder.mAuthorPhoto,
                ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
        holder.mAuthorName.setText(feeds.crerealname);
        //String nickName = String.format("来自[%s]", feeds.circle_name);
        //holder.mNickName.setText(nickName);
        int groupid = feeds.getCircleMember().groupid;
        if (groupid == CircleMember.GROUP_TEACHER){
            holder.mNickName.setVisibility(View.VISIBLE);
            holder.mNickName.setText("老师");
        }else {
            holder.mNickName.setVisibility(View.GONE);
        }

        if (feeds.from == Feeds.FROM_TASK) {
            //holder.mLevel.setVisibility(View.GONE);
            holder.mPublishTime.setVisibility(View.GONE);
            if (feeds.taskStatus == TaskBase.STATUS_FAILED || feeds.taskStatus == TaskBase.STATUS_CANCELED) {
                holder.mSendFeedFailedView.setVisibility(View.VISIBLE);
                holder.mSendProgressbar.setVisibility(View.GONE);
                holder.mSendFeedFailedView.setText("");
            } else if (feeds.taskStatus == TaskBase.STATUS_PENDING || feeds.taskStatus == TaskBase.STATUS_READY) {
                holder.mSendFeedFailedView.setVisibility(View.GONE);
                holder.mSendProgressbar.setVisibility(View.VISIBLE);
            }
        } else {
            //holder.mLevel.setVisibility(View.VISIBLE);
            //holder.mLevel.setText(String.format("LV%d", feeds.crelevel));
            holder.mPublishTime.setVisibility(View.VISIBLE);
            holder.mSendFeedFailedView.setVisibility(View.GONE);
            holder.mSendProgressbar.setVisibility(View.GONE);
        }
    }

    private class PhotoGridViewAdapter extends BaseAdapter {
        private ArrayList<MediaBean> picinfos;
        private int columns = 0;
        private int resourceId;

        // private boolean update;

        public void setPicinfos(ArrayList<MediaBean> picinfos) {
            this.picinfos = picinfos;
            if (picinfos != null && picinfos.size() > 0) {
                this.columns = (picinfos.size() < 3) ? picinfos.size() : 3;
            }
        }

        public PhotoGridViewAdapter(ArrayList<MediaBean> picinfos, int resourceId) {
            this.picinfos = picinfos;
            this.resourceId = resourceId;
            if (picinfos != null && picinfos.size() > 0) {
                this.columns = (picinfos.size() < 3) ? picinfos.size() : 3;
            }
        }

        @Override
        public int getCount() {
            if (picinfos == null) {
                return 0;
            }
            return  Math.min(9, picinfos.size());
        }

        @Override
        public Object getItem(int i) {
            if (picinfos == null || i < 0 || i >= picinfos.size()) {
                return null;
            }
            return picinfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(resourceId, viewGroup, false);
            }
            MediaBean picinfo = picinfos.get(i);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_gridview_image);
            ViewGroup.LayoutParams param = (ViewGroup.LayoutParams) imageView.getLayoutParams();
            if (columns > 0) {
                param.width = imageWidth[columns - 1];
                param.height = param.width;
            }
            //Log.d("test", feeds.content +" "+param.width +"" + param.height+" "+columns);
            imageView.setLayoutParams(param);
            imageView.setMaxWidth(param.width);
            imageView.setMaxHeight(param.height);
            ImageAware imageAware = new ImageViewAware(imageView, false);
            if (columns > 1) {
                ImageLoader.getInstance().displayImage(picinfo.genUrl(param.width, param.height), imageAware,
                        ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER_200);
            } else if (columns == 1) {
                ImageLoader.getInstance().displayImage(picinfo.genUrl(param.width, param.height), imageAware,
                        ImageLoaderManager.DEFAULT_IMAGE_DISPLAYER_500);
            }
            return view;
        }

    }
}
