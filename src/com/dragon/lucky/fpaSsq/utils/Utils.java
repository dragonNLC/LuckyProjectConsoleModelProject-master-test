package com.dragon.lucky.fpaSsq.utils;

import com.dragon.lucky.fpaSsq.bean.SplitBean;

import java.util.List;

public class Utils {


    public static void combine_increase(List<Integer> data, int start, int[] result, int count, int NUM, int arr_len, List<SplitBean> resultData) {
        int i = 0;
        for (i = start; i < arr_len + 1 - count; i++) {
            result[count - 1] = i;
            if (count - 1 == 0) {
                int j;
                int[] tempData = new int[NUM];
                for (j = NUM - 1; j >= 0; j--) {
                    tempData[tempData.length - 1 - j] = data.get(result[j]);
                }
                resultData.add(new SplitBean(tempData));
            } else {
                combine_increase(data, i + 1, result, count - 1, NUM, arr_len, resultData);
            }
        }
    }

}
