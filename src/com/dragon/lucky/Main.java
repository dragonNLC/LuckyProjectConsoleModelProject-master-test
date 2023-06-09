package com.dragon.lucky;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class Main {

    public static void main(String[] args) throws IOException {
//        generate("C:\\Users\\aptdev\\Desktop\\新建文本文档.txt");
    }

    private static boolean generate(String path) throws IOException {
        if (!FileReadHelper.checkFileExists(path)) {
            Log.i("请输入正确的基础数字");
            return false;
        }
        List<String> data = FileReadHelper.readFile(path);
        String baseNumber = data.get(0);
        if (baseNumber.length() <= 0 || !isNumeric2(baseNumber) || isNumeric3(baseNumber)) {
            Log.i("请输入正确的基础数字");
            return false;
        }
        String basePrintNumber = data.get(1);
        if (basePrintNumber.length() <= 0 || !isNumeric(basePrintNumber)) {
            Log.i("请输入正确的最终选择个数");
            return false;
        }
        String[] baseNumbers = baseNumber.split("、");
        List<Integer> baseNumberIntegers = new ArrayList<>();
        for (int i = 0; i < baseNumbers.length; i++) {
            if (isNumeric(baseNumbers[i])) {
                int baseNumberInteger = Integer.parseInt(baseNumbers[i]);
                if (!baseNumberIntegers.contains(baseNumberInteger)) {
                    baseNumberIntegers.add(baseNumberInteger);
                }
            }
        }
        BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
//        bgn.setBaseNumbers(baseNumberIntegers);
        bgn.setPrintNumber(Integer.parseInt(basePrintNumber));

        if (bgn.getPrintNumber() > baseNumberIntegers.size()) {
            bgn.setPrintNumber(baseNumberIntegers.size());
        }

        List<BaseControlNumberBean> bcnData = getBaseControlNumbers(/*bgn.getBaseNumbers()*/new ArrayList<>(), data.subList(2, data.size()));
        if (bcnData == null) {
            Log.e("TAG", "参考数列表数据有误！");
            return false;
        } else {
            bgn.setBaseControlNumbers(bcnData);
        }
        //开始随机计算
        if (bgn.getBaseControlNumbers().size() <= 0) {
            //清除数据，并开始计算
//            List<ResultBean> result = generateResultData(bgn.getBaseNumbers(), bgn.getPrintNumber());
//            结果集
//            FileReadHelper.writeToFile("H:\\text.txt", result);
        } else {
            generateResult(bgn);
        }
        //开始计算随机数


        Log.i("成功！");
        return true;
    }

    private static List<BaseControlNumberBean> getBaseControlNumbers(List<Integer> baseNumber, List<String> control) {
        List<BaseControlNumberBean> result = new ArrayList<>();
        List<ControlBean> cb = new ArrayList<>();
        for (int i = 0; i < control.size(); i++) {
            String singleControlLine = control.get(i);
            Log.i("singleControlLine=  " + singleControlLine);
            if (singleControlLine.contains("-")) {
                String[] controlLines = singleControlLine.split("-");
                cb.add(new ControlBean(controlLines[0], controlLines[1]));
            }
        }

        for (int i = 0; i < cb.size(); i++) {
            BaseControlNumberBean bcn = getBaseControlNumber(baseNumber, cb.get(i));
            if (bcn == null) {
                return null;
            }
            result.add(bcn);
        }
        return result;
    }

    private static BaseControlNumberBean getBaseControlNumber(List<Integer> base, ControlBean cb) {
        BaseControlNumberBean bcn = new BaseControlNumberBean();
        String baseNumber = cb.getControl().trim();
        if (baseNumber.length() <= 0 || !isNumeric2(baseNumber) || isNumeric3(baseNumber)) {
            return null;
        }
        String basePrintNumber = cb.getNumber().trim();
        if (basePrintNumber.length() <= 0 || !isNumeric2(basePrintNumber) || isNumeric3(basePrintNumber)) {
            return null;
        }
        String[] baseNumbers = baseNumber.split("、");
        List<Integer> baseNumberIntegers = new ArrayList<>();
        for (int i = 0; i < baseNumbers.length; i++) {
            if (isNumeric(baseNumbers[i])) {
                int baseNumberInteger = Integer.parseInt(baseNumbers[i]);
                if (!baseNumberIntegers.contains(baseNumberInteger) && base.contains(baseNumberInteger)) {
                    baseNumberIntegers.add(baseNumberInteger);
                }
            }
        }
        String[] basePrintNumbers = basePrintNumber.split("、");
        List<Integer> basePrintNumberIntegers = new ArrayList<>();
        for (int i = 0; i < basePrintNumbers.length; i++) {
            if (isNumeric(basePrintNumbers[i])) {
                int baseNumberInteger = Integer.parseInt(basePrintNumbers[i]);
                if (!basePrintNumberIntegers.contains(baseNumberInteger)) {
                    basePrintNumberIntegers.add(baseNumberInteger);
                }
            }
        }
//        bcn.setBaseNumber(baseNumberIntegers);
        bcn.setPrintNumber(basePrintNumberIntegers);
        return bcn;
    }

    private static void generateResult(BaseGenerateNumberBean bgn) throws IOException {
        List<BaseControlNumberBean> bcn = bgn.getBaseControlNumbers();

        List<ResultBean> resultData = new ArrayList<>();
        for (int i = 0; i < bcn.size(); i++) {
            BaseControlNumberBean b = bcn.get(i);
//            List<ResultBean> result = generateResultData(bgn.getBaseNumbers(), bgn.getPrintNumber(), b);
//            if (result.size() > 0 && i != bcn.size() - 1) {
//                result.get(result.size() - 1).setLast(true);
//            }
//            resultData.addAll(result);
        }
        if (resultData.size() > 1) {
            int size = bgn.getPrintNumber();
            for (int i = 0; i < size; i++) {
                int finalI = i;
                /*resultData.sort(new Comparator<ResultBean>() {
                    @Override
                    public int compare(ResultBean o1, ResultBean o2) {
                        if (finalI != 0) {
                            if (o1.getData().get(finalI - 1).getNumber() != o2.getData().get(finalI - 1).getNumber()) {
                                return 0;
                            } else {
                                return Integer.compare(o1.getData().get(finalI).getNumber(), o2.getData().get(finalI).getNumber());
                            }
                        } else {
                            return Integer.compare(o1.getData().get(finalI).getNumber(), o2.getData().get(finalI).getNumber());
                        }
                    }
                });*/
            }
        }
        //得出结果
        FileReadHelper.writeToFile("H:\\text.txt", resultData);
    }

    private static List<ResultBean> generateResultData(List<Integer> baseData, int randomSize, BaseControlNumberBean b) {
        List<ResultBean> result = new ArrayList<>();
        List<Integer> printNumber = b.getPrintNumber();
        for (int i = 0; i < printNumber.size(); i++) {
            ResultBean rb = new ResultBean();
            List<Byte> baseNumber = new ArrayList<>(b.getBaseNumber());
            List<PointBean> number = new ArrayList<>();
            int p = printNumber.get(i);
            //这里的p表示前面有多少位是需要在结果里面显示的
            if (p > b.getBaseNumber().size()) {
                p = b.getBaseNumber().size();
            }
            //比实际要随机出的数据还多，那按照实际随机的长度来随机
            if (p > randomSize) {
                p = randomSize;
            }
            //根据数量来决定随机出哪些数
            for (int j = 0; j < p; j++) {
                int resultIdx = (int) (Math.random() * baseNumber.size());
                int resultInt = baseNumber.remove(resultIdx);
                number.add(new PointBean(resultInt, PointBean.POINT_SPE));
            }
            //得出必须要出现的数据之后，开始计算剩余显示的数据
            List<Integer> copyBaseData = new ArrayList<>(baseData);
            int less = randomSize - p;
            if (less > 0) {
                //先移除已经随机出来的，而且在基础数据中已有的数字先
                for (int j = 0; j < number.size(); j++) {
                    copyBaseData.remove((Integer) number.get(j).getNumber());
                }
                for (int j = 0; j < less; j++) {
                    //开始随机剩余的
                    int resultIdx = (int) (Math.random() * copyBaseData.size());
                    int resultInt = copyBaseData.remove(resultIdx);
                    number.add(new PointBean(resultInt, PointBean.POINT_NONE));
                }
            }
            number.sort(Comparator.comparingInt(PointBean::getNumber));
//            rb.setData(number);
            result.add(rb);
//            Log.e("TAG", "得出的随机数结果：" + number);
        }
        return result;
    }

    private static List<ResultBean> generateResultData(List<Integer> baseData, int randomSize) {
        List<ResultBean> result = new ArrayList<>();
        ResultBean rb = new ResultBean();
        List<PointBean> number = new ArrayList<>();
        //得出必须要出现的数据之后，开始计算剩余显示的数据
        List<Integer> copyBaseData = new ArrayList<>(baseData);
        if (randomSize > baseData.size()) {
            randomSize = baseData.size();
        }
        for (int j = 0; j < randomSize; j++) {
            //开始随机剩余的
            int resultIdx = (int) (Math.random() * copyBaseData.size());
            int resultInt = copyBaseData.remove(resultIdx);
            number.add(new PointBean(resultInt, PointBean.POINT_NONE));
        }
        number.sort(Comparator.comparingInt(PointBean::getNumber));
//        rb.setData(number);
        result.add(rb);
        return result;
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
