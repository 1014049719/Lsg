package com.talenton.lsg.server.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Wang.'''' on 2016/5/6.
 */
public class ShopPoints implements Parcelable {

    public String gid;
    public String giftname;
    public String imgurl;
    public String points;
    public String giftnum;


    //序列化


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gid);
        dest.writeString(this.giftname);
        dest.writeString(this.imgurl);
        dest.writeString(this.points);
        dest.writeString(this.giftnum);
    }

    public ShopPoints() {
    }

    protected ShopPoints(Parcel in) {
        this.gid = in.readString();
        this.giftname = in.readString();
        this.imgurl = in.readString();
        this.points = in.readString();
        this.giftnum = in.readString();
    }

    public static final Parcelable.Creator<ShopPoints> CREATOR = new Parcelable.Creator<ShopPoints>() {
        @Override
        public ShopPoints createFromParcel(Parcel source) {
            return new ShopPoints(source);
        }

        @Override
        public ShopPoints[] newArray(int size) {
            return new ShopPoints[size];
        }
    };
}
