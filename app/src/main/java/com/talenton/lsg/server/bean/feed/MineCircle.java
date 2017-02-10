package com.talenton.lsg.server.bean.feed;

/**
 * Created by ttt on 2016/4/25.
 */
public class MineCircle {
    /*
    1、我自己的宝宝圈：
    即是用户创建宝宝的时候生成该宝宝的圈子，所以是圈主，条件：attention_type==2 && ext_circle['circle_type']==0

    2、我关注的宝宝圈：
    即我关注其他的宝宝圈子，即可能是管理员或者一般用户，条件：（attention_type==0 || attention_type==1 ）&&ext_circle['circle_type']==0

    3、我的学校圈：
    学校类型的圈子都是私密的，只有1个是公共圈的
    所以我的学校圈条件：ext_circle['circle_type']==3 && ext_circle['is_public']==0

    4、学校广场
    条件：ext_circle['circle_type']==3 && ext_circle['is_public']==1
    */

    //关系类型 0-关注、1-管理员、2- 创建者（圈主）,
    public final static int ATTENTION_TYPE_EMPTY = -1;
    public final static int ATTENTION_TYPE = 0;
    public final static int ATTENTION_TYPE_ADMIN = 1;
    public final static int ATTENTION_TYPE_CREATE = 2;

    //1 推荐圈子 2 首页未登录
    public final static int SETTING_TYPE_RECOMMEND = 2;
    public final static int SETTING_TYPE_HOME = 2;

    public long circle_member_id;// 唯一id,
    public long circle_id;// 圈子id,
    public long uid;//关注用户id,
    public long belong_id;//所属ID（如果是宝宝类就是宝宝ID、学校类就是学校ID）,
    public long create_uid;// 创建者ID（如果类型是宝宝类，则填入家长id）,
    public int attention_type;//关系类型 0-关注、1-管理员、2- 创建者（圈主）,
    //public long create_time;// 创建时间,
    //public long modify_time;//修改时间,
    //public String operator;//最后操作人,

    public CircleListData ext_circle;
    public CircleMember ext_member;
    public CircleMember ext_relation;
    public CircleMember ext_browse;

    public CircleListData getCircleListData(){
        if (ext_circle == null){
            return CircleListData.EMPTY;
        }
        return ext_circle;
    }

    public CircleMember getCircleMember(){
        if (ext_member == null){
            return CircleMember.EMPTY;
        }
        return ext_member;
    }
}
