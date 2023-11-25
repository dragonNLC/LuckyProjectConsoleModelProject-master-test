package com.dragon.lucky.command20;

import com.dragon.lucky.bean.BaseControlNumberBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.Log;

import java.util.*;

public class FilterUtils {

    public static int FILTER_COUNT = 0;//记录方法运行次数
    public static int FILTER_COUNT_SIZE = 0;

    //取得数据然后取并集
    public static void filterResultBean(List<ResultBean> base, List<BaseControlNumberBean> filter, int position, List<Integer> templateData, List<Integer> resultData) {
        if (position == filter.size()) {
            for (int i = 0; i < templateData.size(); i++) {
//                Log.i(" templateData - " + templateData.get(i));
            }
            resultData.addAll(templateData);
            return;
        }
        if (FILTER_COUNT >= 100000000) {
            FILTER_COUNT = 0;
            FILTER_COUNT_SIZE++;
            Log.i(" - 完成一轮筛选(100000000) - " + FILTER_COUNT_SIZE + "   " + System.currentTimeMillis());
        }
//        Log.i("第：" + position + "条规则；");
        BaseControlNumberBean bcn = filter.get(position);
        List<Integer> printRules = bcn.getPrintNumber();

        for (int j = 0; j < printRules.size(); j++) {
            int rule = printRules.get(j);//拿到生成规则
            //得到符合规则的数据
            List<Integer> result = getContains(base, templateData, bcn.getBaseNumber(), rule);

            filterResultBean(base, filter, position + 1, result, resultData);
        }
        FILTER_COUNT++;
//        Log.i("筛选次数：" + FILTER_COUNT);
    }

    public static List<Integer> getContains(List<ResultBean> data, List<Integer> templateData, List<Byte> baseData, int rule) {
        List<Integer> resultData = new ArrayList<>();
        if (templateData == null) {
            for (int i = 0; i < data.size(); i++) {
                ResultBean children = data.get(i);
                byte[] intArr = children.getData();
                int containCount = 0;
                for (int j = 0; j < intArr.length; j++) {
                    if (baseData.contains(intArr[j])) {
                        containCount++;
                    }
                }
            /*if (containCount == rule) {
                resultData.add(children);
            }*/
//                Log.i("containCount = " + containCount + "    rule = " + rule + "  intArr = " + Arrays.toString(intArr) +  "   i = " + i);
                if (rule == 0) {
                    if (containCount > rule) {
                        resultData.add(i);
                    }
                } else {
                    if (containCount < rule) {
//                        Log.i("containCount = " + containCount + "    rule = " + rule + "  intArr = " + Arrays.toString(intArr) +  "   i = " + i);
                        resultData.add(i);
                    }
                }
            }
        } else {
            for (int i = 0; i < templateData.size(); i++) {
                int children = templateData.get(i);
                byte[] intArr = data.get(children).getData();
                int containCount = 0;
                for (int j = 0; j < intArr.length; j++) {
                    if (baseData.contains(intArr[j])) {
                        containCount++;
                    }
                }
//            Log.i("containCount = " + containCount + "    rule = " + rule);
            /*if (containCount == rule) {
                resultData.add(children);
            }*/
                /*if (containCount != rule) {
                    resultData.add(i);
                }*/
                if (rule == 0) {
                    if (containCount > rule) {
                        resultData.add(children);
                    }
                } else {
                    if (containCount < rule) {
//                        Log.i("containCount = " + containCount + "    rule = " + rule + "  intArr = " + Arrays.toString(intArr) +  "   i = " + i);
                        resultData.add(children);
                    }
                }
            }
        }
        return resultData;
    }

    public static List<ResultMergeBean> collectMergeBean(List<ResultBean> data, List<Integer> dataIdx) {
        List<ResultMergeBean> mergeBeans = new ArrayList<>();
        //合并前n位数为同一个数组
        Map<String, ResultMergeBean> mergeBeanMap = new HashMap<>();
        for (int i = 0; i < dataIdx.size(); i++) {
            ResultBean rb = data.get(dataIdx.get(i));
            ResultMergeBean rmb = mergeBeanMap.get(rb.getMergeArr());
            if (rmb == null) {
                rmb = new ResultMergeBean(rb.getMergeArr());
                mergeBeanMap.put(rb.getMergeArr(), rmb);
//                Log.i("mergeArr == null：" + rb.getMergeArr() + "   ");
            }
            rmb.mergeResult.add(dataIdx.get(i));
        }//合并完毕
        mergeBeanMap.forEach((ints, resultMergeBean) -> mergeBeans.add(resultMergeBean));//生成列表
        return mergeBeans;
    }

    public static List<Integer> getContains2(String name, List<ResultBean> allData, List<ResultMergeBean> data, int start, int end, List<ResultMergeBean> data2, int start2, int end2) {
//        Log.i(name + " - data - " + data.size() + "   " + "start = " + start + "   " + "end = " + end);
//        Log.i(name + " - data2 - " + data2.size() + "   " + "start2 = " + start2 + "   " + "end2 = " + end2);
        List<Integer> resultData = new ArrayList<>();
        int count = 0;
        int count2 = 0;
        for (int i = start; i < end; i++) {
            ResultMergeBean children = data.get(i);
            for (int j = start2; j < end2; j++) {
                ResultMergeBean children2 = data2.get(j);
                count++;
                if (count >= 100000000) {
                    count = 0;
                    count2++;
                    Log.i(name + " - 完成一轮校验(100000000) - " + count2 + "   " + System.currentTimeMillis());
                }
                if (children.mergeHead.equals(children2.mergeHead)) {
//                    Log.i(children.mergeHead + " --- " + children2.mergeHead + "   " + children.mergeResult.size());
                    //头部相同了，判断数组内部
                    for (int k = 0; k < children.mergeResult.size(); k++) {
                        int children3 = children.mergeResult.get(k);
//                        Log.i("name:" + name + Arrays.toString(children3.getData()));
                        for (int l = 0; l < children2.mergeResult.size(); l++) {
                            int children4 = children2.mergeResult.get(l);
//                            Log.i(Arrays.toString(children3.getData()) + " --- " + Arrays.toString(children4.getData()) + "   ");
//                            if (children3.getId() == children4.getId()) {
                            if (Arrays.equals(allData.get(children3).getData(), allData.get(children4).getData())) {
                                resultData.add(children3);
                            }
                            count++;
                            if (count >= 100000000) {
                                count = 0;
                                count2++;
                                Log.i(name + " - 完成一轮校验(100000000) - " + count2 + "   " + System.currentTimeMillis());
                            }
                        }
                    }
                    break;
                }
            }
        }
        Log.i(name + " - 本轮计算得到的数量 - " + resultData.size() + "                                 count = " + count + "    start = " + start + "    end = " + end + "    start2 = " + start2 + "   end2 = " + end2);
        return resultData;
    }

    public static List<ResultBean> getContains3(String name, List<ResultBean> data, int start, int end, List<ResultBean> data2, int start2, int end2) {
//        Log.i(name + " - data - " + data.size() + "   " + "start = " + start + "   " + "end = " + end);
//        Log.i(name + " - data2 - " + data2.size() + "   " + "start2 = " + start2 + "   " + "end2 = " + end2);
        List<ResultBean> resultData = new ArrayList<>();
        int count = 0;
        int count2 = 0;
        for (int i = start; i < end; i++) {
            ResultBean children = data.get(i);
            for (int j = start2; j < end2; j++) {
                ResultBean children2 = data2.get(j);
                count++;
                if (count >= 100000000) {
                    count = 0;
                    count2++;
//                    Log.i(name + " - 完成一轮校验(100000000) - " + count2 + "   " + System.currentTimeMillis());
                }
                if (Arrays.equals(children.getData(), children2.getData())) {
                    resultData.add(children);
                }
            }
        }
        Log.i(name + " - 本轮计算得到的数量 - " + resultData.size() + "                                 count = " + count + "    start = " + start + "    end = " + end + "    start2 = " + start2 + "   end2 = " + end2);
        return resultData;
    }

}
