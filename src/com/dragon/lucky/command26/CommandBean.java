package com.dragon.lucky.command26;

public class CommandBean {

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件
    private String commandsPath;//批处理条件
    private String commandsOutputPath;//批处理条件
    private int rowItem;

    private byte[] checkData;

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

    public String getCommandsPath() {
        return commandsPath;
    }

    public void setCommandsPath(String commandsPath) {
        this.commandsPath = commandsPath;
    }

    public String getCommandsOutputPath() {
        return commandsOutputPath;
    }

    public void setCommandsOutputPath(String commandsOutputPath) {
        this.commandsOutputPath = commandsOutputPath;
    }

    public byte[] getCheckData() {
        return checkData;
    }

    public void setCheckData(byte[] checkData) {
        this.checkData = checkData;
    }
}
