package com.dragon.lucky.bean;

import com.dragon.lucky.command2.GenerateResultBean;

import java.util.List;

public class BaseControlNumberBean {

    private List<Byte> baseNumber;
    private List<Integer> printNumber;

    private List<GenerateResultBean> generateResultBeans;

    public BaseControlNumberBean() {
    }

    public BaseControlNumberBean(List<Byte> baseNumber, List<Integer> printNumber) {
        this.baseNumber = baseNumber;
        this.printNumber = printNumber;
    }

    public BaseControlNumberBean(List<Byte> baseNumber, List<Integer> printNumber, List<GenerateResultBean> generateResultBeans) {
        this.baseNumber = baseNumber;
        this.printNumber = printNumber;
        this.generateResultBeans = generateResultBeans;
    }

    public List<Byte> getBaseNumber() {
        return baseNumber;
    }

    public void setBaseNumber(List<Byte> baseNumber) {
        this.baseNumber = baseNumber;
    }

    public List<Integer> getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(List<Integer> printNumber) {
        this.printNumber = printNumber;
    }

    public List<GenerateResultBean> getGenerateResultBeans() {
        return generateResultBeans;
    }

    public void setGenerateResultBeans(List<GenerateResultBean> generateResultBeans) {
        this.generateResultBeans = generateResultBeans;
    }
}
