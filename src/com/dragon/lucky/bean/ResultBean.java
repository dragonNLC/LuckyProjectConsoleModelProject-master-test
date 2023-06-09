package com.dragon.lucky.bean;

import com.dragon.lucky.utils.Log;

import java.util.Arrays;
import java.util.List;

public class ResultBean extends ContentBean {

    private byte[] data;

    private long id;

    private boolean isLast;

    private String mergeArr;

    @Deprecated
    public ResultBean() {
        super(ContentBean.ITEM_TYPE_RESULT_LAYOUT);
        data = new byte[0];
    }

    @Deprecated
    public ResultBean(byte[] data, long id) {
        super(ContentBean.ITEM_TYPE_RESULT_LAYOUT);
        this.data = data;
        this.id = id;
    }

    public ResultBean(byte[] data, long id, int mergeCount) {
        super(ContentBean.ITEM_TYPE_RESULT_LAYOUT);
        this.data = data;
        this.id = id;
        mergeArr = Arrays.toString(Arrays.copyOf(data, mergeCount));
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}


