package com.dragon.lucky.command25;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
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
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        List<String> previewData = FileReadHelper.readFile(command.getInputPath());
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
                previewResult.add(new NumberDataBean(numbers));
            }
        }
//        byte[] heads = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
//        byte[] centers = new byte[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
//        byte[] foots = new byte[]{24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};


        List<NumberDataBean> fqData = AssistUtils.generateFQData(previewResult.get(0).getData().length, mCommand.getDeleteResidueSize(), 35);//分区的标记
        List<NumberDataBean> fqNumber = AssistUtils.generateFQNumber(mCommand.getDeleteResidueSize(), 35);//分区的数据

        /*List<NumberDataBean> baseFirstFilterDatas = new ArrayList<>();
        for (int j = 0; j < previewResult.size(); j++) {
            NumberDataBean numbers = previewResult.get(j);
            int headCount = 0;
            int centerCount = 0;
            int footCount = 0;
            for (int k = 0; k < numbers.getData().length; k++) {
                for (int l = 0; l < heads.length; l++) {
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
                }
                if ((headCount == 2 && centerCount == 2 && footCount == 3) || (headCount == 2 && centerCount == 3 && footCount == 2) || (headCount == 3 && centerCount == 2 && footCount == 2)) {
                    baseFirstFilterDatas.add(numbers);
                }
            }
        }*/
        OnceGenerateBean onceGenerateData = new OnceGenerateBean();
        List<GenerateBean> generateBeans = new ArrayList<>();
        onceGenerateData.setData(generateBeans);
        onceGenerateData.setTitle(new File(mCommand.getInputPath()).getName().split("\\.")[0]);
        if (previewResult.size() > 0) {
            for (int z = 0; z < mCommand.getReplaySize(); z++) {
                List<NumberDataBean> result = new ArrayList<>();
                GenerateBean generateBean = new GenerateBean(result);
                generateBean.setGenerateSize(mCommand.getLines());
                generateBeans.add(generateBean);

                List<NumberDataBean> tempPreviewResult = new ArrayList<>(previewResult);

                int id = (int) (Math.random() * tempPreviewResult.size());
                if (z > 0) {
                    NumberDataBean lastGenerateFirstNumbers = generateBeans.get(z - 1).getData().get(0);
                    while (true) {
                        int nowReplyCount = 0;
                        NumberDataBean nowGenerateNumbers = tempPreviewResult.get(id);
                        for (int k = 0; k < nowGenerateNumbers.getData().length; k++) {
                            byte[] d2 = nowGenerateNumbers.getData();
                            for (int l = 0; l < d2.length; l++) {
                                if (lastGenerateFirstNumbers.getData()[k] == d2[l]) {
                                    nowReplyCount++;
                                    if (nowReplyCount > 1) {//超出相同数字范围的，移除
                                        break;
                                    }
                                }
                            }
                        }
                        if (nowReplyCount != 1) {//符合次数的，加入队列
                            id = (int) (Math.random() * tempPreviewResult.size());
                        } else {
                            break;
                        }
                    }
                } else {
                    id = 0;
                }

                NumberDataBean firstFilterData = tempPreviewResult.remove(id);
                firstFilterData.setZ345Percent(getZ345Percent(tempPreviewResult));
//                tempPreviewResult.remove(firstFilterData);
                result.add(firstFilterData);
//                if (mCommand.getType() == CommandBean.TYPE_1) {
                long count = 0;
                List<Byte> hitData = new ArrayList<>();
                while (true) {
                    List<NumberDataBean> template = new ArrayList<>(tempPreviewResult);
                    List<Integer> ids = new ArrayList<>();
                    Iterator<NumberDataBean> dataBeanIterator = template.iterator();
                    int dataId = 0;
                    while (dataBeanIterator.hasNext()) {
                        NumberDataBean ndb = dataBeanIterator.next();
                        int replyCount = 0;
                        hitData.clear();

//                        Log.i("time1 = " + System.currentTimeMillis());
                        for (int k = 0; k < ndb.getData().length; k++) {
                            for (int j = 0; j < result.size(); j++) {
                                byte[] d2 = result.get(j).getData();
                                for (int l = 0; l < d2.length; l++) {
                                    if (ndb.getData()[k] == d2[l] && !hitData.contains(ndb.getData()[k])) {
                                        hitData.add(ndb.getData()[k]);
                                        replyCount++;
                                        if (replyCount > 0) {//超出相同数字范围的，移除
                                            break;
                                        }
//                                        Log.i("replyCount = " + replyCount);
                                    }
                                }
                            }
                        }
//                        Log.i("time2 = " + System.currentTimeMillis());

                        if (replyCount == 0) {//符合次数的，加入队列
//                            dataBeanIterator.remove();
                            ids.add(dataId);
                        }
//                        Log.i("time3 = " + System.currentTimeMillis());
                        count++;
                        if (count % 1000 == 0) {
//                            Log.i("count = " + count + "   result.size()  = " + result.size() + "   template = " + template.size());
                        }

                        dataId++;
                    }
                    if (ids.size() > 0) {
                        id = (int) (Math.random() * ids.size());
                        firstFilterData = template.get(ids.get(id));
                        firstFilterData.setZ345Percent(getZ345Percent(template, ids));

                        tempPreviewResult.remove(firstFilterData);
                        result.add(firstFilterData);
                        if (result.size() >= mCommand.getLines()) {//已经生成足够的行数了
                            break;
                        }
                    } else {
                        break;
                    }
                }
                Log.i("本次数据运算完毕：" + result.size());

                /////////////////////////////////////////////////
                if (mCommand.getCheckData() != null && mCommand.getCheckData().length > 0) {
                    for (NumberDataBean numberDataBean : result) {
                        int hitCount = printSingleDataIdx(numberDataBean);
                        numberDataBean.setHitCount(hitCount);//命中数
                    }
                }

                ////统计分区数，为后面根据分区删除数字做准备
                /*for (int j = 0; j < result.size(); j++) {
                    NumberDataBean numbers = result.get(j);
                    numbers.setFqData(new byte[mCommand.getDeleteResidueSize()]);//创建分区参数
                    for (int k = 0; k < numbers.getData().length; k++) {
                        for (int i = 0; i < fqNumber.size(); i++) {
                            NumberDataBean fq = fqNumber.get(i);
                            for (int l = 0; l < fq.getData().length; l++) {
                                if (numbers.getData()[k] == fq.getData()[l]) {
                                    numbers.getFqData()[i] = (byte) (numbers.getFqData()[i] + 1);
                                }
                            }
                        }
                    }
                }

                for (NumberDataBean numberDataBean : result) {
                    List<Byte> rdData = new ArrayList<>();

                    int randomSize = mCommand.getDeleteSize();
                    //////////////////////////////随机出来的数是否已经随机出来过了///////////////////////////////////////
                    while (randomSize > 0) {
                        int idx = ((int) (Math.random() * numberDataBean.getData().length));
                        boolean hasIdx = false;
                        for (int i = 0; i < rdData.size(); i++) {
                            if (rdData.get(i) == numberDataBean.getData()[idx]) {
                                hasIdx = true;
                                break;
                            }
                        }
                        if (!hasIdx) {
                            rdData.add(numberDataBean.getData()[idx]);
                        }
                        //判定是否符合要求,两个奇数两个偶数,满足分区要求
                        boolean has2JS2OS = false;
                        int has2JS = 0;
                        int has2OS = 0;
                        for (int i = 0; i < numberDataBean.getData().length; i++) {
                            if (rdData.contains(numberDataBean.getData()[i])) {
                                continue;
                            }
                            if (numberDataBean.getData()[i] % 2 == 0) {
                                has2OS++;
                            }
                            if (numberDataBean.getData()[i] % 2 == 1) {
                                has2JS++;
                            }
                        }
                        if (has2JS >= 2 && has2OS >= 2) {
                            has2JS2OS = true;
                        }
//                        boolean hasAllowFQ = false;
                        //判定删掉那个数之后,剩余的数的分区是否符合要求
                        int fqCount = 0;
                        for (int i = 0; i < numberDataBean.getFqData().length; i++) {
                            if (numberDataBean.getFqData()[i] > 0) {
                                fqCount++;
                            }
                        }
                        if (fqCount >= mCommand.getDeleteResidueSize() && has2JS2OS) {//分区符合,开始标记该数移除
                            numberDataBean.getDeprecatedDataIdx()[idx] = 1;
                            randomSize--;
                        }
                        if (rdData.size() == numberDataBean.getData().length) {
                            break;//数字都随即过了
                        }
                    }
                    /////////////////////////////////////////////////////////////////////
                }*/
            }
            if (mCallBackListener != null) {
                mCallBackListener.onCompile(onceGenerateData);
            } else {
                Log.i("所有数据处理完成！");
            }
        } else {
            Log.i("不存在2+2+3、2+3+2、3+2+2类型的数据");
        }
    }

    private float getZ345Percent(List<NumberDataBean> data) {
        if (mCommand.getCheckData() == null) {
            return 0;
        }
        float z345 = 0;
        for (int i = 0; i < data.size(); i++) {
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < data.get(i).getData().length; j++) {
                    if (mCommand.getCheckData()[k] == data.get(i).getData()[j]) {
                        existsCount++;
                        break;
                    }
                }
            }
            if (existsCount >= 3) {
                z345++;
            }
        }
        return z345 / data.size();
    }

    private float getZ345Percent(List<NumberDataBean> data, List<Integer> ids) {
        if (mCommand.getCheckData() == null) {
            return 0;
        }
        float z345 = 0;
        for (int i = 0; i < ids.size(); i++) {
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < data.get(ids.get(i)).getData().length; j++) {
                    if (mCommand.getCheckData()[k] == data.get(ids.get(i)).getData()[j]) {
                        existsCount++;
                        break;
                    }
                }
            }
            if (existsCount >= 3) {
                z345++;
            }
        }
        return z345 / ids.size();
    }


    private int printSingleDataIdx(NumberDataBean data) {
        if (mCommand.getCheckData() == null) {
            return 0;
        }
        int existsCount = 0;
        for (int k = 0; k < mCommand.getCheckData().length; k++) {
            for (int j = 0; j < data.getData().length; j++) {
                if (mCommand.getCheckData()[k] == data.getData()[j]) {
                    existsCount++;
                    data.getDataHitIdx()[j] = 1;
                    break;
                }
            }
        }
        return existsCount;
    }

}
