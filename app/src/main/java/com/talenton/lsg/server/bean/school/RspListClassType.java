package com.talenton.lsg.server.bean.school;

import java.util.List;

/**
 * @author zjh
 * @date 2016/4/19
 */
public class RspListClassType{
    private List<ClassificationData> list;
    private List<ClassFilterAge> agelist;

    public List<ClassificationData> getList() {
        return list;
    }

    public void setList(List<ClassificationData> list) {
        this.list = list;
    }

    public List<ClassFilterAge> getAgelist() {
        return agelist;
    }

    public void setAgelist(List<ClassFilterAge> agelist) {
        this.agelist = agelist;
    }
}
