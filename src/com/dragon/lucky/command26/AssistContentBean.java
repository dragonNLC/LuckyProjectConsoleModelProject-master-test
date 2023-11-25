package com.dragon.lucky.command26;

public class AssistContentBean {

    private String tag;
    private int count;
    private boolean checkState;

    public AssistContentBean() {
    }

    public AssistContentBean(String tag, int count, boolean checkState) {
        this.tag = tag;
        this.count = count;
        this.checkState = checkState;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCheckState() {
        return checkState;
    }

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }
}
