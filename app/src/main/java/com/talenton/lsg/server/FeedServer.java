package com.talenton.lsg.server;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.talenton.lsg.LsgApplication;
import com.talenton.lsg.R;
import com.talenton.lsg.base.dao.model.FeedsBean;
import com.talenton.lsg.base.okhttp.OkHttpClientManager;
import com.talenton.lsg.base.server.DBHelper;
import com.talenton.lsg.base.server.UploadCompletionHandler;
import com.talenton.lsg.base.server.UserServer;
import com.talenton.lsg.base.server.XLTError;
import com.talenton.lsg.base.server.XLTResponseCallback;
import com.talenton.lsg.base.server.XLTResponseListener;
import com.talenton.lsg.base.server.bean.BabyData;
import com.talenton.lsg.base.server.bean.ObjectCode;
import com.talenton.lsg.base.server.bean.UserInfo;
import com.talenton.lsg.base.server.task.TaskBase;
import com.talenton.lsg.base.server.task.TaskManager;
import com.talenton.lsg.base.util.SystemUtil;
import com.talenton.lsg.base.util.XLTToast;
import com.talenton.lsg.event.AddCommentEvent;
import com.talenton.lsg.event.DeleteCommentEvent;
import com.talenton.lsg.event.DeleteFeedsEvent;
import com.talenton.lsg.event.FeedsTaskEvent;
import com.talenton.lsg.server.bean.feed.AttentionRecordData;
import com.talenton.lsg.server.bean.feed.CircleInfo;
import com.talenton.lsg.server.bean.feed.CircleList;
import com.talenton.lsg.server.bean.feed.CircleListData;
import com.talenton.lsg.server.bean.feed.CircleMember;
import com.talenton.lsg.server.bean.feed.CollectFeedsData;
import com.talenton.lsg.server.bean.feed.CommentInfo;
import com.talenton.lsg.server.bean.feed.Feeds;
import com.talenton.lsg.server.bean.feed.InviteData;
import com.talenton.lsg.server.bean.feed.MineCircle;
import com.talenton.lsg.server.bean.feed.PostToParam;
import com.talenton.lsg.server.bean.feed.ReqFeedsList;
import com.talenton.lsg.server.bean.feed.ReqSocialCircle;
import com.talenton.lsg.server.bean.feed.RspAttentionRecord;
import com.talenton.lsg.server.bean.feed.RspCircleList;
import com.talenton.lsg.server.bean.feed.RspCircleRank;
import com.talenton.lsg.server.bean.feed.RspCollectFeeds;
import com.talenton.lsg.server.bean.feed.RspFeedsList;
import com.talenton.lsg.server.bean.feed.RspListComment;
import com.talenton.lsg.server.bean.feed.RspMineCircle;
import com.talenton.lsg.server.bean.feed.RspPostFeeds;
import com.talenton.lsg.server.bean.school.RspListSearchHot;
import com.talenton.lsg.server.task.TaskFeeds;
import com.talenton.lsg.util.CacheManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ttt on 2016/4/15.
 */
public class FeedServer {

    private static UploadManager uploadManager = new UploadManager();
    public static final int endBaybContent = 80;
    public static final int endTopicContent = 40;

    public static void upload(String fileName, UploadCompletionHandler handler) {
        //String token = "rvSymHO9RLd3vsQ_O6wPv9I_RHD7K1FMAZxzPgT0:7X61R1vBv2gFn91ro_qEToFrCxs=:eyJyZXR1cm5Cb2R5Ijoie1wia2V5XCI6ICQoa2V5KSwgXCJzaXplXCI6ICQoZnNpemUpLCBcIndpZHRoXCI6ICQoaW1hZ2VJbmZvLndpZHRoKSwgXCJoZWlnaHRcIjogJChpbWFnZUluZm8uaGVpZ2h0KX0iLCJzY29wZSI6Imd6eGx0IiwiZGVhZGxpbmUiOjE0NTA4NTg5ODZ9";
        UploadOptions options = new UploadOptions(null, null, false, new UpProgressHandler(){

            @Override
            public void progress(String key, double percent) {
                // TODO Auto-generated method stub
                CacheManager.getInstance().setQnUpProgress((int)(percent*100));
            }
        }, null);
        uploadManager.put(fileName, null, UserServer.getQntoken(), handler, options);
    }

    private static Comparator<Feeds> mFeedsTaskCompratorByPhotoTime = new Comparator<Feeds>() {

        @Override
        public int compare(Feeds lhs, Feeds rhs) {
            return rhs.graphtime < lhs.graphtime ? -1 : (lhs.graphtime == rhs.graphtime ? 0 : 1);
        }

    };


    /* 获得圈子列表接口 */
    public static void getCircleList(CircleList circle, final XLTResponseCallback<RspCircleList> listener){
        JsonObject json = new JsonObject();
        String url = CircleList.URL_GET_CIRCLE_LIST;
        if(circle != null){
            if (circle.circle_id > 0){
                json.addProperty("circle_id", circle.circle_id);
            }
            if (!TextUtils.isEmpty(circle.circle_name)){
                json.addProperty("circle_name", circle.circle_name);
            }
            if (!TextUtils.isEmpty(circle.description)){
                json.addProperty("description", circle.description);
            }
            if (circle.belong_id > 0){
                json.addProperty("belong_id", circle.belong_id);
            }
            if (circle.create_uid > 0){
                json.addProperty("create_uid", circle.create_uid);
            }
            if (circle.circle_type > CircleListData.CIRCLE_EMPTY){
                json.addProperty("circle_type", circle.circle_type);
            }
            if (circle.is_public >  CircleListData.CIRCLE_EMPTY){
                json.addProperty("is_public", circle.is_public);
            }
            if (circle.is_system >  CircleListData.CIRCLE_EMPTY){
                json.addProperty("is_system", circle.is_system);
            }
            if (!TextUtils.isEmpty(circle.query_orderby)){
                json.addProperty("query_orderby", circle.query_orderby);
            }
            if (!TextUtils.isEmpty(circle.query_pager)){
                json.addProperty("query_pager", circle.query_pager);
            }
            if (circle.circle_type == CircleListData.CIRCLE_TYPE_SCHOOL){
                url = CircleList.URL_GET_SCHOOL_CIRCLE;
            }
        }

        OkHttpClientManager.getInstance().addGsonRequest(url, RspCircleList.class,
                new XLTResponseListener<RspCircleList>() {
                    @Override
                    public void onResponse(RspCircleList responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, json.toString());
    }

    /* 直接关注圈子（公共圈子）接口 */
    public static void attentionOrCancelCircle(long circleId, long uid, int attentSources, boolean isAttention, final XLTResponseCallback<ObjectCode> listener){
        JsonObject json = new JsonObject();
        json.addProperty("circle_id", circleId);
        if (uid > 0){
            json.addProperty("uid", uid);
        }
        if (attentSources != -1) {
            json.addProperty("attent_sources", attentSources);
        }
        String url;
        if (isAttention){
            url = "circle.php?mod=circle&ac=attentpubliccircle&cmdcode=26";
        }else {
            url = "circle.php?mod=circle&ac=cancelattentcircle&cmdcode=27";
        }
        OkHttpClientManager.getInstance().addGsonRequest(url, ObjectCode.class, new XLTResponseListener<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode responseData, XLTError errorData) {
                listener.onResponse(responseData, errorData);
            }
        }, json.toString());
    }

    /* 获得我的/关注圈子列表接口 */
    public static void getMineCircle(long circleId, int attentionType, final XLTResponseCallback<RspMineCircle> listener){
        JsonObject json = new JsonObject();
        if (circleId > 0){
            json.addProperty("circle_id", circleId);
        }
        if(attentionType > -1) {
            json.addProperty("attention_type", attentionType);
        }
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=getmycircles&cmdcode=21",
            RspMineCircle.class, new XLTResponseListener<RspMineCircle>() {
                @Override
                public void onResponse(RspMineCircle responseData, XLTError errorData) {
                    listener.onResponse(responseData, errorData);
                }
            }, json.toString());
    }

    /* 获得圈子成员列表接口 */
    public static void getCircleMemberList(long circleId, String order, String pager, final XLTResponseCallback<RspMineCircle> listener){
        JsonObject json = new JsonObject();
        json.addProperty("circle_id", circleId);
        if (!TextUtils.isEmpty(order)){
            json.addProperty("query_orderby", order);
        }
        if (!TextUtils.isEmpty(pager)){
            json.addProperty("query_pager", pager);
        }
        String url = "circle.php?mod=circle&ac=getcirclemembers&cmdcode=23";
        OkHttpClientManager.getInstance().addGsonRequest(url, RspMineCircle.class, new XLTResponseListener<RspMineCircle>() {
            @Override
            public void onResponse(RspMineCircle responseData, XLTError errorData) {
                listener.onResponse(responseData, errorData);
            }
        }, json.toString());

    }

    /* 获得圈子默认设置接口 */
    public static void getDefaultCircle(int settingType, final XLTResponseCallback<RspMineCircle> listener){
        JsonObject json = new JsonObject();
        json.addProperty("setting_type", settingType);
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=getdefaultsetting&cmdcode=70",
            RspMineCircle.class, new XLTResponseListener<RspMineCircle>() {
                @Override
                public void onResponse(RspMineCircle responseData, XLTError errorData) {
                    listener.onResponse(responseData, errorData);
                }
            }, json.toString());
    }

    //分享话题到其他圈子接口
    public static void socialTopicByCircle(ReqSocialCircle circle, final XLTResponseCallback<ObjectCode> listener){
        OkHttpClientManager.getInstance().addGsonRequest(ReqSocialCircle.URL,
                ObjectCode.class, new XLTResponseListener<ObjectCode>() {
                    @Override
                    public void onResponse(ObjectCode responseData, XLTError errorData) {
                        if (listener != null )
                            listener.onResponse(responseData, errorData);
                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(circle));
    }

    /* 获得申请记录接口 */
    public static void getAttentionRecord(long circleId, int attentStatus, final XLTResponseCallback<RspAttentionRecord> listener){
        JsonObject json = new JsonObject();
        //json.addProperty("circle_id", circleId);
        if (attentStatus > AttentionRecordData.ATTENTION_STATUS_EMPTY){
            json.addProperty("attent_status", attentStatus);
        }
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=getcircleattentions&cmdcode=29",
                RspAttentionRecord.class, new XLTResponseListener<RspAttentionRecord>() {
                    @Override
                    public void onResponse(RspAttentionRecord responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, json.toString());
    }

    /* 审核关注圈子申请接口 */
    public static void applyAttentionRecord(long attentionId, int attentStatus, final XLTResponseCallback<ObjectCode> listener){
        JsonObject json = new JsonObject();
        json.addProperty("attention_id", attentionId);
        json.addProperty("attent_status", attentStatus);
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=updateattentionstatus&cmdcode=28",
                ObjectCode.class, new XLTResponseListener<ObjectCode>() {
                    @Override
                    public void onResponse(ObjectCode responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, json.toString());
    }

    /* 获得圈子邀请码接口 */
    public static void getInviteCode(long circleId, final XLTResponseCallback<InviteData> listener){
        JsonObject json = new JsonObject();
        json.addProperty("circle_id", circleId);
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=getcircleinvitationcode&cmdcode=24",
                InviteData.class, new XLTResponseListener<InviteData>() {
                    @Override
                    public void onResponse(InviteData responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, json.toString());
    }

    /* 通过邀请码申请关注圈子接口 */
    public static void AttentionByOther(String inviteCode, final XLTResponseCallback<ObjectCode> listener){
        JsonObject json = new JsonObject();
        json.addProperty("invitation_code", inviteCode);
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=attentcirclebyinvitationcode&cmdcode=25",
                ObjectCode.class, new XLTResponseListener<ObjectCode>() {
                    @Override
                    public void onResponse(ObjectCode responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, json.toString());
    }

    /* 通过邀请码申请关注圈子接口 */
    public static void AttentionBySchoolBaby(long babyId, final XLTResponseCallback<ObjectCode> listener){
        JsonObject json = new JsonObject();
        json.addProperty("baobao_id", babyId);
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=attentbaobaocircle&cmdcode=71",
                ObjectCode.class, new XLTResponseListener<ObjectCode>() {
                    @Override
                    public void onResponse(ObjectCode responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, json.toString());
    }

    //增加话题浏览数接口
    public static void incrementBrowswerNum(long tid){
        JsonObject json = new JsonObject();
        json.addProperty("tid", tid);
        String url = "topic.php?mod=addtopicbrowserecord&cmdcode=81";
        OkHttpClientManager.getInstance().addGsonRequest(url, ObjectCode.class, new XLTResponseListener<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode responseData, XLTError errorData) {
                //listener.onResponse(responseData, errorData);
            }
        }, json.toString());
    }

    /*本圈排名接口*/
    public static void getCircleRankList(long circleId, String pager, final XLTResponseCallback<RspCircleRank> listener){
        JsonObject json = new JsonObject();
        json.addProperty("circle_id", circleId);
        if(!TextUtils.isEmpty(pager)){
            json.addProperty("query_pager", pager);
        }
        OkHttpClientManager.getInstance().addGsonRequest("circle.php?mod=circle&ac=getstatistics&cmdcode=83",
                RspCircleRank.class, new XLTResponseListener<RspCircleRank>() {
                    @Override
                    public void onResponse(RspCircleRank responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                    }
                }, json.toString());
    }

    /**
     *
     * @param tid
     * @param status 0,1 取消/设置话题点赞接口, 2-收藏话题接口, 3-删除收藏话题接口
     * @param listener
     */
    public static void setLikeAndCollect(final long tid, int status, final XLTResponseCallback<ObjectCode> listener){
        JsonObject json = new JsonObject();
        json.addProperty("tid", tid);

        String url = "topic.php?mod=settopiclikecount&cmdcode=79";
        if (status == Feeds.FLAG_COLLECT){
            url = "topic.php?mod=settopiccollection&cmdcode=77";
            json.addProperty("status", 1);
        }else if(status == Feeds.FLAG_UN_COLLECT){
            url = "topic.php?mod=settopiccollection&cmdcode=77";
            json.addProperty("status", 0);
        } else {
            json.addProperty("status", status);
        }

        OkHttpClientManager.getInstance().addGsonRequest(url, ObjectCode.class, new XLTResponseListener<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode responseData, XLTError errorData) {
                if (listener != null)
                    listener.onResponse(responseData, errorData);
            }
        }, json.toString());
    }

    /*获得热门话题接口*/
    public static void getTopicList(final ReqFeedsList req, final XLTResponseCallback<RspFeedsList> listener){
        JsonObject json = new JsonObject();

        if(!TextUtils.isEmpty(req.title)){
            json.addProperty("title", req.title);
        }
        if (req.creuid > 0){
            json.addProperty("creuid", req.creuid);
            json.addProperty("is_public", 1);
        }
        if(!TextUtils.isEmpty(req.query_orderby)){
            json.addProperty("query_orderby", req.query_orderby);
        }
        if(!TextUtils.isEmpty(req.query_pager)){
            json.addProperty("query_pager", req.query_pager);
        }
        String url = ReqFeedsList.URL_TOP;
        if (req.type == CircleListData.CIRCLE_TYPE_MINE){
            url = ReqFeedsList.URL_CIRCLE_MINE;
        }
        else if (req.type == CircleListData.CIRCLE_TYPE_COLLECT){
            url = ReqFeedsList.URL_CIRCLE_COLLECT;
            OkHttpClientManager.getInstance().addGsonRequest(url, RspCollectFeeds.class, new XLTResponseListener<RspCollectFeeds>() {
                @Override
                public void onResponse(RspCollectFeeds responseData, XLTError errorData) {
                    if (responseData != null && responseData.list != null && errorData == null) {
                        RspFeedsList list = new RspFeedsList();
                        list.count = responseData.count;
                        list.list = new LinkedList<Feeds>();
                        for (CollectFeedsData c : responseData.list) {
                            Feeds f = c.ext_topic;
                            if (f != null) {
                                if (f.ext_circle == null) {
                                    f.ext_circle = new CircleListData();
                                    f.ext_circle.circle_id = f.circle_id;
                                    f.ext_circle.circle_name = c.circle_name;
                                }
                                f.ext_circle.type = CircleListData.CIRCLE_TYPE_COLLECT;
                                f.ext_circle.modify_time = c.dateline;
                                FeedServer.subContext(f, endTopicContent);
                                list.list.add(f);
                            }
                        }
                        listener.onResponse(list, errorData);
                    } else {
                        listener.onResponse(null, errorData);
                    }
                }
            }, json.toString());
            return;
        }

        OkHttpClientManager.getInstance().addGsonRequest(url, RspFeedsList.class, new XLTResponseListener<RspFeedsList>() {
            @Override
            public void onResponse(RspFeedsList responseData, XLTError errorData) {
                if (responseData != null){
                    subContext(responseData.list, endTopicContent);
                }
                listener.onResponse(responseData, errorData);
            }
        }, json.toString());
    }

    /**
     *  获取搜热门列表
     */
    public static void getSearchHotList(final XLTResponseListener<RspListSearchHot> listener){
        String url = "topic.php?mod=gettopictags&cmdcode=89";
        OkHttpClientManager.getInstance().addGsonRequest(url, RspListSearchHot.class, new XLTResponseListener<RspListSearchHot>() {
            @Override
            public void onResponse(RspListSearchHot responseData, XLTError errorData) {
                if (errorData == null) {
                    //TODO 请求成功
                }
                if (listener != null) {
                    listener.onResponse(responseData, errorData);
                }
            }
        }, "");
    }

    /* 通过圈子id获得话题列表接口 */
    public static void getFeedsList(final ReqFeedsList req, final XLTResponseCallback<RspFeedsList> listener){
        boolean isHaveCacheData = false;
        final int end = req.circle_type == CircleListData.CIRCLE_TYPE_BABY ? endBaybContent : endTopicContent;
        if (req.type == ReqFeedsList.LIST_LATEST){
            // 从数据库读取
            RspFeedsList res = new RspFeedsList();
            if(req.circle_type == CircleListData.CIRCLE_TYPE_BABY) {
                res.cache = mergeFeedsAndTask(listTaskPending(req), listDBFeeds(req), -1);
            }else {
                res.cache = mergeCircleFeedsAndTask(listTaskPending(req), listDBFeeds(req), -1);
            }
            if (listener != null && res.cache != null && !res.cache.isEmpty()) {
                subContext(res.cache, end);
                listener.onResponse(res, null);
                isHaveCacheData = true;
            }
        }
        final boolean isTrue = isHaveCacheData;

        JsonObject json = new JsonObject();
        json.addProperty("circle_id", req.circle_id);
        if(req.graphtime > 0)
            json.addProperty("graphtime", req.graphtime);
        if(!TextUtils.isEmpty(req.query_orderby)){
            json.addProperty("query_orderby", req.query_orderby);
        }
        if(!TextUtils.isEmpty(req.query_pager)){
            json.addProperty("query_pager", req.query_pager);
        }

        OkHttpClientManager.getInstance().addGsonRequest(ReqFeedsList.URL, RspFeedsList.class, new XLTResponseListener<RspFeedsList>() {
            @Override
            public void onResponse(RspFeedsList responseData, XLTError errorData) {
                if (errorData != null){
                    /*
                    if (req.type == ReqFeedsList.LIST_OLDER) {
                        RspFeedsList res = new RspFeedsList();
                        if(req.circle_type == CircleListData.CIRCLE_TYPE_BABY) {
                            res.cache = mergeFeedsAndTask(listTaskPending(req), listDBFeeds(req), req.dbdateline);
                        }else {
                            res.cache = mergeCircleFeedsAndTask(listTaskPending(req), listDBFeeds(req), req.dbdateline);
                        }
                        subContext(res.cache, end);
                        listener.onResponse(res, null);
                    } else if(!isTrue){
                        listener.onResponse(null, errorData);
                    }
                    */
                    if(!isTrue)
                        listener.onResponse(null, errorData);

                }else if (errorData == null){

                    // 网络请求成功
                    if(responseData != null && responseData.list != null && responseData.list.size() > 0){
                        // 同步数据库
                        if (req.type == ReqFeedsList.LIST_LATEST
                                || req.type == ReqFeedsList.LIST_NEWER){
                            req.deleteFromDb();

                            LinkedList<Feeds> data = responseData.list;
                            for (Feeds f : data) {
                                if (f.flag == Feeds.FLAG_DELETEED) {
                                    DBHelper.getInstance().delete(DBHelper.DAO_FEEDS, new FeedsBean(f.guid),
                                            true);
                                } else {
                                    FeedsBean bean = req.circle_type == CircleListData.CIRCLE_TYPE_BABY ?
                                            f.genFeedsBean() : f.genCircleFeedsBean();
                                    DBHelper.getInstance().insertOrReplace(DBHelper.DAO_FEEDS, bean, true);
                                }
                            }
                        }
                        /*
                        else { //重复过虑

                            Iterator<Feeds> iterator = responseData.list.iterator();
                            while (iterator.hasNext()){
                                Long tid = iterator.next().tid;
                                Boolean b = mHolderFeeds.get(tid);
                                if (b != null){
                                    iterator.remove();
                                }
                            }
                        }
                        mHolderFeeds.clear();
                        for (Feeds feed : responseData.list){
                            mHolderFeeds.put(feed.tid, true);
                        }
                        */
                    }
                    // 合并发表中的动态
                    if (responseData == null) {
                        responseData = new RspFeedsList();
                    }
                    if (req.type == ReqFeedsList.LIST_LATEST
                         || req.type == ReqFeedsList.LIST_NEWER) {
                        // 合并最新的动态
                        //responseData.list = mergeFeedsAndTask(listTaskPending(req),
                        //        responseData.list, -1);
                        if(req.circle_type == CircleListData.CIRCLE_TYPE_BABY) {
                            responseData.list = mergeFeedsAndTask(listTaskPending(req), responseData.list,-1);
                        }else {
                            responseData.list = mergeCircleFeedsAndTask(listTaskPending(req), responseData.list, -1);
                        }
                    } else if (req.type == ReqFeedsList.LIST_OLDER) {
                        // 合并更老的动态
                        if(req.circle_type == CircleListData.CIRCLE_TYPE_BABY) {
                            responseData.list = mergeFeedsAndTask(listTaskPending(req), responseData.list, req.dateline);
                        }else {
                            responseData.list = mergeCircleFeedsAndTask(listTaskPending(req), responseData.list, req.dateline);
                        }
                    }
                    subContext(responseData.list, end);
                    listener.onResponse(responseData, errorData);
                }
            }
        }, json.toString());
    }

    /* 通过圈子id获得圈子信息接口 */
    public static void getCircleInfo(long circleId, final XLTResponseCallback<CircleInfo> listener){
        JsonObject json = new JsonObject();
        json.addProperty("circle_id", circleId);
        String url = "circle.php?mod=circle&ac=getcirclebyid&cmdcode=73";
        OkHttpClientManager.getInstance().addGsonRequest(url, CircleInfo.class, new XLTResponseListener<CircleInfo>() {
            @Override
            public void onResponse(CircleInfo responseData, XLTError errorData) {
                listener.onResponse(responseData, errorData);
            }
        }, json.toString());
    }

    //修改圈子信息接口
    public static void modifyCircle(long circleId, String des, String photo, String bg, final XLTResponseCallback<ObjectCode> listener){
        JsonObject json = new JsonObject();
        json.addProperty("circle_id", circleId);
        if (!TextUtils.isEmpty(des)){
            json.addProperty("description", des);
        }
        if (!TextUtils.isEmpty(photo)){
            json.addProperty("circle_photo", photo);
        }
        if (!TextUtils.isEmpty(bg)){
            json.addProperty("circle_bg", bg);
        }
        String url = "circle.php?mod=circle&ac=updatecircle&cmdcode=71";
        OkHttpClientManager.getInstance().addGsonRequest(url, ObjectCode.class, new XLTResponseListener<ObjectCode>() {
            @Override
            public void onResponse(ObjectCode responseData, XLTError errorData) {
                if (listener != null)
                    listener.onResponse(responseData, errorData);
            }
        }, json.toString());
    }

    public static boolean checkDeleteFeedsAuthority(Feeds feeds, int action) {
        boolean authority = false;
        if (feeds.creuid == UserServer.getCurrentUser().uid) {
            authority = true;
        }
        else if(action == Feeds.ACTION_MODIFY){
            return false;
        }
        else if(feeds.ext_circle_member_attention_type == MineCircle.ATTENTION_TYPE_CREATE
                        || feeds.ext_circle_member_attention_type == MineCircle.ATTENTION_TYPE_ADMIN){
            return true;
        }
        return authority;
    }

    public static void deleteFeeds(long circieId, final String guid, long tid, final XLTResponseCallback<JSONObject> listener){
        JsonObject json = new JsonObject();
        json.addProperty("tid", tid);
        DBHelper.getInstance().delete(DBHelper.DAO_FEEDS, new FeedsBean(guid), true);
        EventBus.getDefault().post(new DeleteFeedsEvent(circieId, guid, tid));
        OkHttpClientManager.getInstance().addGsonRequest(Feeds.URL_DELETE, JSONObject.class, new XLTResponseListener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responseData, XLTError errorData) {
                if (listener != null) {
                    listener.onResponse(responseData, errorData);
                }
            }
        }, json.toString());
    }

    public static void getFeeds(long tid, final String guid, final XLTResponseCallback<Feeds> listener){
        JsonObject json = new JsonObject();
        json.addProperty("tid", tid);
        OkHttpClientManager.getInstance().addGsonRequest(Feeds.URL_GET, Feeds.class, new XLTResponseListener<Feeds>(){
            @Override
            public void onResponse(Feeds responseData, XLTError errorData) {
                /*
                if (responseData == null || (responseData != null && responseData.flag == Feeds.FLAG_DELETEED)) {
                    XLTToast.makeText(LsgApplication.getAppContext(), R.string.feeds_deleted).show();
                    DBHelper.getInstance().delete(DBHelper.DAO_FEEDS, new FeedsBean(guid), true);
                }else if(!TextUtils.isEmpty(responseData.guid)){
                    DBHelper.getInstance().insertOrReplace(DBHelper.DAO_FEEDS, responseData.genFeedsBean(), true);
                }
                */
                if (listener != null) {
                    listener.onResponse(responseData, errorData);
                }
            }
        }, json.toString());
    }

    public static void commitFeeds(final Feeds feeds, final XLTResponseCallback<RspPostFeeds> listener) {
        //if(feeds != null) return;
        String url = Feeds.URL_POST_PHOTO;
        if (feeds.ispl == 1) {
            //url = Feeds.URL_POST_GALLERY;
        } else if (feeds.tid > 0) {
            url = Feeds.URL_EDIT;
        }

        OkHttpClientManager.getInstance().addGsonRequest(url, RspPostFeeds.class, new XLTResponseListener<RspPostFeeds>() {
            @Override
            public void onResponse(RspPostFeeds responseData, XLTError errorData) {
                listener.onResponse(responseData, errorData);
                if (errorData == null) {
                    XLTToast.makeText(LsgApplication.getAppContext(), R.string.post_success_hint, Toast.LENGTH_LONG)
                            .show();
                }
            }
        }, OkHttpClientManager.getInstance().getmGson().toJson(feeds), 30000);
    }

    public static void rePostFeeds(Feeds feeds) {
        TaskFeeds task = (TaskFeeds) TaskManager.getTaskFromDB(feeds.appLocalid);
        if (task == null) {
            task = new TaskFeeds(feeds);
        } else if (task.status != TaskBase.STATUS_SUCCESS) {
            task.status = TaskBase.STATUS_READY;
            task.retrys = 0;// 重新开始任务
        } else {
            // 已经发送成功的动态
            EventBus.getDefault().post(new FeedsTaskEvent(task));
            return;
        }
        TaskManager.mWifiConnected = SystemUtil.isWifiConnected();
        TaskManager.mNetworkConnected = SystemUtil.isNetworkConnected();
        TaskManager.addTask(task);
        // DBHelper.getInstance().addFeeds(feeds.genFeedsBean(), false);
    }

    /**
     * 获取正在上传中的动态
     *
     */
    public static LinkedList<Feeds> listTaskPending(ReqFeedsList req) {
        List<TaskBase> tasks = TaskManager.getAllTaskFromDB(TaskFeeds.taskTpe);
        LinkedList<Feeds> feeds = new LinkedList<Feeds>();
        for (TaskBase task : tasks) {
            if (task.status == TaskBase.STATUS_SUCCESS) {
                continue;
            }
            Feeds f = ((TaskFeeds) task).feeds;
            if (req == null) {
                // 查询全部
                f.taskStatus = task.status;
                f.taskMessage = task.message;
                f.from = Feeds.FROM_TASK;
                feeds.add(f);
            } else if (f.circle_type != req.circle_type || f.circle_id != req.circle_id) {
                continue;
            } else if ((req.circle_type == CircleListData.CIRCLE_TYPE_BABY  && f.graphtime < req.dateline)
                    ||(req.circle_type != CircleListData.CIRCLE_TYPE_BABY && f.modify_time < req.dateline)) {
                f.taskStatus = task.status;
                f.taskMessage = task.message;
                f.from = Feeds.FROM_TASK;
                feeds.add(f);
            }
        }
        return feeds;
    }

    /**
     * 获取上传列表
     *
     */
    public static LinkedList<Feeds> listAllFeedsTask() {
        LinkedList<Feeds> feeds = new LinkedList<Feeds>();
        List<TaskBase> tasks = TaskManager.getAllTaskFromDB(TaskFeeds.taskTpe);
        if (tasks != null) {
            for (TaskBase task : tasks) {
                Feeds f = ((TaskFeeds) task).feeds;
                f.taskStatus = task.status;
                f.taskMessage = task.message;
                f.from = Feeds.FROM_TASK;
                feeds.add(f);
            }
        }
        return feeds;
    }

    /**
     * 获取本地数据库动态
     *
     */
    public static LinkedList<Feeds> listDBFeeds(ReqFeedsList req) {
        List<FeedsBean> dbfeeds = req.listFromDb();
        if (dbfeeds != null && dbfeeds.size() > 0) {
            LinkedList<Feeds> feeds = new LinkedList<Feeds>();
            for (FeedsBean f : dbfeeds) {
                feeds.add(Feeds.getInstance(f));
            }
            return feeds;
        }
        return null;
    }

    public static void subContext(LinkedList<Feeds> feeds, int end){
        if (feeds == null || feeds.size() == 0) return;
        for (Feeds feed : feeds) {
            if (TextUtils.isEmpty(feed.content) || feed.content.length() <= end) continue;
            int e = end;
            if((feed.content.charAt(end) >> 12) == 0xe){
                e = end-1;
            }else {
                int unicode = Character.codePointAt(feed.content, end);
                //int skip = Character.charCount(unicode);
                if (unicode > 0xff){
                    e = end-1;
                }
            }
            feed.content = String.format("%s...", feed.content.substring(0,e));
        }
    }

    public static void subContext(Feeds feed, int end){
        if (feed == null) return;
        if (TextUtils.isEmpty(feed.content) || feed.content.length() <= end) return;

        int e = end;
        if((feed.content.charAt(end) >> 12) == 0xe){
            e = end-1;
        }else {
            int unicode = Character.codePointAt(feed.content, end);
            //int skip = Character.charCount(unicode);
            if (unicode > 0xff){
                e = end-1;
            }
        }
        feed.content = String.format("%s...", feed.content.substring(0,e));
    }

    public static String subContext(String content, int end){

        if (TextUtils.isEmpty(content) || content.length() <= end) return content;

        int e = end;
        if((content.charAt(end) >> 12) == 0xe){
            e = end-1;
        }else {
            int unicode = Character.codePointAt(content, end);
            //int skip = Character.charCount(unicode);
            if (unicode > 0xff){
                e = end-1;
            }
        }
        return String.format("%s...", content.substring(0,e));
    }

    /**
     * 合并发送中的动态(新建、修改的动态)和普通动态
     *
     * 本地的发送任务和动态任务是两个不同表（taskbeandao,feedsbeandao）
     * 要把网络或者数据库返回的动态列表，和发送中的动态混合在一起返回给界面。
     *
     * @param data
     * @param timePoint
     *            timePoint>0: 合并更老的
     *            {data,task:data.end.time<=task.time<timePoint}
     *            timePoint<=0:合并最新的{data,task:data.end.time<=task.time}
     */
    private static LinkedList<Feeds> mergeFeedsAndTask(LinkedList<Feeds> tasks, LinkedList<Feeds> data, long timePoint) {
        if (tasks == null || tasks.size() == 0) {
            return data;
        }
        Collections.sort(tasks, mFeedsTaskCompratorByPhotoTime);
        if (data == null || data.size() == 0) {
            if (timePoint > 0) {
                // 返回所有更老的任务
                LinkedList<Feeds> res = new LinkedList<Feeds>();
                for (Feeds t : tasks) {
                    if (t.graphtime < timePoint) {
                        res.add(t);
                    }
                }
                return res;
            } else {
                // 返回最新的
                return tasks;
            }
        } else {
            // 编辑中的动态记录一下
            HashSet<String> feedsEdting = new HashSet<String>();
            for (Feeds f : tasks) {
                if (!TextUtils.isEmpty(f.guid)) {
                    feedsEdting.add(f.guid);
                }
            }
            LinkedList<Feeds> res = new LinkedList<Feeds>();
            // 最老动态的时间点
            long tail = data.get(data.size() - 1).graphtime;
            if (timePoint > 0) {
                // 返回更老的任务
                for (Feeds t : tasks) {
                    if (t.graphtime < timePoint && t.graphtime >= tail) {
                        res.add(t);
                    }
                }
            } else {
                for (Feeds t : tasks) {
                    if (t.graphtime >= tail) {
                        res.add(t);
                    }
                }
            }
            if (feedsEdting.size() > 0) {
                // 正在编辑中的动态，屏蔽之
                int n = data.size();
                for (int i = n - 1; i >= 0; i--) {
                    if (feedsEdting.contains(data.get(i).guid)) {
                        data.remove(i);
                    }
                }
            }
            data.addAll(res);
            Collections.sort(data, mFeedsTaskCompratorByPhotoTime);
            return data;
        }
    }
    private static LinkedList<Feeds> mergeCircleFeedsAndTask(LinkedList<Feeds> tasks, LinkedList<Feeds> data, long timePoint) {
        if (tasks == null || tasks.size() == 0) {
            return data;
        }
        Collections.sort(tasks, mFeedsTaskCompratorByPhotoTime);
        if (data == null || data.size() == 0) {
            if (timePoint > 0) {
                // 返回所有更老的任务
                LinkedList<Feeds> res = new LinkedList<Feeds>();
                for (Feeds t : tasks) {
                    if (t.modify_time < timePoint) {
                        res.add(t);
                    }
                }
                return res;
            } else {
                // 返回最新的
                return tasks;
            }
        } else {
            // 编辑中的动态记录一下
            HashSet<String> feedsEdting = new HashSet<String>();
            for (Feeds f : tasks) {
                if (!TextUtils.isEmpty(f.guid)) {
                    feedsEdting.add(f.guid);
                }
            }
            LinkedList<Feeds> res = new LinkedList<Feeds>();
            // 最老动态的时间点
            long tail = data.get(data.size() - 1).modify_time;
            if (timePoint > 0) {
                // 返回更老的任务
                for (Feeds t : tasks) {
                    if (t.modify_time < timePoint && t.modify_time >= tail) {
                        res.add(t);
                    }
                }
            } else {
                for (Feeds t : tasks) {
                    if (t.modify_time >= tail) {
                        res.add(t);
                    }
                }
            }
            if (feedsEdting.size() > 0) {
                // 正在编辑中的动态，屏蔽之
                int n = data.size();
                for (int i = n - 1; i >= 0; i--) {
                    if (feedsEdting.contains(data.get(i).guid)) {
                        data.remove(i);
                    }
                }
            }
            data.addAll(res);
            Collections.sort(data, mFeedsTaskCompratorByPhotoTime);
            return data;
        }

    }


    public static void postFeeds(Feeds feeds) {
        UserInfo userInfo = UserServer.getCurrentUser();
        if (userInfo.uid > 0){
            BabyData babyDatas = userInfo.getBaobaodata();
            if (babyDatas != null && babyDatas.baobaouid > 0) {
                feeds.baobaorealname = babyDatas.name;
            }
            feeds.creuid = userInfo.uid;
            feeds.crerealname = userInfo.realname;
            if (feeds.ext_member == null){
                feeds.ext_member = new CircleMember();
                feeds.ext_member.realname = userInfo.realname;
                feeds.ext_member.avatar = userInfo.avartar;
                feeds.ext_member.groupid = userInfo.groupid;
            }
        }
        feeds.fb_flag = Feeds.POST_NORMAL;
        TaskFeeds task = new TaskFeeds(feeds);
        TaskManager.addTask(task);
    }


    public static void comment(CommentInfo comment, final XLTResponseCallback<CommentInfo> listener) {

        comment.localid = String.valueOf(TaskManager.generateLocalFeedsId());
        EventBus.getDefault().post(new AddCommentEvent(comment));
        //EventBus.getDefault().post(new FeedsAmountEvent(comment.guid, 0, 1, 0));
        OkHttpClientManager.getInstance().addGsonRequest(CommentInfo.URL_POST, CommentInfo.class,
                new XLTResponseListener<CommentInfo>() {

                    @Override
                    public void onResponse(CommentInfo responseData, XLTError errorData) {
                        listener.onResponse(responseData, errorData);
                        if (errorData == null && responseData != null) {
                            EventBus.getDefault().post(new AddCommentEvent(responseData));
                            XLTToast.makeText(LsgApplication.getAppContext(), R.string.comment_success).show();

                        } else {
                            XLTToast.makeText(LsgApplication.getAppContext(), errorData.getMesssage()).show();
                        }

                    }
                }, OkHttpClientManager.getInstance().getmGson().toJson(comment));
    }

    public static boolean canDeleteComment(Feeds feeds, long authorId) {
        // checkDeleteCommentAuthority
        boolean author = false;
        if (authorId == UserServer.getCurrentUser().uid) {
            author = true;
        }else if(feeds.creuid == UserServer.getCurrentUser().uid){
            author = true;
        }
        else if(feeds.ext_circle_member_attention_type == MineCircle.ATTENTION_TYPE_CREATE
                || feeds.ext_circle_member_attention_type == MineCircle.ATTENTION_TYPE_ADMIN){
            author = true;
        }
        return author;
    }

    public static void deleteComment(final CommentInfo comment, final XLTResponseCallback<Object> listener) {
        JsonObject jo = new JsonObject();
        jo.addProperty("comment_id", comment.comment_id);
        // 再从网络刷新

        OkHttpClientManager.getInstance().addGsonRequest(CommentInfo.URL_DELETE, Object.class,
                new XLTResponseListener<Object>() {

                    @Override
                    public void onResponse(Object responseData, XLTError errorData) {
                        if (listener != null) {
                            listener.onResponse(responseData, errorData);
                        }
                        if (errorData == null) {
                            //EventBus.getDefault().post(new FeedsAmountEvent(comment.guid, 0, -1, 0));
                            EventBus.getDefault().post(new DeleteCommentEvent(comment.circle_id, comment.tid, comment.comment_id));
                        }
                    }
                }, jo.toString());
    }

    public static void listComment(final long tid, String queryOrderby, String queryPager, final XLTResponseCallback<RspListComment> listener){
        JsonObject jo = new JsonObject();
        jo.addProperty("tid", tid);
        if (!TextUtils.isEmpty(queryOrderby)){
            jo.addProperty("query_orderby", queryOrderby);
        }
        if (!TextUtils.isEmpty(queryPager)){
            jo.addProperty("query_pager", queryPager);
        }
        // 再从网络刷新

        OkHttpClientManager.getInstance().addGsonRequest(CommentInfo.URL_LIST, RspListComment.class,
                new XLTResponseListener<RspListComment>() {

                    @Override
                    public void onResponse(RspListComment responseData, XLTError errorData) {

                        if (listener != null) {
                            listener.onResponse(responseData, errorData);
                        }
                    }
                }, jo.toString());
    }
}