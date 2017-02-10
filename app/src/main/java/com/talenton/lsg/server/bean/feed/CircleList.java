package com.talenton.lsg.server.bean.feed;

/**
 * Created by ttt on 2016/4/21.
 */
public class CircleList {
    public final static String URL_GET_CIRCLE_LIST = "circle.php?mod=circle&ac=getcircles&cmdcode=20";
    public final static String URL_GET_SCHOOL_CIRCLE = "circle.php?mod=circle&ac=getmyschoolcircles&cmdcode=139";

    public long	circle_id; //圈子id
    public String circle_name;//圈子名称
    public String description;//圈子描述
    public long	belong_id;//所属ID 如果是宝宝类就是宝宝ID、学校类就是学校ID
    public long	create_uid;//创建者ID 如果类型是宝宝类，则填入家长id，即圈主
    public int circle_type;//圈子类型 0-宝宝类、1-同龄圈类、2-主题类、3-学校类
    public int is_public;//是否公开	0 私密 1 公开
    public int is_system;//是否系统圈子 0 否 1 是
    public String query_orderby;// 排序字段 按所填写的字段进行倒序排列数据，不填则默认创建按时间倒序
    public String query_pager;//查询页数 第几页，默认第一页

    public CircleList(){
        circle_id = 0;
        belong_id = 0;
        create_uid = 0;
        circle_type = CircleListData.CIRCLE_EMPTY;
        is_public = CircleListData.CIRCLE_EMPTY;
        is_system = CircleListData.CIRCLE_EMPTY;
    }
}
