package com.dragon.lucky.command7;

import java.util.ArrayList;
import java.util.List;

public class GenerateConditionBean {

    private List<Percent> percents;

    public GenerateConditionBean(int count, float... percentArr) {
        percents = new ArrayList<Percent>();
        for (int i = 0; i < percentArr.length; i += 2) {
            if (i + 1 >= percentArr.length) {
                break;
            }
            percents.add(new Percent(count, percentArr[i], percentArr[i + 1]));
        }
    }

    public List<Percent> getPercents() {
        return percents;
    }

    public void setPercents(List<Percent> percents) {
        this.percents = percents;
    }

    public static class Percent {

        private int count;
        private float headPercent;
        private float footPercent;

        public Percent() {
        }

        public Percent(int count, float headPercent, float footPercent) {
            this.count = count;
            this.headPercent = headPercent;
            this.footPercent = footPercent;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public float getHeadPercent() {
            return headPercent;
        }

        public void setHeadPercent(float headPercent) {
            this.headPercent = headPercent;
        }

        public float getFootPercent() {
            return footPercent;
        }

        public void setFootPercent(float footPercent) {
            this.footPercent = footPercent;
        }
    }

}
