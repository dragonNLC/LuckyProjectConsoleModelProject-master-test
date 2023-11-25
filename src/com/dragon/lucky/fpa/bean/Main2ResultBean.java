package com.dragon.lucky.fpa.bean;

import com.dragon.lucky.bean.TwoPoint;

import java.util.ArrayList;
import java.util.List;

public class Main2ResultBean {

    public List<TwoPoint> redNumber = new ArrayList<>();
    public List<TwoPoint> blueNumber = new ArrayList<>();

    public List<TwoPoint> getRedNumber() {
        return redNumber;
    }

    public void setRedNumber(List<TwoPoint> redNumber) {
        this.redNumber = redNumber;
    }

    public List<TwoPoint> getBlueNumber() {
        return blueNumber;
    }

    public void setBlueNumber(List<TwoPoint> blueNumber) {
        this.blueNumber = blueNumber;
    }

    @Override
    public String toString() {
        return "Main2ResultBean{" +
                "redNumber=" + redNumber +
                ", blueNumber=" + blueNumber +
                '}';
    }
}
