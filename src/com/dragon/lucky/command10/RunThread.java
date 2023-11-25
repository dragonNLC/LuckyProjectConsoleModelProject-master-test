package com.dragon.lucky.command10;

import com.dragon.lucky.bean.BaseControlNumberBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.utils.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class RunThread extends Thread {

    List<ResultBean> inputData;
    List<BaseControlNumberBean> controlBean;
    public List<Integer> resultBeans = new ArrayList<>();
    public List<CutResultBean2> cutResultBeans2 = new ArrayList<>();
    public List<ResultMergeBean> mergeBeans = new ArrayList<>();
    public GenerateRunner mGenerateRunner;

    public RunThread(List<ResultBean> inputData, List<BaseControlNumberBean> controlBean, GenerateRunner generateRunner) {
        this.inputData = inputData;
        this.controlBean = controlBean;
        this.mGenerateRunner = generateRunner;
    }

    @Override
    public void run() {
        super.run();
        Log.i("线程：" + currentThread().getName());
        FilterUtils.filterResultBean(inputData, controlBean, 0, null, resultBeans);
        HashSet<Integer> filterData = new HashSet<>(resultBeans);
        resultBeans.clear();
        resultBeans.addAll(filterData);
        resultBeans.sort(Integer::compareTo);
        /*try {
            FileReadHelper.writeToFile("H:\\1\\" + getName() + ".txt", resultBeans);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
//        for (int i = 0; i < resultBeans.size(); i++) {
//            Log.i(resultBeans.get(i) + "    33333333333333");
//        }
        mergeBeans = FilterUtils.collectMergeBean(inputData, resultBeans);

        Log.i("线程结束：" + currentThread().getName() + "   总数量：  " + resultBeans.size() + "  合并后的数量：" + mergeBeans.size());
        if (mGenerateRunner != null) {
            mGenerateRunner.onThreadCompile(currentThread().getName(), this);
        }
    }

}
