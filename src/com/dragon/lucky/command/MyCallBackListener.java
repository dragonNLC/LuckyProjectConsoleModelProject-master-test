package com.dragon.lucky.command;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultMergeBean;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyCallBackListener implements CallbackListener {

    private CopyOnWriteArrayList<String> threadName;
    private CopyOnWriteArrayList<RunThread> threads;
    private int threadCount;

    public MyCallBackListener() {
        threadName = new CopyOnWriteArrayList<>();
        threads = new CopyOnWriteArrayList<>();
    }

    @Override
    public synchronized void onThreadCompile(String name, RunThread thread) {
//            Log.i("onThreadCompile");
        threadName.remove(name);
        threads.add(thread);
        List<ResultBean> result = new ArrayList<>();
        if (threadName.size() <= 0) {
            if (threads.size() > 1) {
                //完成了，开始求并集
                Log.i("正在求并集...");

                for (int i = 0; i < threads.size(); i++) {
                    //折半运算
                    List<ResultMergeBean> data = threads.get(i).mergeBeans;
                    List<CutResultBean2> cutResultBeans = new ArrayList<>();
                    int size = data.size() / 8;
                    for (int j = 0; j < 8; j++) {
                        /*if (i == 0) {
                            CutResultBean2 cutBean = new CutResultBean2(data);
                            cutResultBeans.add(cutBean);
                        } else {
                            CutResultBean2 cutBean = new CutResultBean2(data.subList(j * size, Math.min((j + 1) * size, data.size())));
                            cutResultBeans.add(cutBean);
                        }*/
                    }
                    threads.get(i).cutResultBeans2 = cutResultBeans;
                }

                for (int i = 0; i < threads.size(); i += 2) {
                    List<CutResultBean2> cutResultBeans = threads.get(i).cutResultBeans2;
                    List<CutResultBean2> cutResultBeans2 = threads.get(i + 1).cutResultBeans2;
                    for (int j = 0; j < cutResultBeans.size(); j++) {
                        threadCount++;
//                        new ContainThread(cutResultBeans.get(j).data, cutResultBeans2.get(j).data).start();
                    }
                }
            } else {
                result.addAll(threads.get(0).resultBeans);
//                try {

//                    FileReadHelper.writeToFile(outPath, result);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                Log.i(threads.size() + " 处理完成：结果数-" + result.size());
            }
        }
    }

    @Override
    public void addThread(String name) {
//            Log.i("addThread");
        threadName.add(name);
    }

}
