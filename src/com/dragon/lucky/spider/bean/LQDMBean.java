package com.dragon.lucky.spider.bean;

import java.util.List;

public class LQDMBean {

    public String title;
    public int[] data;
    public List<LQDMSingleDataBean> previewData;
    public int hitCount;

    public LQDMBean() {
    }

    public LQDMBean(String title, int[] data, List<LQDMSingleDataBean> previewData, int hitCount) {
        this.title = title;
        this.data = data;
        this.previewData = previewData;
        this.hitCount = hitCount;
    }

    public static class LQDMSingleDataBean {
        public int data;
        public boolean isTrue;

        public LQDMSingleDataBean() {
        }

        public LQDMSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }
    }

}
