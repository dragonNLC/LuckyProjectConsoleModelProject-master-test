package com.dragon.lucky.command17;

import com.dragon.lucky.bean.ContentBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.command9.CollectBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;

    private CallbackListener mCallBackListener;

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
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        List<String> previewData = FileReadHelper.readFile(command.getInputPath());
        List<NumberDataBean> previewResult = new ArrayList<>();
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
                byte[] numbers = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        numbers[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                previewResult.add(new NumberDataBean(numbers));
            }
        }
        byte[] heads = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        byte[] centers = new byte[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        byte[] foots = new byte[]{25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
        //开始统计
        List<NumberDataBean> data7Place = new ArrayList<>();
        List<NumberDataBean> data6Place = new ArrayList<>();
        List<NumberDataBean> data5Place = new ArrayList<>();

        for (int j = 0; j < previewResult.size(); j++) {
            NumberDataBean numbers = previewResult.get(j);
            for (int k = 0; k < numbers.getData().length; k++) {
                for (int l = 0; l < heads.length; l++) {
                    if (numbers.getData()[k] == heads[l]) {
                        numbers.setHeadSize(numbers.getHeadSize() + 1);
                    }
                }
                for (int l = 0; l < centers.length; l++) {
                    if (numbers.getData()[k] == centers[l]) {
                        numbers.setCenterSize(numbers.getCenterSize() + 1);
                    }
                }
                for (int l = 0; l < foots.length; l++) {
                    if (numbers.getData()[k] == foots[l]) {
                        numbers.setFootSize(numbers.getFootSize() + 1);
                    }
                }
            }
            if (numbers.getData().length == 7) {
                data7Place.add(numbers);
            }
            if (numbers.getData().length == 6) {
                data6Place.add(numbers);
            }
            if (numbers.getData().length == 5) {
                data5Place.add(numbers);
            }
        }
//        Log.i("7位数据数量：" + data7Place.size());

        Map<String, PartitionBean> collect7PlaceMap = new HashMap<>();
        collect7PlaceMap.put("0:0:7", new PartitionBean());
        collect7PlaceMap.put("0:1:6", new PartitionBean());
        collect7PlaceMap.put("0:2:5", new PartitionBean());
        collect7PlaceMap.put("0:3:4", new PartitionBean());
        collect7PlaceMap.put("0:4:3", new PartitionBean());
        collect7PlaceMap.put("0:5:2", new PartitionBean());
        collect7PlaceMap.put("0:6:1", new PartitionBean());
        collect7PlaceMap.put("0:7:0", new PartitionBean());
        collect7PlaceMap.put("1:0:6", new PartitionBean());
        collect7PlaceMap.put("1:1:5", new PartitionBean());
        collect7PlaceMap.put("1:2:4", new PartitionBean());
        collect7PlaceMap.put("1:3:3", new PartitionBean());
        collect7PlaceMap.put("1:4:2", new PartitionBean());
        collect7PlaceMap.put("1:5:1", new PartitionBean());
        collect7PlaceMap.put("1:6:0", new PartitionBean());
        collect7PlaceMap.put("2:0:5", new PartitionBean());
        collect7PlaceMap.put("2:1:4", new PartitionBean());
        collect7PlaceMap.put("2:2:3", new PartitionBean());
        collect7PlaceMap.put("2:3:2", new PartitionBean());
        collect7PlaceMap.put("2:4:1", new PartitionBean());
        collect7PlaceMap.put("2:5:0", new PartitionBean());
        collect7PlaceMap.put("3:0:4", new PartitionBean());
        collect7PlaceMap.put("3:1:3", new PartitionBean());
        collect7PlaceMap.put("3:2:2", new PartitionBean());
        collect7PlaceMap.put("3:3:1", new PartitionBean());
        collect7PlaceMap.put("3:4:0", new PartitionBean());
        collect7PlaceMap.put("4:0:3", new PartitionBean());
        collect7PlaceMap.put("4:1:2", new PartitionBean());
        collect7PlaceMap.put("4:2:1", new PartitionBean());
        collect7PlaceMap.put("4:3:0", new PartitionBean());
        collect7PlaceMap.put("5:0:2", new PartitionBean());
        collect7PlaceMap.put("5:1:1", new PartitionBean());
        collect7PlaceMap.put("5:2:0", new PartitionBean());
        collect7PlaceMap.put("6:1:0", new PartitionBean());
        collect7PlaceMap.put("6:0:1", new PartitionBean());
        collect7PlaceMap.put("7:0:0", new PartitionBean());
        for (int i = 0; i < data7Place.size(); i++) {
//            Log.i(data7Place.get(i).getHeadSize()  + ":" + data7Place.get(i).getCenterSize()  + ":" + data7Place.get(i).getFootSize());
            PartitionBean partitionBean = collect7PlaceMap.get(data7Place.get(i).getHeadSize()  + ":" + data7Place.get(i).getCenterSize()  + ":" + data7Place.get(i).getFootSize());
            partitionBean.setCount(partitionBean.getCount() + 1);
            int zx = printSingleDataIdx(data7Place.get(i));
            if (zx == 1) {
                partitionBean.setZ1(partitionBean.getZ1() + 1);
            } else if (zx == 2) {
                partitionBean.setZ2(partitionBean.getZ2() + 1);
            } else if (zx == 3) {
                partitionBean.setZ3(partitionBean.getZ3() + 1);
            } else if (zx == 4) {
                partitionBean.setZ4(partitionBean.getZ4() + 1);
            } else if (zx == 5) {
                partitionBean.setZ5(partitionBean.getZ5() + 1);
            }
        }

        Map<String, PartitionBean> collect6PlaceMap = new HashMap<>();
        collect6PlaceMap.put("0:0:6", new PartitionBean());
        collect6PlaceMap.put("0:1:5", new PartitionBean());
        collect6PlaceMap.put("0:2:4", new PartitionBean());
        collect6PlaceMap.put("0:3:3", new PartitionBean());
        collect6PlaceMap.put("0:4:2", new PartitionBean());
        collect6PlaceMap.put("0:5:1", new PartitionBean());
        collect6PlaceMap.put("0:6:0", new PartitionBean());
        collect6PlaceMap.put("1:0:5", new PartitionBean());
        collect6PlaceMap.put("1:1:4", new PartitionBean());
        collect6PlaceMap.put("1:2:3", new PartitionBean());
        collect6PlaceMap.put("1:3:2", new PartitionBean());
        collect6PlaceMap.put("1:4:1", new PartitionBean());
        collect6PlaceMap.put("1:5:0", new PartitionBean());
        collect6PlaceMap.put("2:0:4", new PartitionBean());
        collect6PlaceMap.put("2:1:3", new PartitionBean());
        collect6PlaceMap.put("2:2:2", new PartitionBean());
        collect6PlaceMap.put("2:3:1", new PartitionBean());
        collect6PlaceMap.put("2:4:0", new PartitionBean());
        collect6PlaceMap.put("3:0:3", new PartitionBean());
        collect6PlaceMap.put("3:1:2", new PartitionBean());
        collect6PlaceMap.put("3:2:1", new PartitionBean());
        collect6PlaceMap.put("3:3:0", new PartitionBean());
        collect6PlaceMap.put("4:0:2", new PartitionBean());
        collect6PlaceMap.put("4:1:1", new PartitionBean());
        collect6PlaceMap.put("4:2:0", new PartitionBean());
        collect6PlaceMap.put("5:0:1", new PartitionBean());
        collect6PlaceMap.put("5:1:0", new PartitionBean());
        collect6PlaceMap.put("6:0:0", new PartitionBean());
        for (int i = 0; i < data6Place.size(); i++) {
            PartitionBean partitionBean = collect6PlaceMap.get(data6Place.get(i).getHeadSize()  + ":" + data6Place.get(i).getCenterSize()  + ":" + data6Place.get(i).getFootSize());
            partitionBean.setCount(partitionBean.getCount() + 1);
            int zx = printSingleDataIdx(data6Place.get(i));
            if (zx == 1) {
                partitionBean.setZ1(partitionBean.getZ1() + 1);
            } else if (zx == 2) {
                partitionBean.setZ2(partitionBean.getZ2() + 1);
            } else if (zx == 3) {
                partitionBean.setZ3(partitionBean.getZ3() + 1);
            } else if (zx == 4) {
                partitionBean.setZ4(partitionBean.getZ4() + 1);
            } else if (zx == 5) {
                partitionBean.setZ5(partitionBean.getZ5() + 1);
            }
        }


        Map<String, PartitionBean> collect5PlaceMap = new HashMap<>();
        collect5PlaceMap.put("0:0:5", new PartitionBean());
        collect5PlaceMap.put("0:1:4", new PartitionBean());
        collect5PlaceMap.put("0:2:3", new PartitionBean());
        collect5PlaceMap.put("0:3:2", new PartitionBean());
        collect5PlaceMap.put("0:4:1", new PartitionBean());
        collect5PlaceMap.put("0:5:0", new PartitionBean());
        collect5PlaceMap.put("1:0:4", new PartitionBean());
        collect5PlaceMap.put("1:1:3", new PartitionBean());
        collect5PlaceMap.put("1:2:2", new PartitionBean());
        collect5PlaceMap.put("1:3:1", new PartitionBean());
        collect5PlaceMap.put("1:4:0", new PartitionBean());
        collect5PlaceMap.put("2:0:3", new PartitionBean());
        collect5PlaceMap.put("2:1:2", new PartitionBean());
        collect5PlaceMap.put("2:2:1", new PartitionBean());
        collect5PlaceMap.put("2:3:0", new PartitionBean());
        collect5PlaceMap.put("3:0:2", new PartitionBean());
        collect5PlaceMap.put("3:1:1", new PartitionBean());
        collect5PlaceMap.put("3:2:0", new PartitionBean());
        collect5PlaceMap.put("4:0:1", new PartitionBean());
        collect5PlaceMap.put("5:0:0", new PartitionBean());
        for (int i = 0; i < data5Place.size(); i++) {
            PartitionBean partitionBean = collect5PlaceMap.get(data5Place.get(i).getHeadSize()  + ":" + data5Place.get(i).getCenterSize()  + ":" + data5Place.get(i).getFootSize());
            partitionBean.setCount(partitionBean.getCount() + 1);

            int zx = printSingleDataIdx(data5Place.get(i));
            if (zx == 1) {
                partitionBean.setZ1(partitionBean.getZ1() + 1);
            } else if (zx == 2) {
                partitionBean.setZ2(partitionBean.getZ2() + 1);
            } else if (zx == 3) {
                partitionBean.setZ3(partitionBean.getZ3() + 1);
            } else if (zx == 4) {
                partitionBean.setZ4(partitionBean.getZ4() + 1);
            } else if (zx == 5) {
                partitionBean.setZ5(partitionBean.getZ5() + 1);
            }
        }
        OnceGenerateBean onceGenerateBean = new OnceGenerateBean();
        onceGenerateBean.setData7Place(collect7PlaceMap);
        onceGenerateBean.setData6Place(collect6PlaceMap);
        onceGenerateBean.setData5Place(collect5PlaceMap);
        onceGenerateBean.setSize(previewResult.size());
        onceGenerateBean.setTitle(new File(mCommand.getInputPath()).getName().split("\\.")[0]);

        if (mCallBackListener != null) {
            mCallBackListener.onCompile(onceGenerateBean);
        }
    }


    private int printSingleDataIdx(NumberDataBean data) {
        int existsCount = 0;
        for (int k = 0; k < mCommand.getCheckData().length; k++) {
            for (int j = 0; j < data.getData().length; j++) {
                if (mCommand.getCheckData()[k] == data.getData()[j]) {
                    existsCount++;
                    data.getDataHitIdx()[j] = 1;
                    break;
                }
            }
        }
        return existsCount;
    }

}
