package com.dragon.lucky.command8;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;

import java.io.IOException;
import java.util.*;

public class RunThread extends Thread {

    private GenerateBaseDataBean mData;
    public List<GenerateBaseDataBean.BaseData> mResultBean;
    public GenerateRunner mGenerateRunner;
    private String mPath;

    public RunThread(GenerateBaseDataBean data, GenerateRunner generateRunner, String path) {
        this.mData = data;
        this.mGenerateRunner = generateRunner;
        this.mPath = path;
        mResultBean = new ArrayList<>();
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < mData.getFilterData().size(); i++) {
            GenerateBaseDataBean.FilterData data = mData.getFilterData().get(i);
            for (int j = 0; j < mData.getData().size(); j++) {
                GenerateBaseDataBean.BaseData baseData = mData.getData().get(j);
                if (baseData.getCount() >= data.getStart() && baseData.getCount() < data.getEnd() + 1) {
                    data.getBaseData().add(baseData);
                    baseData.setTarget(true);
                }
            }
        }
        for (int i = 0; i < mData.getFilterData().size(); i++) {
            GenerateBaseDataBean.FilterData filterData = mData.getFilterData().get(i);
            mResultBean.addAll(filterData.getBaseData());//得到筛选结果
        }
        Log.i("去除条件结果：" + mResultBean.size());
        if (mGenerateRunner != null) {
            mGenerateRunner.onThreadCompile(currentThread().getName(), this);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mData.getData().size(); i++) {
            GenerateBaseDataBean.BaseData baseData = mData.getData().get(i);
            if (!baseData.isTarget()) {
                sb.append(Arrays.toString(baseData.getData()));
                sb.append("\n");
            }
        }
        try {
            FileReadHelper.writeToFile(mPath, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
