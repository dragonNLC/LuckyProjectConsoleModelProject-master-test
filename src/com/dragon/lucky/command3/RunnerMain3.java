package com.dragon.lucky.command3;


import com.dragon.lucky.utils.Log;

import java.io.IOException;

public class RunnerMain3 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

    public static void run(String cmd) throws IOException {
        Log.i("cmd = " + cmd);
        String[] args = cmd.split(" ");
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
    }

}
