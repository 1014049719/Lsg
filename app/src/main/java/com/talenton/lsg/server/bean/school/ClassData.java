package com.talenton.lsg.server.bean.school;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zjh
 * @date 2016/4/8
 */
public class ClassData implements Parcelable {
    public static int FREE = 0; //免费
    public static int CHARGE = 1; //收费
    private long aid; //课程id
    private String title; //标题
    private int ispay; //是否付费 0=免费,1=付费
    private int joincount; //参加人数
    private long startdateline; //开始学习时间
    private int lastupdate;
    private int totalcatalog; //总节数
    private int iscomplete; //是否已经更新完成
    private String pic; //封面图片路径
    private long dateline; //发布时间
    private int likecount; //点赞数
    private int age; //年龄段
    private String username; //发布者
    private double price; //价格
    private String updatesections; //更新的章节

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(int iscomplete) {
        this.iscomplete = iscomplete;
    }

    public int getTotalcatalog() {
        return totalcatalog;
    }

    public void setTotalcatalog(int totalcatalog) {
        this.totalcatalog = totalcatalog;
    }

    public int getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(int lastupdate) {
        this.lastupdate = lastupdate;
    }

    public long getStartdateline() {
        return startdateline;
    }

    public void setStartdateline(long startdateline) {
        this.startdateline = startdateline;
    }

    public int getJoincount() {
        return joincount;
    }

    public void setJoincount(int joincount) {
        this.joincount = joincount;
    }

    public int getIspay() {
        return ispay;
    }

    public void setIspay(int ispay) {
        this.ispay = ispay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getUpdatesections() {
        return updatesections;
    }

    public void setUpdatesections(String updatesections) {
        this.updatesections = updatesections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassData classData = (ClassData) o;

        if (aid != classData.aid) return false;
        if (ispay != classData.ispay) return false;
        if (joincount != classData.joincount) return false;
        if (startdateline != classData.startdateline) return false;
        if (lastupdate != classData.lastupdate) return false;
        if (totalcatalog != classData.totalcatalog) return false;
        if (iscomplete != classData.iscomplete) return false;
        if (dateline != classData.dateline) return false;
        if (likecount != classData.likecount) return false;
        if (age != classData.age) return false;
        if (Double.compare(classData.price, price) != 0) return false;
        if (title != null ? !title.equals(classData.title) : classData.title != null) return false;
        if (pic != null ? !pic.equals(classData.pic) : classData.pic != null) return false;
        if (username != null ? !username.equals(classData.username) : classData.username != null)
            return false;
        return !(updatesections != null ? !updatesections.equals(classData.updatesections) : classData.updatesections != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (aid ^ (aid >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + ispay;
        result = 31 * result + joincount;
        result = 31 * result + (int) (startdateline ^ (startdateline >>> 32));
        result = 31 * result + lastupdate;
        result = 31 * result + totalcatalog;
        result = 31 * result + iscomplete;
        result = 31 * result + (pic != null ? pic.hashCode() : 0);
        result = 31 * result + (int) (dateline ^ (dateline >>> 32));
        result = 31 * result + likecount;
        result = 31 * result + age;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (updatesections != null ? updatesections.hashCode() : 0);
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.aid);
        dest.writeString(this.title);
        dest.writeInt(this.ispay);
        dest.writeInt(this.joincount);
        dest.writeLong(this.startdateline);
        dest.writeInt(this.lastupdate);
        dest.writeInt(this.totalcatalog);
        dest.writeInt(this.iscomplete);
        dest.writeString(this.pic);
        dest.writeLong(this.dateline);
        dest.writeInt(this.likecount);
        dest.writeInt(this.age);
        dest.writeString(this.username);
        dest.writeDouble(this.price);
        dest.writeString(this.updatesections);
    }

    public ClassData() {
    }

    protected ClassData(Parcel in) {
        this.aid = in.readLong();
        this.title = in.readString();
        this.ispay = in.readInt();
        this.joincount = in.readInt();
        this.startdateline = in.readLong();
        this.lastupdate = in.readInt();
        this.totalcatalog = in.readInt();
        this.iscomplete = in.readInt();
        this.pic = in.readString();
        this.dateline = in.readLong();
        this.likecount = in.readInt();
        this.age = in.readInt();
        this.username = in.readString();
        this.price = in.readDouble();
        this.updatesections = in.readString();
    }

    public static final Parcelable.Creator<ClassData> CREATOR = new Parcelable.Creator<ClassData>() {
        @Override
        public ClassData createFromParcel(Parcel source) {
            return new ClassData(source);
        }

        @Override
        public ClassData[] newArray(int size) {
            return new ClassData[size];
        }
    };
}
