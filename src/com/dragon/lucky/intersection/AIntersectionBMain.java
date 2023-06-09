package com.dragon.lucky.intersection;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultMergeBean;
import com.dragon.lucky.command.CommandBean;
import com.dragon.lucky.command.FilterUtils;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AIntersectionBMain {


    public static void main(String[] args) throws IOException {
        CommandBean commandBean = analysisCommand(args);
        if (!FileReadHelper.checkFileExists(commandBean.getInputPath())) {
            Log.i("导入文件不存在");
            return;
        }
        List<String> inputData = FileReadHelper.readFile(commandBean.getInputPath());
        List<ResultBean> inputDataResult = new ArrayList<>();
        for (int i = 0; i < inputData.size(); i++) {
            String d = inputData.get(i);
            String line = d.replace("[", "").replace("]", "");
            String[] splitLine = line.split(", ");
            if (splitLine != null && splitLine.length > 0) {
                byte[] splitLineInt = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        splitLineInt[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                inputDataResult.add(new ResultBean(splitLineInt, i, commandBean.getMergeCount()));
            }
        }
        Log.i("inputDataResult = " + inputDataResult.size());

        List<String> input2Data = FileReadHelper.readFile(commandBean.getPreviewFilePath());
        List<ResultBean> input2DataResult = new ArrayList<>();
        for (int i = 0; i < input2Data.size(); i++) {
            String d = input2Data.get(i);
            String line = d.replace("[", "").replace("]", "");
            String[] splitLine = line.split(", ");
            if (splitLine != null && splitLine.length > 0) {
                byte[] splitLineInt = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        splitLineInt[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                input2DataResult.add(new ResultBean(splitLineInt, i, commandBean.getMergeCount()));
            }
        }
        Log.i("input2DataResult = " + input2DataResult.size());
        List<ResultMergeBean> mergeResultBeans = FilterUtils.collectMergeBean(inputDataResult);
        List<ResultMergeBean> mergeResultBeans2 = FilterUtils.collectMergeBean(input2DataResult);
        Log.i("mergeResultBeans = " + mergeResultBeans.size());
        Log.i("mergeResultBeans2 = " + mergeResultBeans2.size());
        List<ResultBean> resultAppendData = new ArrayList<>(FilterUtils.getContains2("1", mergeResultBeans, 0, mergeResultBeans.size(), mergeResultBeans2, 0, mergeResultBeans2.size()));
        //求到并集，然后就可以了，写出到文件
        Log.i("计算完成，结果数：" + resultAppendData.size());

        resultAppendData.sort(Comparator.comparingLong(ResultBean::getId));
        try {
            FileReadHelper.writeToFile(commandBean.getOutputPath(), resultAppendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //根据输入内容获取实际指令
    public static CommandBean analysisCommand(String[] args) {
        CommandBean command = null;
        if (args != null && args.length > 0) {
            command = new CommandBean();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-i"://输入原始数据
                        command.setInputPath(args[++i]);
//                        Log.i("args[++i] = "+ args[i]);
                        break;
                    case "-i2"://输入原始数据
                        command.setPreviewFilePath(args[++i]);
//                        Log.i("args2[++i] = "+ args[i]);
                        break;
                    case "-o"://输出结果文件
                        command.setOutputPath(args[++i]);
//                        Log.i("args3[++i] = "+ args[i]);
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i(输入原始数据文件)；\n-i2（输入原始数据文件2）;\n-o（输出结果文件）;");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                String commands = scanner.nextLine();
                String[] commandSplit = commands.split(" ");
                scanner.close();
                return analysisCommand(commandSplit);
            }
        }
        return command;
    }

}
