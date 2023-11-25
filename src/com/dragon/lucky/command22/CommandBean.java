package com.dragon.lucky.command22;

public class CommandBean {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;
    public static final int TYPE_4 = 4;

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件
    private int fq;//分区数
    private int size;//处理多少位的数据

    private byte[] checkData;
    private String commandsPath;//批处理条件
    private String commandsOutputPath;//批处理条件

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

    public String getCommandsOutputPath() {
        return commandsOutputPath;
    }

    public void setCommandsOutputPath(String commandsOutputPath) {
        this.commandsOutputPath = commandsOutputPath;
    }

    public int getFq() {
        return fq;
    }

    public void setFq(int fq) {
        this.fq = fq;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
