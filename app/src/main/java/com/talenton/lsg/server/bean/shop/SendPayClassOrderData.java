package com.talenton.lsg.server.bean.shop;

import java.util.ArrayList;

/**
 * Created by xiaoxiang on 2016/5/25.
 */

public class SendPayClassOrderData {
    public final static String URL = "classroom.php?mod=join&cmdcode=90";
    public long  aid;
    public int paytype;
    public SendPayClassOrderData(long  aid,int paytype){
        this.aid=aid;
        this.paytype=paytype;
    }
}