package com.dragon.lucky.spider.bean;

import java.util.List;

public class HQSSMBean {

    public String title;
    public int[] data;
    public List<HQSSMSingleDataBean> previewData;
    public int hitCount;

    public HQSSMBean() {
    }

    public HQSSMBean(String title, int[] data, List<HQSSMSingleDataBean> previewData, int hitCount) {
        this.title = title;
        this.data = data;
        this.previewData = previewData;
        this.hitCount = hitCount;
    }

    public static class HQSSMSingleDataBean {
        public int data;
        public boolean isTrue;

        public HQSSMSingleDataBean() {
        }

        public HQSSMSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }
    }

}
