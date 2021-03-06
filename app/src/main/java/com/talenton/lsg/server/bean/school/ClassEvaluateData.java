package com.talenton.lsg.server.bean.school;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zjh
 * @date 2016/4/11
 */
public class ClassEvaluateData implements Parcelable {
    private long cid; //评论ID
    private long uid; //评论人ID
    private String username; //评论人用户名
    private String postip; //评论IP
    private int port; //评论端口
    private long dateline; //评论时间
    private int status; //评论状态 1=删除 2=未审核
    private String message; //评论内容
    private float starcount; //星数
    private int likecount; //点赞数
    private String avartar; //头像地址
    private String realname; //真实姓名

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostip() {
        return postip;
    }

    public void setPostip(String postip) {
        this.postip = postip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getStarcount() {
        return starcount;
    }

    public void setStarcount(float starcount) {
        this.starcount = starcount;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public String getAvartar() {
        return avartar;
    }

    public void setAvartar(String avartar) {
        this.avartar = avartar;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.cid);
        dest.writeLong(this.uid);
        dest.writeString(this.username);
        dest.writeString(this.postip);
        dest.writeInt(this.port);
        dest.writeLong(this.dateline);
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeFloat(this.starcount);
        dest.writeInt(this.likecount);
        dest.writeString(this.avartar);
        dest.writeString(this.realname);
    }

    public ClassEvaluateData() {
    }

    protected ClassEvaluateData(Parcel in) {
        this.cid = in.readLong();
        this.uid = in.readLong();
        this.username = in.readString();
        this.postip = in.readString();
        this.port = in.readInt();
        this.dateline = in.readLong();
        this.status = in.readInt();
        this.message = in.readString();
        this.starcount = in.readFloat();
        this.likecount = in.readInt();
        this.avartar = in.readString();
        this.realname = in.readString();
    }

    public static final Creator<ClassEvaluateData> CREATOR = new Creator<ClassEvaluateData>() {
        @Override
        public ClassEvaluateData createFromParcel(Parcel source) {
            return new ClassEvaluateData(source);
        }

        @Override
        public ClassEvaluateData[] newArray(int size) {
            return new ClassEvaluateData[size];
        }
    };
}
