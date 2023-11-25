package com.dragon.lucky.command8a;

import com.dragon.lucky.utils.Utils;

import java.io.IOException;

public class RunnerMain8a {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
