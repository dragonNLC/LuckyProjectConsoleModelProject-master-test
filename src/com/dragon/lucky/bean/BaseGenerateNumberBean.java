package com.dragon.lucky.bean;

import java.util.List;

public class BaseGenerateNumberBean {

    private List<Byte> baseNumbers;

    private int printNumber;

    private List<BaseControlNumberBean> baseControlNumbers;

    public BaseGenerateNumberBean() {
    }

    public BaseGenerateNumberBean(List<Byte> baseNumbers, int printNumber, List<BaseControlNumberBean> baseControlNumbers) {
        this.baseNumbers = baseNumbers;
        this.printNumber = printNumber;
        this.baseControlNumbers = baseControlNumbers;
    }

    public List<Byte> getBaseNumbers() {
        return baseNumbers;
    }

    public void setBaseNumbers(List<Byte> baseNumbers) {
        this.baseNumbers = baseNumbers;
    }

    public int getPrintNumber() {
        return printNumber;
    }

    public void setPrintNumber(int printNumber) {
        this.printNumber = printNumber;
    }

    public List<BaseControlNumberBean> getBaseControlNumbers() {
        return baseControlNumbers;
    }

    public void setBaseControlNumbers(List<BaseControlNumberBean> baseControlNumbers) {
        this.baseControlNumbers = baseControlNumbers;
    }
}
