package com.dragon.lucky.command;

import com.dragon.lucky.Main3;
import com.dragon.lucky.bean.BaseControlNumberBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultMergeBean;
import com.dragon.lucky.bean.cut.CutResultBean;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;

import java.io.IOException;
import java.util.*;

public class RunThread extends Thread {

    List<ResultBean> result;
    List<BaseControlNumberBean> controlBean;
    public List<ResultBean> resultBeans = new ArrayList<>();
    public List<CutResultBean> cutResultBeans = new ArrayList<>();
    public List<CutResultBean2> cutResultBeans2 = new ArrayList<>();
    public List<ResultMergeBean> mergeBeans = new ArrayList<>();
    public GenerateRunner mGenerateRunner;

    public RunThread(List<ResultBean> resultBeans, List<BaseControlNumberBean> controlBean, GenerateRunner generateRunner) {
        this.result = resultBeans;
        this.controlBean = controlBean;
        this.mGenerateRunner = generateRunner;
    }

    @Override
    public void run() {
        super.run();
        Log.i("线程：" + currentThread().getName());
        FilterUtils.filterResultBean(result, controlBean, 0, resultBeans);
        resultBeans.sort(Comparator.comparingLong(ResultBean::getId));
        /*try {
            FileReadHelper.writeToFile("H:\\1\\" + getName() + ".txt", resultBeans);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        mergeBeans = FilterUtils.collectMergeBean(resultBeans);

        Log.i("线程结束：" + currentThread().getName() + "   总数量：  " + resultBeans.size() + "  合并后的数量：" + mergeBeans.size());
        if (mGenerateRunner != null) {
            mGenerateRunner.onThreadCompile(currentThread().getName(), this);
        }
    }

}
