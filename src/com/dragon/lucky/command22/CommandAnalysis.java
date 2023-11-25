package com.dragon.lucky.command22;

import com.dragon.lucky.utils.Log;

import java.util.Scanner;

public class CommandAnalysis {

    //需要解析的有
    /****
     * 1、输入文件
     * 2、输出文件
     * 3、预加载数据文件
     * 4、
     */
    private CommandBean command;

    private static CommandAnalysis sInstance;

    public static CommandAnalysis getInstance() {
        if (sInstance == null) {
            sInstance = new CommandAnalysis();
        }
        return sInstance;
    }

    private CommandAnalysis() {

    }

    //根据输入内容获取实际指令
    public void analysisCommand(String[] args) {
        if (args != null && args.length > 0) {
            command = new CommandBean();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-m"://输入预加载文件
                        command.setCommandsPath(args[++i]);
                        break;
                    case "-mo"://输入预加载文件
                        command.setCommandsOutputPath(args[++i]);
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-m(多行命令输入)；\n-mo(多行命令输出文件)；");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                String commands = scanner.nextLine();
                String[] commandSplit = commands.split(" ");
                analysisCommand(commandSplit);
                scanner.close();
            }
        }
    }

    //根据输入内容获取实际指令
    public void analysisCommand(String[] args, CommandBean commandBean) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-m"://输入预加载文件
                        commandBean.setCommandsPath(args[++i]);
                        break;
                    case "-mo"://输入预加载文件
                        commandBean.setCommandsOutputPath(args[++i]);
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-m(多行命令输入)；\n-mo(多行命令输出文件)；");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                String commands = scanner.nextLine();
                String[] commandSplit = commands.split(" ");
                analysisCommand(commandSplit);
                scanner.close();
            }
        }
    }

    public CommandBean getCommand() {
        return command;
    }

}
