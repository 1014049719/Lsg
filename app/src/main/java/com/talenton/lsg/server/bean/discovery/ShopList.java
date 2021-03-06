package com.talenton.lsg.server.bean.discovery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ShopList implements Parcelable {

    /**
     * blogid : 2
     * subject : 开始活动了
     * dateline : 1460529717
     * attachment :  xxxxxxl
     * mendian_addr : 青秀山上东国际
     */

    public String blogid;
    public String subject;
    public String dateline;
    public String attachment;
    public String mendian_addr;
    public String type;
    public String describe;


    //序列化
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.blogid);
        dest.writeString(this.subject);
        dest.writeString(this.dateline);
        dest.writeString(this.attachment);
        dest.writeString(this.mendian_addr);
        dest.writeString(this.type);
        dest.writeString(this.describe);

    }

    public ShopList() {
    }

    protected ShopList(Parcel in) {
        this.blogid = in.readString();
        this.subject = in.readString();
        this.dateline = in.readString();
        this.attachment = in.readString();
        this.mendian_addr = in.readString();
        this.type = in.readString();
        this.describe = in.readString();

    }

    public static final Parcelable.Creator<ShopList> CREATOR = new Parcelable.Creator<ShopList>() {
        @Override
        public ShopList createFromParcel(Parcel source) {
            return new ShopList(source);
        }

        @Override
        public ShopList[] newArray(int size) {
            return new ShopList[size];
        }
    };
}
