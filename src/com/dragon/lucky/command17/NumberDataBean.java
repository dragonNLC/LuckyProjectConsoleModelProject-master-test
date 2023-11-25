package com.dragon.lucky.command17;


public class NumberDataBean {

    private byte[] data;
    private int hitCount;
    private byte[] dataHitIdx;

    private int headSize;
    private int centerSize;
    private int footSize;

    public NumberDataBean() {
    }

    public NumberDataBean(byte[] data) {
        this.data = data;
        this.dataHitIdx = new byte[data.length];
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.dataHitIdx = new byte[data.length];
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public byte[] getDataHitIdx() {
        return dataHitIdx;
    }

    public void setDataHitIdx(byte[] dataHitIdx) {
        this.dataHitIdx = dataHitIdx;
    }

    public int getHeadSize() {
        return headSize;
    }

    public void setHeadSize(int headSize) {
        this.headSize = headSize;
    }

    public int getCenterSize() {
        return centerSize;
    }

    public void setCenterSize(int centerSize) {
        this.centerSize = centerSize;
    }

    public int getFootSize() {
        return footSize;
    }

    public void setFootSize(int footSize) {
        this.footSize = footSize;
    }
}


