package com.talenton.lsg.server.bean.feed;

/**
 * Created by ttt on 2016/4/25.
 */
public class CircleMember {
    public final static int GROUP_PARENT = 21;
    public final static int GROUP_TEACHER = 22;

    public static CircleMember EMPTY = new CircleMember();

    public long uid; //用户ID
    public String avatar; //头像URL
    public String realname;//用户名
    public int groupid;//用户角色 21 家长 22 老师
    public int gxid;// 关系id,
    public String gxname;// 关系名称
    public int browse_count;// 访问次数,
    public long create_time;// 最近访问时间

}