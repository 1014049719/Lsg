package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/22.
 */
public class SendDeleteAdressData {
    public final static String URL = "mobile/dele_address.php?cmdcode=47";
    public int address_id;
    public SendDeleteAdressData(int address_id){
        this.address_id=address_id;
    }
}
