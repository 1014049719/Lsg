package com.talenton.lsg.server.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ttt on 2016/5/30.
 */
public class FamilyData implements Parcelable{

    public long circleId;
    public long memberId;
    public long uid;
    public long create_uid;// 创建者ID（如果类型是宝宝类，则填入家长id）,
    public String photo;
    public String relname;
    public int attentionType;
    public int gxid;
    public String gxName;
    public int topic_count;// 动态总数,
    public int images_count;// 照片总数
    public long babyid;

    public FamilyData(){

    }

    protected FamilyData(Parcel in) {
        circleId = in.readLong();
        memberId = in.readLong();
        uid = in.readLong();
        create_uid = in.readLong();
        photo = in.readString();
        relname = in.readString();
        attentionType = in.readInt();
        gxid = in.readInt();
        gxName = in.readString();
        topic_count = in.readInt();
        images_count = in.readInt();
        babyid = in.readLong();
    }

    public static final Creator<FamilyData> CREATOR = new Creator<FamilyData>() {
        @Override
        public FamilyData createFromParcel(Parcel in) {
            return new FamilyData(in);
        }

        @Override
        public FamilyData[] newArray(int size) {
            return new FamilyData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(circleId);
        dest.writeLong(memberId);
        dest.writeLong(uid);
        dest.writeLong(create_uid);
        dest.writeString(photo);
        dest.writeString(relname);
        dest.writeInt(attentionType);
        dest.writeInt(gxid);
        dest.writeString(gxName);
        dest.writeInt(topic_count);
        dest.writeInt(images_count);
        dest.writeLong(babyid);
    }


}
