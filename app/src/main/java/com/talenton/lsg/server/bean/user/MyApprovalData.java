package com.talenton.lsg.server.bean.user;

/**
 * @author zjh
 * @date 2016/5/24
 */
public class MyApprovalData {
    public static final int APPROVAL_NO = 0; //未审批
    public static final int APPROVAL_ACCEPT = 1; //通过
    public static final int APPROVAL_REFUSE = 2; //拒绝

    private long examineid; //审批ID
    private long schoolid; //学校ID
    private long uid; //申请人ID
    private String realname; //申请人姓名
    private long baobaouid; //申请宝宝ID
    private String baobaorealname; //宝宝姓名
    private long dateline; //申请时间
    private int flag; //审批标志，0-未审批、1-通过、2-拒绝
    private String schoolname; //学校名称
    private String avartar; //头像地址
    private String message; //验证信息

    public long getExamineid() {
        return examineid;
    }

    public void setExamineid(long examineid) {
        this.examineid = examineid;
    }

    public long getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(long schoolid) {
        this.schoolid = schoolid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public long getBaobaouid() {
        return baobaouid;
    }

    public void setBaobaouid(long baobaouid) {
        this.baobaouid = baobaouid;
    }

    public String getBaobaorealname() {
        return baobaorealname;
    }

    public void setBaobaorealname(String baobaorealname) {
        this.baobaorealname = baobaorealname;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getAvartar() {
        return avartar;
    }

    public void setAvartar(String avartar) {
        this.avartar = avartar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
