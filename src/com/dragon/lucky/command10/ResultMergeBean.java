package com.dragon.lucky.command10;

import com.dragon.lucky.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

public class ResultMergeBean {

    public List<Integer> mergeResult = new ArrayList<>();
    public String mergeHead;

    public ResultMergeBean(String mergeHead) {
        this.mergeHead = mergeHead;
    }

}
