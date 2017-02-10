package com.talenton.lsg.base.server.bean;

import com.talenton.lsg.base.util.DateUtil;

import java.util.Calendar;

/**
 * Created by ttt on 2016/4/20.
 */
public class BabyData {

    public static BabyData EMPTY = new BabyData();

    public long baobaouid = 0;//宝宝ID
    public String name;//宝宝姓名
    public long birthday = 0;//出生日期时间戳
    public long schoolid = 0;//学校ID
    public String resideprovince;//省
    public String residecity;//市
    public String residedist;//区
    public String classname;//班级名称
    public int isattention;//0-未关注、1-已关注
    public String avartar;//家长头像路径
    public String realname;//家长姓名
    public String school_name;//学校名称

    public SchoolData schooldata;
    public BabyCircle circledata;

    public BabyData(){
        schooldata = SchoolData.EMPTY;
    }

    public String getAge() {
        if (birthday == 0) return "";
        Calendar now = Calendar.getInstance();
        String[] date = DateUtil.parseTimeToYMD(birthday).split("\\-");
        int birthyear = Integer.parseInt(date[0]);
        int birthmonth = Integer.parseInt(date[1]);
        int y = now.get(Calendar.YEAR) - birthyear;
        int m = now.get(Calendar.MONTH) + 1 - birthmonth;
        if (m < 0) {
            y--;
            m = 12 + m;
        }
        String age = "";
        if (y <= 25) {
            StringBuilder a = new StringBuilder();
            if (y > 0) {
                a.append(y);
                a.append("岁");
            }
            a.append(m);
            a.append("个月");
            age = a.toString();
        }
        return age;
    }
}
