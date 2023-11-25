package com.dragon.lucky.spiderSsq.bean;

import java.util.List;

public class SubItemBean {

    public List<String> subNumbers;

    public SubItemBean() {
    }

    public SubItemBean(List<String> subNumbers) {
        this.subNumbers = subNumbers;
    }

    public List<String> getSubNumbers() {
        return subNumbers;
    }

    public void setSubNumbers(List<String> subNumbers) {
        this.subNumbers = subNumbers;
    }
}
