package com.dragon.lucky.command27;

import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.util.*;

public class FilterUtils {

    public static int FILTER_COUNT = 0;//记录方法运行次数
    public static int FILTER_COUNT_SIZE = 0;

    //取得数据然后取并集
    public static List<NumberDataBean> filterResultBean(List<NumberDataBean> base, List<LineBean> filter) {
        Iterator<NumberDataBean> numberData = base.iterator();
        List<NumberDataBean> result = new ArrayList<>();
        int count = 0;
        while (numberData.hasNext()) {
            NumberDataBean numberDataBean = numberData.next();
            byte[] numberDataBytes = numberDataBean.getData();
            boolean remove = false;
            laeblPoint:
            for (int m = 0; m < filter.size(); m++) {
                LineBean lineBean = filter.get(m);
                for (int k = 0; k < lineBean.getData().size(); k++) {
                    NumberBean numberBean = lineBean.getData().get(k);
                    int containCount = 0;
                    for (int j = 0; j < numberDataBytes.length; j++) {
                        for (int l = 0; l < numberBean.getData().length; l++) {
                            if (numberDataBytes[j] == numberBean.getData()[l]) {
                                containCount++;
                            }
                        }
                    }
                    if (containCount > numberBean.getGenerateSize()) {
                        count++;
                        Log.i("containCount = " + containCount + "   " + numberBean.getGenerateSize() + "   " + count);
//                        numberData.remove();
                        remove = true;
                        break laeblPoint;
                    }
                }
            }
            if (!remove) {
                result.add(numberDataBean);
            }
        }
        return result;
    }

    //取得数据然后取并集
    public static List<NumberDataBean> filterResultBean2(List<NumberDataBean> base, List<NumberBean> filter) {
        Iterator<NumberDataBean> numberData = base.iterator();
        int count = 0;
        while (numberData.hasNext()) {
            NumberDataBean numberDataBean = numberData.next();
            byte[] numberDataBytes = numberDataBean.getData();
            laeblPoint:
            for (int k = 0; k < filter.size(); k++) {
                NumberBean numberBean = filter.get(k);
                if (numberBean.getData() == null) {
//                        Log.i("continue");
                    continue;
                } else {
//                        Log.i("not continue");
                }
                int containCount = 0;
                for (int j = 0; j < numberDataBytes.length; j++) {
                    for (int l = 0; l < numberBean.getData().length; l++) {
                        if (numberDataBytes[j] == numberBean.getData()[l]) {
                            containCount++;
                        }
                    }
                }
                if (containCount > numberBean.getGenerateSize()) {
                    count++;
                        Log.i("containCount = " + containCount + "   " + numberBean.getGenerateSize() + "   " + count);
                    Log.i("numberData = " + Arrays.toString(numberBean.getData())  + "  " + Arrays.toString(numberDataBean.getData()));
                    numberData.remove();
                    break laeblPoint;
                }
            }
        }
        return base;
    }

    //取得数据然后取并集
    /*public static List<NumberDataBean> filterResultBean2(List<NumberDataBean> base, List<NumberBean> filter) {
        Iterator<NumberDataBean> numberData = base.iterator();
        List<NumberDataBean> result = new ArrayList<>();
        int count = 0;
        while (numberData.hasNext()) {
            NumberDataBean numberDataBean = numberData.next();
            byte[] numberDataBytes = numberDataBean.getData();
            boolean remove = false;
            for (int k = 0; k < filter.size(); k++) {
                NumberBean numberBean = filter.get(k);
                if (numberBean.getData() == null) {
                    continue;
                }
                int containCount = 0;
                for (int j = 0; j < numberDataBytes.length; j++) {
                    for (int l = 0; l < numberBean.getData().length; l++) {
                        if (numberDataBytes[j] == numberBean.getData()[l]) {
//                            Log.i("numberDataBytes[j] = " + numberDataBytes[j] + "   " + numberBean.getData()[l] + "   " + j);
                            containCount++;
                        }
                    }
                }
                if (containCount > numberBean.getGenerateSize()) {
//                    Log.i("numberDataBean = " + Arrays.toString(numberDataBean.getData()));
                    count++;
//                        Log.i("containCount = " + containCount + "   " + numberBean.getGenerateSize() + "   " + count);
//                    numberData.remove();
                    remove = true;
                    break;
                }
            }
            if (!remove) {
                result.add(numberDataBean);
            }
        }
        base.clear();
        System.gc();
        return result;
    }*/

    public static List<NumberDataBean> filterNumber(List<NumberDataBean> data) {
        List<NumberDataBean> result = new ArrayList<>();
        Map<String, Integer> cacheFilterData = new HashMap<>();
        int id = 0;
        for (int i = 0; i < data.size(); i++) {
            cacheFilterData.clear();
            boolean filter = false;
            NumberDataBean d = data.get(i);
            byte[] cacheData = d.getData();

            /*int SKIP = 200;

            if (i % SKIP == 0) {
                int lastData = 24;
                if (data.size() - i < lastData) {
                    id = (int) (Math.random() * SKIP) + i;
                } else {
                    do {
                        id = (int) (Math.random() * SKIP) + i;
                    } while (id % 100 != lastData);
                }
//                id = (int) (Math.random() * SKIP) + i;
//                Log.i("id = " + id);
            }
            if (i != id) {
                continue;
            }*/
            //过滤要从结构上做文章
            id = i;
            for (int j = 0; j < cacheData.length; j++) {
                String b = String.valueOf(cacheData[j]);
                String key = "";
                for (int k = 0; k < b.length(); k++) {
                    if (!Utils.isEmpty(key) && key.equals(b.substring(k, k + 1))) {
                        break;
                    }
                    key = b.substring(k, k + 1);
                    if (!cacheFilterData.containsKey(b.substring(k, k + 1))) {
                        cacheFilterData.put(b.substring(k, k + 1), 0);
                    }
                    cacheFilterData.put(b.substring(k, k + 1), cacheFilterData.get(b.substring(k, k + 1)) + 1);
                }
            }
            for (String k :
                    cacheFilterData.keySet()) {
                if (cacheFilterData.get(k) >= 4) {
                    filter = true;
                    break;
                }
            }
            if (!filter && d.getData().length > 6 && d.getData().length < 8 && id == i) {
                result.add(d);
            }
        }
        return result;
    }

}
