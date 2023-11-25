package com.dragon.lucky.command18;

import com.dragon.lucky.utils.Log;

import java.util.*;

/**
 * 输入一个2位数的数组，输入一个3位数的数组，3位数降位为两位数，统计每个数的次数，然后将次数记录在两位数的次数后面，剩下没有在两位数数组中出现的，独自列在下方
 */
public class AssistUtils {

    public static List<NumberDataBean> generateFQNumber(int fq, float maxNumber) {
        int result = (int) Math.round(maxNumber / fq);
        int[] maxData = new int[fq];
        for (int i = 1; i < fq + 1; i++) {
            int d = result * i;
            if (d > maxNumber) {
                d = (int) maxNumber;
            }
            d = d - (result * (i - 1));
            maxData[i - 1] = d;
        }
        List<NumberDataBean> resultData = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < fq; i++) {
            NumberDataBean number = new NumberDataBean();
            byte[] fqData = new byte[maxData[i]];
            for (int j = 0; j < fqData.length; j++) {
                fqData[j] = (byte) (++count);
            }
            number.setData(fqData);
            resultData.add(number);
        }
        return resultData;
    }

   /* public static List<NumberDataBean> generateFQData(int size, int fq, float maxNumber) {
        int result = (int) Math.round(maxNumber / fq);
        int[] maxData = new int[fq];
        for (int i = 1; i < fq + 1; i++) {
            int d = result * i;
            if (d > maxNumber) {
                d = (int) maxNumber;
            }
            d = d - (result * (i - 1));
            maxData[i - 1] = d;
//            Log.i("maxData[i - 1] = " + maxData[i - 1]);
        }

        byte[][] dataset = new byte[fq][size + 1];
        for (int i = 0; i < fq; i++) {
            byte[] data = new byte[size + 1];
            for (int j = 0; j < size + 1; j++) {
                if (j > maxData[i]) {
                    data = Arrays.copyOf(data, j);
                    break;
                } else {
                    data[j] = (byte) j;
                }
            }
            dataset[i] = data;
        }
        //数据合并
        List<NumberBean> numberBeans = new ArrayList<>();
        for (int i = 0; i < dataset.length; i++) {
            List<NumberDataBean> numberDataBeans = new ArrayList<>();
            for (int j = 0; j < dataset[i].length; j++) {
                NumberDataBean dataBean = new NumberDataBean();
                byte[] d = new byte[1];
                d[0] = dataset[i][j];
                dataBean.setData(d);
                numberDataBeans.add(dataBean);
            }
            NumberBean numberBean = new NumberBean();
            numberBean.setGenerateData(numberDataBeans);
            numberBeans.add(numberBean);
        }
        appendChild(numberBeans);
        int allowCount = 0;
        List<NumberDataBean> allowNumberData = new ArrayList<>();
        for (int i = 0; i < numberBeans.size(); i++) {
            NumberBean numberBean = numberBeans.get(i);
            numberBean.getGenerateData().sort(new Comparator<NumberDataBean>() {
                @Override
                public int compare(NumberDataBean o1, NumberDataBean o2) {
                    return Arrays.toString(o1.getData()).compareTo(Arrays.toString(o2.getData()));
                }
            });
            for (int j = 0; j < numberBean.getGenerateData().size(); j++) {
                NumberDataBean numberDataBean = numberBean.getGenerateData().get(j);
                int count = 0;
                for (int k = 0; k < numberDataBean.getData().length; k++) {
                    count += numberDataBean.getData()[k];
                }
                if (count == size) {
                    allowCount++;
                    allowNumberData.add(numberDataBean);
                    Log.i("求和：" + count + "   原始数：" + Arrays.toString(numberDataBean.getData()));
                }
            }
        }
        Log.i("符合要求的组合数：" + allowCount);
        return allowNumberData;
    }

    private static void appendChild(List<NumberBean> data) {
        NumberBean n1 = data.remove(0);
        NumberBean n2 = data.remove(0);
        List<NumberDataBean> d1 = n1.getGenerateData();
        List<NumberDataBean> d2 = n2.getGenerateData();
        NumberBean result = new NumberBean();
        List<NumberDataBean> resultData = new ArrayList<>();
        result.setGenerateData(resultData);
        if (d1.size() == 0) {
            resultData.addAll(d2);
        } else if (d2.size() == 0) {
            resultData.addAll(d1);
        } else {
            for (int i = 0; i < d1.size(); i++) {
                NumberDataBean d1S = d1.get(i);
                for (int j = 0; j < d2.size(); j++) {
                    NumberDataBean d2S = d2.get(j);
                    byte[] b1 = d1S.getData();
                    byte[] b2 = d2S.getData();
                    byte[] d = new byte[b1.length + b2.length];
                    for (int k = 0; k < b1.length; k++) {
                        d[k] = b1[k];
                    }
                    int addCount = 0;
                    for (int k = 0; k < b2.length; k++) {
                        addCount++;
                        d[b1.length + addCount - 1] = b2[k];
                    }
                    d = Arrays.copyOf(d, b1.length + addCount);//重新拷贝数据
                    resultData.add(new NumberDataBean(d));//将结果赋值进去
                }
            }
        }
        Log.i(Thread.currentThread().getName() + " 开始去重：" + resultData.size());
        List<NumberDataBean> collectNumber = new ArrayList<>(removeDuplicates(resultData));
        Log.i(Thread.currentThread().getName() + " 结果数：" + collectNumber.size());
        result.setGenerateData(collectNumber);
        data.add(0, result);
        if (data.size() <= 1) {
            Log.i(Thread.currentThread().getName() + " 第三阶段处理完毕：" + data.get(0).getGenerateData().size());
        } else {
            appendChild(data);
        }
    }

    private static List<NumberDataBean> removeDuplicates(List<NumberDataBean> resultData) {
        //去重
        List<ContainBean> checkData = new ArrayList<>();
        for (int i = 0; i < resultData.size(); i++) {
            checkData.add(new ContainBean(Arrays.toString(resultData.get(i).getData()), i));
        }
        HashSet<ContainBean> filterData = new HashSet<>(checkData);
        List<ContainBean> checkData2 = new ArrayList<>(filterData);

        List<NumberDataBean> collectNumber = new ArrayList<>();
        for (int i = 0; i < checkData2.size(); i++) {
            collectNumber.add(resultData.get(checkData2.get(i).getId()));
        }
        return collectNumber;
    }*/

}
