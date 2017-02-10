package com.talenton.lsg.server.bean.user;

import com.talenton.lsg.base.server.bean.SchoolData;
import com.talenton.lsg.server.bean.feed.CircleListData;

/**
 * Created by ttt on 2016/5/3.
 */
public class SchoolBabyData {
    public long baobaouid = 0;//宝宝ID
    public String name;//宝宝姓名
    public long birthday = 0;//出生日期时间戳
    public long schoolid = 0;//学校ID
    public String schoolname;//学校名称
    public String resideprovince;//省
    public String residecity;//市
    public String residedist;//区
    public String classname;//班级名称
    public int isattention;//0-未关注、1-已关注
    public String avartar;//家长头像路径
    public String realname;//家长姓名

    public CircleListData circledata;
    public SchoolData schooldata;

    public CircleListData getCircledata(){
        if (circledata == null){
            return CircleListData.EMPTY;
        }
        return circledata;
    }

    public SchoolData getSchooldata(){
        if (schooldata == null){
            return SchoolData.EMPTY;
        }
        return schooldata;
    }
}
