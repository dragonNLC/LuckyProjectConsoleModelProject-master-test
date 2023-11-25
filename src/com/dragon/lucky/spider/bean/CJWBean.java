package com.dragon.lucky.spider.bean;

import java.util.List;

public class CJWBean {

    public String title;
    public int[] data;
    public List<CJWSingleDataBean> previewData;
    public int hitCount;

    public CJWBean() {
    }

    public CJWBean(String title, int[] data, List<CJWSingleDataBean> previewData, int hitCount) {
        this.title = title;
        this.data = data;
        this.previewData = previewData;
        this.hitCount = hitCount;
    }

    public static class CJWSingleDataBean {
        public int data;
        public boolean isTrue;

        public CJWSingleDataBean() {
        }

        public CJWSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }
    }

}
