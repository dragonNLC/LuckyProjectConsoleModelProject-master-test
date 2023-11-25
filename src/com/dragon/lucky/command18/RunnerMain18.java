package com.dragon.lucky.command18;


import java.io.IOException;

/*****
 * 输入数据文件、分区文件、检测数据，过滤出数据文件中符合分区的数据，得出命中1、2、3、4、5的数据，汇总输出到excel表中
 */
public class RunnerMain18 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
