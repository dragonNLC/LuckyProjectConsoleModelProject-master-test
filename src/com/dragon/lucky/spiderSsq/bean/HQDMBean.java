package com.dragon.lucky.spiderSsq.bean;

import java.util.List;

public class HQDMBean {

    public String title;
    public int[] data;
    public List<HQDMSingleDataBean> previewData;
    public int hitCount;

    public HQDMBean() {
    }

    public HQDMBean(String title, int[] data, List<HQDMSingleDataBean> previewData, int hitCount) {
        this.title = title;
        this.data = data;
        this.previewData = previewData;
        this.hitCount = hitCount;
    }

    public static class HQDMSingleDataBean {
        public int data;
        public boolean isTrue;

        public HQDMSingleDataBean() {
        }

        public HQDMSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }
    }

}
