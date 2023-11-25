package com.dragon.lucky.command9;


import com.dragon.lucky.command8.GenerateBaseDataBean;

import java.util.ArrayList;
import java.util.List;

public class CommandBean {

    private List<String> filterPath;//基础数对应的条件
    private String outputPath;//输出结果文件
    private String previewFilePath;//预加载计算文件，及原始数的结果数
    private String commandsPath;//批处理条件

    private byte[] checkData;

    private List<GenerateBaseDataBean> mData;

    public CommandBean() {
        filterPath = new ArrayList<>();

        setCheckData(new byte[]{3, 13, 22, 29, 34});
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public byte[] getCheckData() {
        return checkData;
    }

    public void setCheckData(byte[] checkData) {
        this.checkData = checkData;
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

    public String getPreviewFilePath() {
        return previewFilePath;
    }

    public void setPreviewFilePath(String previewFilePath) {
        this.previewFilePath = previewFilePath;
    }
}
