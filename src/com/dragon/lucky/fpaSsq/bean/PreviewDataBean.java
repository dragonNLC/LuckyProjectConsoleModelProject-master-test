package com.dragon.lucky.fpaSsq.bean;

import java.util.Arrays;
import java.util.List;

public class PreviewDataBean {

    public String title;
    public int[] data;
    public List<PreviewSingleDataBean> previewData;
    public List<PreviewSingleDataBean> previewData2;

    public PreviewDataBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public List<PreviewSingleDataBean> getPreviewData() {
        return previewData;
    }

    public void setPreviewData(List<PreviewSingleDataBean> previewData) {
        this.previewData = previewData;
    }

    public List<PreviewSingleDataBean> getPreviewData2() {
        return previewData2;
    }

    public void setPreviewData2(List<PreviewSingleDataBean> previewData2) {
        this.previewData2 = previewData2;
    }

    public static class PreviewSingleDataBean {
        public int data;
        public boolean isTrue;

        public PreviewSingleDataBean() {
        }

        public PreviewSingleDataBean(int data, boolean isTrue) {
            this.data = data;
            this.isTrue = isTrue;
        }

        @Override
        public String toString() {
            return "PreviewSingleDataBean{" +
                    "data=" + data +
                    ", isTrue=" + isTrue +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PreviewDataBean{" +
                "title='" + title + '\'' +
                ", data=" + Arrays.toString(data) +
                ", previewData=" + previewData +
                ", previewData2=" + previewData2 +
                '}';
    }
}
