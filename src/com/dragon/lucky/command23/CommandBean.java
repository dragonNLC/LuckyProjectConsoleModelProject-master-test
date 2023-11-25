package com.dragon.lucky.command23;

public class CommandBean {

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件
    private int rowItem;

    public CommandBean() {
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

    public int getRowItem() {
        return rowItem;
    }

    public void setRowItem(int rowItem) {
        this.rowItem = rowItem;
    }
}
