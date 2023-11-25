package com.dragon.lucky.command19;


import com.dragon.lucky.bean.TwoPoint;

import java.io.IOException;
import java.util.List;

/*****
 * 主要是处理两位、三位数据的
 * 输入数据文件、条件文件，进行比对处理，拿到结果数进行命中1、2查询，将结果汇总到excel表格
 */
public class RunnerMain19 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

    public static void run(String cmd, List<TwoPoint> redNumber, List<TwoPoint> yellowNumber, List<TwoPoint> oriNumber, List<TwoPoint> blueNumber) throws IOException {
        String[] args = cmd.split(" ");
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().setRedNumber(redNumber);
        ProcessRunner.getInstance().setYellowNumber(yellowNumber);
        ProcessRunner.getInstance().setOriNumber(oriNumber);
        ProcessRunner.getInstance().setBlueNumber(blueNumber);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
    }

}
