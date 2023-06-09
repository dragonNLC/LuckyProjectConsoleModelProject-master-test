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
        CommandBean commandBean = analysisCommand(args);
        if (!FileReadHelper.checkFileExists(commandBean.getInputPath())) {
            Log.i("导入文件不存在");
            return;
        }
        List<String> previewData = FileReadHelper.readFile(commandBean.getInputPath());
        List<ResultBean> previewResult = new ArrayList<>();
        for (int i = 0; i < previewData.size(); i++) {
            String d = previewData.get(i);
            String line = d.replace("[", "").replace("]", "");
            String[] splitLine = line.split(", ");
            if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                byte[] splitLineInt = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        splitLineInt[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                previewResult.add(new ResultBean(splitLineInt, i, commandBean.getMergeCount()));
            }
        }
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            List<ResultBean> tempResult = new ArrayList<>();
            ResultBean rb = previewResult.get(i);
            int[] resultArr = new int[commandBean.getGenerateSize()];
            List<Byte> byteData = new ArrayList<>();
            for (int j = 0; j < rb.getData().length; j++) {
                byteData.add(rb.getData()[j]);
            }
            Utils.combine_increase(byteData, 0, resultArr, commandBean.getGenerateSize(), commandBean.getGenerateSize(), byteData.size(), tempResult, new long[1], commandBean.getMergeCount());
            if (result.size() == 0) {
                result.addAll(tempResult);
            } else {
                List<ResultBean> appendTempResultData = new ArrayList<>();
                for (int j = 0; j < tempResult.size(); j++) {
                    ResultBean temp = tempResult.get(j);
                    boolean exists = false;
                    for (int k = 0; k < result.size(); k++) {
                        ResultBean temp2 = result.get(k);
                        if (Arrays.equals(temp.getData(), temp2.getData())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }
        }

        List<String> appendData = FileReadHelper.readFile(commandBean.getPreviewFilePath());
        List<ResultBean> appendDataResult = new ArrayList<>();
        for (int i = 0; i < appendData.size(); i++) {
            String d = appendData.get(i);
            String line = d.replace("[", "").replace("]", "");
            String[] splitLine = line.split(", ");
            if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                byte[] splitLineInt = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        splitLineInt[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                appendDataResult.add(new ResultBean(splitLineInt, i, commandBean.getMergeCount()));
            }
        }

        //拿到数据后进行比对
        if (commandBean.getGenerateFilterType() == 1) {//求并集
            List<ResultMergeBean> mergeResultBeans = FilterUtils.collectMergeBean(result);
            List<ResultMergeBean> mergeResultBeans2 = FilterUtils.collectMergeBean(appendDataResult);
            List<ResultBean> resultAppendData = new ArrayList<>(FilterUtils.getContains2("1", mergeResultBeans, 0, mergeResultBeans.size(), mergeResultBeans2, 0, mergeResultBeans2.size()));
            //求到并集，然后就可以了，写出到文件
            Log.i("计算完成，结果数：" + resultAppendData.size());
            try {
                FileReadHelper.writeToFile(commandBean.getOutputPath(), resultAppendData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {//求差集，即前面数组存在的数在后面数组也存在的话，要去掉，剩下不存在的
            //
            List<ResultBean> lastData = new ArrayList<>();
            for (int i = 0; i < appendDataResult.size(); i++) {
                ResultBean rb = appendDataResult.get(i);
                boolean exists = false;
                for (int j = 0; j < result.size(); j++) {
                    ResultBean rb2 = result.get(j);
                    if (Arrays.equals(rb.getData(), rb2.getData())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    lastData.add(rb);
                }
            }
            //求到并集，然后就可以了，写出到文件
            Log.i("计算完成，结果数：" + lastData.size());
            try {
                FileReadHelper.writeToFile(commandBean.getOutputPath(), lastData);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        break;
                    case "-p"://输入原始数据
                        command.setPreviewFilePath(args[++i]);
                        break;
                    case "-o"://输出结果文件
                        command.setOutputPath(args[++i]);
                        break;
                    case "-a"://
                        command.setGenerateFilterType(1);
                        break;
                    case "-d"://
                        command.setGenerateFilterType(2);
                        break;
                    case "-s":
                        command.setGenerateSize(Integer.parseInt(args[++i]));
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i(输入原始数据文件)；\n-p（下级数组文件）;\n-o（输出结果文件）;\n-a(求交集)；\n-d(求差集)；\n-s(输入生成的数组位数)；");
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
