package com.dragon.lucky.filter2;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.command.CommandBean;
import com.dragon.lucky.command15.NumberDataBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.*;

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


    public void run(CommandBean commandBean, CallbackListener callbackListener) throws IOException {
        this.mCommand = commandBean;
        if (!FileReadHelper.checkFileExists(commandBean.getInputPath())) {
            Log.i("导入文件不存在");
            return;
        }
        List<String> previewData = FileReadHelper.readFile(commandBean.getInputPath());
        List<String> filterData = FileReadHelper.readFile(commandBean.getFilterPath());
        List<FilterBean> filterBeans = new ArrayList<>();
        List<ResultBean> previewResult = new ArrayList<>();
        Log.i("开始导入");
        for (int i = 0; i < previewData.size(); i++) {
            String d = previewData.get(i);
            if (!d.contains("[") || !d.contains("]")) {
                continue;
            }
            String line = d.replace("[", "").replace("]", "");
            String[] splitLine = line.split(", ");
            if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                byte[] splitLineInt = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        splitLineInt[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                previewResult.add(new ResultBean(splitLineInt, i, commandBean.getMergeCount()));
            }
        }
        for (int i = 0; i < filterData.size(); i++) {
            String filter = filterData.get(i);
            String[] filters = filter.split("-");
            if (filters.length > 1) {
                FilterBean filterBean = new FilterBean(Integer.parseInt(filters[0]), Integer.parseInt(filters[1]));
                filterBeans.add(filterBean);
            }
        }
        Log.i("开始生成");
        HashSet<ResultBean> collectData = new HashSet<>();
        int[] resultArr = new int[commandBean.getGenerateSize()];
        List<ResultBean> tempResult = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            Log.i("i = " + i + "    collectData = " + collectData.size());
            ResultBean rb = previewResult.get(i);
//            List<Byte> byteData = new ArrayList<>();
//            for (int j = 0; j < rb.getData().length; j++) {
//                byteData.add(rb.getData()[j]);
//            }
            Utils.combine_increase_gdaf2(rb.getData(), 0, resultArr, commandBean.getGenerateSize(), commandBean.getGenerateSize(), rb.getData().length, tempResult, new long[1], commandBean.getMergeCount());
            for (int j = 0; j < tempResult.size(); j++) {
                tempResult.get(j).setCollectCount(1);
            }
//            result.addAll(tempResult);
            collectData.addAll(tempResult);
            tempResult.clear();
            /*if (result.size() == 0) {
                for (int j = 0; j < tempResult.size(); j++) {
                    tempResult.get(j).setCollectCount(1);
                }
                result.addAll(tempResult);
            } else {
                List<ResultBean> appendTempResultData = new ArrayList<>();
                for (int j = 0; j < tempResult.size(); j++) {
                    ResultBean temp = tempResult.get(j);
                    boolean exists = false;
                    for (int k = 0; k < result.size(); k++) {
                        ResultBean temp2 = result.get(k);
                        if (Arrays.equals(temp.getData(), temp2.getData())) {
                            exists = true;
                            temp2.setCollectCount(temp2.getCollectCount() + 1);
                            break;
                        }
                    }
                    if (!exists) {
                        temp.setCollectCount(1);
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }*/
        }
        List<ResultBean> result = new ArrayList<>(collectData);

        //开始过滤
        Log.i("开始过滤");
        if (filterBeans.size() > 0) {
            for (FilterBean filterBean : filterBeans) {
                //如果符合的
                result.removeIf(rb -> rb.getCollectCount() >= filterBean.getStart() && rb.getCollectCount() < filterBean.getEnd());
            }
        }
        //拿到数据后进行比对
        Log.i("计算完成，结果数：" + result.size());
        //开始检查得到的五位里面中4\5位的个数
        int existsData1 = printSingleDataIdx(result, 1, null);//先得到符合的数
        int existsData2 = printSingleDataIdx(result, 2, null);//先得到符合的数
        int existsData3 = printSingleDataIdx(result, 3, null);//先得到符合的数
        List<Integer> count4 = new ArrayList<>();
        int existsData4 = printSingleDataIdx(result, 4, count4);//先得到符合的数
        List<Integer> count5 = new ArrayList<>();
        int existsData5 = printSingleDataIdx(result, 5, count5);//先得到符合的数
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------结果：" + result.size() + "条-----------------------------");
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



        float percent1 = (existsData1 / (float) result.size()) * 100;
        sb.append("中“1”位数量：").append(existsData1).append("     ").append(String.format("%.2f", percent1)).append("%");
        sb.append("\n");
        float percent2 = (existsData2 / (float) result.size()) * 100;
        sb.append("中“2”位数量：").append(existsData2).append("     ").append(String.format("%.2f", percent2)).append("%");
        sb.append("\n");
        float percent3 = (existsData3 / (float) result.size()) * 100;
        sb.append("中“3”位数量：").append(existsData3).append("     ").append(String.format("%.2f", percent3)).append("%");
        sb.append("\n");
        float percent4 = (existsData4 / (float) result.size()) * 100;
        sb.append("中“4”位数量：").append(existsData4).append("     ").append(String.format("%.2f", percent4)).append("%");
        sb.append("\n");
        float percent5 = (existsData5 / (float) result.size()) * 100;
        sb.append("中“5”位数量：").append(existsData5).append("     ").append(String.format("%.2f", percent5)).append("%");
        sb.append("\n");
        sb.append("中“4”位统计数：");
        /*for (int i = 0; i < count4.size(); i++) {
            sb.append(count4.get(i));
            if (i < count4.size()) {
                sb.append(",");
            }
        }*/

        int p = -1;
        int c = 0;
        for (int i = 0; i < count4.size(); i++) {
            if (count4.get(i) != p) {
                if (p != -1) {
                    sb.append(p);
                    sb.append("-");
                    sb.append(c);
                    sb.append(",");
                }
                p = count4.get(i);
                c = 0;
            }
            c++;
            /*sb.append(count5.get(i));
            if (i < count5.size()) {
                sb.append(",");
            }*/
        }
        if (count4.size() > 0) {
            sb.append(p);
            sb.append("-");
            sb.append(c);
        }

        sb.append("\n");
        sb.append("中“5”位统计数：");
        p = -1;
        c = 0;
        for (int i = 0; i < count5.size(); i++) {
            if (count5.get(i) != p) {
                if (p != -1) {
                    sb.append(p);
                    sb.append("-");
                    sb.append(c);
                    sb.append(",");
                }
                p = count5.get(i);
                c = 0;
            }
            c++;
            /*sb.append(count5.get(i));
            if (i < count5.size()) {
                sb.append(",");
            }*/
        }
        if (count5.size() > 0) {
            sb.append(p);
            sb.append("-");
            sb.append(c);
        }

        sb.append("\n");
        result.sort((o1, o2) -> Integer.compare(o2.getCollectCount(), o1.getCollectCount()));
        for (int i = 0; i < result.size(); i++) {
            sb.append(Arrays.toString(result.get(i).getData()));
            if (result.get(i).getCollectCount() > 0) {
                sb.append(",[");
                sb.append(result.get(i).getCollectCount());
                sb.append("]");
            }
            sb.append("\n");
        }
        if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
            try {
                FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (callbackListener != null) {
            callbackListener.onCompile();
        } else {
            Log.i("所有数据处理完成！");
        }
    }

    private int printSingleDataIdx(List<ResultBean> data, int existsCountStandard, List<Integer> count) {
        int size = 0;
        for (int i = 0; i < data.size(); i++) {
            ResultBean d = data.get(i);
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < d.getData().length; j++) {
                    if (mCommand.getCheckData()[k] == d.getData()[j]) {
                        existsCount++;
                        break;
                    }
                }
            }
            if (existsCount == existsCountStandard) {
                size++;
                if (count != null/* && !count.contains((Integer) d.getCollectCount())*/) {
                    count.add(d.getCollectCount());
                }
            }
        }
        if (count != null) {
            count.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
        }
        return size;
    }

}
