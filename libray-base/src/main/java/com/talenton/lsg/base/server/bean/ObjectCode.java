package com.talenton.lsg.base.server.bean;

/**
 * Created by ttt on 2016/4/13.
 */
public class ObjectCode {
    public final static String URL_REGISTER = "user.php?mod=register&cmdcode=1"; //注册
    public final static String URL_GET_SMS_CODE = "user.php?mod=smsyzcode&cmdcode=6"; //获取短信验证码
    public final static String URL_MODIFY_PASSWORD_NEED_SMS = "user.php?mod=changepass_yzcode&cmdcode=7";//输入验证码修改密码
    public final static String URL_MODIFY_BIND = "user.php?mod=modbind&cmdcode=16";//修改绑定手机
    public final static String URL_MODIFY_PERSONAL = "user.php?mod=modinfo&cmdcode=5";// 修改个人信息

    public int res;
    public String msg;
    public String cmdcode;
}
