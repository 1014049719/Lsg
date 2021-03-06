package com.talenton.lsg.base.server.bean;

/**
 * Created by ttt on 2016/5/30.
 */
public class BabyCircle {
    public long circle_id;// 圈子id,
    public String circle_name;// 圈子名称,
    public String description;//圈子描述,
    public long belong_id;//所属ID（如果是宝宝类就是宝宝ID、学校类就是学校ID）,
    public long create_uid;//创建者ID（如果类型是宝宝类，则填入家长id）,
    public int circle_type;//类型 0-宝宝类、1-同龄圈类、2-主题类、3-学校类,
    public String age_group;//年龄段 用||隔开 比如0||3 当圈子类型是同龄圈时适用
    public int is_public;// 是否公开 0 私密 1 公开,
    public int is_system;//是否系统圈子 0 否 1 是,
    public String circle_photo;//圈子照片,
    public String circle_bg; //圈子背景
    public int day_topics_count;//今日圈子话题数,
    public int members_count;//圈子用户总数,
    public int topics_count;// 圈子话题总数,
    public String topic_table;//使用的话题表名称,
    //public long create_time;//创建时间,
    //public long modify_time;//修改时间,
    //public String operator;//最后操作人,
}
