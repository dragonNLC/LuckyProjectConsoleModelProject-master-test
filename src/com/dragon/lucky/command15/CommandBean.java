package com.dragon.lucky.command15;

public class CommandBean {

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件

    private byte[] checkData;
    private int checkDataCount;
    private int firstFilterDataLen;
    private int secondFilterDataLen;
    private String commandsPath;//批处理条件

    public CommandBean() {
        checkDataCount = 5;
        firstFilterDataLen = -1;
        secondFilterDataLen = -1;
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

    public byte[] getCheckData() {
        return checkData;
    }

    public void setCheckData(byte[] checkData) {
        this.checkData = checkData;
    }

    public String getCommandsPath() {
        return commandsPath;
    }

    public void setCommandsPath(String commandsPath) {
        this.commandsPath = commandsPath;
    }

    public int getCheckDataCount() {
        return checkDataCount;
    }

    public void setCheckDataCount(int checkDataCount) {
        this.checkDataCount = checkDataCount;
    }

    public int getFirstFilterDataLen() {
        return firstFilterDataLen;
    }

    public void setFirstFilterDataLen(int firstFilterDataLen) {
        this.firstFilterDataLen = firstFilterDataLen;
    }

    public int getSecondFilterDataLen() {
        return secondFilterDataLen;
    }

    public void setSecondFilterDataLen(int secondFilterDataLen) {
        this.secondFilterDataLen = secondFilterDataLen;
    }
}
