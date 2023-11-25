package com.dragon.lucky.command25;


import java.io.IOException;

/****
 * 输入数据文件，相同数个数、
 * 生成条数、重复次数，随机生成一条数据，
 * 后续数据根据前面输入的参数进行生成，将得出的结果汇总之后统一输出到excel中
 */
public class RunnerMain25 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
