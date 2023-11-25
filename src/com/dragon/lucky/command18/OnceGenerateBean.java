package com.dragon.lucky.command18;

import java.util.List;

public class OnceGenerateBean {

    private List<GenerateBean> data;
    private String title;

    public OnceGenerateBean() {
    }

    public OnceGenerateBean(List<GenerateBean> data, String title) {
        this.data = data;
        this.title = title;
    }

    public List<GenerateBean> getData() {
        return data;
    }

    public void setData(List<GenerateBean> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
