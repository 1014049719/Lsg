package com.talenton.lsg.server.bean.user.event;

/**
 * @author zjh
 * @date 2016/5/30
 */
public class ModifyUserNameEvent {
    public ModifyUserNameEvent(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
