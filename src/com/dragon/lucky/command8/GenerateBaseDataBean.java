package com.dragon.lucky.command8;

import java.util.ArrayList;
import java.util.List;

public class GenerateBaseDataBean {

    private List<BaseData> mData;
    private List<FilterData> mFilterData;
    private String mPath;

    public GenerateBaseDataBean() {
        mFilterData = new ArrayList<>();
    }

    public GenerateBaseDataBean(List<BaseData> mData) {
        this.mData = mData;
    }

    public List<BaseData> getData() {
        return mData;
    }

    public void setData(List<BaseData> data) {
        this.mData = data;
    }

    public List<FilterData> getFilterData() {
        return mFilterData;
    }

    public void setFilterData(List<FilterData> filterData) {
        this.mFilterData = filterData;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public static class BaseData {

        private int id;
        private byte[] data;
        private int count;
        private List<AppendData> appendData;
        private boolean isTarget;

        public BaseData() {
        }

        public BaseData(byte[] data, int count, int id) {
            this.data = data;
            this.count = count;
            this.id = id;
            this.appendData = new ArrayList<>();
        }

        public BaseData(List<Byte> data, int count) {
            this.data = new byte[data.size()];
            for (int i = 0; i < data.size(); i++) {
                this.data[i] = data.get(i);
            }
            this.count = count;
            this.appendData = new ArrayList<>();
        }

        public boolean isTarget() {
            return isTarget;
        }

        public void setTarget(boolean target) {
            isTarget = target;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<AppendData> getAppendData() {
            return appendData;
        }

        public void setAppendData(List<AppendData> appendData) {
            this.appendData = appendData;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class AppendData {

        private List<Byte> byteData;
        private int appendCount;

        public AppendData() {
        }

        public AppendData(List<Byte> byteData) {
            this.byteData = byteData;
        }

        public AppendData(byte[] byteData) {
            this.byteData = new ArrayList<>();
            for (int i = 0; i < byteData.length; i++) {
                this.byteData.add(byteData[i]);
            }
            appendCount++;
        }

        public AppendData(byte[] byteData, byte b) {
            this.byteData = new ArrayList<>();
            for (int i = 0; i < byteData.length; i++) {
                this.byteData.add(byteData[i]);
            }
            this.byteData.add(b);
            appendCount += 2;
        }

        public int getAppendCount() {
            return appendCount;
        }

        public void setAppendCount(int appendCount) {
            this.appendCount = appendCount;
        }

        public List<Byte> getByteData() {
            return byteData;
        }

        public void setByteData(List<Byte> byteData) {
            this.byteData = byteData;
        }
    }

    public static class FilterData {
        private int start;
        private int end;
        private List<BaseData> baseData;

        public FilterData() {
            this.baseData = new ArrayList<>();
        }

        public FilterData(int start, int end) {
            this.start = start;
            this.end = end;
            this.baseData = new ArrayList<>();
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public List<BaseData> getBaseData() {
            return baseData;
        }

        public void setBaseData(List<BaseData> baseData) {
            this.baseData = baseData;
        }
    }

}
