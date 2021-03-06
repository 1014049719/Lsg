package com.talenton.lsg.server.bean.school.event;

import com.talenton.lsg.server.bean.school.ClassEvaluateData;

/**
 * @author zjh
 * @date 2016/5/10
 */
public class CreateEvaluteEvent {
    private ClassEvaluateData evaluateData;

    public CreateEvaluteEvent(ClassEvaluateData evaluateData) {
        this.evaluateData = evaluateData;
    }

    public ClassEvaluateData getEvaluateData() {
        return evaluateData;
    }

    public void setEvaluateData(ClassEvaluateData evaluateData) {
        this.evaluateData = evaluateData;
    }
}
