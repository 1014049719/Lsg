package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/7.
 */

import java.io.Serializable;


public final class GoodsInfo implements Serializable {
    /**
     *
     */

    private static final long serialVersionUID = -582457752806705419L;

    public final static String URL_LIST = "mod=gift&ac=giftinfo&cmdcode=23";

    public final static String KEY_GIFT_TYPE = "gifttype";

    public final static int GIFT_TYPE_FAMILY = 1;

    public final static int GIFT_TYPE_TEACHER = 2;

    public String promote_price;
    public int goods_id;
    public String name;
    public  String  market_price;
    public double shop_price;
    public String thumb;
    public String url;
    public String promote_start_date;
    public String promote_end_date;
    public String salesnum;
    public String haol;
    public String goods_img;
}

