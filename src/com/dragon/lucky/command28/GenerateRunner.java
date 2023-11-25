package com.dragon.lucky.command28;

import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

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

    public void run(CommandBean command) throws IOException {
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        Map<String, CollectBean> collect = new HashMap<>();

        List<String> inputData = FileReadHelper.readFile(command.getInputPath());

        for (int k = 0; k < inputData.size(); k++) {
            List<String> inputData2 = FileReadHelper.readFile(inputData.get(k));
            List<NumberDataBean> previewResult = new ArrayList<>();
            for (int i = 0; i < inputData2.size(); i++) {
                String d = inputData2.get(i);
                if (!d.contains("[") || !d.contains("]")) {
                    continue;
                }
                String line = "";
                String line2 = "0";
                if (d.contains("],[")) {
                    String[] content = d.split("],\\[");
                    line = content[0].replace("[", "").replace("]", "");
                    line2 = content[1].replace("]", "");
                } else {
                    line = d.replace("[", "").replace("]", "");
                }
                String[] splitLine = line.split(", ");
                if (splitLine != null && splitLine.length > 0) {
                    byte[] numbers = new byte[splitLine.length];
                    for (int j = 0; j < splitLine.length; j++) {
                        if (Utils.isNumeric(splitLine[j])) {
                            numbers[j] = Byte.parseByte(splitLine[j]);
                        }
                    }
                    previewResult.add(new NumberDataBean(numbers, Integer.parseInt(line2)));
                }
            }
            previewResult.sort(new Comparator<NumberDataBean>() {
                @Override
                public int compare(NumberDataBean o1, NumberDataBean o2) {
                    return Integer.compare(o1.getDataCount(), o2.getDataCount());
                }
            });
            for (int i = 0; i < previewResult.size(); i++) {
                NumberDataBean ndb = previewResult.get(i);
                if (!collect.containsKey(Arrays.toString(ndb.getData()))) {
                    collect.put(Arrays.toString(ndb.getData()), new CollectBean(Arrays.toString(ndb.getData()), 0));
                }
                CollectBean cb = collect.get(Arrays.toString(ndb.getData()));
                cb.count = cb.count + 1;
                cb.files.add(inputData.get(k));
                int tag = 0;
                if (i <= 4) {
                    tag = 1;
                } else if (i >= previewResult.size() - 5) {
                    tag = 2;
                } else {
                    if (ndb.getDataCount() >= previewResult.get(previewResult.size() - 5).getDataCount()) {
                        tag = 2;
                    } else if (ndb.getDataCount() <= previewResult.get(4).getDataCount()) {
                        tag = 1;
                    }
                }
                cb.orm.put(inputData.get(k), new CollectBean.CollectOrderTagBean(ndb.getDataCount(), tag));
            }
        }

        List<CollectBean> collectBeans = new ArrayList<>();
        for (String key :
                collect.keySet()) {
            collectBeans.add(collect.get(key));
        }

        collectBeans.sort(new Comparator<CollectBean>() {
            @Override
            public int compare(CollectBean o1, CollectBean o2) {
                return Integer.compare(o2.count, o1.count);
            }
        });
        POIExcelUtils.output(command.getOutputPath(), collectBeans, inputData);
//        FileReadHelper.writeToFileForRun28(command.getOutputPath(), collectBeans);
        Log.i("输出完成！");
    }

}
