package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/5/31.
 */
public class SendGoodsIdData {
    public final static String URL = "mobile/goods_info.php";
    public int id;
    public SendGoodsIdData(int id){
        this.id=id;
    }
}
