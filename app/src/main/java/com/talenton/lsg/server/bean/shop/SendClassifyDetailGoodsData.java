package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/26.
 */
public class SendClassifyDetailGoodsData {

    public final static String URL = "mobile/category.php?mdcode=44";
    public int cat_id;
    public int orderprice;
    public int ordersalesnum;
    public int page;
    public  SendClassifyDetailGoodsData(int cat_id,int orderprice,int ordersalesnum,int page){
        this.cat_id=cat_id;
        this.orderprice=orderprice;
        this.ordersalesnum=ordersalesnum;
        this.page=page;
    }
}
