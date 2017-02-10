package com.talenton.lsg.event;

/**
 * @author zjh
 * @date 2016/6/1
 */
public class UpdateAvatarEvent {
    private String imgPath;

    public UpdateAvatarEvent(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
