package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/7.
 */

import java.io.Serializable;


public final class GoodsCartInfo implements Serializable {
    /**
     *
     */

    private static final long serialVersionUID = -582457752806705419L;


    public int rec_id;
    public int user_id;
    public int is_real;
    public String extension_code;
    public int is_gift;
    public int is_shipping;
    public String goods_attr_id;
    public int goods_id;
    public String goods_sn;
    public String name;
    public  String market_price;
    public double goods_price;
    public int goods_number;
  //  public String goods_thumb;
    public String goods_img;
    public String goods_attr;
    public double subtotal;


}