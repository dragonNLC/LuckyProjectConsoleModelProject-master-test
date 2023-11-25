package com.dragon.lucky.spiderSsq.bean;

public class FrequencyBean {

    private int people;//多少人预测了这个号码
    private int count;//这种数组-人的组合有多少组
    private int z;//命中了几个

    public FrequencyBean(int people) {
        this.people = people;
    }

    public int getPeople() {
        return people;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
