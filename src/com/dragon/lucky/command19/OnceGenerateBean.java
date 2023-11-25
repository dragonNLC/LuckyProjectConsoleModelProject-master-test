package com.dragon.lucky.command19;


import com.dragon.lucky.bean.ResultBean;

import java.util.List;

public class OnceGenerateBean {

    private List<ResultBean> data;
    private String title;
    private int z1;
    private int z2;
    private List<ResultBean> data2;
    private int z21;
    private int z22;

    public OnceGenerateBean() {
    }

    public List<ResultBean> getData() {
        return data;
    }

    public void setData(List<ResultBean> data) {
        this.data = data;
    }

    public int getZ1() {
        return z1;
    }

    public void setZ1(int z1) {
        this.z1 = z1;
    }

    public int getZ2() {
        return z2;
    }

    public void setZ2(int z2) {
        this.z2 = z2;
    }

    public List<ResultBean> getData2() {
        return data2;
    }

    public void setData2(List<ResultBean> data2) {
        this.data2 = data2;
    }

    public int getZ21() {
        return z21;
    }

    public void setZ21(int z21) {
        this.z21 = z21;
    }

    public int getZ22() {
        return z22;
    }

    public void setZ22(int z22) {
        this.z22 = z22;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
