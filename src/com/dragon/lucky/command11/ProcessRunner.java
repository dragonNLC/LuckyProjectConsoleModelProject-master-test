package com.dragon.lucky.command11;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessRunner implements CallbackListener {

    public volatile static ProcessRunner sRunner;
    private List<String> mCommands;
    private int mCommandIdx;

    private ProcessRunner() {
        mCommandIdx = 0;
    }

    public static ProcessRunner getInstance() {
        if (sRunner == null) {
            synchronized (ProcessRunner.class) {
                sRunner = new ProcessRunner();
            }
        }
        return sRunner;
    }

    public void run(CommandBean commandBean) throws IOException {
        if (!Utils.isEmpty(CommandAnalysis.getInstance().getCommand().getCommandsPath())) {
            //读取批量条件
            if (!Utils.isEmpty(commandBean.getCommandsPath())) {
                if (!FileReadHelper.checkFileExists(commandBean.getCommandsPath())) {
                    Log.i("预加载文件不存在");
                    return;
                }
                mCommands = FileReadHelper.readFile(commandBean.getCommandsPath());
                CommandBean singleCommand = new CommandBean();
                String[] commandSplit = mCommands.get(mCommandIdx).split(" ");
                CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
                new Thread(() -> {
                    try {
                        GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                mCommandIdx++;
                Log.i("开始执行第：" + mCommandIdx + "条命令。");
            }
        } else {
            GenerateRunner.getInstance().run(commandBean, null);
        }
    }

    @Override
    public void onCompile() {
        System.gc();
        if (mCommandIdx < mCommands.size()) {
            CommandBean singleCommand = new CommandBean();
            String[] commandSplit = mCommands.get(mCommandIdx).split(" ");
            CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
            new Thread(() -> {
                try {
                    GenerateRunner.getInstance().run(singleCommand, ProcessRunner.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            mCommandIdx++;
            Log.i("开始执行第：" + mCommandIdx + "条命令。");
        } else {
            Log.i("开始求所有输入数的结果的交集");
            getContain();
        }
    }

    public void getContain() {
        if (!Utils.isEmpty(CommandAnalysis.getInstance().getCommand().getFinalFilePath())) {
            //开始求相同数，并得出最终不会出现的数
            List<List<String>> dataIdxAll = new ArrayList<>();
            for (int i = 0; i < mCommands.size(); i++) {
                String cmd = mCommands.get(i);
                CommandBean singleCommand = new CommandBean();
                String[] commandSplit = cmd.split(" ");
                CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
                try {
                    List<String> dataIdx = FileReadHelper.readFile(singleCommand.getOutputPath());
                    //拿到数据，求相同数
                    dataIdxAll.add(dataIdx);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<String> finalDataIdx = new ArrayList<>();
            for (int i = 1; i < dataIdxAll.size() - 1; i++) {
                if (i == 1) {
                    List<String> d1 = dataIdxAll.get(i - 1);
                    List<String> d2 = dataIdxAll.get(i);
                    for (int j = 0; j < d1.size(); j++) {
                        for (int k = 0; k < d2.size(); k++) {
                            if (d1.get(j).contains(d2.get(k))) {
                                finalDataIdx.add(d1.get(j));
                                Log.i("第“" + d1.get(j) + "”位一个数都没出现！");
                                break;
                            }
                        }
                    }
                } else {
                    List<String> d1 = finalDataIdx;
                    List<String> d2 = dataIdxAll.get(i);
                    List<String> tempFinalDataIdx = new ArrayList<>();
                    for (int j = 0; j < d1.size(); j++) {
                        for (int k = 0; k < d2.size(); k++) {
                            if (d1.get(j).contains(d2.get(k))) {
                                tempFinalDataIdx.add(d1.get(j));
                                Log.i("第“" + d1.get(j) + "”位一个数都没出现！");
                                break;
                            }
                        }
                    }
                    finalDataIdx.clear();
                    finalDataIdx.addAll(tempFinalDataIdx);
                }
                Log.i("输入数的结果的交集数量为：" + finalDataIdx.size());
            }
            Log.i("所有输入数的结果的交集数量为：" + finalDataIdx.size());
            //拿到相同的不存在的数了，跟导入的数据进行比对
            List<String> previewData = null;
            try {
                previewData = FileReadHelper.readFile(CommandAnalysis.getInstance().getCommand().getFinalFilePath());
                List<ResultBean> previewResult = new ArrayList<>();
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
                        byte[] splitLineInt = new byte[splitLine.length];
                        for (int j = 0; j < splitLine.length; j++) {
                            if (Utils.isNumeric(splitLine[j])) {
                                splitLineInt[j] = Byte.parseByte(splitLine[j]);
                            }
                        }
                        previewResult.add(new ResultBean(splitLineInt, i, 1));
                    }
                }
                List<ResultBean> print = new ArrayList<>();
                for (int i = 0; i < finalDataIdx.size(); i++) {
                    String idx = finalDataIdx.get(i);
                    int id = Integer.parseInt(idx);
                    if (id < previewResult.size()) {
                        print.add(previewResult.get(id));
                    }
                }
                FileReadHelper.writeToFile(CommandAnalysis.getInstance().getCommand().getFinalOutputFilePath(), print);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Log.i("处理完毕");
            }
        } else {
            Log.i("未输入对比的基础数据，处理结束");
        }
    }

}
