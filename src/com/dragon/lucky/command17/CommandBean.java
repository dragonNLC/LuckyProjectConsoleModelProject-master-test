package com.dragon.lucky.command17;

public class CommandBean {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;
    public static final int TYPE_4 = 4;

    private String inputPath;//输入原始文件
    private String outputPath;//输出结果文件
    private int generateReplySize;//生成的数组里面允许几个数相同
    private int lines;//每次生成多少行
    private int replaySize;//一条命令生成多少次
    private int type;//一条命令生成多少次

    private byte[] checkData;
    private String commandsPath;//批处理条件
    private String commandsOutputPath;//批处理条件

    public CommandBean() {
        generateReplySize = 0;
        lines = 5;
        replaySize = 5;
        type = TYPE_1;
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

    public int getGenerateReplySize() {
        return generateReplySize;
    }

    public void setGenerateReplySize(int generateReplySize) {
        this.generateReplySize = generateReplySize;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getReplaySize() {
        return replaySize;
    }

    public void setReplaySize(int replaySize) {
        this.replaySize = replaySize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
