package com.dragon.lucky.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultBean2Temp {

    private String tag;

    private int id;

    public ResultBean2Temp() {
    }

    public ResultBean2Temp(String tag, int id) {
        this.tag = tag;
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


