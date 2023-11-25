package com.dragon.lucky.command24;


import com.dragon.lucky.bean.ResultBean;

import java.util.List;

public class OnceGenerateBean {

    private List<ResultBean> data;
    private String title;
    private int z5;

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
}
