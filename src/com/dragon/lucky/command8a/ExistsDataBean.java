package com.dragon.lucky.command8a;

public class ExistsDataBean {

    private int idx;
    private byte[] data;
    private int collectionCount;

    public ExistsDataBean(int idx, byte[] data, int collectionCount) {
        this.idx = idx;
        this.data = data;
        this.collectionCount = collectionCount;
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

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }
}
