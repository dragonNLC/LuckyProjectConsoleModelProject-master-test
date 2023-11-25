package com.dragon.lucky.command18;


import java.util.ArrayList;
import java.util.List;

public class NumberBean {

    private byte[] data;

    private List<NumberDataBean> generateData;

    private int generateSize;

    public NumberBean() {
    }

    public NumberBean(byte[] data, int generateSize) {
        this.data = data;
        this.generateSize = generateSize;
    }

    public NumberBean(List<NumberDataBean> generateData) {
        this.generateData = generateData;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
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


