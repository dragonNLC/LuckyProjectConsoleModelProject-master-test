package com.dragon.lucky.command;

import java.io.IOException;

public class RunnerMain {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        GenerateRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
