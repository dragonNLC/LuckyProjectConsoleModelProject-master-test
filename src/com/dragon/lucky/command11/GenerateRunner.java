package com.dragon.lucky.command11;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;

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

    public static List<ResultBean> allGenerateData;

    private CallbackListener mCallBackListener;

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!Utils.isEmpty(command.getPreviewFilePath())) {
            if (!FileReadHelper.checkFileExists(command.getPreviewFilePath())) {
                Log.i("预加载文件不存在");
                return;
            }
            List<String> previewData = FileReadHelper.readFile(command.getPreviewFilePath());
            List<ResultBean> previewResult = new ArrayList<>();
            for (int i = 0; i < previewData.size(); i++) {
                String d = previewData.get(i);
//                String[] content = d.split("],\\[");
//                String line = content[0].replace("[", "").replace("]", "");
                String line = "";
                if (!d.contains("[") || !d.contains("]")) {
                    continue;
                }
                if (d.contains("],[")) {
                    String[] content = d.split("],\\[");
                    line = content[0].replace("[", "").replace("]", "");
                } else {
                    line = d.replace("[", "").replace("]", "");
                }
                String[] splitLine = line.split(", ");
                if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                    byte[] splitLineInt = new byte[splitLine.length];
                    for (int j = 0; j < splitLine.length; j++) {
                        if (Utils.isNumeric(splitLine[j])) {
                            splitLineInt[j] = Byte.parseByte(splitLine[j]);
                        }
                    }
                    previewResult.add(new ResultBean(splitLineInt, i, mCommand.getMergeCount()));
                }
            }
            allGenerateData = previewResult;
            printData(allGenerateData);
            return;
        }
    }

    private void printData(List<ResultBean> previewResult) {
        if (mCommand.getCheckData() == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("-----------------------结果：" + previewResult.size() + "条-----------------------------");
            sb.append("\n");
            /*for (int i = 0; i < previewResult.size(); i++) {
                sb.append(Arrays.toString(previewResult.get(i).getData()));
                sb.append("\n");
            }*/

            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFileForRun3(mCommand.getOutputPath(), sb.toString(), previewResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            List<ResultBean> printData = new ArrayList<>();
            List<Integer> notExists = new ArrayList<>();
            for (int i = 0; i < previewResult.size(); i++) {
                ResultBean data = previewResult.get(i);
                int existsCount = 0;
                for (int k = 0; k < mCommand.getCheckData().length; k++) {
                    for (int j = 0; j < data.getData().length; j++) {
                        if (mCommand.getCheckData()[k] == data.getData()[j]) {
                            existsCount++;
                            break;
                        }
                    }
                }
                if (existsCount == mCommand.getCheckDataCount()) {
                    notExists.add(i);
                    printData.add(data);
                }
            }
            StringBuilder sb = new StringBuilder();
            Log.i("中" + mCommand.getCheckDataCount() + "个的数量：" + notExists.size());
            for (int i = 0; i < notExists.size(); i++) {
//                Log.i("第" + (i + 1) + "行存在对应的数据！");
                sb.append(notExists.get(i) + 1);
                sb.append("\n");
            }
            sb.append("\n");
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (mCallBackListener != null) {
            mCallBackListener.onCompile();
        }
    }

}
