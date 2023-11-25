package com.dragon.lucky.command8a;


import com.dragon.lucky.command8.GenerateBaseDataBean;

import java.util.ArrayList;
import java.util.List;

public class CommandBean {

    private List<String> inputPath;//输入原始文件
    private List<String> filterPath;//基础数对应的条件
    private String outputPath;//输出结果文件
    private String commandsPath;//批处理条件

    private int mergeCount;//多少位合并为一个标识头
    private int sizeCount;//条件组多少组分为一个线程处理
    private int arrayCount;//求交集组分割数量
    private byte[] checkData;
    private int generateSize;
    private int generateFilterType;

    private int divideThreadCount;//分成多少个线程降位

    private List<GenerateConditionBean> mGenerateConditions;

    private List<GenerateBaseDataBean> mData;

    public CommandBean() {
        inputPath = new ArrayList<>();
        filterPath = new ArrayList<>();

        mergeCount = 4;
        sizeCount = 100;
        arrayCount = 2;
        generateFilterType = -1;

        generateSize = 4;
        setCheckData(new byte[]{3, 13, 22, 29, 34});
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
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

    public List<GenerateConditionBean> getGenerateConditions() {
        return mGenerateConditions;
    }

    public void setGenerateConditions(List<GenerateConditionBean> mGenerateConditions) {
        this.mGenerateConditions = mGenerateConditions;
    }

    public int getDivideThreadCount() {
        return divideThreadCount;
    }

    public void setDivideThreadCount(int divideThreadCount) {
        this.divideThreadCount = divideThreadCount;
    }

    public List<String> getInputPath() {
        return inputPath;
    }

    public void setInputPath(List<String> inputPath) {
        this.inputPath = inputPath;
    }

    public List<GenerateBaseDataBean> getData() {
        return mData;
    }

    public void setData(List<GenerateBaseDataBean> data) {
        this.mData = data;
    }

    public List<String> getFilterPath() {
        return filterPath;
    }

    public void setFilterPath(List<String> filterPath) {
        this.filterPath = filterPath;
    }

    public String getCommandsPath() {
        return commandsPath;
    }

    public void setCommandsPath(String commandsPath) {
        this.commandsPath = commandsPath;
    }
}
