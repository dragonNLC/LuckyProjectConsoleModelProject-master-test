package com.dragon.lucky.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultBean2  {

    private List<Byte> data;

    private long id;

    private boolean isLast;

    private String mergeArr;

    public ResultBean2() {
        data = new ArrayList<>();
    }

    public ResultBean2(long id) {
        this.id = id;
        data = new ArrayList<>();
    }

    public ResultBean2(List<Byte> data, long id) {
        this.data = data;
        this.id = id;
    }

    public List<Byte> getData() {
        return data;
    }

    public void setData(List<Byte> data) {
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

}


