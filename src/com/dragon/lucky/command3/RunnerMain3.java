package com.dragon.lucky.command3;

import java.io.IOException;

public class RunnerMain3 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        GenerateRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
