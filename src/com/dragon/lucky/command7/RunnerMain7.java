package com.dragon.lucky.command7;

import com.dragon.lucky.utils.Utils;

import java.io.IOException;

public class RunnerMain7 {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

    public static void run(String cmd) throws IOException {
        String[] args = cmd.split(" ");
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
    }

}//-m H:\Data\calculate\20230627\run7.txt
