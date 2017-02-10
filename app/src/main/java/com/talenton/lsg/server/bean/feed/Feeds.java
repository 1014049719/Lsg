package com.talenton.lsg.server.bean.feed;

import java.util.ArrayList;
import java.util.LinkedList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.talenton.lsg.base.dao.model.FeedsBean;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.util.DateUtil;

/**
 * Created by ttt on 2016/5/4.
 */
public class Feeds implements Parcelable{
    /**
     *
     */
    public final static String URL_POST_PHOTO = "topic.php?mod=createtopic&cmdcode=72";// 发表
    public final static String URL_DELETE = "topic.php?mod=deletetopic&cmdcode=105";// 删除
    public final static String URL_GET = "topic.php?mod=gettopicbyid&cmdcode=75";// 获取
    public final static String URL_EDIT = "topic.php?mod=edittopic&cmdcode=106";// 编辑

    // from start
    public final static int FLAG_NORMAL = 0;
    public final static int FROM_TASK = 4;
    public final static int FROM_DB = 5;
    // from end

    // flag start
    public final static int FLAG_DELETEED = 1;
    public final static int FLAG_UN_LIKE = 0; //取消点赞
    public final static int FLAG_LIKE = 1; //点赞
    public final static int FLAG_COLLECT = 2;//收藏
    public final static int FLAG_UN_COLLECT = 3;//取消收藏
    // flag end
    // fb_flag start
    public final static int POST_NORMAL = 0;// 普通发表
    public final static int POST_COLLECT = 1;// 转发发表

    public final static int ACTION_DELETE = 1;
    public final static int ACTION_MODIFY = 2;
    public final static int ACTION_ADD = 3;

    public int ispl; //批量上传
    public long tid; //话题ID
    public String guid;
    public String localid;
    public long appLocalid;
    public int circle_type;
    public int ext_circle_member_attention_type;// 关注类型 -1 无关注 0-关注、1-管理员、2- 创建者（圈主）;
    public String title;//标题
    public long circle_id;//圈子id
    public String circle_name; //圈子名称
    public long creuid;//发布人UID
    public String crerealname;//发布人姓名
    public long dateline;//发布时间
    public long graphtime;//拍照时间
    public long modify_time;//修改时间
    public String content;
    public ArrayList<MediaBean> attachinfo;
    public String position;
    public CircleListData ext_circle;


    public ArrayList<CommentInfo> ext_topic_comments;
    public int commentcount = 0;//评论数
    public ArrayList<GiftRecvDetail> giftdata;
    public int giftcount = 0;
    public ArrayList<GiftRecvDetail> countByGift;

    // 发送者身份标示
    public CircleMember ext_member;
    public int groupkey;
    public int gxid;
    public String gxname;
    public String baobaorealname;
    public int isessence;//是否为精华 0 否 1 是
    public int flag;//删除标记 0 否 1 是
    public int isopen;//是否公开话题 0 否 1 是
    public String ttype;//话题类型
    public int hot;//是否为热门话题 0 否 1 是

    // 转发有关信息
    public String oldguid;// 转发前guid
    public int fb_flag;// POST_NORMAL,POST_COLLECT
    public long oldcreuid;// “转发后，原动态的发布者ID”,
    public String oldcrenickname;// : “原动态的发布者名”,

    public int likecount;// 点赞数,
    public int ext_is_collect;// 是否收藏 0 否 1 是
    public int ext_is_like;// 是否点赞 0 否 1 是
    public int browsecount;// 浏览次数,

    public int taskStatus;// 任务执行情况
    public String taskMessage;// 任务执行情况
    public int from;// 数据来源
    public boolean isUseOriginal;
    public String strPothoTime;

    public int localdType;
    public int sendtopublic;//0-不发送到学校广场、1-发送到学校广场

    public FeedsBean genFeedsBean() {
        Gson gson = OkHttpClientManager.getInstance().getmGson();
        return new FeedsBean(guid, circle_type, tid, 0L, 0L, circle_id, graphtime, gson.toJson(this).toString());
    }

    public FeedsBean genCircleFeedsBean(){
        Gson gson = OkHttpClientManager.getInstance().getmGson();
        return new FeedsBean(guid, circle_type, tid, 0L, 0L, circle_id, modify_time, gson.toJson(this).toString());
    }

    public static Feeds getInstance(FeedsBean item) {
        Gson gson = OkHttpClientManager.getInstance().getmGson();
        Feeds f = gson.fromJson(item.getContent(), Feeds.class);
        if (f.from != Feeds.FROM_TASK) {
            f.from = Feeds.FROM_DB;
        }
        return f;
    }


    public Feeds() {
        appLocalid = 0;
        localid = "";
        guid = "";
    }

    public CircleMember getCircleMember(){
        if (ext_member == null){
            return CircleMember.EMPTY;
        }
        return ext_member;
    }

    public String getPhotoTime() {
        if (TextUtils.isEmpty(strPothoTime)) {
            strPothoTime = DateUtil.parseTimeToYMD(graphtime);
        }
        return strPothoTime;
    }

    public boolean isVideo() {
        if (attachinfo != null && attachinfo.size() > 0 && attachinfo.get(0).itype == MediaBean.TYPE_VIDEO) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Feeds [guid=" + guid + ", appLocalid=" + appLocalid + ", dtype=" + circle_type + "]";
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(guid);
        //dest.writeString(fbztx);
        //dest.writeString(crenickname);
        dest.writeList(giftdata);

    }

    /**
     * Constructs a Question from a Parcel
     *
     * @param parcel
     *            Source Parcel
     */
    public Feeds(Parcel parcel) {
        this.guid = parcel.readString();
        //this.fbztx = parcel.readString();
       // this.crenickname = parcel.readString();
        this.giftdata = parcel.readArrayList(GiftRecvDetail.class.getClassLoader());
    }

    public static Creator<Feeds> CREATOR = new Creator<Feeds>() {

        @Override
        public Feeds createFromParcel(Parcel source) {
            return new Feeds(source);
        }

        @Override
        public Feeds[] newArray(int size) {
            return new Feeds[size];
        }

    };
}
