package com.dragon.lucky.command2;

import com.dragon.lucky.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

public class DataBean {

    private List<ResultBean> mDate;

    public DataBean() {
        mDate = new ArrayList<>();
    }

    public DataBean(List<ResultBean> date) {
        mDate = new ArrayList<>();
        mDate.addAll(date);
    }

    public List<ResultBean> getDate() {
        return mDate;
    }

}
