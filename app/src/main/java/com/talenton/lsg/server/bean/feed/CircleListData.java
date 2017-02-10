package com.talenton.lsg.server.bean.feed;

/**
 * Created by ttt on 2016/4/21.
 */
public class CircleListData {

    public static CircleListData EMPTY = new CircleListData();

    //类型 0-宝宝类、1-同龄圈类、2-主题类、3-学校类 97-获得我的收藏话题 98-我发表的话题 99-热门话题 100-个人主页
    public final static int CIRCLE_EMPTY = -1;
    public final static int CIRCLE_TYPE_BABY = 0;
    public final static int CIRCLE_TYPE_AGE = 1;
    public final static int CIRCLE_TYPE_THEME = 2;
    public final static int CIRCLE_TYPE_SCHOOL = 3;
    public final static int CIRCLE_TYPE_COLLECT = 97;
    public final static int CIRCLE_TYPE_MINE = 98;
    public final static int CIRCLE_TYPE_TOPIC = 99;
    public final static int CIRCLE_TYPE_MINE_PUBLIC = 100;

    public long circle_id;// 圈子id,
    public String circle_name;// 圈子名称,
    public String description;//圈子描述,
    public long belong_id;//所属ID（如果是宝宝类就是宝宝ID、学校类就是学校ID）,
    public long create_uid;//创建者ID（如果类型是宝宝类，则填入家长id）,
    public int circle_type;//类型 0-宝宝类、1-同龄圈类、2-主题类、3-学校类,
    public int is_public;// 是否公开 0 私密 1 公开,
    public int is_system;//是否系统圈子 0 否 1 是,
    public String circle_photo;//圈子照片,
    public int day_topics_count;//今日圈子话题数,
    public int members_count;//圈子用户总数,
    public int topics_count;// 圈子话题总数,
    public String topic_table;//使用的话题表名称,
    //public long create_time;//创建时间,
    public long modify_time;//修改时间,
    //public String operator;//最后操作人,
    public boolean ext_is_attented;// 当前用户是否关注此圈子 TRUE 关注 FLASE 未关注；未登录全部返回FLASE,
    public int ext_attent_status;// 当前用户是否申请关注此圈子 -2，无申请记录，-1，拒绝，0审核中，1通过

    public int type; //97-获得我的收藏话题 98-我发表的话题 99-热门话题

    public CircleListData(){
        ext_is_attented = false;
        ext_attent_status = AttentionRecordData.ATTENTION_STATUS_EMPTY;
    }
}
