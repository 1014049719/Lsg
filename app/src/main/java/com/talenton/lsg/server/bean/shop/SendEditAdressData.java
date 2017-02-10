package com.talenton.lsg.server.bean.shop;

/**
 * Created by xiaoxiang on 2016/4/22.
 */
public class SendEditAdressData {

    public final static String URL = "mobile/update_address.php?cmdcode=46";


    public int address_id;
    public String address;
    public String mobile;
    public String consignee;
    public String area;
    public String zipcode;
    public int is_default;
    public String best_time;

    public SendEditAdressData(int address_id,String consignee,String mobile,String area,String address,int is_default){
        this.address_id=address_id;
        this.address=address;
        this.mobile=mobile;
        this.consignee=consignee;
        this.area=area;
        this.is_default=is_default;
    }

}
