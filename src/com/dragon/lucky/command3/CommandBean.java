package com.dragon.lucky.command3;

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

    public CommandBean() {
        mergeCount = 4;
        sizeCount = 60;
        arrayCount = 2;
        generateFilterType = -1;
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
}
