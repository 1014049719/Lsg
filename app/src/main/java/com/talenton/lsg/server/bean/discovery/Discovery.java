package com.talenton.lsg.server.bean.discovery;

/**
 * Created by Administrator on 2016/4/7.
 */
public class Discovery {

    public String name;
    public int number;
    public int imageSrc;

   //构造函数
    public Discovery(int imageSrc, String name) {
        this.imageSrc = imageSrc;
        this.name = name;
    }
}
