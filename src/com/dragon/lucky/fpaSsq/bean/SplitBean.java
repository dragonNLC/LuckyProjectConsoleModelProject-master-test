package com.dragon.lucky.fpaSsq.bean;

import java.util.Arrays;

public class SplitBean {

    private int[] data;

    public int collectCount;

    private String dataStr;

    public SplitBean(int[] data) {
        this.data = data;
        this.dataStr = Arrays.toString(data);
    }

    public int[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SplitBean{" +
                "data=" + Arrays.toString(data) +
                ", collectCount=" + collectCount +
                ", dataStr='" + dataStr + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof SplitBean) {
            boolean result = ((SplitBean) obj).dataStr.equals(this.dataStr);
            if (result) {
                setCollectCount(getCollectCount() + 1);
                ((SplitBean) obj).setCollectCount(((SplitBean) obj).getCollectCount() + 1);
            }
            return result;
        }
        return super.equals(obj);
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    @Override
    public int hashCode() {
        return Arrays.toString(data).hashCode();
    }


}
