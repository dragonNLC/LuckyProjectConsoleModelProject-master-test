package com.dragon.lucky.command14;

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
                    case "-o"://输出结果文件
                        command.setOutputPath(args[++i]);
                        break;
                    case "-i"://输入预加载文件
                        command.setInputPath(args[++i]);
                        break;
                    case "-m"://输入预加载文件
                        command.setCommandsPath(args[++i]);
                        break;
                    case "-n1"://输入预加载文件
                        command.setFirstFilterDataLen(Integer.parseInt(args[++i]));
                        break;
                    case "-n2"://输入预加载文件
                        command.setSecondFilterDataLen(Integer.parseInt(args[++i]));
                        break;
                    case "-c":
                        int len = Integer.parseInt(args[++i]);
                        byte[] checkData = new byte[len];
                        for (int j = 0; j < checkData.length; j++) {
                            checkData[j] = Byte.parseByte(args[++i]);
                        }
                        command.setCheckData(checkData);
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i（输入预加载文件）；\n-c(校验文件中是否存在该数组，-c后的第一位是校验数组的长度)；\n-o（输出结果文件）;\n-n1（输入第一次保留的位数）；\n-n2（输入第二次保留的位数）；\n-m(多行命令输入)；");
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
                    case "-i"://输入原始数据
                        commandBean.setInputPath(args[++i]);
                        break;
                    case "-o"://输出结果文件
                        commandBean.setOutputPath(args[++i]);
                        break;
                    case "-n1"://输入预加载文件
                        commandBean.setFirstFilterDataLen(Integer.parseInt(args[++i]));
                        break;
                    case "-n2"://输入预加载文件
                        commandBean.setSecondFilterDataLen(Integer.parseInt(args[++i]));
                        break;
                    case "-m"://输入预加载文件
                        commandBean.setCommandsPath(args[++i]);
                        break;
                    case "-c":
                        int len = Integer.parseInt(args[++i]);
                        byte[] checkData = new byte[len];
                        for (int j = 0; j < checkData.length; j++) {
                            checkData[j] = Byte.parseByte(args[++i]);
                        }
                        commandBean.setCheckData(checkData);
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i（输入预加载文件）；\n-c(校验文件中是否存在该数组，-c后的第一位是校验数组的长度)；\n-o（输出结果文件）;\n-n1（输入第一次保留的位数）；\n-n2（输入第二次保留的位数）；\n-m(多行命令输入)；");
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
