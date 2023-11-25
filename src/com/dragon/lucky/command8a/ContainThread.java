package com.dragon.lucky.command8a;


import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.command8.GenerateBaseDataBean;
import com.dragon.lucky.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//求并集的类
public class ContainThread extends Thread {

    private GenerateRunner mRunner;
    private List<GenerateBaseDataBean.BaseData> content1;
    private List<GenerateBaseDataBean.BaseData> content2;

    public ContainThread(GenerateRunner runner, List<GenerateBaseDataBean.BaseData> content1, List<GenerateBaseDataBean.BaseData> content2) {
        this.mRunner = runner;
        this.content1 = content1;
        this.content2 = content2;
        Log.i("content1 = " + content1.size());
        Log.i("content2 = " + content2.size());
    }

    @Override
    public void run() {
        super.run();
        mRunner.containCompileData(getContain(content1, content2));
    }

    public static List<GenerateBaseDataBean.BaseData> getContain(List<GenerateBaseDataBean.BaseData> content1, List<GenerateBaseDataBean.BaseData> content2) {
        List<GenerateBaseDataBean.BaseData> resultData = new ArrayList<>();
        for (int i = 0; i < content1.size(); i++) {
            GenerateBaseDataBean.BaseData children = content1.get(i);
            for (int j = 0; j < content2.size(); j++) {
                GenerateBaseDataBean.BaseData children2 = content2.get(j);
//                if (Arrays.equals(children.getData(), children2.getData())) {
                if (children.getId() == children2.getId()) {
                    resultData.add(children);
                }
            }
        }
        return resultData;
    }

    //求差集
    public static List<GenerateBaseDataBean.BaseData> getNotContain(List<GenerateBaseDataBean.BaseData> content1, List<GenerateBaseDataBean.BaseData> content2) {
        List<GenerateBaseDataBean.BaseData> resultData = new ArrayList<>();
        for (int i = 0; i < content1.size(); i++) {
            GenerateBaseDataBean.BaseData children = content1.get(i);
            boolean exists = false;
            for (int j = 0; j < content2.size(); j++) {
                GenerateBaseDataBean.BaseData children2 = content1.get(j);
//                if (children.getId() == children2.getId()) {
                if (Arrays.equals(children.getData(), children2.getData())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                resultData.add(children);
            }
        }
        return resultData;
    }

}