package com.talenton.lsg.base;

/**
 * 程序配置类：定义一些全局常量
 * Created by ttt on 2016/3/24.
 */
public class AppConfig {
    public static final String CHANNEL_NAME = "offical";
    public static final float FORMAT_COUNT_MIN_VALUE = 10000f;
    public static final float FORMAT_COUNT_MAX_VALUE = 10000 * FORMAT_COUNT_MIN_VALUE;
    public static String HOME_URL;
    public static String HOME_URL_SHOP;
    public static String NoHTTP_HOME_URL;
    public static String NoHTTP_HOME_URL_SHOP;
    public static final boolean DEBUG = false;

    static {
        if (DEBUG) {
            // DEVELOP
            HOME_URL = "http://180.139.136.202:883/xt/";
            NoHTTP_HOME_URL = "180.139.136.202:883/xt/";
            HOME_URL_SHOP = "http://180.139.136.202:883/lsgsc/";
            NoHTTP_HOME_URL_SHOP = "180.139.136.202:883/lsgsc/";
            //prebuild
            //HOME_URL = "http://test.jyex.cn/";
            //NoHTTP_HOME_URL = "test.jyex.cn/";
        } else {
            HOME_URL = "http://www.jyex.cn/xt/";
            NoHTTP_HOME_URL = "www.jyex.cn/xt/";
            HOME_URL_SHOP = "http://www.jyex.cn/lsgsc/";
            NoHTTP_HOME_URL_SHOP = "www.jyex.cn/lsgsc/";
        }
    }

}