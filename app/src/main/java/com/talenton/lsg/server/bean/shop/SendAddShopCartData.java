package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/28.
 */
public class SendAddShopCartData {
    public final static String URL = "mobile/addtocart.php?cmdcode=54";
    public int goods_id;
    public int number;
    public  SendAddShopCartData(int goods_id,int number){
        this.goods_id=goods_id;
        this.number=number;
    }
}
