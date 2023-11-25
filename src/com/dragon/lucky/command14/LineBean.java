package com.dragon.lucky.command14;

import java.util.List;

public class LineBean {

    private List<NumberBean> data;

    public LineBean() {
    }

    public LineBean(List<NumberBean> data) {
        this.data = data;
    }

    public List<NumberBean> getData() {
        return data;
    }

    public void setData(List<NumberBean> data) {
        this.data = data;
    }
}
