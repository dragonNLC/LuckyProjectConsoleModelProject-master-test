package com.dragon.lucky.filter2;

import com.dragon.lucky.command.CommandBean;
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
                    case "-i"://输入原始数据
                        command.setInputPath(args[++i]);
                        break;
                    case "-o"://输出结果文件
                        command.setOutputPath(args[++i]);
                        break;
                    case "-s":
                        command.setGenerateSize(Integer.parseInt(args[++i]));
                        break;
                    case "-m"://输入预加载文件
                        command.setCommandsPath(args[++i]);
                        break;
                    case "-f":
                        command.setFilterPath(args[++i]);
                        break;
                    case "-c":
                        int len = Integer.parseInt(args[++i]);
                        byte[] checkData = new byte[/*args.length - i - 1*/len];
                        for (int j = 0; j < checkData.length; j++) {
                            checkData[j] = Byte.parseByte(args[++i]);
                        }
                        command.setCheckData(checkData);
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i(输入原始数据文件)；\n-f(输入去除的次数区域数据文件)；\n-o（输出结果文件）;\n-s(输入生成的数组位数)；\n-m(多行命令输入)；\n-c(校验文件中是否存在该数组，在-c后带一个检验长度)；");
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
                    case "-s":
                        commandBean.setGenerateSize(Integer.parseInt(args[++i]));
                        break;
                    case "-m"://输入预加载文件
                        commandBean.setCommandsPath(args[++i]);
                        break;
                    case "-f":
                        commandBean.setFilterPath(args[++i]);
                        break;
                    case "-c":
                        int len = Integer.parseInt(args[++i]);
                        byte[] checkData = new byte[/*args.length - i - 1*/len];
                        for (int j = 0; j < checkData.length; j++) {
                            checkData[j] = Byte.parseByte(args[++i]);
                        }
                        commandBean.setCheckData(checkData);
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i(输入原始数据文件)；\n-f(输入去除的次数区域数据文件)；\n-o（输出结果文件）;\n-s(输入生成的数组位数)；\n-m(多行命令输入)；\n-c(校验文件中是否存在该数组，在-c后带一个检验长度)；");
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
