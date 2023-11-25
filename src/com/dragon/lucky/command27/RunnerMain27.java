package com.dragon.lucky.command27;


import java.io.IOException;

/****
 * 输入原始数文件，根据位数条件生成对应的数据
 */
public class RunnerMain27 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
