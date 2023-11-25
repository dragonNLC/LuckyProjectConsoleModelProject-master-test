package com.dragon.lucky.fpaSsq.bean;

public class PrintDataBean {

    private String title;
    private int[] redBalls;
    private int[] blueBalls;

    public PrintDataBean() {
    }

    public PrintDataBean(String title, int[] redBalls, int[] blueBalls) {
        this.title = title;
        this.redBalls = redBalls;
        this.blueBalls = blueBalls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getRedBalls() {
        return redBalls;
    }

    public void setRedBalls(int[] redBalls) {
        this.redBalls = redBalls;
    }

    public int[] getBlueBalls() {
        return blueBalls;
    }

    public void setBlueBalls(int[] blueBalls) {
        this.blueBalls = blueBalls;
    }
}
