package com.dragon.lucky.bean;

import java.util.ArrayList;
import java.util.List;

public class ResultMergeBean {

    public List<ResultBean> mergeResult = new ArrayList<>();
    public String mergeHead;

    public ResultMergeBean(String mergeHead) {
        this.mergeHead = mergeHead;
    }

}
