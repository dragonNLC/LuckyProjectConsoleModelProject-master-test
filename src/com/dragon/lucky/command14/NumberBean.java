package com.dragon.lucky.command14;

import com.dragon.lucky.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

public class NumberBean {

    private List<Integer> data;

    private List<NumberDataBean> generateData;

    private int generateSize;

    public NumberBean() {
        data = new ArrayList<>();
    }

    public NumberBean(List<Integer> data, int generateSize) {
        this.data = data;
        this.generateSize = generateSize;
    }

    public NumberBean(List<NumberDataBean> generateData) {
        this.generateData = generateData;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public int getGenerateSize() {
        return generateSize;
    }

    public void setGenerateSize(int generateSize) {
        this.generateSize = generateSize;
    }

    public List<NumberDataBean> getGenerateData() {
        return generateData;
    }

    public void setGenerateData(List<NumberDataBean> generateData) {
        this.generateData = generateData;
    }
}


