package com.dragon.lucky.utils;

import com.dragon.lucky.bean.PointBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultBean2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;

public class Utils {

    public static int METHOD_COUNT = 0;//方法计算次数

    public static void generateArr(List<PointBean> data, int start, int count, int size,
                                   int[] id, List<List<PointBean>> result) {
        for (int i = start; i < data.size() - count + 1; i++) {
            id[count - 1] = i;
            if (count - 1 == 0) {
                List<PointBean> tempResult = new ArrayList<>();
                for (int j = size - 1; j >= 0; j--) {
                    tempResult.add(data.get(id[j]));
                }
                result.add(tempResult);
            } else {
                generateArr(data, i + 1, count - 1, size, new int[size], result);
            }
        }
    }

    public static void combine_decrease(List<PointBean> data, int start, int[] result, int count, int NUM) {
        int i;
        for (i = start; i >= count; i--) {
            result[count - 1] = i - 1;
            if (count > 1) {
                combine_decrease(data, i - 1, result, count - 1, NUM);
            } else {
                int j;
                for (j = NUM - 1; j >= 0; j--) {
                    Log.e("TAG", data.get(result[j]) + "");
                }
                Log.e("TAG", "\n");
            }
        }
    }

    public static void combine_increase(List<PointBean> data, int start, int[] result, int count, int NUM, int arr_len) {
        int i = 0;
        for (i = start; i < arr_len + 1 - count; i++) {
            result[count - 1] = i;
            if (count - 1 == 0) {
                int j;
                StringBuilder sb = new StringBuilder();
                for (j = NUM - 1; j >= 0; j--) {
                    sb.append(data.get(result[j]).getNumber());
                    if (j != 0) {
                        sb.append("、");
                    }
                }
                Log.e("TAG", sb.toString());
//                Log.e("TAG", "\n");
            } else
                combine_increase(data, i + 1, result, count - 1, NUM, arr_len);
        }
    }

    public static int printCount = 0;//用来输出的标志位
    public static int printCount2 = 0;//用来输出的标志位

    public static void combine_increase(List<Byte> data, int start, int[] result, int count, int NUM, int arr_len, List<ResultBean> resultData, long[] id, int mergeArr) {
        if (id.length > 0) {
            Log.i("生成组合数：" + id[id.length - 1]);
        }
        int i = 0;
        for (i = start; i < arr_len + 1 - count; i++) {
            result[count - 1] = i;
            if (count - 1 == 0) {
                int j;
//                StringBuilder sb = new StringBuilder();
                byte[] tempData = new byte[NUM];
                for (j = NUM - 1; j >= 0; j--) {
//                    sb.append(data.get(result[j]));
//                    tempData.add(new PointBean(data.get(result[j]), PointBean.POINT_NONE));
                    tempData[tempData.length - 1 - j] = data.get(result[j]);
//                    tempData.add(data.get(result[j]));
//                    if (j != 0) {
//                        sb.append("、");
//                    }
                    printCount++;
                    if (printCount >= 100) {
                        printCount = 0;
                        Log.i("子运算次数加一(/100)");
                    }
                }
                id[0] += 1;
                if (mergeArr == -1) {
                    resultData.add(new ResultBean(tempData, id[0]));
                } else {
                    resultData.add(new ResultBean(tempData, id[0], Math.min(mergeArr, NUM)));
                }
//                Log.e("TAG", sb.toString());
            } else {
                combine_increase(data, i + 1, result, count - 1, NUM, arr_len, resultData, id, mergeArr);
            }
        }
        printCount2++;
        if (printCount2 >= 100) {
            printCount2 = 0;
            METHOD_COUNT++;
            Log.i("运算次数(/100)：" + METHOD_COUNT);
        }
    }

    public static void combine_increase2(List<Byte> data, int start, int[] result, int count, int NUM, int arr_len, List<ResultBean2> resultData, long[] id) {
        if (id.length > 0) {
            Log.i("生成组合数：" + id[id.length - 1]);
        }
        int i = 0;
        for (i = start; i < arr_len + 1 - count; i++) {
            result[count - 1] = i;
            if (count - 1 == 0) {
                int j;
//                StringBuilder sb = new StringBuilder();
                List<Byte> tempData = new ArrayList<>();
                for (j = NUM - 1; j >= 0; j--) {
//                    sb.append(data.get(result[j]));
//                    tempData.add(new PointBean(data.get(result[j]), PointBean.POINT_NONE));
                    tempData.add(NUM - 1 - j, data.get(result[j]));
//                    tempData.add(data.get(result[j]));
//                    if (j != 0) {
//                        sb.append("、");
//                    }
                    printCount++;
                    if (printCount >= 100) {
                        printCount = 0;
                        Log.i("子运算次数加一(/100)");
                    }
                }
                id[0] += 1;
                resultData.add(new ResultBean2(tempData, id[0]));
//                Log.e("TAG", sb.toString());
            } else {
                combine_increase2(data, i + 1, result, count - 1, NUM, arr_len, resultData, id);
            }
        }
        printCount2++;
        if (printCount2 >= 100) {
            printCount2 = 0;
            METHOD_COUNT++;
            Log.i("运算次数(/100)：" + METHOD_COUNT);
        }
    }


    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("\\+?[0-9]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isNumeric2(String str) {
        if (isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9、]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isNumeric3(String str) {
        if (isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[、]*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
