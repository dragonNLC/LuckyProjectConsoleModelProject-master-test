package com.dragon.lucky.filter;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultMergeBean;
import com.dragon.lucky.command.CommandBean;
import com.dragon.lucky.command.FilterUtils;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GenerateDataAndFilterMain {

    public static void main(String[] args) throws IOException {
        CommandAnalysis.getInstance().analysisCommand(args);
        ProcessRunner.getInstance().run(CommandAnalysis.getInstance().getCommand());
        for (; ; ) {

        }
    }

}
