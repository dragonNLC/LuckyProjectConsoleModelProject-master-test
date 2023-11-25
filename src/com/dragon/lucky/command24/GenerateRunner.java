package com.dragon.lucky.command24;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;
    private List<Integer> compileData = new ArrayList<>();
    private int threadCount;
    private int compileThreadCount;

    private CopyOnWriteArrayList<String> threadName = new CopyOnWriteArrayList<>();

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
                if (!d.contains("[") || !d.contains("]")) {
                    continue;
                }
//                String line = d.replace("[", "").replace("]", "");
                String line = "";
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

            printData(previewResult);
        }
    }

    private void printData(List<ResultBean> inputData) throws IOException {
        Map<String, Integer> collections = new HashMap<>();
        for (int i = 0; i < inputData.size(); i++) {
            ResultBean rb = inputData.get(i);
            for (int j = 0; j < rb.getData().length; j++) {
                if (!collections.containsKey(String.valueOf(rb.getData()[j]))) {
                    collections.put(String.valueOf(rb.getData()[j]), 0);
                }
                collections.put(String.valueOf(rb.getData()[j]), collections.get(String.valueOf(rb.getData()[j])) + 1);
            }
        }
        StringBuilder sb = new StringBuilder();
        List<CollectionData> keyList = new ArrayList<>();

        for (String key:
             collections.keySet()) {
            keyList.add(new CollectionData(key, collections.get(key)));
        }
        keyList.sort(Comparator.comparing(o -> o.count));

        for (CollectionData c :
                keyList) {
            sb.append(c.key);
            sb.append(" : ");
            sb.append(c.count);
            sb.append("\n");
        }

        FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
        Log.i("处理完成！");
    }

}
