package com.dragon.lucky.spider.bean;

import java.util.List;

public class HQSMBean {

    public String title;
    public int[] data;
    public List<HQSMSingleDataBean> previewData;
    public int hitCount;

    public HQSMBean() {
    }

    public HQSMBean(String title, int[] data, List<HQSMSingleDataBean> previewData, int hitCount) {
        this.title = title;
        this.data = data;
        this.previewData = previewData;
        this.hitCount = hitCount;
    }

    public static class HQSMSingleDataBean {
        public int data;
        public boolean isTrue;

        public HQSMSingleDataBean() {
        }

        public HQSMSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }
    }

}
