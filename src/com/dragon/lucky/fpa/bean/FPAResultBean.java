package com.dragon.lucky.fpa.bean;

import com.dragon.lucky.bean.TwoPoint;

import java.util.ArrayList;
import java.util.List;

public class FPAResultBean {

    public List<TwoPoint> redResult = new ArrayList<>();
    public List<TwoPoint> yellowResult = new ArrayList<>();
    public List<TwoPoint> oriResult = new ArrayList<>();

    public FPAResultBean() {
    }

    public FPAResultBean(List<TwoPoint> redResult, List<TwoPoint> yellowResult, List<TwoPoint> oriResult) {
        this.redResult = redResult;
        this.yellowResult = yellowResult;
        this.oriResult = oriResult;
    }



}
