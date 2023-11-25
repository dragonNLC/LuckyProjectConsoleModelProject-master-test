package com.dragon.lucky.command25;

import com.dragon.lucky.bean.ResultBean;

import java.util.List;

public class GenerateBean {

    private List<NumberDataBean> data;
    private int generateSize;

    public GenerateBean() {
    }

    public GenerateBean(List<NumberDataBean> data) {
        this.data = data;
    }

    public List<NumberDataBean> getData() {
        return data;
    }

    public void setData(List<NumberDataBean> data) {
        this.data = data;
    }

    public int getGenerateSize() {
        return generateSize;
    }

    public void setGenerateSize(int generateSize) {
        this.generateSize = generateSize;
    }
}
