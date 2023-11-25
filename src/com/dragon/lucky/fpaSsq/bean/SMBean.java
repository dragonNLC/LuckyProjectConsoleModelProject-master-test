package com.dragon.lucky.fpaSsq.bean;

import java.util.List;

public class SMBean {

    public List<SMItemBean> dataItem;
    public List<SMTypeBean> typesItem;

    public SMBean() {
    }

    public SMBean(List<SMItemBean> dataItem, List<SMTypeBean> typesItem) {
        this.dataItem = dataItem;
        this.typesItem = typesItem;
    }

    public static class SMItemBean {
        public String title;
        public int[] redData;
        public int[] blueData;

        public List<SMDataBean> itemData;

        public int hitCount;

        public SMItemBean() {
        }

        public SMItemBean(String title, int[] redData, int[] blueData, List<SMDataBean> itemData, int hitCount) {
            this.title = title;
            this.redData = redData;
            this.blueData = blueData;
            this.itemData = itemData;
            this.hitCount = hitCount;
        }
    }

    public static class SMDataBean {
        public int[] data;
        public int[] hitData;

        public SMDataBean() {
        }

        public SMDataBean(int[] data, int[] hitData) {
            this.data = data;
            this.hitData = hitData;
        }
    }

    public static class SMTypeBean {
        public float[] data;

        public SMTypeBean() {
        }

        public SMTypeBean(float[] data) {
            this.data = data;
        }
    }

}
