package com.dragon.lucky.command9;


import java.io.IOException;

public class RunnerMain9 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
