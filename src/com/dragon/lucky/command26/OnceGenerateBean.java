package com.dragon.lucky.command26;

import java.util.List;

public class OnceGenerateBean {

    private List<AssistContentBean> data;
    private String title;

    public OnceGenerateBean() {
    }

    public OnceGenerateBean(List<AssistContentBean> data, String title) {
        this.data = data;
        this.title = title;
    }

    public List<AssistContentBean> getData() {
        return data;
    }

    public void setData(List<AssistContentBean> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
