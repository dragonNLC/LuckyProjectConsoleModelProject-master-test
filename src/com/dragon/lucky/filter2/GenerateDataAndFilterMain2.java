package com.dragon.lucky.filter2;

import java.io.IOException;

public class GenerateDataAndFilterMain2 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }


}
