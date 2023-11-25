package com.dragon.lucky.command8;

import com.dragon.lucky.bean.BaseControlNumberBean;
import com.dragon.lucky.bean.ContentBean;
import com.dragon.lucky.bean.ControlBean;
import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GenerateHelper {

    public static GenerateHelper getInstance() {
        return new GenerateHelper();
    }


    //获取条件数组列表
    public static List<BaseControlNumberBean> getBaseControlNumbers(List<ControlBean> adapterData) {
        List<BaseControlNumberBean> result = new ArrayList<>();
        List<ControlBean> cbs = new ArrayList<>();
        for (int i = 0; i < adapterData.size(); i++) {
            ControlBean tempData = adapterData.get(i);
            if (tempData.getItemType() == ContentBean.ITEM_TYPE_CONDITION_LAYOUT) {
                cbs.add(tempData);
            } else {
                break;
            }
        }
        for (int i = 0; i < cbs.size(); i++) {
            ControlBean cb = cbs.get(i);
            BaseControlNumberBean bcn = getBaseControlNumber(cb);
            if (bcn == null) {
                return null;
            }
            result.add(bcn);
        }
        return result;
    }

    //获取条件数组
    public static BaseControlNumberBean getBaseControlNumber(ControlBean cb) {
        BaseControlNumberBean bcn = new BaseControlNumberBean();
        String baseNumber = cb.getControl().trim();
        if (baseNumber.length() <= 0 || !Utils.isNumeric2(baseNumber) || Utils.isNumeric3(baseNumber)) {
            Log.i("baseNumber = " + baseNumber);
            return null;
        }
        String basePrintNumber = cb.getNumber().trim();
        if (basePrintNumber.length() <= 0 || !Utils.isNumeric2(basePrintNumber) || Utils.isNumeric3(basePrintNumber)) {
            Log.i("basePrintNumber = " + basePrintNumber);
            return null;
        }
        String[] baseNumbers = baseNumber.split("、");
        List<Byte> baseNumberIntegers = new ArrayList<>();
        for (int i = 0; i < baseNumbers.length; i++) {
            if (Utils.isNumeric(baseNumbers[i])) {
                byte baseNumberInteger = Byte.parseByte(baseNumbers[i]);
                if (!baseNumberIntegers.contains(baseNumberInteger)) {
                    baseNumberIntegers.add(baseNumberInteger);
                }
            }
        }
        String[] basePrintNumbers = basePrintNumber.split("、");
        List<Integer> basePrintNumberIntegers = new ArrayList<>();
        for (int i = 0; i < basePrintNumbers.length; i++) {
            if (Utils.isNumeric(basePrintNumbers[i])) {
                int baseNumberInteger = Integer.parseInt(basePrintNumbers[i]);
                if (!basePrintNumberIntegers.contains(baseNumberInteger)) {
                    basePrintNumberIntegers.add(baseNumberInteger);
                }
            }
        }
        bcn.setBaseNumber(baseNumberIntegers);
        bcn.setPrintNumber(basePrintNumberIntegers);
        return bcn;
    }


    //获取条件数组列表
    public static List<BaseControlNumberBean> getBaseControlNumbers(List<ResultBean> adapterData, int size) {
        List<BaseControlNumberBean> result = new ArrayList<>();
        for (int i = 0; i < adapterData.size(); i++) {
            ResultBean cb = adapterData.get(i);
            BaseControlNumberBean bcn = getBaseControlNumber(cb, size);
            if (bcn == null) {
                return null;
            }
            result.add(bcn);
        }
        return result;
    }

    //获取条件数组
    public static BaseControlNumberBean getBaseControlNumber(ResultBean cb, int size) {
        BaseControlNumberBean bcn = new BaseControlNumberBean();
        List<Byte> baseNumberIntegers = new ArrayList<>();
        for (int i = 0; i < cb.getData().length; i++) {
            byte baseNumberInteger = cb.getData()[i];
            if (!baseNumberIntegers.contains(baseNumberInteger)) {
                baseNumberIntegers.add(baseNumberInteger);
            }
        }
        List<Integer> basePrintNumberIntegers = new ArrayList<>();
        basePrintNumberIntegers.add(size);
        bcn.setBaseNumber(baseNumberIntegers);
        bcn.setPrintNumber(basePrintNumberIntegers);
//        Log.i("data = " + baseNumberIntegers.toString());
//        Log.i("basePrintNumberIntegers = " + basePrintNumberIntegers);
        return bcn;
    }

    public static List<ResultBean> getResultBeans(List<ResultBean> data, List<Integer> dataIdx) {
        HashSet<Integer> complexData = new HashSet<>(dataIdx);
        dataIdx.clear();
        dataIdx.addAll(complexData);
        dataIdx.sort(Integer::compareTo);
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < dataIdx.size(); i++) {
            result.add(data.get(dataIdx.get(i)));
        }
        return result;
    }

}
