package com.dragon.lucky.command24;


import java.io.IOException;

/*****
 *
 * 输入数组、检测数据，获取命中五位的数据，所有批量处理结果汇总之后统一输出到Excel表中。
 *
 *
 */
public class RunnerMain24 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
