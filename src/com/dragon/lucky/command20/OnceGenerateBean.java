package com.dragon.lucky.command20;


import com.dragon.lucky.bean.ResultBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnceGenerateBean {

    private List<ResultBean> data;
    private String title;
    private String path;
    private int z5;
    private Map<Byte, Integer> collect = new HashMap<>();
    private byte[] checkData;

    public OnceGenerateBean() {
    }

    public List<ResultBean> getData() {
        return data;
    }

    public void setData(List<ResultBean> data) {
        this.data = data;
    }

    public int getZ5() {
        return z5;
    }

    public void setZ5(int z5) {
        this.z5 = z5;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<Byte, Integer> getCollect() {
        return collect;
    }

    public void setCollect(Map<Byte, Integer> collect) {
        this.collect = collect;
    }

    public byte[] getCheckData() {
        return checkData;
    }

    public void setCheckData(byte[] checkData) {
        this.checkData = checkData;
    }
}
