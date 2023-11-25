package com.dragon.lucky.command17;


import java.io.IOException;

/****
 * 输入数据文件，根据分区规划划分，同时统计中n的个数，得出的结果输出到excel表中
 */
public class RunnerMain17 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
