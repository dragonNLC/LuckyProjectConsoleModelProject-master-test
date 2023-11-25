package com.dragon.lucky.command26;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GenerateRunner {

    private static GenerateRunner sInstance;


    private GenerateRunner() {

    }

    public static GenerateRunner getInstance() {
        if (sInstance == null) {
            synchronized (GenerateRunner.class) {
                if (sInstance == null) {
                    sInstance = new GenerateRunner();
                }
            }
        }
        return sInstance;
    }

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        List<String> inputData = FileReadHelper.readFile(command.getInputPath());
        List<NumberDataBean> previewResult = new ArrayList<>();
        for (int i = 0; i < inputData.size(); i++) {
            String d = inputData.get(i);
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

        HashMap<String, AssistContentBean> map = new HashMap<>();
        for (int i = 1; i < 36; i++) {
            boolean checkState = false;
            if (command.getCheckData() != null) {
                for (int j = 0; j < command.getCheckData().length; j++) {
                    if (command.getCheckData()[j] == (byte)i) {
                        checkState = true;
                    }
                }
            }
            map.put(String.valueOf(i), new AssistContentBean(String.valueOf(i), 0, checkState));
        }
        for (int i = 0; i < previewResult.size(); i++) {
            NumberDataBean ndb = previewResult.get(i);
            for (int j = 0; j < ndb.getData().length; j++) {
                String key = String.valueOf(ndb.getData()[j]);
                if (!map.containsKey(key)) {
                    boolean checkState = false;
                    if (command.getCheckData() != null) {
                        for (int x = 0; x < command.getCheckData().length; x++) {
                            if (command.getCheckData()[x] == ndb.getData()[j]) {
                                checkState = true;
                            }
                        }
                    }
                    map.put(key, new AssistContentBean(key, 0, checkState));
                }
                map.get(key).setCount(map.get(key).getCount() + 1);
            }
        }
        List<AssistContentBean> result = new ArrayList<>();
        for (String key :
                map.keySet()) {
            result.add(map.get(key));
        }

        result.sort(new Comparator<AssistContentBean>() {
            @Override
            public int compare(AssistContentBean o1, AssistContentBean o2) {
                return Integer.compare(Integer.parseInt(o1.getTag()), Integer.parseInt(o2.getTag()));
            }
        });
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.size(); i++) {
            AssistContentBean acb = result.get(i);
            sb.append(acb.getTag());
            sb.append("-");
            sb.append(acb.getCount());
            sb.append("\n");
        }
        if (!Utils.isEmpty(command.getOutputPath())) {
            FileReadHelper.writeToFile(command.getOutputPath(), sb.toString());
        }
        OnceGenerateBean onceGenerateBean = new OnceGenerateBean();
        onceGenerateBean.setTitle(new File(command.getInputPath()).getName().split("\\.")[0]);
        onceGenerateBean.setData(result);
        Log.i("本次处理完成");
        if (callbackListener != null) {
            callbackListener.onCompile(onceGenerateBean);
        }
    }

    private String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return sdf.format(new Date());
    }

}
