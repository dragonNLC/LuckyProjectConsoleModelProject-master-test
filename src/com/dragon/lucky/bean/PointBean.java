package com.dragon.lucky.bean;

public class PointBean {

    public static final int POINT_NONE = 0;
    public static final int POINT_SPE = 1;


    private int number;
    private int type;

    public PointBean() {
    }

    public PointBean(int number, int type) {
        this.number = number;
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
