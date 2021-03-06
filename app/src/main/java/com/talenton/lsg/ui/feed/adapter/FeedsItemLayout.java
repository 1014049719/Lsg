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
import android.widget.AdapterView.OnItemClickListener;
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
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.MediaBean;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.ui.ImageDetailViewerActivity;
import com.talenton.lsg.ui.feed.OnFeedsClickListener;
import com.talenton.lsg.ui.school.PlayerActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Created by ttt on 2016/5/4.
 */
public class FeedsItemLayout {
    public Context mContext;
    public boolean isDetailPage = false;
    public Feeds feeds;
    public ItemViewHolder holder;
    protected static int textLinkColor = 0;
    protected static int imageWidth[] = null;
    private static int gridViewWidth = 0;
    protected static int imageWidthDetail = 0;
    protected View.OnClickListener basicClickListener;
    protected OnFeedsClickListener mOnFeedsClickListener;
    protected LayoutInflater inflater;
    private String  mUpPhotoTime;
    //private PostToParam mPostToParam;

    protected static OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
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
    protected static OptimizeGridView.OnTouchItemListener mOnTouchItemListener = new OptimizeGridView.OnTouchItemListener() {

        @Override
        public boolean onTouchItem(View parent, int motionPosition, int motionEvent) {
            if (motionEvent == MotionEvent.ACTION_UP) {
                Object tag2 = parent.getTag(R.id.tag_second);
                if (tag2 instanceof Long){
                    Long tid = (Long)tag2;
                    Object tag1 = parent.getTag(R.id.tag_first);
                    if (tid > 0 && tag1 instanceof Boolean && !((Boolean)tag1)){
                        FeedServer.incrementBrowswerNum(tid);
                    }
                }

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
        TextView mAuthorName, mLevel;
        TextView mNickName;
        TextView mPublishTime;
        TextView mTextContent, mPhotoTime, mPhotoDay, mPhotoMonth;
        //TextView mFeedsTag, mFirstTag;
        //CornerView mFeedsTagLine;
        //CommentTextView mCollectFrom;
        //ImageView mFeedsTagLogo, mFeedsSkin;
        //View mTagContainer;
        OptimizeGridView mPhotoGridView;
        View mSendProgressbar, mLayoutInfo;
        TextView mSendFeedFailedView;
        View mFeedsDelete,mFeedsEdit,mOperatorView, mSendGift;
        TextView mFeedsViewAmount;
        View mFeedsBrowser, mFeedsShare;
        CommentTextView mForwardTextContent;
        //GiftTotalContainerLayout mGiftTotalListView;
        CommentContainerLayout mCommentListView;
        View mItemContainer, mPhotoTimeContainer;
        public View mRoot;
    }

    /**
     * Feeds view holder
     *            view type BbGroupFeedsFragment.FEEDS_BBG or
     *            BbGroupFeedsFragment.FEEDS_CLASS
     */
    public FeedsItemLayout(Context context, View convertView, OnFeedsClickListener onFeedsClickListener) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mOnFeedsClickListener = onFeedsClickListener;
        initViewItems(convertView);
    }

    public ItemViewHolder getHolder() {
        return holder;
    }

    public void setFeedsDetail() {
        isDetailPage = true;
    }

    /**
     * @param feed
     *  hidenFeeds
     *            界面外部当前隐藏的feedsid
     * @param upPhotoTime
     *            上一行的拍照时间
     */
    public void setFeeds(Feeds feed, String upPhotoTime) {
        this.feeds = feed;
        mUpPhotoTime = upPhotoTime;
        fillData();
    }

    private void initViewListeners() {
        basicClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnFeedsClickListener != null) {
                    mOnFeedsClickListener.onFeedsClick(FeedsItemLayout.this, view);
                    return;
                }
            }
        };
        holder.mItemContainer.setOnClickListener(basicClickListener);
        if(holder.mLayoutInfo != null){
            holder.mLayoutInfo.setOnClickListener(basicClickListener);
        }
        if (holder.mOperatorView != null) {
            holder.mOperatorView.setOnClickListener(basicClickListener);
        }
        if(holder.mFeedsDelete != null){
            holder.mFeedsDelete.setOnClickListener(basicClickListener);
        }
        if(holder.mFeedsEdit != null){
            holder.mFeedsEdit.setOnClickListener(basicClickListener);
        }
        if (holder.mFeedsBrowser != null) {
            holder.mFeedsBrowser.setOnClickListener(basicClickListener);
        }
        if (holder.mSendFeedFailedView != null) {
            holder.mSendFeedFailedView.setOnClickListener(basicClickListener);
        }
        //holder.mSendGift.setOnClickListener(basicClickListener);
        initGridViewListener(holder.mPhotoGridView);
        /*
        if (holder.mTagContainer != null) {
            holder.mTagContainer.setOnClickListener(basicClickListener);
        }
        holder.mGiftTotalListView.setOnClickListener(basicClickListener);
        */
        if (holder.mFeedsShare != null) {
            holder.mFeedsShare.setOnClickListener(basicClickListener);
        }
        holder.mAuthorPhoto.setOnClickListener(basicClickListener);
        if(holder.mPhotoTimeContainer != null){
            holder.mPhotoTimeContainer.setOnClickListener(basicClickListener);
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
        //holder.mLevel = (TextView) mainLayout.findViewById(R.id.level);
        holder.mLayoutInfo = mainLayout.findViewById(R.id.layout_info);
        holder.mNickName = (TextView) mainLayout.findViewById(R.id.nick_name);
        holder.mPublishTime = (TextView) mainLayout.findViewById(R.id.publish_time);
        holder.mTextContent = (TextView) mainLayout.findViewById(R.id.feeds_text_content);
        //holder.mFeedsTag = (TextView) mainLayout.findViewById(R.id.feeds_tag);
        //holder.mFeedsTagLine = (CornerView) mainLayout.findViewById(R.id.feeds_tag_line);
        //holder.mFeedsTagLogo = (ImageView) mainLayout.findViewById(R.id.feeds_tag_icon);
        //holder.mFeedsSkin = (ImageView) mainLayout.findViewById(R.id.feeds_skin);
        //holder.mTagContainer = mainLayout.findViewById(R.id.feeds_tag_container);
        // holder.mFristContainer =
        // mainLayout.findViewById(R.id.first_tag_container);
        //holder.mCollectFrom = (CommentTextView) mainLayout.findViewById(R.id.collect_from);
        //holder.mFirstTag = (TextView) mainLayout.findViewById(R.id.first_tag);
        holder.mPhotoTime = (TextView) mainLayout.findViewById(R.id.images_time);
        holder.mPhotoGridView = (OptimizeGridView) mainLayout.findViewById(R.id.feeds_content_gridview);
        holder.mSendFeedFailedView = (TextView) mainLayout.findViewById(R.id.feeds_send_failed_view);
        holder.mSendProgressbar = mainLayout.findViewById(R.id.feeds_send_progressbar);
        holder.mFeedsViewAmount = (TextView) mainLayout.findViewById(R.id.feeds_view_num);
        holder.mFeedsBrowser = mainLayout.findViewById(R.id.feeds_browser);
        holder.mFeedsShare = mainLayout.findViewById(R.id.feeds_share);
        holder.mFeedsDelete = mainLayout.findViewById(R.id.feeds_delete);
        holder.mFeedsEdit = mainLayout.findViewById(R.id.feeds_edit);
        holder.mOperatorView = mainLayout.findViewById(R.id.feeds_operator);
        holder.mSendGift = mainLayout.findViewById(R.id.feeds_gift);
        holder.mCommentListView = (CommentContainerLayout) mainLayout.findViewById(R.id.item_comment_list);
        //holder.mGiftTotalListView = (GiftTotalContainerLayout) mainLayout.findViewById(R.id.item_gift_list);
        holder.mItemContainer = mainLayout.findViewById(R.id.feeds_container);
        holder.mPhotoTimeContainer = mainLayout.findViewById(R.id.photo_time_container);
        holder.mPhotoDay = (TextView) mainLayout.findViewById(R.id.photo_day);
        holder.mPhotoMonth = (TextView) mainLayout.findViewById(R.id.photo_month);
        initViewListeners();
    }

    private void fillData() {
        fillAuthorBasicInfo();
        fillTextContent();
        fillPhotoContent(feeds, holder.mPhotoGridView);
        fillGiftContent();
        fillActionContent();
        fillCommentContent();
    }

    private void setImageWidth() {
        if (isDetailPage) {
            if (imageWidthDetail <= 0) {
                int margin = mContext.getResources().getDimensionPixelSize(R.dimen.space_15_0) * 2;
                DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                imageWidthDetail = dm.widthPixels - margin;
            }
        } else if (imageWidth == null || imageWidth.length == 0) {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int margin = mContext.getResources().getDimensionPixelSize(R.dimen.space_47_5);
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

    private void fillCommentContent() {
        if (holder.mCommentListView == null) {
            return;
        }
        if (feeds.ext_topic_comments == null || feeds.ext_topic_comments.isEmpty() || isDetailPage) {
            holder.mCommentListView.setVisibility(View.GONE);
            return;
        }
        holder.mCommentListView.setCommonInfo(feeds);

        if (holder.mCommentListView != null) {
            if (feeds.ext_topic_comments == null || feeds.ext_topic_comments.isEmpty() || isDetailPage) {
                holder.mCommentListView.setOnFeedsClickListener(null);
                return;
            }
            holder.mCommentListView.setOnFeedsClickListener(mOnFeedsClickListener);
        }
    }

    private void fillActionContent() {
        if (holder.mFeedsViewAmount == null) {
            return;
        }
        holder.mFeedsViewAmount.setText(mContext.getResources().getString(R.string.circle_feeds_list_view_count,
                feeds.browsecount > 0 ? feeds.browsecount : 0));
    }

    private void fillGiftContent() {
        if (holder.mSendGift != null)
            holder.mSendGift.setVisibility(View.GONE);
        /*
        if (holder.mGiftTotalListView != null) {
            holder.mGiftTotalListView.setGiftInfo(feeds);
        }
        */
    }

    private void fillTextContent() {
        // String content = String.format("%d\n(%s,local:%d)\n%s\n%s",
        // feeds.from, feeds.guid, feeds.localid, feeds.content,
        // TimeUtil.parseTimeToYMDHMS(new Date(feeds.graphtime * 1000)));
        String content = feeds.content;
        if (TextUtils.isEmpty(content)) {
            holder.mTextContent.setVisibility(View.GONE);
        } else {
            if (!isDetailPage && content.length() > 83){
                content = FeedServer.subContext(feeds.content, FeedServer.endBaybContent);
            }
            holder.mTextContent.setVisibility(View.VISIBLE);
            holder.mTextContent.setText(content);
        }
        if(isDetailPage) {
            int visibility = FeedServer.checkDeleteFeedsAuthority(feeds, Feeds.ACTION_DELETE) ? View.VISIBLE : View.GONE;

            if(holder.mFeedsDelete != null){
                holder.mFeedsDelete.setVisibility(visibility);
            }
            visibility = FeedServer.checkDeleteFeedsAuthority(feeds, Feeds.ACTION_MODIFY) ? View.VISIBLE : View.GONE;
            visibility = feeds.from == Feeds.FROM_TASK ? View.GONE : visibility;
            if(holder.mFeedsEdit != null){
                holder.mFeedsEdit.setVisibility(visibility);
            }
            visibility = feeds.from == Feeds.FROM_TASK ? View.GONE : View.VISIBLE;
            holder.mFeedsShare.setVisibility(visibility);
        }else {
            holder.mFeedsDelete.setVisibility(View.GONE);
            holder.mFeedsEdit.setVisibility(View.GONE);
        }
        if (holder.mOperatorView != null) {
            holder.mOperatorView.setVisibility(View.VISIBLE);
        }
        /*
        if (feeds.fb_flag == Feeds.POST_COLLECT && holder.mCollectFrom != null) {
            // 转发的情况
            holder.mCollectFrom.setVisibility(View.VISIBLE);
            holder.mCollectFrom.setTag(feeds.oldcreuid);
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append("转发于");
            int pos = ssb.length();
            ssb.append(feeds.oldcrenickname);
            ssb.setSpan(mOnFeedsClickListener, pos, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(":");
            if (textLinkColor == 0) {
                textLinkColor = mContext.getResources().getColor(R.color.menu_main);
            }
            holder.mCollectFrom.setText(ssb, TextView.BufferType.SPANNABLE);
            holder.mCollectFrom.setLinkTextColor(textLinkColor);
        } else {
            holder.mCollectFrom.setVisibility(View.GONE);
        }
        */
    }

    private void fillPhotoContent(Feeds feeds, OptimizeGridView photoGridView) {
        ArrayList<MediaBean> pics = feeds.attachinfo;
        holder.mPublishTime
                .setText(mContext.getString(R.string.circle_feeds_post_at, DateUtil.parseTime(mContext, feeds.dateline)));
        if (holder.mPhotoTimeContainer != null) {
            String photoTime = feeds.getPhotoTime();
            if (!TextUtils.isEmpty(feeds.guid) && !TextUtils.isEmpty(photoTime)
                    && !photoTime.equals(mUpPhotoTime)) {
                holder.mPhotoTimeContainer.setVisibility(View.VISIBLE);
                holder.mPhotoDay.setText(photoTime.substring(8));
                holder.mPhotoMonth.setText(photoTime.substring(0, 7));
            } else {
                holder.mPhotoTimeContainer.setVisibility(View.INVISIBLE);
            }
        }

        boolean hasMedia = false;
        int resourceId = R.layout.item_feeds_photo_gridview;
        ArrayList<String> urls = new ArrayList<String>();
        if (pics != null) {
            photoGridView.setTag(R.id.tag_first, isDetailPage);
            photoGridView.setTag(R.id.tag_second, feeds.from == Feeds.FROM_TASK ? 0 : feeds.tid);
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
            /*
            if (isDetailPage) {
                holder.mPhotoTime.setVisibility(View.VISIBLE);
                holder.mPhotoTime
                        .setText(mContext.getString(R.string.circle_photo_at, DateUtil.parseTime(feeds.graphtime)));
            } else {
                holder.mPhotoTime.setVisibility(View.INVISIBLE);
            }
            */
            setImageWidth();
            PhotoGridViewAdapter photoAdapter = new PhotoGridViewAdapter(null, resourceId);
            photoGridView.setAdapter(photoAdapter);
            photoGridView.setVisibility(View.VISIBLE);
            if (isDetailPage) {
                photoGridView.setNumColumns(1);
            } else {
                if(pics.size() > 1){
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearParams.width = gridViewWidth;
                    photoGridView.setLayoutParams(linearParams);
                }
                photoGridView.setNumColumns((pics.size() < 2) ? pics.size() : 3);
            }
            photoAdapter.setPicinfos(pics);
            photoAdapter.notifyDataSetChanged();
        } else {
            photoGridView.setTag(null);
            photoGridView.setVisibility(View.GONE);
            //holder.mPhotoTime.setVisibility(View.INVISIBLE);
        }
        if (isDetailPage) {
            holder.mPhotoTime.setVisibility(View.INVISIBLE);
        } else {
            holder.mPhotoTime.setVisibility(View.GONE);
        }
    }

    private void fillAuthorBasicInfo() {

        ImageLoader.getInstance().displayImage(feeds.getCircleMember().avatar, holder.mAuthorPhoto,
                ImageLoaderManager.DEFAULT_USER_IMAGE_DISPLAYER);
        String gx = TextUtils.isEmpty(feeds.gxname) ? "" : feeds.gxname;
        holder.mNickName.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(gx) && !TextUtils.isEmpty(feeds.baobaorealname)) {
            holder.mAuthorName.setText(String.format("%s%s", feeds.baobaorealname, gx));
            holder.mNickName.setText(feeds.crerealname);
        } else {
            if(!TextUtils.isEmpty(gx)){
                holder.mAuthorName.setText(gx);
                holder.mNickName.setText(feeds.crerealname);
            }else {
                holder.mNickName.setVisibility(View.GONE);
                holder.mAuthorName.setText(feeds.crerealname);
            }
        }

        if (feeds.from == Feeds.FROM_TASK) {
            //holder.mLevel.setVisibility(View.GONE);
            holder.mPublishTime.setVisibility(View.GONE);
            if (feeds.taskStatus == TaskBase.STATUS_FAILED || feeds.taskStatus == TaskBase.STATUS_CANCELED) {
                holder.mSendFeedFailedView.setVisibility(View.VISIBLE);
                holder.mSendProgressbar.setVisibility(View.GONE);
                //holder.mSendFeedFailedView.setText("");
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
            if (isDetailPage) {
                this.columns = 1;
            } else if (picinfos != null && picinfos.size() > 0) {
                this.columns = (picinfos.size() < 3) ? picinfos.size() : 3;
            }
        }

        public PhotoGridViewAdapter(ArrayList<MediaBean> picinfos, int resourceId) {
            this.picinfos = picinfos;
            this.resourceId = resourceId;
            if (isDetailPage) {
                this.columns = 1;
            } else if (picinfos != null && picinfos.size() > 0) {
                this.columns = (picinfos.size() < 3) ? picinfos.size() : 3;
            }
        }

        @Override
        public int getCount() {
            if (picinfos == null) {
                return 0;
            }
            return isDetailPage ? picinfos.size() : Math.min(9, picinfos.size());
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
            if (isDetailPage) {
                param.width = imageWidthDetail;
                param.height = param.width;
            } else if (columns > 0) {
                param.width = imageWidth[columns - 1];
                param.height = param.width;
            }
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
