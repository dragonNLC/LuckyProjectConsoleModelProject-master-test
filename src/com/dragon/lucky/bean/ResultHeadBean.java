package com.dragon.lucky.bean;

import java.util.List;

public class ResultHeadBean extends ContentBean {

    private int size;

    public ResultHeadBean() {
        super(ContentBean.ITEM_TYPE_RESULT_HEAD_LAYOUT);
    }

    public ResultHeadBean(int size) {
        super(ContentBean.ITEM_TYPE_RESULT_HEAD_LAYOUT);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}


