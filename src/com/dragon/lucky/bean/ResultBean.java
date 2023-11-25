package com.dragon.lucky.bean;

import com.dragon.lucky.utils.Log;

import java.util.Arrays;
import java.util.List;

public class ResultBean extends ContentBean {

    private byte[] data;
    private byte[] hitData;
    private String dataStr;

    private long id;

    private boolean isLast;

    private String mergeArr;

    private int collectCount;

    private int hitSize;

    @Deprecated
    public ResultBean() {
        super(ContentBean.ITEM_TYPE_RESULT_LAYOUT);
        data = new byte[0];
        dataStr = Arrays.toString(data);
    }

    @Deprecated
    public ResultBean(byte[] data, long id) {
        super(ContentBean.ITEM_TYPE_RESULT_LAYOUT);
        this.data = data;
        this.id = id;
        dataStr = Arrays.toString(data);
    }

    public ResultBean(byte[] data, long id, int mergeCount) {
        super(ContentBean.ITEM_TYPE_RESULT_LAYOUT);
        this.data = data;
        this.id = id;
        mergeArr = Arrays.toString(Arrays.copyOf(data, mergeCount));
        dataStr = Arrays.toString(data);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        dataStr = Arrays.toString(data);
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMergeArr() {
        return mergeArr;
    }

    public void setMergeArr(String mergeArr) {
        this.mergeArr = mergeArr;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }


    public int getHitSize() {
        return hitSize;
    }

    public void setHitSize(int hitSize) {
        this.hitSize = hitSize;
    }

    public byte[] getHitData() {
        return hitData;
    }

    public void setHitData(byte[] hitData) {
        this.hitData = hitData;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }

    @Override
    public int hashCode() {
        return dataStr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof ResultBean) {
            boolean result = ((ResultBean) obj).dataStr.equals(this.dataStr);
            if (result) {
                setCollectCount(getCollectCount() + 1);
                ((ResultBean) obj).setCollectCount(((ResultBean) obj).getCollectCount() + 1);
            }
            return result;
        }
        return super.equals(obj);
    }
}


