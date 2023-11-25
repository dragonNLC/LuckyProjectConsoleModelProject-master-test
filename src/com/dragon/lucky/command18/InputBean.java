package com.dragon.lucky.command18;

public class InputBean {

    private byte[] fqData;

    public InputBean() {
    }

    public InputBean(byte[] fqData) {
        this.fqData = fqData;
    }

    public byte[] getFqData() {
        return fqData;
    }

    public void setFqData(byte[] fqData) {
        this.fqData = fqData;
    }
}
