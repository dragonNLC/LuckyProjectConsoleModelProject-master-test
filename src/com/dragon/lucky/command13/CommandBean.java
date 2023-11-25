package com.dragon.lucky.command13;

public class CommandBean {

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件
    private String previewFilePath;//预加载计算文件，及原始数的结果数

    private int mergeCount;//多少位合并为一个标识头
    private int sizeCount;//条件组多少组分为一个线程处理
    private int arrayCount;//求交集组分割数量
    private byte[] checkData;
    private int checkDataCount;
    private int generateSize;
    private int generateFilterType;
    private String commandsPath;//批处理条件
    private String finalFilePath;
    private String finalOutputFilePath;
    private int minCount;
    private int maxCount;

    public CommandBean() {
        mergeCount = 4;
        sizeCount = 100;
        arrayCount = 2;
        generateFilterType = -1;
        checkDataCount = 5;

        minCount = 5;
        maxCount = -1;
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

    public String getCommandsPath() {
        return commandsPath;
    }

    public void setCommandsPath(String commandsPath) {
        this.commandsPath = commandsPath;
    }

    public String getFinalFilePath() {
        return finalFilePath;
    }

    public void setFinalFilePath(String finalFilePath) {
        this.finalFilePath = finalFilePath;
    }

    public String getFinalOutputFilePath() {
        return finalOutputFilePath;
    }

    public void setFinalOutputFilePath(String finalOutputFilePath) {
        this.finalOutputFilePath = finalOutputFilePath;
    }

    public int getCheckDataCount() {
        return checkDataCount;
    }

    public void setCheckDataCount(int checkDataCount) {
        this.checkDataCount = checkDataCount;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
