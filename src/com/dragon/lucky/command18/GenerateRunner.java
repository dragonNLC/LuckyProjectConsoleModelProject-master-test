package com.dragon.lucky.command18;

import com.dragon.lucky.bean.ContentBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

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
        if (!FileReadHelper.checkFileExists(command.getPreviewPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("条件文件不存在");
            return;
        }
        List<String> previewData = FileReadHelper.readFile(command.getPreviewPath());
        List<NumberDataBean> previewResult = new ArrayList<>();
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
        List<String> inputData = FileReadHelper.readFile(command.getInputPath());
        List<InputBean> inputBeans = new ArrayList<>();
        for (int i = 0; i < inputData.size(); i++) {
            String[] inputArr = inputData.get(i).split(":");
            byte[] fqs = new byte[inputArr.length];
            for (int j = 0; j < inputArr.length; j++) {
                fqs[j] = Byte.parseByte(inputArr[j]);
            }
            inputBeans.add(new InputBean(fqs));
        }

        /*byte[] heads = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        byte[] centers = new byte[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        byte[] foots = new byte[]{24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};*/
        List<NumberDataBean> fqNumber = AssistUtils.generateFQNumber(mCommand.getFq(), 35);//分区的数据

        List<NumberDataBean> baseFirstFilterDatas = new ArrayList<>();
        for (int j = 0; j < previewResult.size(); j++) {
            NumberDataBean numbers = previewResult.get(j);
//            int headCount = 0;
//            int centerCount = 0;
//            int footCount = 0;
            labrlPoint:
            for (int k = 0; k < numbers.getData().length; k++) {
                for (int i = 0; i < fqNumber.size(); i++) {
                    NumberDataBean fq = fqNumber.get(i);
                    for (int l = 0; l < fq.getData().length; l++) {
                        if (numbers.getData()[k] == fq.getData()[l]) {
                            numbers.getFqData()[i] = (byte) (numbers.getFqData()[i] + 1);
                        }
                    }
                }
                /*for (int l = 0; l < heads.length; l++) {
                    if (numbers.getData()[k] == heads[l]) {
                        headCount++;
                    }
                }
                for (int l = 0; l < centers.length; l++) {
                    if (numbers.getData()[k] == centers[l]) {
                        centerCount++;
                    }
                }
                for (int l = 0; l < foots.length; l++) {
                    if (numbers.getData()[k] == foots[l]) {
                        footCount++;
                    }
                }*/
                for (int i = 0; i < inputBeans.size(); i++) {
                    InputBean inputBean = inputBeans.get(i);
//                    if (headCount == inputBean.getHeader() && centerCount == inputBean.getCenter() && footCount == inputBean.getFooter()) {
//                        baseFirstFilterDatas.add(numbers);
//                    }
                    if (numbers.getFqData().length == inputBean.getFqData().length) {
                        if (Arrays.equals(numbers.getFqData(), inputBean.getFqData())) {
                            baseFirstFilterDatas.add(numbers);
                        }
                    }
                }
            }
        }


        StringBuilder sb = new StringBuilder();
        int existsData1 = printSingleDataIdx(baseFirstFilterDatas, 1);//先得到符合的数
        int existsData2 = printSingleDataIdx(baseFirstFilterDatas, 2);//先得到符合的数
        int existsData3 = printSingleDataIdx(baseFirstFilterDatas, 3);//先得到符合的数
        int existsData4 = printSingleDataIdx(baseFirstFilterDatas, 4);//先得到符合的数
        int existsData5 = printSingleDataIdx(baseFirstFilterDatas, 5);//先得到符合的数
        sb.append("-----------------------结果：" + baseFirstFilterDatas.size() + "条-----------------------------");
        sb.append("\n");
        float percent1 = (existsData1 / (float) baseFirstFilterDatas.size()) * 100;
        sb.append("中“1”位数量：").append(existsData1).append("     ").append(String.format("%.2f", percent1)).append("%");
        sb.append("\n");
        float percent2 = (existsData2 / (float) baseFirstFilterDatas.size()) * 100;
        sb.append("中“2”位数量：").append(existsData2).append("     ").append(String.format("%.2f", percent2)).append("%");
        sb.append("\n");
        float percent3 = (existsData3 / (float) baseFirstFilterDatas.size()) * 100;
        sb.append("中“3”位数量：").append(existsData3).append("     ").append(String.format("%.2f", percent3)).append("%");
        sb.append("\n");
        float percent4 = (existsData4 / (float) baseFirstFilterDatas.size()) * 100;
        sb.append("中“4”位数量：").append(existsData4).append("     ").append(String.format("%.2f", percent4)).append("%");
        sb.append("\n");
        float percent5 = (existsData5 / (float) baseFirstFilterDatas.size()) * 100;
        sb.append("中“5”位数量：").append(existsData5).append("     ").append(String.format("%.2f", percent5)).append("%");
        sb.append("\n");
        /*for (NumberDataBean baseFirstFilterData : baseFirstFilterDatas) {
            sb.append(Arrays.toString(baseFirstFilterData.getData()));
            sb.append("\n");
        }*/
        if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
            try {
                FileReadHelper.writeToFileForRun18(mCommand.getOutputPath(), sb.toString(), baseFirstFilterDatas);
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

}
