package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/25.
 */
public class SendDeleteShopCartData {
    public final static String URL = "mobile/dele_cart_goods.php?cmdcode=50";
    public int rec_id;
    public SendDeleteShopCartData(int rec_id){
        this.rec_id=rec_id;
    }
}
