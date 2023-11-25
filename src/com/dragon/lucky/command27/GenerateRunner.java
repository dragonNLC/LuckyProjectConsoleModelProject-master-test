package com.dragon.lucky.command27;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;

    private CallbackListener mCallBackListener;

    private GenerateRunner() {

    }

    public static GenerateRunner getInstance() {
        if (sInstance == null) {
            synchronized (GenerateRunner.class) {
                if (sInstance == null) {
                    sInstance = new GenerateRunner();
                }
            }
        }
        return sInstance;
    }

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        List<String> inputData = FileReadHelper.readFile(command.getInputPath());
        List<LineBean> inputResult = new ArrayList<>();
        for (int i = 0; i < inputData.size(); i++) {
            String d = inputData.get(i);
            String[] contents = d.split("-");
            d = contents[0];
            int generateSize = 0;
            int[] generateSizeArr = null;
            if (contents[1].contains("、")) {
                String[] contentArr = contents[1].split("、");
                generateSizeArr = new int[contentArr.length];
                for (int j = 0; j < contentArr.length; j++) {
                    generateSizeArr[j] = Integer.parseInt(contentArr[j]);
                }
            } else {
                generateSize = Integer.parseInt(contents[1]);
            }
            String[] splitLine;
            if (d.contains("、")) {
                splitLine = d.replace(" ", "").split("、");
            } else {
                splitLine = d.replace(" ", "").split(",");
            }
            if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                byte[] numbers = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        numbers[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                List<NumberBean> numberData = new ArrayList<>();
                if (generateSizeArr != null) {
                    for (int j = 0; j < generateSizeArr.length; j++) {
                        numberData.add(new NumberBean(numbers, generateSizeArr[j]));
                    }
                    inputResult.add(new LineBean(numberData));
                } else {
                    numberData.add(new NumberBean(numbers, generateSize));
                    inputResult.add(new LineBean(numberData));
                }
            }
        }
        StringBuilder print = new StringBuilder();
        //拿到基础数之后，开始按照需要生成对应的数据队列
        for (int i = 0; i < inputResult.size(); i++) {
            LineBean nb = inputResult.get(i);
            List<NumberBean> numberData = nb.getData();
            for (int j = 0; j < numberData.size(); j++) {
                NumberBean number = numberData.get(j);
                List<NumberDataBean> result = new ArrayList<>();
                int[] resultArr = new int[number.getGenerateSize()];
                if (number.getGenerateSize() != 0) {
                    Utils.combine_increase_for_number27(number.getData(), 0, resultArr, number.getGenerateSize(), number.getGenerateSize(), number.getData().length, result);
                }
                number.setGenerateData(result);
                print.append("生成数据数量:").append(result.size());
                print.append(" ");
            }
        }
        Log.i("数据生成完毕。");
        Log.i(print.toString());
        if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
            try {
                FileReadHelper.writeToFileForRun27(mCommand.getOutputPath(), "", inputResult.get(0).getData().get(0).getGenerateData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("数据输出完毕。");

    }

}
