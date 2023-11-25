package com.dragon.lucky.command14;

import com.dragon.lucky.bean.ContentBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberDataBean {

    private List<Integer> data;

    public NumberDataBean() {
    }

    public NumberDataBean(int[] bData) {
        data = new ArrayList<>();
        for (int i = 0; i < bData.length; i++) {
            data.add(bData[i]);
        }
    }

    public NumberDataBean(List<Integer> data) {
        this.data = data;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}


