package com.dragon.lucky.command6;

public class ExistsDataBean {

    private int idx;
    private byte[] data;

    public ExistsDataBean(int idx, byte[] data) {
        this.idx = idx;
        this.data = data;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
