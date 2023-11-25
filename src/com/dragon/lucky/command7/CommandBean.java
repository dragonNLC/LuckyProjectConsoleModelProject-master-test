package com.dragon.lucky.command7;


import java.util.ArrayList;
import java.util.List;

public class CommandBean {

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件
    private String previewFilePath;//预加载计算文件，及原始数的结果数
    private String commandsPath;//批处理条件

    private int mergeCount;//多少位合并为一个标识头
    private int sizeCount;//条件组多少组分为一个线程处理
    private int arrayCount;//求交集组分割数量
    private byte[] checkData;
    private int generateSize;
    private int generateFilterType;

    private float m1Percent;//第一轮的去头部百分比
    private float m1WPercent;//第一轮的去尾部百分比

    private int divideThreadCount;//分成多少个线程降位

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

        generateSize = 4;

        m1Percent = 0f;
        m1WPercent = 1f;

        m2Percent = 0f;
        m2WPercent = 0f;

        m3Percent = 0f;
        m3WPercent = 1f;

        m4Percent = 0.0f;
        m4WPercent = 1.0f;

        divideThreadCount = 24;

        mGenerateConditions = new ArrayList<>();
        mGenerateConditions.add(new GenerateConditionBean(30, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(29, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(28, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(27, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(26, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(25, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(24, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(23, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(22, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(21, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(20, 0.0f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(19, 0.0f, 0.2f, 0.8f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(18, 0.0f, 0.2f, 0.8f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(17, 0.0f, 0.2f, 0.8f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(16, 0.0f, 0.2f, 0.8f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(15, 0.0f, 0.2f, 0.8f, 1.0f));
        /*mGenerateConditions.add(new GenerateConditionBean(14, 0.2f, 0.299f, 0.4f, 0.599f));
        mGenerateConditions.add(new GenerateConditionBean(12, 0.3f, 0.399f));
        mGenerateConditions.add(new GenerateConditionBean(11, 0.3f, 0.399f, 0.8f, 0.899f));
        mGenerateConditions.add(new GenerateConditionBean(8, 0.3f, 0.399f));
        mGenerateConditions.add(new GenerateConditionBean(7, 0.9f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(4, 0.9f, 1.0f));
        mGenerateConditions.add(new GenerateConditionBean(3, 0.8f, 0.899f));
        mGenerateConditions.add(new GenerateConditionBean(2, 0.4f, 0.499f, 0.8f, 0.899f));
        mGenerateConditions.add(new GenerateConditionBean(1, 0.3f, 0.499f, 0.6f, 0.699f));*/

        setCheckData(new byte[]{5, 6, 9, 11, 14});
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

    public int getDivideThreadCount() {
        return divideThreadCount;
    }

    public void setDivideThreadCount(int divideThreadCount) {
        this.divideThreadCount = divideThreadCount;
    }

    public String getCommandsPath() {
        return commandsPath;
    }

    public void setCommandsPath(String commandsPath) {
        this.commandsPath = commandsPath;
    }

}
