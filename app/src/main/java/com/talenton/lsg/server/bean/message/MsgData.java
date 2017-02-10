package com.talenton.lsg.server.bean.message;

import android.content.Context;
import android.text.TextUtils;

import com.talenton.lsg.R;
import com.talenton.lsg.base.AppConfig;

/**
 * @author zjh
 * @date 2016/5/11
 */
public class MsgData {
    public static final int TYPE_OPEN = 0; //可以点击
    public static final int TYPE_OPEN_AND_IMG = 1; //可以点击且右边有缩略图
    public static final int TYPE_OPEN_NO = 2; //不可以点击

    private int msgType = -1;

    public MsgData(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        if (msgType != -1){
            return msgType;
        }
        if (imgurl != null && !TextUtils.isEmpty(imgurl) && !"null".equals(imgurl)){
            return TYPE_OPEN_AND_IMG;
        }else{
            if (omode == 0 || omode == 3){
                return TYPE_OPEN_NO;
            }else {
                return TYPE_OPEN;
            }
        }
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public static MsgData getTipsMsgData(Context context){
        MsgData msgData = new MsgData(TYPE_OPEN);
//        msgData.setAvatar(AppConfig.HOME_URL + context.getString(R.string.circle_logo_icon));
        msgData.setIsTips(true);
        msgData.setContent(context.getString(R.string.message_text_introduce));
        msgData.setNickname(context.getString(R.string.message_text_welcome));
        msgData.setDateline(System.currentTimeMillis());
        return msgData;
    }

    private int id;
    private int ts_id; //推送任务ID
    private String srcuid; //发送者UID
    private String nickname; //发送者名称(如果是家长，显示【李杨爸爸】/昵称/真实姓名/用户名,如果是老师，显示【李丽老师】)
    private String ruid; //接收者UID
    private int ptype;//推送类型 (0:业务推送  1:后台推送, .....)
    private int tstype; //推送消息类别// 1-动态、2-家园、其他待定义
    private String ntype; ////推送内容类别
    private String ntype2; //推送内容类别2//（子栏目）
    private int omode; //跳转处理方式
    private String content; //"xxx发布了一条新的动态",//内容
    private String imgurl;//缩略图url
    private String url; //处理方式相关内容(动态guid/URL/亲友ID等)
    private long dateline;//生成时间
    private String avatar; //发布者头像
    private boolean isTips; //是否为提示数据

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTs_id() {
        return ts_id;
    }

    public void setTs_id(int ts_id) {
        this.ts_id = ts_id;
    }

    public String getSrcuid() {
        return srcuid;
    }

    public void setSrcuid(String srcuid) {
        this.srcuid = srcuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }

    public int getPtype() {
        return ptype;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public int getTstype() {
        return tstype;
    }

    public void setTstype(int tstype) {
        this.tstype = tstype;
    }

    public String getNtype() {
        return ntype;
    }

    public void setNtype(String ntype) {
        this.ntype = ntype;
    }

    public String getNtype2() {
        return ntype2;
    }

    public void setNtype2(String ntype2) {
        this.ntype2 = ntype2;
    }

    public int getOmode() {
        return omode;
    }

    public void setOmode(int omode) {
        this.omode = omode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isTips() {
        return isTips;
    }

    public void setIsTips(boolean isTips) {
        this.isTips = isTips;
    }
}
