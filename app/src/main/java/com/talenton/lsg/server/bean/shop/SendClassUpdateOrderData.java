package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/5/25.
 */
public class SendClassUpdateOrderData {
    public final static String URL="classroom.php?mod=updateorder&cmdcode=91";
    public String  orderid;
    public String  reason;
    public int result;
    public SendClassUpdateOrderData(String sOrderid,int iResult,String sReason){
        orderid = sOrderid;
        reason=sReason;
        result=iResult;

    }
}
