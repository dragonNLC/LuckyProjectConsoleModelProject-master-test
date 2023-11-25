package com.dragon.lucky.command17;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnceGenerateBean {

    private Map<String, PartitionBean> data7Place;
    private Map<String, PartitionBean> data6Place;
    private Map<String, PartitionBean> data5Place;
    private String title;
    private int size;

    public OnceGenerateBean() {
    }

    public Map<String, PartitionBean> getData7Place() {
        return data7Place;
    }

    public void setData7Place(Map<String, PartitionBean> data7Place) {
        this.data7Place = data7Place;
    }

    public Map<String, PartitionBean> getData6Place() {
        return data6Place;
    }

    public void setData6Place(Map<String, PartitionBean> data6Place) {
        this.data6Place = data6Place;
    }

    public Map<String, PartitionBean> getData5Place() {
        return data5Place;
    }

    public void setData5Place(Map<String, PartitionBean> data5Place) {
        this.data5Place = data5Place;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
