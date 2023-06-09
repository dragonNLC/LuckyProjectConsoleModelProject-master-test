package com.dragon.lucky.bean;

public class ControlBean extends ContentBean {

    private String control;
    private String number;

    public ControlBean() {
        super(ContentBean.ITEM_TYPE_CONDITION_LAYOUT);
    }

    public ControlBean(String control, String number) {
        super(ContentBean.ITEM_TYPE_CONDITION_LAYOUT);
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
