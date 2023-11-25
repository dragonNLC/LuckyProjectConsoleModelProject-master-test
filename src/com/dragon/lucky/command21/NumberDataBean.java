package com.dragon.lucky.command21;


public class NumberDataBean {

    private byte[] data;
    private int hitCount;
    private byte[] dataHitIdx;

    private byte[] fqData;

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

    public byte[] getFqData() {
        return fqData;
    }

    public void setFqData(byte[] fqData) {
        this.fqData = fqData;
    }
}


