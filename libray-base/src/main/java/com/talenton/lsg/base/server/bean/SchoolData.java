package com.talenton.lsg.base.server.bean;

/**
 * Created by ttt on 2016/4/20.
 */
public class SchoolData {

    public final static SchoolData EMPTY = new SchoolData();
    public long schoolid = 0;//学校ID
    public String name;//学校名称
    public String resideprovince;//省
    public String residecity;//市
    public String residedist;//区
    public String address;//详细地址
    public String memo;//备注
    public String mobile;//联系电话
    public int flag; ////审批标志，-1 未申请 0-未审批、1-通过、2-拒绝,
    public boolean isShow = true;
    public SchoolData(){

    }

    public SchoolData(long schoolid, String name, String resideprovince, String residecity, String residedist, String address, String memo, String mobile, int flag) {
        this.schoolid = schoolid;
        this.name = name;
        this.resideprovince = resideprovince;
        this.residecity = residecity;
        this.residedist = residedist;
        this.address = address;
        this.memo = memo;
        this.mobile = mobile;
        this.flag = flag;
    }
}
