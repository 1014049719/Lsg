package com.talenton.lsg.server.bean.school;

public class AdvertiseBean {
    private int id;
    private String advPic; //广告图片地址
    private String advUrl; //广告跳转URL
    private int showTime; //轮播秒数
    private int advOrder; //显示顺序
    private int oMode; //打开方式，0本地打开；1-跳转到WEBVIEW,（如果appsource=360，这里填-1）
    private String closeImageUrl;

    public AdvertiseBean(String advPic) {
        this.advPic = advPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getAdvUrl() {
        return advUrl;
    }

    public void setAdvUrl(String advUrl) {
        this.advUrl = advUrl;
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }

    public int getAdvOrder() {
        return advOrder;
    }

    public void setAdvOrder(int advOrder) {
        this.advOrder = advOrder;
    }

    public int getoMode() {
        return oMode;
    }

    public void setoMode(int oMode) {
        this.oMode = oMode;
    }

    public String getCloseImageUrl() {
        return closeImageUrl;
    }

    public void setCloseImageUrl(String closeImageUrl) {
        this.closeImageUrl = closeImageUrl;
    }
}