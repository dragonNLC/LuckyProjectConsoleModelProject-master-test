package com.dragon.lucky.command6;


import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.Log;

import java.util.ArrayList;
import java.util.List;

//求并集的类
public class ContainThread extends Thread {

    private GenerateRunner mRunner;
    private int start;
    private int end;
    private List<ResultMergeBean> content1;
    private int start2;
    private int end2;
    private List<ResultMergeBean> content2;
    private List<ResultBean> allData;

    public ContainThread(GenerateRunner runner, List<ResultBean> allData, List<ResultMergeBean> content1, int start, int end, List<ResultMergeBean> content2, int start2, int end2) {
        this.mRunner = runner;
        this.allData = allData;
        this.content1 = content1;
        this.start = start;
        this.end = end;
        this.content2 = content2;
        this.start2 = start2;
        this.end2 = end2;
    }

    @Override
    public void run() {
        super.run();
//        Log.i("content1 = " + content1.size());
//        Log.i("content2 = " + content2.size());
//        Log.i(getName() + " = " + start + "   " + end + "   " + start2 + "   " + end2);
        List<Integer> result = new ArrayList<>(FilterUtils.getContains2(getName(), allData, content1, start, end, content2, start2, end2));
        /*try {
            FileReadHelper.writeToFile("H:\\1\\" + getName() + ".txt", content1);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        mRunner.containCompileData(result);
    }

}