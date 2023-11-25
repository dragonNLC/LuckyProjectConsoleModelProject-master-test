package com.dragon.lucky.command16;


public class NumberDataBean {

    private byte[] data;
    private int hitCount;
    private byte[] dataHitIdx;
    private byte[] deprecatedDataIdx;

    private byte[] fqData;

    private float z345Percent;

    public NumberDataBean() {
    }

    public NumberDataBean(byte[] data) {
        this.data = data;
        this.dataHitIdx = new byte[data.length];
        this.deprecatedDataIdx = new byte[data.length];
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.dataHitIdx = new byte[data.length];
        this.deprecatedDataIdx = new byte[data.length];
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

    public byte[] getDeprecatedDataIdx() {
        return deprecatedDataIdx;
    }

    public void setDeprecatedDataIdx(byte[] deprecatedDataIdx) {
        this.deprecatedDataIdx = deprecatedDataIdx;
    }

    public byte[] getFqData() {
        return fqData;
    }

    public void setFqData(byte[] fqData) {
        this.fqData = fqData;
    }

    public float getZ345Percent() {
        return z345Percent;
    }

    public void setZ345Percent(float z345Percent) {
        this.z345Percent = z345Percent;
    }
}


