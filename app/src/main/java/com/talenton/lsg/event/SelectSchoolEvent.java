package com.talenton.lsg.event;

import com.talenton.lsg.base.server.bean.SchoolData;

/**
 * @author zjh
 * @date 2016/6/1
 */
public class SelectSchoolEvent {
    private SchoolData schoolData;

    public SelectSchoolEvent(SchoolData schoolData) {
        this.schoolData = schoolData;
    }

    public SchoolData getSchoolData() {
        return schoolData;
    }

    public void setSchoolData(SchoolData schoolData) {
        this.schoolData = schoolData;
    }
}
