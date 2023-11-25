package com.dragon.lucky.spiderSsq.bean;

import java.util.HashMap;
import java.util.Map;

public class NumberBean {

    public int number;
    public Map<Integer, FrequencyBean> frequencies;

    public NumberBean(int number) {
        this.number = number;
        this.frequencies = new HashMap<>();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Map<Integer, FrequencyBean> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(Map<Integer, FrequencyBean> frequencies) {
        this.frequencies = frequencies;
    }

}
