package com.dragon.lucky.command25;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenerateRunner2BK {

    private static GenerateRunner2BK sInstance;
    private CommandBean mCommand;

    private CallbackListener mCallBackListener;

    private GenerateRunner2BK() {

    }

    public static GenerateRunner2BK getInstance() {
        if (sInstance == null) {
            synchronized (GenerateRunner2BK.class) {
                if (sInstance == null) {
                    sInstance = new GenerateRunner2BK();
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

        OnceGenerateBean onceGenerateData = new OnceGenerateBean();
        List<GenerateBean> generateBeans = new ArrayList<>();
        onceGenerateData.setData(generateBeans);
        onceGenerateData.setTitle(new File(mCommand.getInputPath()).getName().split("\\.")[0]);

        mCommand.setReplaySize(mCommand.getReferencekData().length);//设置最多抽几次
        mCommand.setLines(4);//设置最多抽几条

        if (previewResult.size() > 0) {
            int doReplyCount = 300;//重复300次都不出来，就不随机了
            for (int z = 0; z < mCommand.getReplaySize(); z++) {
                //保存本次抽取的结果
                List<NumberDataBean> result = new ArrayList<>();
                GenerateBean generateBean = new GenerateBean(result);
                generateBean.setGenerateSize(mCommand.getLines());
                generateBeans.add(generateBean);
                //保存本次抽取的结果

                /*获取符合要求的数，用于第一条的抽取*/
                List<NumberDataBean> tempPreviewResult = new ArrayList<>(previewResult);
                List<Integer> baseDataIdx = new ArrayList<>();
                for (int i = 0; i < tempPreviewResult.size(); i++) {
                    for (int k = 0; k < tempPreviewResult.get(i).getData().length; k++) {
                        if (tempPreviewResult.get(i).getData()[k] == mCommand.getReferencekData()[z]) {
                            baseDataIdx.add(i);
                            break;
                        }
                    }
                }
                /*获取符合要求的数，用于第一条的抽取*/


                /*如果符合要求的数大于0，则进行抽取，抽的是第一条的数*/
                if (baseDataIdx.size() > 0) {
                    List<Integer> randomData = new ArrayList<>();
                    boolean run = true;
                    int id = 0;
                    Log.i("正在进行处理");
                    do {
                        if (randomData.size() == tempPreviewResult.size()) {
                            Log.i("没有数据符合要求");
                            break;
                        }
//                        Log.i("randomData.size()  = " + randomData.size());
//                        Log.i("tempPreviewResult.size()  = " + tempPreviewResult.size());
                        id = (int) (Math.random() * tempPreviewResult.size());
                        if (randomData.contains(((Integer) id))) {
                            continue;
                        }
                        randomData.add(id);
                        NumberDataBean tempRandomData = tempPreviewResult.get(id);
                        for (int k = 0; k < tempRandomData.getData().length; k++) {
                            if (tempRandomData.getData()[k] == mCommand.getReferencekData()[z]) {
//                                tempRandomData.setDataPos(k);-----------------------------
                                run = false;
                                break;
                            }
                        }
                    } while (run);//第一条符合条件
                    Log.i("跳出第一个循环");
                    /*如果符合要求的数大于0，则进行抽取，抽的是第一条的数*/

                    /*如果是开始抽第二次了，那么第一条的数还要符合要求才可以，与前面的所有数的相同数不能超过3个*/
                    if (z > 0) {
                        while (true) {
                            boolean add = true;
                            NumberDataBean nowGenerateNumbers = tempPreviewResult.get(id);
                            labelPoint:
                            for (int i = 0; i < generateBeans.size(); i++) {
                                GenerateBean generate = generateBeans.get(i);
                                for (int j = 0; j < generate.getData().size(); j++) {
                                    NumberDataBean numberDataBean = generate.getData().get(j);
                                    int nowReplyCount = 0;
                                    byte[] d2 = nowGenerateNumbers.getData();
                                    for (int k = 0; k < numberDataBean.getData().length; k++) {
                                        for (int l = 0; l < d2.length; l++) {
                                            if (numberDataBean.getData()[k] == d2[l]) {
                                                nowReplyCount++;
                                                if (nowReplyCount > 3) {//这一条数跟前面的数的相同个数超过了要求了，不能使用，要重新抽取
                                                    add = false;
                                                    break labelPoint;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (add) {
                                break;
                            } else {
                                run = true;
                                do {
                                    id = (int) (Math.random() * tempPreviewResult.size());
                                    NumberDataBean tempRandomData = tempPreviewResult.get(id);
                                    for (int k = 0; k < tempRandomData.getData().length; k++) {
                                        if (tempRandomData.getData()[k] == mCommand.getReferencekData()[z]) {
//                                            tempRandomData.setDataPos(k);-----------------------
                                            run = false;
                                            break;
                                        }
                                    }
                                } while (run);//第一条符合条件
                            }
                        }
                    }
                    /*如果是开始抽第二次了，那么第一条的数还要符合要求才可以，与前面的所有数的相同数不能超过3个*/

                    Log.i("跳出第二个循环");
                    /////////////////////////////////抽出了符合第一条的数////////////////////////////////
                    NumberDataBean firstFilterData = tempPreviewResult.remove(id);

                    byte existsByte = mCommand.getReferencekData()[z];
                    firstFilterData.setZ345Percent(getZ345Percent(tempPreviewResult));
                    result.add(firstFilterData);
                    long count = 0;
//                while (true) {
                    List<NumberDataBean> template = new ArrayList<>(tempPreviewResult);
                    List<Integer> ids = new ArrayList<>();
                    Iterator<NumberDataBean> dataBeanIterator = template.iterator();
                    int dataId = 0;
                    while (dataBeanIterator.hasNext()) {
                        NumberDataBean ndb = dataBeanIterator.next();
                        int replyCount = 0;
                        for (int k = 0; k < ndb.getData().length; k++) {
                            if (ndb.getData()[k] == existsByte) {
//                                ndb.setDataPos(k);
                                replyCount++;
                                break;
                            }
                        }
                        if (replyCount == 1) {//符合次数的，加入队列
                            ids.add(dataId);
                        }
                        //然后需要判断这个是否符合与前面的数最多有三个相同的结果
                        count++;
                        if (count % 1000 == 0) {
//                            Log.i("count = " + count + "   result.size()  = " + result.size() + "   template = " + template.size());
                        }
                        dataId++;
                    }
                    Log.i("跳出第三个循环");

                    if (ids.size() > 0) {
                        while (true) {
                            id = (int) (Math.random() * ids.size());
                            boolean add = true;
                            firstFilterData = template.get(ids.remove(id));
                            labelPoint:
                            for (int i = 0; i < generateBeans.size(); i++) {
                                GenerateBean generate = generateBeans.get(i);
                                for (int j = 0; j < generate.getData().size(); j++) {
                                    NumberDataBean numberDataBean = generate.getData().get(j);
                                    int nowReplyCount = 0;
                                    byte[] d2 = firstFilterData.getData();
                                    for (int k = 0; k < numberDataBean.getData().length; k++) {
                                        for (int l = 0; l < d2.length; l++) {
                                            if (numberDataBean.getData()[k] == d2[l]) {
                                                nowReplyCount++;
                                                if (nowReplyCount > 3) {//超出相同数字范围的，移除
                                                    add = false;
                                                    break labelPoint;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (add) {
                                labelPoint:
                                for (int j = 0; j < result.size(); j++) {
                                    NumberDataBean numberDataBean = result.get(j);
                                    int nowReplyCount = 0;
                                    byte[] d2 = firstFilterData.getData();
                                    for (int k = 0; k < numberDataBean.getData().length; k++) {
                                        for (int l = 0; l < d2.length; l++) {
                                            if (numberDataBean.getData()[k] == d2[l] && d2[l] != existsByte) {
                                                nowReplyCount++;
                                                if (nowReplyCount > 0) {//超出相同数字范围的，移除
                                                    add = false;
                                                    break labelPoint;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (add) {
                                firstFilterData.setZ345Percent(getZ345Percent(template, ids));
                                for (int i = 0; i < firstFilterData.getData().length; i++) {
                                    if (firstFilterData.getData()[i] == existsByte) {
//                                        firstFilterData.setDataPos(i);-------------------------------
                                        break;
                                    }
                                }
                                tempPreviewResult.remove(firstFilterData);
                                result.add(firstFilterData);
                            }
                            if (ids.size() <= 0) {
                                break;//实在是抽不出了
                            }
                            if (result.size() >= mCommand.getLines()) {//已经生成足够的行数了
                                break;
                            }
                        }
                        Log.i("跳出第4个循环");
                    } else {
                        break;
                    }
//                }
                    Log.i("本次数据运算完毕：" + result.size());

                    if (result.size() < mCommand.getLines()) {//数量小于4，去掉
                        Log.i("数量不符合要求，本次筛选无效！");
                        generateBeans.remove(generateBeans.size() - 1);
                        z--;
                        doReplyCount--;
                        if (doReplyCount <= 0) {//超过了随机的要求，退出本次随机，进入下一次
                            Log.i("已随机多次，无法得到正确数据，进入下一次计算！");
                            z++;
                            doReplyCount = 300;
                        }
                    } else {
                        doReplyCount = 300;
                        /////////////////////////////////////////////////
                        if (mCommand.getCheckData() != null && mCommand.getCheckData().length > 0) {
                            for (NumberDataBean numberDataBean : result) {
                                int hitCount = printSingleDataIdx(numberDataBean);
                                numberDataBean.setHitCount(hitCount);//命中数
                            }
                        }
                    }
                } else {
                    Log.i("第‘" + z + "’个数不存在相同数，进入下一个计算");
                }
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

    //检测中3、4、5的比例
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

    //检测中3、4、5的比例
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

    //检测中的个数
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
