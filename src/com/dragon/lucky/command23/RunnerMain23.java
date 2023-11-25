package com.dragon.lucky.command23;

import java.io.IOException;

/***
 * 读取txt文本内的数据,统一汇总导出到excel表
 */
public class RunnerMain23 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
