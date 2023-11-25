package com.dragon.lucky.command25;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GenerateRunner2 {

    private static GenerateRunner2 sInstance;
    private CommandBean mCommand;

    private CallbackListener mCallBackListener;

    private GenerateRunner2() {

    }

    public static GenerateRunner2 getInstance() {
        if (sInstance == null) {
            synchronized (GenerateRunner2.class) {
                if (sInstance == null) {
                    sInstance = new GenerateRunner2();
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

        if (mCommand.isReferenceClose()) {
            mCommand.setReplaySize(1);//设置最多抽几次
        } else {
            mCommand.setReplaySize(mCommand.getReferencekData().length);//设置最多抽几次
        }
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
                List<Integer> headDataIdx = new ArrayList<>();
                for (int i = 0; i < tempPreviewResult.size(); i++) {
                    int existsCount = 0;
                    for (int k = 0; k < tempPreviewResult.get(i).getData().length; k++) {
                        for (int j = 0; j < mCommand.getReferencekData().length; j++) {
                            if (tempPreviewResult.get(i).getData()[k] == mCommand.getReferencekData()[j]) {
                                existsCount++;
                                tempPreviewResult.get(i).getDataPos()[k] = 1;
                                break;
                            }
                        }
                    }
                    if (existsCount == mCommand.getReferencekData().length) {
                        headDataIdx.add(i);
                    } else {
                        Arrays.fill(tempPreviewResult.get(i).getDataPos(), (byte) 0);
                    }
                }
                /*获取符合要求的数，用于第一条的抽取*/


                /*如果符合要求的数大于0，则进行抽取，抽的是第一条的数*/
                if (headDataIdx.size() > 0) {
                    NumberDataBean tempRandomData = null;
//                    List<Integer> randomData = new ArrayList<>();
                    boolean run = true;
                    int id = 0;
                    Log.i("正在进行处理");
                    do {
                        if (headDataIdx.size() <= 0) {
                            Log.i("没有数据符合要求");
                            tempRandomData = null;
                            break;
                        }
                        boolean add = true;
                        id = (int) (Math.random() * headDataIdx.size());
                        tempRandomData = tempPreviewResult.get(headDataIdx.remove(id));

                        if (z > 0) {
                            labelPoint:
                            for (int i = 0; i < generateBeans.size(); i++) {
                                GenerateBean generate = generateBeans.get(i);
                                for (int j = 0; j < generate.getData().size(); j++) {
                                    NumberDataBean numberDataBean = generate.getData().get(j);
                                    int nowReplyCount = 0;
                                    byte[] d2 = tempRandomData.getData();
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
                            }
                        } else {
                            run = false;
                        }
                    } while (run);//第一条符合条件
                    Log.i("跳出第一个循环");
                    /*如果符合要求的数大于0，则进行抽取，抽的是第一条的数*/

                    if (tempRandomData != null) {//只有当抽出第一条的时候，后面的继续下去才有意义
                        /////////////////////////////////抽出了符合第一条的数////////////////////////////////
                        byte existsByte = mCommand.getReferencekData()[z];
                        tempRandomData.setZ345Percent(getZ345Percent(tempPreviewResult));
                        tempRandomData.getDataPosMap().put(z + "-" + result.size(), tempRandomData.getDataPos());
                        tempRandomData.setDataPos(new byte[tempRandomData.getDataPos().length]);
                        Log.i((z + "-" + result.size()));
                        result.add(tempRandomData);

                        /*-------------------*/
                        long count = 0;
                        List<NumberDataBean> template = new ArrayList<>(tempPreviewResult);
                        List<Integer> ids = new ArrayList<>();
                        Iterator<NumberDataBean> dataBeanIterator = template.iterator();
                        /*-------------------*/

                        /*获取符合某个数的所有数组*/
                        if (mCommand.isReferenceClose()) {
                            for (int i = 0; i < template.size(); i++) {
                                ids.add(i);
                            }
                        } else {
                            int dataId = 0;
                            while (dataBeanIterator.hasNext()) {
                                NumberDataBean ndb = dataBeanIterator.next();
                                int replyCount = 0;
                                for (int k = 0; k < ndb.getData().length; k++) {
                                    if (ndb.getData()[k] == existsByte && checkNotExistsOther(ndb.getData(), k, mCommand.getReferencekData())) {
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
                        }
                        /*获取符合某个数的所有数组*/
                        Log.i("跳出第二个循环");

                        /*存在数组才能进行下一步抽取*/
                        if (ids.size() > 0) {
                            while (true) {
                                id = (int) (Math.random() * ids.size());
                                boolean add = true;
                                tempRandomData = template.get(ids.remove(id));

                                /*判断跟前面抽取出来的数的相同个数不能超过3个*/
                                labelPoint:
                                for (int i = 0; i < generateBeans.size(); i++) {
                                    GenerateBean generate = generateBeans.get(i);
                                    for (int j = 0; j < generate.getData().size(); j++) {
                                        NumberDataBean numberDataBean = generate.getData().get(j);
                                        int nowReplyCount = 0;
                                        byte[] d2 = tempRandomData.getData();
                                        for (int k = 0; k < numberDataBean.getData().length; k++) {
                                            for (int l = 0; l < d2.length; l++) {
                                                if (numberDataBean.getData()[k] == d2[l]) {
                                                    nowReplyCount++;
                                                    if (mCommand.isReferenceClose()) {//不能跟前面的有相同的
                                                        add = false;
                                                        break labelPoint;
                                                    } else {
                                                        if (nowReplyCount > 3) {//超出相同数字范围的，移除
                                                            add = false;
                                                            break labelPoint;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                /*跟本次的抽取结果进行对比，不能有相同的*/
                                if (add) {
                                    labelPoint:
                                    for (int j = 0; j < result.size(); j++) {
                                        NumberDataBean numberDataBean = result.get(j);
                                        int nowReplyCount = 0;
                                        byte[] d2 = tempRandomData.getData();
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
                                /*跟本次的抽取结果进行对比，不能有相同的*/

                                if (add) {
                                    tempRandomData.setZ345Percent(getZ345Percent(template, ids));
                                    for (int i = 0; i < tempRandomData.getData().length; i++) {
                                        if (tempRandomData.getData()[i] == existsByte) {
                                            tempRandomData.getDataPos()[i] = 1;
                                            break;
                                        }
                                    }
                                    tempPreviewResult.remove(tempRandomData);

                                    tempRandomData.getDataPosMap().put(z + "-" + result.size(), tempRandomData.getDataPos());
                                    Log.i((z + "-" + result.size()) + "   " + Arrays.toString(tempRandomData.getDataPos()));
                                    tempRandomData.setDataPos(new byte[tempRandomData.getDataPos().length]);
                                    result.add(tempRandomData);
                                }
                                if (ids.size() <= 0) {
                                    break;//实在是抽不出了
                                }
                                if (result.size() >= mCommand.getLines()) {//已经生成足够的行数了
                                    break;
                                }
                            }
                            Log.i("跳出第3个循环");
                        } else {
                            break;
                        }
                    }
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

    private boolean checkNotExistsOther(byte[] src, int skipSrcPos, byte[] des) {
        for (int i = 0; i < src.length; i++) {
            if (i == skipSrcPos) {
                continue;
            }
            for (int j = 0; j < des.length; j++) {
                if (src[i] == des[j]) {
                    return false;
                }
            }
        }
        return true;
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
