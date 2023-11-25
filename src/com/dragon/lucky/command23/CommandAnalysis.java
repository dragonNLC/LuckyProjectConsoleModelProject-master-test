package com.dragon.lucky.command23;

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
                    case "-i"://输入预加载文件
                        command.setInputPath(args[++i]);
                        break;
                    case "-o"://输入预加载文件
                        command.setOutputPath(args[++i]);
                        break;
                    case "-r"://输入预加载文件
                        command.setRowItem(Integer.parseInt(args[++i]));
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i(输入文件)；\n-o(输出文件)；\n-r(多少条数据换行)；");
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
                    case "-i"://输入预加载文件
                        commandBean.setInputPath(args[++i]);
                        break;
                    case "-o"://输入预加载文件
                        commandBean.setOutputPath(args[++i]);
                        break;
                    case "-r"://输入预加载文件
                        commandBean.setRowItem(Integer.parseInt(args[++i]));
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i(输入文件)；\n-o(输出文件)；\n-r(多少条数据换行)；");
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
