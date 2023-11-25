package com.dragon.lucky.spiderSsq.bean;

import java.util.List;

public class LQSMBean {

    public String title;
    public int[] data;
    public List<LQSMSingleDataBean> previewData;
    public int hitCount;

    public LQSMBean() {
    }

    public LQSMBean(String title, int[] data, List<LQSMSingleDataBean> previewData, int hitCount) {
        this.title = title;
        this.data = data;
        this.previewData = previewData;
        this.hitCount = hitCount;
    }

    public static class LQSMSingleDataBean {
        public int data;
        public boolean isTrue;

        public LQSMSingleDataBean() {
        }

        public LQSMSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }
    }

}
