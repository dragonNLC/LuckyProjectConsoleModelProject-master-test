package com.dragon.lucky.command28;

import java.io.IOException;

/***
 * 读取txt文本内的数据,统一汇总导出到excel表
 */
public class RunnerMain28 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
//-i C:\Users\aptdev\Desktop\za\20231101\run.txt -o C:\Users\aptdev\Desktop\za\20231101\out.txt
//-i C:\Users\aptdev\Desktop\za\20231101\run.txt -o C:\Users\aptdev\Desktop\za\20231101\out.xlsx