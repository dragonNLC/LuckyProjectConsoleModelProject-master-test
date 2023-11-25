package com.dragon.lucky.fpaSsq.bean;

import java.util.List;

public class DMBean {

    public List<DMItemBean> dataItem;
    public List<DMTypeBean> typesItem;

    public DMBean() {
    }

    public DMBean(List<DMItemBean> dataItem, List<DMTypeBean> typesItem) {
        this.dataItem = dataItem;
        this.typesItem = typesItem;
    }

    public static class DMItemBean {
        public String title;
        public int[] redData;
        public int[] blueData;

        public List<DMDataBean> itemData;

        public int hitCount;

        public DMItemBean() {
        }

        public DMItemBean(String title, int[] redData, int[] blueData, List<DMDataBean> itemData, int hitCount) {
            this.title = title;
            this.redData = redData;
            this.blueData = blueData;
            this.itemData = itemData;
            this.hitCount = hitCount;
        }
    }

    public static class DMDataBean {
        public int data;
        public int hitData;

        public DMDataBean() {
        }

        public DMDataBean(int data, int hitData) {
            this.data = data;
            this.hitData = hitData;
        }

        @Override
        public String toString() {
            return "DMDataBean{" +
                    "data=" + data +
                    ", hitData=" + hitData +
                    '}';
        }
    }

    public static class DMTypeBean {
        public float[] data;

        public DMTypeBean() {
        }

        public DMTypeBean(float[] data) {
            this.data = data;
        }
    }

}
