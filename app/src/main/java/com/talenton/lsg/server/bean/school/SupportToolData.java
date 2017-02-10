package com.talenton.lsg.server.bean.school;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zjh
 * @date 2016/4/11
 */
public class SupportToolData implements Parcelable {
    private long aid; //教具ID
    private long shopid; //商品ID
    private String shopname; //商品名称
    private double shopprice; //商品价格
    private double market_price; //市场价
    private String shopmemo; //商品描述
    private String shopimg; //商品图片
    private String shopurl; //商品地址
    private double shoppraise; //好评率
    private int shopsales; //销量

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public long getShopid() {
        return shopid;
    }

    public void setShopid(long shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public double getShopprice() {
        return shopprice;
    }

    public void setShopprice(double shopprice) {
        this.shopprice = shopprice;
    }

    public String getShopmemo() {
        return shopmemo;
    }

    public void setShopmemo(String shopmemo) {
        this.shopmemo = shopmemo;
    }

    public String getShopimg() {
        return shopimg;
    }

    public void setShopimg(String shopimg) {
        this.shopimg = shopimg;
    }

    public String getShopurl() {
        return shopurl;
    }

    public void setShopurl(String shopurl) {
        this.shopurl = shopurl;
    }


    public double getShoppraise() {
        return shoppraise;
    }

    public void setShoppraise(double shoppraise) {
        this.shoppraise = shoppraise;
    }

    public int getShopsales() {
        return shopsales;
    }

    public void setShopsales(int shopsales) {
        this.shopsales = shopsales;
    }

    public double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(double market_price) {
        this.market_price = market_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.aid);
        dest.writeLong(this.shopid);
        dest.writeString(this.shopname);
        dest.writeDouble(this.shopprice);
        dest.writeDouble(this.market_price);
        dest.writeString(this.shopmemo);
        dest.writeString(this.shopimg);
        dest.writeString(this.shopurl);
        dest.writeDouble(this.shoppraise);
        dest.writeInt(this.shopsales);
    }

    public SupportToolData() {
    }

    protected SupportToolData(Parcel in) {
        this.aid = in.readLong();
        this.shopid = in.readLong();
        this.shopname = in.readString();
        this.shopprice = in.readDouble();
        this.market_price = in.readDouble();
        this.shopmemo = in.readString();
        this.shopimg = in.readString();
        this.shopurl = in.readString();
        this.shoppraise = in.readDouble();
        this.shopsales = in.readInt();
    }

    public static final Creator<SupportToolData> CREATOR = new Creator<SupportToolData>() {
        @Override
        public SupportToolData createFromParcel(Parcel source) {
            return new SupportToolData(source);
        }

        @Override
        public SupportToolData[] newArray(int size) {
            return new SupportToolData[size];
        }
    };
}
