package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/21.
 */

public class SendSearchGoodsData {
    public final static String URL = "mobile/search.php?cmdcode=43";

    public String keywords;
    public int orderprice;
    public int orderdateline;
    public int page;
    public int ordersalesnum;


    public SendSearchGoodsData(String  keywords){
        this.keywords=keywords;

    }
    public SendSearchGoodsData(String  keywords ,int orderprice,int orderdateline,int ordersalesnum,int page){
        this.keywords=keywords;
        this.orderprice=orderprice;
        this.orderdateline=orderdateline;
        this.ordersalesnum=ordersalesnum;
        this.page=page;
    }

}

