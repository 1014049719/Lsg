package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/5/30.
 */
public class SendLogisticsData {
    public String key;
    public String com;
    public String no;
    public String url;
    public SendLogisticsData(String  key,String  com,String no){
        this.key=key;
        this.com=com;
        this.no=no;
        this.url="http://v.juhe.cn/exp/index?"+"key="+key+"&com="+com+"&no="+no;
    }
}
