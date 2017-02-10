package com.talenton.lsg.server.bean.feed;

/**
 * Created by ttt on 2016/4/26.
 */
public class AttentionRecordData {
    //状态 -1-拒绝、0-申请、1-同意,
    public static final int ATTENTION_STATUS_EMPTY = -2;
    public static final int ATTENTION_STATUS_REFUSE = -1;
    public static final int ATTENTION_STATUS_APPLY = 0;
    public static final int ATTENTION_STATUS_AGREE = 1;

    public long attention_id;//关注申请id,
    public long circle_id;//圈子id
    public long applicant_uid;//申请人id,
    public String invitation_code;//邀请码(通过手机号码和同校关注无数据),
    public int attent_status; //状态 -1-拒绝、0-申请、1-同意,
    public int  attent_sources; //关注来源 0 邀请码 1 同校 2 手机号码,
    public long create_time;//创建时间
    public String ext_baobao_name;//宝宝姓名

    public CircleMember ext_member;

    public CircleMember getCircleMember(){
        if (ext_member == null){
            return CircleMember.EMPTY;
        }
        return ext_member;
    }
}
