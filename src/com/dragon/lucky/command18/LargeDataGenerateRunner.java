package com.dragon.lucky.command18;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LargeDataGenerateRunner implements ReadCallBackListener {

    private static LargeDataGenerateRunner sInstance;
    private CommandBean mCommand;

    private CallbackListener mCallBackListener;
    private List<NumberDataBean> fqNumber;

    private LargeDataGenerateRunner() {

    }

    public static LargeDataGenerateRunner getInstance() {
        if (sInstance == null) {
            synchronized (LargeDataGenerateRunner.class) {
                if (sInstance == null) {
                    sInstance = new LargeDataGenerateRunner();
                }
            }
        }
        return sInstance;
    }

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!FileReadHelper.checkFileExists(command.getPreviewPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("条件文件不存在");
            return;
        }
        FileReadHelper.deleteFile(mCommand.getOutputPath() + ".temp");
        Log.i("文件过大，转到大文件处理！");

        int limit = 10000000;
        fqNumber = AssistUtils.generateFQNumber(mCommand.getFq(), 35);//分区的数据
        FileReadHelper.readFileWithLimit(command.getPreviewPath(), limit, this);

        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------结果：" + allDataLen + "条-----------------------------");
        sb.append("\n");
        float percent1 = (exists1 / (float) allDataLen) * 100;
        sb.append("中“1”位数量：").append(exists1).append("     ").append(String.format("%.2f", percent1)).append("%");
        sb.append("\n");
        float percent2 = (exists2 / (float) allDataLen) * 100;
        sb.append("中“2”位数量：").append(exists2).append("     ").append(String.format("%.2f", percent2)).append("%");
        sb.append("\n");
        float percent3 = (exists3 / (float) allDataLen) * 100;
        sb.append("中“3”位数量：").append(exists3).append("     ").append(String.format("%.2f", percent3)).append("%");
        sb.append("\n");
        float percent4 = (exists4 / (float) allDataLen) * 100;
        sb.append("中“4”位数量：").append(exists4).append("     ").append(String.format("%.2f", percent4)).append("%");
        sb.append("\n");
        float percent5 = (exists5 / (float) allDataLen) * 100;
        sb.append("中“5”位数量：").append(exists5).append("     ").append(String.format("%.2f", percent5)).append("%");
        sb.append("\n");

        if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
            try {
                FileReadHelper.appendToFileHead(mCommand.getOutputPath() + ".temp", mCommand.getOutputPath(), sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mCallBackListener != null) {
            mCallBackListener.onCompile();
        } else {
            Log.i("所有数据处理完成！");
        }
    }

    private long allDataLen;
    private long exists1;
    private long exists2;
    private long exists3;
    private long exists4;
    private long exists5;

    private int printSingleDataIdx(List<NumberDataBean> data, int existsCountStandard) {
        int size = 0;
        for (int i = 0; i < data.size(); i++) {
            NumberDataBean d = data.get(i);
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < d.getData().length; j++) {
                    if (mCommand.getCheckData()[k] == d.getData()[j]) {
                        existsCount++;
                        break;
                    }
                }
            }
            if (existsCount == existsCountStandard) {
                size++;
            }
        }
        return size;
    }

    @Override
    public void onResultGroup(List<String> previewData) {
        List<NumberDataBean> previewResult = new ArrayList<>();
//        Log.i("完成！");
        for (int i = 0; i < previewData.size(); i++) {
            String d = previewData.get(i);
            if (!d.contains("[") || !d.contains("]")) {
                continue;
            }
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
                byte[] numbers = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        numbers[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                previewResult.add(new NumberDataBean(mCommand.getFq(), numbers));
            }
        }
        List<String> inputData = null;
        try {
            inputData = FileReadHelper.readFile(mCommand.getInputPath());
            List<InputBean> inputBeans = new ArrayList<>();
            for (int i = 0; i < inputData.size(); i++) {
                String[] inputArr = inputData.get(i).split(":");
                byte[] fqs = new byte[inputArr.length];
                for (int j = 0; j < inputArr.length; j++) {
                    fqs[j] = Byte.parseByte(inputArr[j]);
                }
                inputBeans.add(new InputBean(fqs));
            }
            List<NumberDataBean> baseFirstFilterDatas = new ArrayList<>();
            for (int j = 0; j < previewResult.size(); j++) {
                NumberDataBean numbers = previewResult.get(j);
                for (int k = 0; k < numbers.getData().length; k++) {
                    for (int i = 0; i < fqNumber.size(); i++) {
                        NumberDataBean fq = fqNumber.get(i);
                        for (int l = 0; l < fq.getData().length; l++) {
                            if (numbers.getData()[k] == fq.getData()[l]) {
                                numbers.getFqData()[i] = (byte) (numbers.getFqData()[i] + 1);
                            }
                        }
                    }
                    for (int i = 0; i < inputBeans.size(); i++) {
                        InputBean inputBean = inputBeans.get(i);
                        if (numbers.getFqData().length == inputBean.getFqData().length) {
                            if (Arrays.equals(numbers.getFqData(), inputBean.getFqData())) {
                                baseFirstFilterDatas.add(numbers);
                            }
                        }
                    }
                }
            }

            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFileForRun18(mCommand.getOutputPath() + ".temp", baseFirstFilterDatas);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            exists1 += printSingleDataIdx(baseFirstFilterDatas, 1);//先得到符合的数
            exists2 += printSingleDataIdx(baseFirstFilterDatas, 2);//先得到符合的数
            exists3 += printSingleDataIdx(baseFirstFilterDatas, 3);//先得到符合的数
            exists4 += printSingleDataIdx(baseFirstFilterDatas, 4);//先得到符合的数
            exists5 += printSingleDataIdx(baseFirstFilterDatas, 5);//先得到符合的数
            allDataLen += baseFirstFilterDatas.size();

            baseFirstFilterDatas.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
