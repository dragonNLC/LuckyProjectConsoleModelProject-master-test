package com.dragon.lucky.command25;

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
    private int deleteSize;
    private int deleteResidueSize;

    private byte[] checkData;
    private byte[] referencekData;
    private boolean referenceClose;
    private String commandsPath;//批处理条件
    private String commandsOutputPath;//批处理条件

    public CommandBean() {
        generateReplySize = 0;
        lines = 7;
        replaySize = 7;
        type = TYPE_1;
        deleteResidueSize = 3;
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

    public byte[] getReferencekData() {
        return referencekData;
    }

    public void setReferencekData(byte[] referencekData) {
        this.referencekData = referencekData;
    }

    public boolean isReferenceClose() {
        return referenceClose;
    }

    public void setReferenceClose(boolean referenceClose) {
        this.referenceClose = referenceClose;
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

    public int getDeleteSize() {
        return deleteSize;
    }

    public void setDeleteSize(int deleteSize) {
        this.deleteSize = deleteSize;
    }

    public int getDeleteResidueSize() {
        return deleteResidueSize;
    }

    public void setDeleteResidueSize(int deleteResidueSize) {
        this.deleteResidueSize = deleteResidueSize;
    }
}
