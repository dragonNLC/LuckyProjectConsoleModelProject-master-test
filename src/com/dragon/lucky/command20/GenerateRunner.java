package com.dragon.lucky.command20;

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

    private void printData(List<ResultBean> previewResult) {
        OnceGenerateBean onceGenerateBean = new OnceGenerateBean();
//        Log.i("校验完毕，出现总条数：" + previewResult.size());
        List<ResultBean> existsData5 = printSingleDataIdx(previewResult, 5);//先得到符合的数

        onceGenerateBean.setData(existsData5);
        onceGenerateBean.setZ5(existsData5.size());
        onceGenerateBean.setTitle(new File(mCommand.getPreviewFilePath()).getName().split("\\.")[0]);
        onceGenerateBean.setCollect(new HashMap<>());
        onceGenerateBean.setCheckData(mCommand.getCheckData());
        onceGenerateBean.setPath(new File(mCommand.getPreviewFilePath()).getParentFile().getAbsolutePath() + File.separator + new File(mCommand.getPreviewFilePath()).getName().split("\\.")[0] + "-run20-out.txt");

        for (int i = 1; i < 36; i++) {
            if (!onceGenerateBean.getCollect().containsKey((byte) i)) {
                onceGenerateBean.getCollect().put((byte) i, 0);
            }
//            onceGenerateBean.getCollect().put((byte) i, onceGenerateBean.getCollect().get((byte) i) + 1);
        }

        for (int i = 0; i < existsData5.size(); i++) {
            ResultBean rb = existsData5.get(i);
            for (int j = 0; j < rb.getData().length; j++) {
                if (!onceGenerateBean.getCollect().containsKey(rb.getData()[j])) {
                    onceGenerateBean.getCollect().put(rb.getData()[j], 0);
                }
                onceGenerateBean.getCollect().put(rb.getData()[j], onceGenerateBean.getCollect().get(rb.getData()[j]) + 1);
//                Log.i("onceGenerateBean.getCollect().get(rb.getData()[j]) = " + onceGenerateBean.getCollect().get(rb.getData()[j]));
            }
        }

        if (mCallBackListener != null) {
            mCallBackListener.onCompile(onceGenerateBean);
        }
    }

    private List<ResultBean> printSingleDataIdx(List<ResultBean> data, int existsCountStandard) {
        List<ResultBean> resultBeans = new ArrayList<>();
        int size = 0;
        for (int i = 0; i < data.size(); i++) {
            ResultBean d = data.get(i);
            byte[] hitData = new byte[d.getData().length];
            int existsCount = 0;
            for (int j = 0; j < d.getData().length; j++) {
                for (int k = 0; k < mCommand.getCheckData().length; k++) {
                    if (mCommand.getCheckData()[k] == d.getData()[j]) {
                        existsCount++;
                        hitData[j] = 1;
                        break;
                    }
                }
            }
            d.setHitData(hitData);
            if (existsCount == existsCountStandard) {
                size++;
                resultBeans.add(d);
            }

        }
        return resultBeans;
    }

}
