package com.dragon.lucky.command14;


import java.io.IOException;

/****
 * 输入参数文件，根据参数文件进行数据生成，然后前n-1位进行合集，最后跟最后一行进行
 */
public class RunnerMain14 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
