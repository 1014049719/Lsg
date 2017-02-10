package com.talenton.lsg.server.bean.school.event;

/**
 * @author zjh
 * @date 2016/5/5
 */
public class OpenFileEvent {
    private int type;
    private int position;

    public OpenFileEvent(int type, int position) {
        this.type = type;
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
