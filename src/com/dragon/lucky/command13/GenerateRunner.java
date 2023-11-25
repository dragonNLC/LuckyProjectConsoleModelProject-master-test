package com.dragon.lucky.command13;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;

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

    public static List<ResultBean> allGenerateData;

    private CallbackListener mCallBackListener;

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!Utils.isEmpty(command.getPreviewFilePath()) && !Utils.isEmpty(command.getInputPath())) {
            if (!FileReadHelper.checkFileExists(command.getPreviewFilePath())) {
                Log.i("预加载文件不存在");
                return;
            }
            if (!FileReadHelper.checkFileExists(command.getInputPath())) {
                Log.i("反指文件不存在");
                return;
            }
            List<String> previewData = FileReadHelper.readFile(command.getPreviewFilePath());
            List<NumberBean> previewResult = new ArrayList<>();
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
                    List<Integer> numbers = new ArrayList<>();
                    for (int j = 0; j < splitLine.length; j++) {
                        if (Utils.isNumeric(splitLine[j])) {
                            numbers.add(Integer.parseInt(splitLine[j]));
                        }
                    }
                    previewResult.add(new NumberBean(numbers));
                }
            }
            List<String> commandData = FileReadHelper.readFile(command.getInputPath());
            if (commandData.size() > 0) {
                String[] commandClips = null;
                if (commandData.get(0).contains("、")) {
                    commandClips = commandData.get(0).split("、");
                } else {
                    commandClips = commandData.get(0).split(",");
                }
//                Log.i("commandClips.length " + commandClips.length);
                int[] filterNumbers = new int[commandClips.length];
                for (int i = 0; i < commandClips.length; i++) {
                    filterNumbers[i] = Integer.parseInt(commandClips[i]);
                }
                for (int i = 0; i < previewResult.size(); i++) {
                    NumberBean rb = previewResult.get(i);
                    for (int j = 0; j < filterNumbers.length; j++) {
                        if (rb.getData().contains(filterNumbers[j])) {
                            rb.getData().remove((Integer) filterNumbers[j]);
                            rb.resetDataStr();
                        }
                    }
                }
                previewResult.removeIf(num -> num.getData().size() <= 0);
//                previewResult.sort((o1, o2) -> Integer.compare(o2.getData().size(), o1.getData().size()));
//                previewResult.sort(Comparator.comparing(NumberBean::getDataStr2));
                List<NumberBean> collectNumber2 = new ArrayList<>();
                //排序，将高位的排前面去
                //开始合并
                NumberBean.change = false;
                Log.i("开始合并！");
                HashSet<NumberBean> hashSet = new HashSet<>(previewResult);
                List<NumberBean> filterData = new ArrayList<>(hashSet);
                Log.i("开始获取需要的数！");
                for (int i = 0; i < filterData.size(); i++) {
                    if (!filterData.get(i).isChild()) {
                        collectNumber2.add(filterData.get(i));
                    }
                    if (i % 10000 == 0) {
//                        Log.i("i = " + i);
                    }
                }

                /*NumberBean.change = true;
                Log.i("开始合并2！");
                HashSet<NumberBean> hashSet2 = new HashSet<>(collectNumber);
                collectNumber.clear();
                List<NumberBean> filterData2 = new ArrayList<>(hashSet2);
                Log.i("开始获取需要的数！");
                for (int i = 0; i < filterData2.size(); i++) {
                    if (!filterData2.get(i).isChild()) {
                        collectNumber.add(filterData2.get(i));
                    }
                    if (i % 10000 == 0) {
//                        Log.i("i = " + i);
                    }
                }*/
                printData(collectNumber2);
            }
            return;
        }
    }

    private void printData(List<NumberBean> previewResult) {
        Log.i("剩余数：" + previewResult.size());
        Log.i("mCommand.getMinCount()：" + mCommand.getMinCount());
        Log.i("mCommand.getMaxCount()：" + mCommand.getMaxCount());
        Iterator<NumberBean> iterator = previewResult.iterator();
        while (iterator.hasNext()) {
//                        Log.i("处理下一个");
            NumberBean nb = iterator.next();
            if (mCommand.getMaxCount() > -1) {
                if (nb.getData().size() >= mCommand.getMinCount() && nb.getData().size() <= mCommand.getMaxCount()) {
//                    Log.i("nb.getData().size() = " + nb.getData().size());
//                    iterator.remove();
                } else {
                    nb.setRemove(true);
                }
            } else {
                if (nb.getData().size() < mCommand.getMinCount()) {
                    nb.setRemove(true);
//                    iterator.remove();
                }
            }
        }

        List<NumberBean> filterData = new ArrayList<>();
        Log.i("开始获取需要的数！");
        for (int i = 0; i < previewResult.size(); i++) {
            if (!previewResult.get(i).isRemove()) {
//                Log.i("previewResult.getData().size() = " + previewResult.get(i).getData().size());
                filterData.add(previewResult.get(i));
            }
//            if (i % 10000 == 0) {
//                Log.i("i = " + i);
//            }
        }

        filterData.sort(new Comparator<NumberBean>() {
            @Override
            public int compare(NumberBean o1, NumberBean o2) {
                return Integer.compare(o2.getData().size(), o1.getData().size());
            }
        });

        previewResult = filterData;
        List<NumberBean> collectNumber = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            if (i % 10000 == 0) {
                Log.i("i == " + i + "   " + previewResult.size());
            }
            NumberBean numberBean = previewResult.get(i);
            numberBean.setRemove(false);
            if (collectNumber.size() == 0) {
                collectNumber.add(numberBean);
                continue;
            }
            Iterator<NumberBean> iterator2 = collectNumber.iterator();
            boolean contain = false;
            boolean print = false;
            while (iterator2.hasNext()) {
//                        Log.i("处理下一个");
                NumberBean nb = iterator2.next();

                /*boolean run = false;
                if ((nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21]"))
                        || (numberBean.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21, 22]"))) {
//                    Log.i("??????11111");
                    run = true;
                }
                if ((nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21, 22]"))
                        || (numberBean.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21]"))) {
//                    Log.i("??????2222");
//                    collectNumber.add(numberBean);
                    run = true;
                }
                if (!run) {
                    contain = true;
                    break;
                }*/

                /*if (nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21]") && !print) {
                    Log.i("i == " + i + "??????1111111");
                    print = true;
                }

                if (nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21, 22]") && !print) {
                    Log.i("i == " + i + "??????22222");
                    print = true;
                }

                if (numberBean.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21]") && !print) {
                    Log.i("i == " + i + "??????33333");
                    print = true;
                }

                if (numberBean.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21, 22]") && !print) {
                    Log.i("i == " + i + "??????444444");
                    print = true;
                }*/

                if (nb.getData().size() == numberBean.getData().size()) {
                    continue;
                }

                List<Integer> largeData = null;
                List<Integer> miniData = null;
                boolean nbMaxThanNumber = false;
                if (nb.getData().size() > numberBean.getData().size()) {
                    nbMaxThanNumber = true;
                    largeData = nb.getData();
                    miniData = numberBean.getData();
                } else {
                    miniData = nb.getData();
                    largeData = numberBean.getData();
                }
                if (largeData.containsAll(miniData)) {
                    if (!nbMaxThanNumber) {
                        nb.setRemove(true);
//                        collectNumber.add(numberBean);
                    } else {
                        contain = true;
                        break;
                    }
                    /*if (numberBean.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 19, 21, 22]")) {
                        Log.i("i == " + i + "??????5555555 = " + largeData.toString() + "   " + miniData.toString());
                    }*/
//                    break;
                }
            }
            if (!contain) {
//                Log.i("");
                collectNumber.add(numberBean);
            }//得到结果
        }
        List<NumberBean> printNumbers = new ArrayList<>();
        for (int i = 0; i < collectNumber.size(); i++) {
            if (!collectNumber.get(i).isRemove()) {
                printNumbers.add(collectNumber.get(i));
            }
        }

        previewResult = printNumbers;

        previewResult.sort(Comparator.comparing(NumberBean::getDataStr2));

        Log.i("准备输出：" + previewResult.size());
        //开始检查得到的五位里面中4\5位的个数
        int existsData1 = printSingleDataIdx(previewResult, 1);//先得到符合的数
        int existsData2 = printSingleDataIdx(previewResult, 2);//先得到符合的数
        int existsData3 = printSingleDataIdx(previewResult, 3);//先得到符合的数
        int existsData4 = printSingleDataIdx(previewResult, 4);//先得到符合的数
        int existsData5 = printSingleDataIdx(previewResult, 5);//先得到符合的数
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------结果：" + previewResult.size() + "条-----------------------------");
        sb.append("\n");
        /*sb.append("中“1”位数量：" + existsData1);
        sb.append("\n");
        sb.append("中“2”位数量：" + existsData2);
        sb.append("\n");
        sb.append("中“3”位数量：" + existsData3);
        sb.append("\n");
        sb.append("中“4”位数量：" + existsData4);
        sb.append("\n");
        sb.append("中“5”位数量：" + existsData5);
        sb.append("\n");*/


        float percent1 = (existsData1 / (float) previewResult.size()) * 100;
        sb.append("中“1”位数量：").append(existsData1).append("     ").append(String.format("%.2f", percent1)).append("%");
        sb.append("\n");
        float percent2 = (existsData2 / (float) previewResult.size()) * 100;
        sb.append("中“2”位数量：").append(existsData2).append("     ").append(String.format("%.2f", percent2)).append("%");
        sb.append("\n");
        float percent3 = (existsData3 / (float) previewResult.size()) * 100;
        sb.append("中“3”位数量：").append(existsData3).append("     ").append(String.format("%.2f", percent3)).append("%");
        sb.append("\n");
        float percent4 = (existsData4 / (float) previewResult.size()) * 100;
        sb.append("中“4”位数量：").append(existsData4).append("     ").append(String.format("%.2f", percent4)).append("%");
        sb.append("\n");
        float percent5 = (existsData5 / (float) previewResult.size()) * 100;
        sb.append("中“5”位数量：").append(existsData5).append("     ").append(String.format("%.2f", percent5)).append("%");
        sb.append("\n");

        if (previewResult.size() > 3000000) {
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFileForRun13(mCommand.getOutputPath(), sb.toString(), previewResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < previewResult.size(); i++) {
                sb.append(previewResult.get(i).getData().toString());
                sb.append("\n");
            }
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /*for (int i = 0; i < previewResult.size(); i++) {
            sb.append(previewResult.get(i).getData().toString());
            sb.append("\n");
        }
        if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
            try {
                FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        if (mCallBackListener != null) {
            mCallBackListener.onCompile();
        } else {
            Log.i("所有数据处理完成！");
        }
    }

    private int printSingleDataIdx(List<NumberBean> data, int existsCountStandard) {
        int size = 0;
        for (int i = 0; i < data.size(); i++) {
            NumberBean d = data.get(i);
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < d.getData().size(); j++) {
                    if (mCommand.getCheckData()[k] == d.getData().get(j)) {
                        existsCount++;
                        break;
                    }
                }
            }
            if (existsCount == existsCountStandard) {
                size++;
            }
        }
        return size;
    }

}
