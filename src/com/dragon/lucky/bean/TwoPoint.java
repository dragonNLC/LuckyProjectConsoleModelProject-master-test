package com.dragon.lucky.bean;

public class TwoPoint {

    public int a;
    public int b;

    public TwoPoint() {
    }

    public TwoPoint(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "TwoPoint{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
