package com.dragon.lucky.command7;

import com.dragon.lucky.bean.BaseControlNumberBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DivideThread extends Thread {

    List<ResultBean> inputData;
    public List<ResultBean> result;
    public GenerateRunner mGenerateRunner;
    private int generateSize;
    private int mergeCount;

    public DivideThread(List<ResultBean> inputData, GenerateRunner generateRunner, int generateSize, int mergeCount) {
        this.inputData = inputData;
        this.mGenerateRunner = generateRunner;
        this.generateSize = generateSize;
        this.mergeCount = mergeCount;
        this.result = new ArrayList<>();
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < inputData.size(); i++) {
            List<ResultBean> tempResult = new ArrayList<>();
            ResultBean rb = inputData.get(i);
            int[] resultArr = new int[generateSize];
            List<Byte> byteData = new ArrayList<>();
            for (int j = 0; j < rb.getData().length; j++) {
                byteData.add(rb.getData()[j]);
            }
//            Log.i(getName() + "----start----" + System.currentTimeMillis());
            Utils.combine_increase(byteData, 0, resultArr, generateSize, generateSize, byteData.size(), tempResult, new long[1], mergeCount);
//            Log.i(getName() + "----second----" + System.currentTimeMillis());
            if (result.size() == 0) {
                for (int j = 0; j < tempResult.size(); j++) {
                    tempResult.get(j).setCollectCount(1);
                }
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
                            temp2.setCollectCount(temp2.getCollectCount() + 1);
                            break;
                        }
                    }
                    if (!exists) {
                        temp.setCollectCount(1);
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }
//            Log.i(getName() + "----Third----" + System.currentTimeMillis());
        }
        Log.i(System.currentTimeMillis() + "---sort");
        result.sort((o1, o2) -> Integer.compare(o2.getCollectCount(), o1.getCollectCount()));
        Log.i(System.currentTimeMillis() + "---sort" + result.size());
        if (mGenerateRunner != null) {
            mGenerateRunner.onThreadCompile2(currentThread().getName(), this);
        }
    }

}
