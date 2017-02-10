package com.talenton.lsg.server.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Wang.'''' on 2016/5/10.
 */
public class MyOrders implements Parcelable {
    /**
     * order_id : 28
     * order_sn : 2016042183041
     * order_time : 2016-04-21 17:28:47
     * order_status : 0
     * pay_status : 0
     * shipping_status : 0
     * total_fee : 4999.50
     * num : 10
     * goodslist : [{"rec_id":"41","order_id":"28","goods_id":"9","name":"诺基亚E66","goods_sn":"ECS000009","market_price":"2757.60","goods_number":"2","goods_price":"2298.00","goods_attr":"颜色:白色","is_real":"1","is_gift":"0","subtotal":"4596.00","extension_code":"","goods_thumb":"images/200905/thumb_img/9_thumb_G_1241511871555.jpg"}]
     */

    private String order_id;
    private String order_sn;
    private String order_time;
    private String order_status;
    private String order_zhuangtai;
    private String pay_status;
    private String shipping_status;
    private String total_fee;
    private int num;
    private String wl_name;
    private String wl_code;
    private String wl_sn;



    /**
     * rec_id : 41
     * order_id : 28
     * goods_id : 9
     * name : 诺基亚E66
     * goods_sn : ECS000009
     * market_price : 2757.60
     * goods_number : 2
     * goods_price : 2298.00
     * goods_attr : 颜色:白色
     * is_real : 1
     * is_gift : 0
     * subtotal : 4596.00
     * extension_code :
     * goods_thumb : images/200905/thumb_img/9_thumb_G_1241511871555.jpg
     */

    private List<GoodslistBean> goodslist;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getOrder_zhuangtai() {
        return order_zhuangtai;
    }

    public void setOrder_zhuangtai(String order_zhuangtai) {
        this.order_zhuangtai = order_zhuangtai;
    }

    public String getWl_name() {
        return wl_name;
    }

    public void setWl_name(String wl_name) {
        this.wl_name = wl_name;
    }

    public String getWl_code() {
        return wl_code;
    }

    public void setWl_code(String wl_code) {
        this.wl_code = wl_code;
    }

    public String getWl_sn() {
        return wl_sn;
    }

    public void setWl_sn(String wl_sn) {
        this.wl_sn = wl_sn;
    }

    public List<GoodslistBean> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<GoodslistBean> goodslist) {
        this.goodslist = goodslist;
    }

    public static class GoodslistBean implements Parcelable {
        private String rec_id;
        private String order_id;
        private String goods_id;
        private String name;
        private String goods_sn;
        private String market_price;
        private String goods_number;
        private String goods_price;
        private String goods_attr;
        private String is_real;
        private String is_gift;
        private String subtotal;
        private String extension_code;
        private String goods_thumb;
        private String goods_img;
        private String pj;

        public String getRec_id() {
            return rec_id;
        }

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(String goods_number) {
            this.goods_number = goods_number;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_attr() {
            return goods_attr;
        }

        public void setGoods_attr(String goods_attr) {
            this.goods_attr = goods_attr;
        }

        public String getIs_real() {
            return is_real;
        }

        public void setIs_real(String is_real) {
            this.is_real = is_real;
        }

        public String getIs_gift() {
            return is_gift;
        }

        public void setIs_gift(String is_gift) {
            this.is_gift = is_gift;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getExtension_code() {
            return extension_code;
        }

        public void setExtension_code(String extension_code) {
            this.extension_code = extension_code;
        }

        public String getGoods_thumb() {
            return goods_thumb;
        }

        public void setGoods_thumb(String goods_thumb) {
            this.goods_thumb = goods_thumb;
        }

        public String getPj() {
            return pj;
        }

        public void setPj(String pj) {
            this.pj = pj;
        }

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

//序列化

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.rec_id);
            dest.writeString(this.order_id);
            dest.writeString(this.goods_id);
            dest.writeString(this.name);
            dest.writeString(this.goods_sn);
            dest.writeString(this.market_price);
            dest.writeString(this.goods_number);
            dest.writeString(this.goods_price);
            dest.writeString(this.goods_attr);
            dest.writeString(this.is_real);
            dest.writeString(this.is_gift);
            dest.writeString(this.subtotal);
            dest.writeString(this.extension_code);
            dest.writeString(this.goods_thumb);
            dest.writeString(this.goods_img);
            dest.writeString(this.pj);
        }

        public GoodslistBean() {
        }

        protected GoodslistBean(Parcel in) {
            this.rec_id = in.readString();
            this.order_id = in.readString();
            this.goods_id = in.readString();
            this.name = in.readString();
            this.goods_sn = in.readString();
            this.market_price = in.readString();
            this.goods_number = in.readString();
            this.goods_price = in.readString();
            this.goods_attr = in.readString();
            this.is_real = in.readString();
            this.is_gift = in.readString();
            this.subtotal = in.readString();
            this.extension_code = in.readString();
            this.goods_thumb = in.readString();
            this.goods_img = in.readString();
            this.pj = in.readString();
        }

        public static final Parcelable.Creator<GoodslistBean> CREATOR = new Parcelable.Creator<GoodslistBean>() {
            @Override
            public GoodslistBean createFromParcel(Parcel source) {
                return new GoodslistBean(source);
            }

            @Override
            public GoodslistBean[] newArray(int size) {
                return new GoodslistBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.order_sn);
        dest.writeString(this.order_time);
        dest.writeString(this.order_status);
        dest.writeString(this.order_zhuangtai);
        dest.writeString(this.pay_status);
        dest.writeString(this.shipping_status);
        dest.writeString(this.total_fee);
        dest.writeInt(this.num);
        dest.writeString(this.wl_name);
        dest.writeString(this.wl_code);
        dest.writeString(this.wl_sn);
        dest.writeTypedList(this.goodslist);
    }

    public MyOrders() {
    }

    protected MyOrders(Parcel in) {
        this.order_id = in.readString();
        this.order_sn = in.readString();
        this.order_time = in.readString();
        this.order_status = in.readString();
        this.order_zhuangtai = in.readString();
        this.pay_status = in.readString();
        this.shipping_status = in.readString();
        this.total_fee = in.readString();
        this.num = in.readInt();
        this.wl_name = in.readString();
        this.wl_code = in.readString();
        this.wl_sn = in.readString();
        this.goodslist = in.createTypedArrayList(GoodslistBean.CREATOR);
    }

    public static final Parcelable.Creator<MyOrders> CREATOR = new Parcelable.Creator<MyOrders>() {
        @Override
        public MyOrders createFromParcel(Parcel source) {
            return new MyOrders(source);
        }

        @Override
        public MyOrders[] newArray(int size) {
            return new MyOrders[size];
        }
    };
}
