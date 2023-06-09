package com.dragon.lucky.command2;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultBean2;

import java.util.ArrayList;
import java.util.List;

public class GenerateResultBean {

    private List<ResultBean2> data;

    public GenerateResultBean() {
        data = new ArrayList<>();
    }

    public GenerateResultBean(List<ResultBean2> data) {
        this.data = data;
    }

    public List<ResultBean2> getData() {
        return data;
    }

    public void setData(List<ResultBean2> data) {
        this.data = data;
    }

}
