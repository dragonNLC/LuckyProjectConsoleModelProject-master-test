package com.dragon.lucky.filter;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultMergeBean;
import com.dragon.lucky.command.CommandBean;
import com.dragon.lucky.command.FilterUtils;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        List<String> previewData = FileReadHelper.readFile(commandBean.getInputPath());
        List<ResultBean> previewResult = new ArrayList<>();
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
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            List<ResultBean> tempResult = new ArrayList<>();
            ResultBean rb = previewResult.get(i);
            int[] resultArr = new int[commandBean.getGenerateSize()];
            List<Byte> byteData = new ArrayList<>();
            for (int j = 0; j < rb.getData().length; j++) {
                byteData.add(rb.getData()[j]);
            }
            Utils.combine_increase(byteData, 0, resultArr, commandBean.getGenerateSize(), commandBean.getGenerateSize(), byteData.size(), tempResult, new long[1], commandBean.getMergeCount());
            result.addAll(tempResult);
            /*if (result.size() == 0) {
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
                            break;
                        }
                    }
                    if (!exists) {
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }*/
        }
        HashSet<ResultBean> firstFilterResult = new HashSet<>(result);
        result.clear();
        result.addAll(firstFilterResult);

        List<String> appendData = FileReadHelper.readFile(commandBean.getPreviewFilePath());
        List<ResultBean> appendDataResult = new ArrayList<>();
        for (int i = 0; i < appendData.size(); i++) {
            String d = appendData.get(i);
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
                appendDataResult.add(new ResultBean(splitLineInt, i, commandBean.getMergeCount()));
            }
        }

        Log.i("开始求差集");
        //拿到数据后进行比对
        if (commandBean.getGenerateFilterType() == 1) {//求交集
            /*List<ResultMergeBean> mergeResultBeans = FilterUtils.collectMergeBean(result);
            List<ResultMergeBean> mergeResultBeans2 = FilterUtils.collectMergeBean(appendDataResult);
            List<ResultBean> resultAppendData = new ArrayList<>(FilterUtils.getContains2("1", mergeResultBeans, 0, mergeResultBeans.size(), mergeResultBeans2, 0, mergeResultBeans2.size()));*/

            List<ResultBean> tempResultAppendData = new ArrayList<>();
            List<ResultBean> resultAppendData = new ArrayList<>();
            tempResultAppendData.addAll(result);
            tempResultAppendData.addAll(appendDataResult);
            HashSet<ResultBean> filterResult = new HashSet<>(tempResultAppendData);
            tempResultAppendData.clear();
            tempResultAppendData.addAll(filterResult);

            for (int i = 0; i < tempResultAppendData.size(); i++) {
                if (tempResultAppendData.get(i).getCollectCount() > 0) {//求交集
                    resultAppendData.add(tempResultAppendData.get(i));
                }
            }

            //求到并集，然后就可以了，写出到文件
            Log.i("计算完成，结果数-1：" + resultAppendData.size());
            //开始检查得到的五位里面中4\5位的个数
            int existsData1 = printSingleDataIdx(resultAppendData, 1);//先得到符合的数
            int existsData2 = printSingleDataIdx(resultAppendData, 2);//先得到符合的数
            int existsData3 = printSingleDataIdx(resultAppendData, 3);//先得到符合的数
            int existsData4 = printSingleDataIdx(resultAppendData, 4);//先得到符合的数
            int existsData5 = printSingleDataIdx(resultAppendData, 5);//先得到符合的数
            StringBuilder sb = new StringBuilder();
            sb.append("-----------------------结果：" + resultAppendData.size() + "条-----------------------------");
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


            float percent1 = (existsData1 / (float) resultAppendData.size()) * 100;
            sb.append("中“1”位数量：").append(existsData1).append("     ").append(String.format("%.2f", percent1)).append("%");
            sb.append("\n");
            float percent2 = (existsData2 / (float) resultAppendData.size()) * 100;
            sb.append("中“2”位数量：").append(existsData2).append("     ").append(String.format("%.2f", percent2)).append("%");
            sb.append("\n");
            float percent3 = (existsData3 / (float) resultAppendData.size()) * 100;
            sb.append("中“3”位数量：").append(existsData3).append("     ").append(String.format("%.2f", percent3)).append("%");
            sb.append("\n");
            float percent4 = (existsData4 / (float) resultAppendData.size()) * 100;
            sb.append("中“4”位数量：").append(existsData4).append("     ").append(String.format("%.2f", percent4)).append("%");
            sb.append("\n");
            float percent5 = (existsData5 / (float) resultAppendData.size()) * 100;
            sb.append("中“5”位数量：").append(existsData5).append("     ").append(String.format("%.2f", percent5)).append("%");
            sb.append("\n");

            for (int i = 0; i < resultAppendData.size(); i++) {
                Arrays.sort(resultAppendData.get(i).getData());
                sb.append(Arrays.toString(resultAppendData.get(i).getData()));
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
        } else {//求差集，即前面数组存在的数在后面数组也存在的话，要去掉，剩下不存在的

            List<ResultBean> tempLastData = new ArrayList<>();
            List<ResultBean> lastData = new ArrayList<>();
            tempLastData.addAll(result);
            tempLastData.addAll(appendDataResult);
            HashSet<ResultBean> filterResult = new HashSet<>(tempLastData);
            tempLastData.clear();
            tempLastData.addAll(filterResult);

            for (int i = 0; i < tempLastData.size(); i++) {
                if (tempLastData.get(i).getCollectCount() == 0) {//求差集
                    lastData.add(tempLastData.get(i));
                }
            }

            /*for (int i = 0; i < appendDataResult.size(); i++) {
                ResultBean rb = appendDataResult.get(i);
                boolean exists = false;
                for (int j = 0; j < result.size(); j++) {
                    ResultBean rb2 = result.get(j);
                    if (Arrays.equals(rb.getData(), rb2.getData())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    lastData.add(rb);
                }
            }*/
            //求到差集，然后就可以了，写出到文件
            Log.i("计算完成，结果数-2：" + lastData.size());
//            Log.i("计算完成，结果数-2：" + result.size());
            //开始检查得到的五位里面中4\5位的个数
            int existsData1 = printSingleDataIdx(lastData, 1);//先得到符合的数
            int existsData2 = printSingleDataIdx(lastData, 2);//先得到符合的数
            int existsData3 = printSingleDataIdx(lastData, 3);//先得到符合的数
            int existsData4 = printSingleDataIdx(lastData, 4);//先得到符合的数
            int existsData5 = printSingleDataIdx(lastData, 5);//先得到符合的数
            StringBuilder sb = new StringBuilder();
            sb.append("-----------------------结果：" + lastData.size() + "条-----------------------------");
            sb.append("\n");
            sb.append("中“1”位数量：" + existsData1);
            sb.append("\n");
            sb.append("中“2”位数量：" + existsData2);
            sb.append("\n");
            sb.append("中“3”位数量：" + existsData3);
            sb.append("\n");
            sb.append("中“4”位数量：" + existsData4);
            sb.append("\n");
            sb.append("中“5”位数量：" + existsData5);
            sb.append("\n");
            for (int i = 0; i < lastData.size(); i++) {
                Arrays.sort(lastData.get(i).getData());
                sb.append(Arrays.toString(lastData.get(i).getData()));
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
    }

    private int printSingleDataIdx(List<ResultBean> data, int existsCountStandard) {
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
            }
        }
        return size;
    }

}
