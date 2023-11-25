package com.dragon.lucky.command13;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            List<List<NotExistsBean>> dataIdxAll = new ArrayList<>();
            for (int i = 0; i < mCommands.size(); i++) {
                String cmd = mCommands.get(i);
                CommandBean singleCommand = new CommandBean();
                String[] commandSplit = cmd.split(" ");
                CommandAnalysis.getInstance().analysisCommand(commandSplit, singleCommand);
                try {
                    List<String> dataIdx = FileReadHelper.readFile(singleCommand.getOutputPath());
                    List<NotExistsBean> notExistsBeans = new ArrayList<>();
                    for (int j = 0; j < dataIdx.size(); j++) {
                        String d = dataIdx.get(j);
                        String[] dd = d.split("-");
                        if (dd.length == 2) {
                            int line = Integer.parseInt(dd[0]);
                            String positionsStr = dd[1].replace("[", "").replace("]", "").replace(" ", "");
                            String[] splitPositions = positionsStr.split(",");
                            NotExistsBean nsb = new NotExistsBean();
                            nsb.setLine(line);
                            for (int k = 0; k < splitPositions.length; k++) {
                                nsb.addPosition(Integer.parseInt(splitPositions[k]));
                            }
                            notExistsBeans.add(nsb);
                        }
                    }
                    //拿到数据，求相同数
                    dataIdxAll.add(notExistsBeans);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<NotExistsBean> finalDataIdx = new ArrayList<>();
            for (int i = 1; i < dataIdxAll.size() - 1; i++) {
                if (i == 1) {
                    List<NotExistsBean> d1 = dataIdxAll.get(i - 1);
                    List<NotExistsBean> d2 = dataIdxAll.get(i);
                    for (int j = 0; j < d1.size(); j++) {
                        for (int k = 0; k < d2.size(); k++) {
                            if (d1.get(j).getLine() == d2.get(k).getLine()) {
//                                finalDataIdx.add(d1.get(j));
                                NotExistsBean nb = new NotExistsBean();
                                Log.i("第“" + d1.get(j).getLine() + "”位一个数都没出现！");
                                for (int l = 0; l < d1.get(j).getPositions().size(); l++) {
                                    if (d2.get(k).getPositions().contains(d1.get(j).getPosition(l))) {
                                        nb.addPosition(d1.get(j).getPosition(l));
                                    }
                                }
                                if (nb.getPositions().size() > 0) {
                                    nb.setLine(d1.get(j).getLine());
                                    finalDataIdx.add(nb);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    List<NotExistsBean> d1 = finalDataIdx;
                    List<NotExistsBean> d2 = dataIdxAll.get(i);
                    List<NotExistsBean> tempFinalDataIdx = new ArrayList<>();
                    for (int j = 0; j < d1.size(); j++) {
                        for (int k = 0; k < d2.size(); k++) {
                            if (d1.get(j).getLine() == d2.get(k).getLine()) {
//                                finalDataIdx.add(d1.get(j));
                                NotExistsBean nb = new NotExistsBean();
                                Log.i("第“" + d1.get(j).getLine() + "”行");
                                for (int l = 0; l < d1.get(j).getPositions().size(); l++) {
                                    if (d2.get(k).getPositions().contains(d1.get(j).getPosition(l))) {
                                        Log.i("第“" + d1.get(j).getLine() + "”行");
                                        nb.addPosition(d1.get(j).getPosition(l));
                                    }
                                }
                                if (nb.getPositions().size() > 0) {
                                    nb.setLine(d1.get(j).getLine());
                                    tempFinalDataIdx.add(nb);
                                }
                                break;
                            }
                        }
                    }
                    finalDataIdx.clear();
                    finalDataIdx.addAll(tempFinalDataIdx);
                }
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
//                    String line = d.replace("[", "").replace("]", "");
                    String line = "";
                    if (d.contains("],[")) {
                        String[] content = d.split("],\\[");
                        line = content[0].replace("[", "").replace("]", "");
                        Log.i("line1 = " + line);
                    } else {
                        line = d.replace("[", "").replace("]", "");
                        Log.i("line2 = " + line);
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
                Map<Byte, Integer> allCollectCount = new HashMap<>();
                for (int i = 0; i < previewResult.size(); i++) {
                    ResultBean rb = previewResult.get(i);
                    for (int j = 0; j < rb.getData().length; j++) {
                        if (!allCollectCount.containsKey(rb.getData()[j])) {
                            allCollectCount.put(rb.getData()[j], 0);
//                            Log.i("rb.getData()[j] = " + rb.getData()[j]);
                        }
                        int count = allCollectCount.get(rb.getData()[j]);
                        allCollectCount.put(rb.getData()[j], count + 1);
                    }
                }

                Map<Byte, Integer> collectCount = new HashMap<>();
                for (int i = 0; i < finalDataIdx.size(); i++) {
                    NotExistsBean idx = finalDataIdx.get(i);
                    if (idx.getLine() < previewResult.size()) {
                        ResultBean rb = previewResult.get(idx.getLine());
                        for (int j = 0; j < idx.getPositions().size(); j++) {
                            int p = idx.getPosition(j);
                            if (p < rb.getData().length) {
                                if (!collectCount.containsKey(rb.getData()[p])) {
                                    collectCount.put(rb.getData()[p], 0);
                                    allCollectCount.remove(rb.getData()[p]);//如果反指里面已经有这个了，那么就要在非反指里面剔除掉
                                }
                                int count = collectCount.get(rb.getData()[p]);
                                collectCount.put(rb.getData()[p], count + 1);
                            }
                        }
                    }
                }
                StringBuilder printContent = new StringBuilder();
                printContent.append("反指数量：").append(collectCount.size());
                printContent.append("\n");
                for (Byte key :
                        collectCount.keySet()) {
                    Log.i("key = " + key);
                    printContent.append(key);
                    printContent.append("-");
                    printContent.append(collectCount.get(key));
                    printContent.append("\n");
                }
                printContent.append("剩余数量：").append(allCollectCount.size());
                printContent.append("\n");
                for (Byte key :
                        allCollectCount.keySet()) {
                    printContent.append(key);
                    printContent.append("-");
                    printContent.append(allCollectCount.get(key));
                    printContent.append("\n");
                }
                FileReadHelper.writeToFile(CommandAnalysis.getInstance().getCommand().getFinalOutputFilePath(), printContent.toString());
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
