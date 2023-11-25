package com.dragon.lucky.command4;

import com.dragon.lucky.command4.GenerateConditionBean;

import java.util.ArrayList;
import java.util.List;

public class CommandBean {

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件
    private String previewFilePath;//预加载计算文件，及原始数的结果数

    private int mergeCount;//多少位合并为一个标识头
    private int sizeCount;//条件组多少组分为一个线程处理
    private int arrayCount;//求交集组分割数量
    private byte[] checkData;
    private int generateSize;
    private int generateFilterType;

    private float m1Percent;//第一轮的去头部百分比
    private float m1WPercent;//第一轮的去尾部百分比

    //////////////////////同上//////////////////////////////////
    private float m2Percent;
    private float m2WPercent;

    private float m3Percent;
    private float m3WPercent;

    private float m4Percent;
    private float m4WPercent;

    private List<GenerateConditionBean> mGenerateConditions;

    public CommandBean() {
        mergeCount = 4;
        sizeCount = 100;
        arrayCount = 2;
        generateFilterType = -1;

        generateSize = 5;

        m1Percent = 0.003f;
        m1WPercent = 0.963f;

        m2Percent = 0.0011f;
        m2WPercent = 0.938f;

        m3Percent = 0.002f;
        m3WPercent = 0.956f;

        m4Percent = 0.0f;
        m4WPercent = 1.0f;

        mGenerateConditions = new ArrayList<>();
        /*mGenerateConditions.add(new GenerateConditionBean(1, 0.26f, 0.42f, 0.58f, 0.71f, 0.81f, 0.93f));
        mGenerateConditions.add(new GenerateConditionBean(2, 0.12f, 0.21f, 0.84f, 1));
        mGenerateConditions.add(new GenerateConditionBean(3, 0.13f, 0.29f, 0.46f, 0.57f, 0.62f, 0.72f, 0.74f, 0.98f));
        mGenerateConditions.add(new GenerateConditionBean(4, 0.15f, 0.28f, 0.73f, 1));
        mGenerateConditions.add(new GenerateConditionBean(5, 0.37f, 0.49f, 0.67f, 0.99f));
        mGenerateConditions.add(new GenerateConditionBean(6, 0.45f, 0.62f, 0.79f, 1));
        mGenerateConditions.add(new GenerateConditionBean(7, 0.18f, 0.36f, 0.51f, 0.61f, 0.8f, 1));
        mGenerateConditions.add(new GenerateConditionBean(8, 0.73f, 0.88f, 0.89f, 1));*/

        setCheckData(new byte[]{6, 11, 13, 30, 32});
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getPreviewFilePath() {
        return previewFilePath;
    }

    public void setPreviewFilePath(String previewFilePath) {
        this.previewFilePath = previewFilePath;
    }

    public int getArrayCount() {
        return arrayCount;
    }

    public void setArrayCount(int arrayCount) {
        this.arrayCount = arrayCount;
    }

    public int getMergeCount() {
        return mergeCount;
    }

    public void setMergeCount(int mergeCount) {
        this.mergeCount = mergeCount;
    }

    public int getSizeCount() {
        return sizeCount;
    }

    public void setSizeCount(int sizeCount) {
        this.sizeCount = sizeCount;
    }

    public byte[] getCheckData() {
        return checkData;
    }

    public void setCheckData(byte[] checkData) {
        this.checkData = checkData;
    }

    public int getGenerateSize() {
        return generateSize;
    }

    public void setGenerateSize(int generateSize) {
        this.generateSize = generateSize;
    }

    public int getGenerateFilterType() {
        return generateFilterType;
    }

    public void setGenerateFilterType(int generateFilterType) {
        this.generateFilterType = generateFilterType;
    }

    public float getM1Percent() {
        return m1Percent;
    }

    public void setM1Percent(float m1Percent) {
        this.m1Percent = m1Percent;
    }

    public float getM1WPercent() {
        return m1WPercent;
    }

    public void setM1WPercent(float m1WPercent) {
        this.m1WPercent = m1WPercent;
    }

    public float getM2Percent() {
        return m2Percent;
    }

    public void setM2Percent(float m2Percent) {
        this.m2Percent = m2Percent;
    }

    public float getM2WPercent() {
        return m2WPercent;
    }

    public void setM2WPercent(float m2WPercent) {
        this.m2WPercent = m2WPercent;
    }

    public float getM3Percent() {
        return m3Percent;
    }

    public void setM3Percent(float m3Percent) {
        this.m3Percent = m3Percent;
    }

    public float getM3WPercent() {
        return m3WPercent;
    }

    public void setM3WPercent(float m3WPercent) {
        this.m3WPercent = m3WPercent;
    }

    public float getM4Percent() {
        return m4Percent;
    }

    public void setM4Percent(float m4Percent) {
        this.m4Percent = m4Percent;
    }

    public float getM4WPercent() {
        return m4WPercent;
    }

    public void setM4WPercent(float m4WPercent) {
        this.m4WPercent = m4WPercent;
    }

    public List<GenerateConditionBean> getmGenerateConditions() {
        return mGenerateConditions;
    }

    public void setmGenerateConditions(List<GenerateConditionBean> mGenerateConditions) {
        this.mGenerateConditions = mGenerateConditions;
    }
}
