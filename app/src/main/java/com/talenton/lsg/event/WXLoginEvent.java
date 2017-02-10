package com.talenton.lsg.event;

/**
 * Created by ttt on 2016/4/7.
 */
public class WXLoginEvent {
    public boolean ret;
    public String error;
    public int authtype;
    public String openid;
    public String access_token;
    public String nickname;
    public String figureurl;

    public WXLoginEvent(boolean ret, String error, int authtype, String openid, String access_token, String nickname,String figureurl) {
        this.ret = ret;
        this.error = error;
        this.authtype = authtype;
        this.openid = openid;
        this.access_token = access_token;
        this.nickname = nickname;
        this.figureurl = figureurl;
    }
}
