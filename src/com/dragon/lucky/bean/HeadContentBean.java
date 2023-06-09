package com.dragon.lucky.bean;

public class HeadContentBean extends ContentBean {

    private String control;
    private String number;

    public HeadContentBean() {
        super(ContentBean.ITEM_TYPE_INPUT_HEAD_LAYOUT);
    }

    public HeadContentBean(String control, String number) {
        super(ContentBean.ITEM_TYPE_INPUT_HEAD_LAYOUT);
        this.control = control;
        this.number = number;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
