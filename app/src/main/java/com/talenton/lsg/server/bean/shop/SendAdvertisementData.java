package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/20.
 */
public class SendAdvertisementData {

    public final static String URL = "user.php?mod=ad&cmdcode=15";
    public int advPosition;
    public SendAdvertisementData(int advPosition){
        this.advPosition=advPosition;};
}
