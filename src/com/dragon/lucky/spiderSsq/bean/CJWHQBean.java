package com.dragon.lucky.spiderSsq.bean;

import java.util.List;

public class CJWHQBean {

    public String title;
    public int[] data;
    public List<CJWHQSingleDataBean> previewData;
    public int hitCount;

    public CJWHQBean() {
    }

    public CJWHQBean(String title, int[] data, List<CJWHQSingleDataBean> previewData, int hitCount) {
        this.title = title;
        this.data = data;
        this.previewData = previewData;
        this.hitCount = hitCount;
    }

    public static class CJWHQSingleDataBean {
        public int data;
        public boolean isTrue;

        public CJWHQSingleDataBean() {
        }

        public CJWHQSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }
    }

}
