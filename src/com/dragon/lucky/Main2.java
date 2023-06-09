package com.dragon.lucky;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class Main2 {

    /*public static void main(String[] args) throws IOException {
        String input = "";
        String output = "";
        if (args != null && args.length == 2) {
            input = args[0];
            output = args[1];
        } else {
            System.out.println("请输入文本位置与输出文件位置，并以空格键隔开...");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String commands = scanner.nextLine();
                String[] commandSplit = commands.split(" ");
                if (commandSplit.length > 1) {
                    input = commandSplit[0];
                    output = commandSplit[1];
                    scanner.close();
                    break;
                } else if (input.equals("")){
                    input = commands;
                } else {
                    output = commands;
                    scanner.close();
                    break;
                }
            }
        }
        Utils.METHOD_COUNT = 0;
        new Thread(new RunnableThread(input, output))
                .start();
    }*/

    private static boolean  generate(String path, String outPath) throws IOException {
        if (!FileReadHelper.checkFileExists(path)) {
            Log.i("文件不存在");
            return false;
        }
        List<String> data = FileReadHelper.readFile(path);

        BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
        if (data.size() > 1) {
            HeadContentBean hcb = new HeadContentBean(data.get(0), data.get(1));
            String baseNumber = hcb.getControl();
            if (baseNumber.length() <= 0 /*|| !Utils.isNumeric2(baseNumber) || Utils.isNumeric3(baseNumber)*/) {
                Log.i("请输入正确的基础数字");
//                Log.i("baseNumber.length() = " + baseNumber.length());
//                Log.i("!Utils.isNumeric2(baseNumber) = " + !Utils.isNumeric2(baseNumber));
//                Log.i("Utils.isNumeric3(baseNumber) = " + Utils.isNumeric3(baseNumber));
                return false;
            }
            String basePrintNumber = hcb.getNumber();
            if (basePrintNumber.length() <= 0 || !Utils.isNumeric(basePrintNumber) || Integer.parseInt(basePrintNumber) <= 0) {
                Log.i("请输入正确的最终选择个数");
                return false;
            }

            Log.i("基本数字：" + baseNumber);
            Log.i("筛选个数：" + basePrintNumber);

            String[] baseNumbers = baseNumber.split("、");
            List<Integer> baseNumberIntegers = new ArrayList<>();
            for (int i = 0; i < baseNumbers.length; i++) {
                if (Utils.isNumeric(baseNumbers[i])) {
                    int baseNumberInteger = Integer.parseInt(baseNumbers[i]);
                    if (!baseNumberIntegers.contains(baseNumberInteger)) {
                        baseNumberIntegers.add(baseNumberInteger);
                    }
                }
            }
//            bgn.setBaseNumbers(baseNumberIntegers);
            bgn.setPrintNumber(Integer.parseInt(basePrintNumber));

            if (bgn.getPrintNumber() > baseNumberIntegers.size()) {
                Log.i("最终选择个数不能大于基础数据个数！");
                return false;
            }
        }

        List<ControlBean> cb = new ArrayList<>();
        for (int i = 2; i < data.size(); i++) {
            String singleControlLine = data.get(i);
            Log.i("辅助条件" + (i - 1) + "：" + singleControlLine);
            if (singleControlLine.contains("-")) {
                String[] controlLines = singleControlLine.split("-");
                cb.add(new ControlBean(controlLines[0], controlLines[1]));
            }
        }
//        Log.i("cb = " + cb.size());

        List<BaseControlNumberBean> bcnData = getBaseControlNumbers(cb);
        if (bcnData == null) {
            Log.i("参考数列表数据有误！");
            return false;
        } else {
//            Log.i("bcnData = " + bcnData.size());
            bgn.setBaseControlNumbers(bcnData);
        }
        List<ResultBean> resultBeans = generateResults(bgn);
        Log.i("结果条数：" + resultBeans.size());
        FileReadHelper.writeToFile(outPath, resultBeans);
        Log.i("处理完成！");
        return true;
    }

    private static List<BaseControlNumberBean> getBaseControlNumbers(List<ControlBean> adapterData) {
        List<BaseControlNumberBean> result = new ArrayList<>();
        List<ControlBean> cbs = new ArrayList<>();
        for (int i = 0; i < adapterData.size(); i++) {
            ControlBean tempData = adapterData.get(i);
//            Log.e("TAG", "tempData.getItemType()：" + tempData.getItemType());
            if (tempData.getItemType() == ContentBean.ITEM_TYPE_CONDITION_LAYOUT) {
                cbs.add(tempData);
            } else {
                break;
            }
        }

//        Log.e("TAG", "结果：" + adapterData.size());
//        Log.e("TAG", "结果：" + cbs.size());

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

    private static BaseControlNumberBean getBaseControlNumber(ControlBean cb) {
        BaseControlNumberBean bcn = new BaseControlNumberBean();
        String baseNumber = cb.getControl().trim();
        if (baseNumber.length() <= 0 || !Utils.isNumeric2(baseNumber) || Utils.isNumeric3(baseNumber)) {
            return null;
        }
        String basePrintNumber = cb.getNumber().trim();
        if (basePrintNumber.length() <= 0 || !Utils.isNumeric2(basePrintNumber) || Utils.isNumeric3(basePrintNumber)) {
            return null;
        }
        String[] baseNumbers = baseNumber.split("、");
        List<Integer> baseNumberIntegers = new ArrayList<>();
        for (int i = 0; i < baseNumbers.length; i++) {
            if (Utils.isNumeric(baseNumbers[i])) {
                int baseNumberInteger = Integer.parseInt(baseNumbers[i]);
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
//        bcn.setBaseNumber(baseNumberIntegers);
        bcn.setPrintNumber(basePrintNumberIntegers);
        return bcn;
    }

    private static List<ResultBean> generateResults(BaseGenerateNumberBean bgn) {
        List<ResultBean> result = new ArrayList<>();
        List<ResultBean> resultBeans = new ArrayList<>();
        int[] resultArr = new int[bgn.getPrintNumber()];
        Utils.combine_increase(bgn.getBaseNumbers(), 0, resultArr, bgn.getPrintNumber(), bgn.getPrintNumber(), bgn.getBaseNumbers().size(), result, new long[1], -1);

//        Log.e("TAG", "result = " + result.size());
        //拿到最终结果数，开始跟条件进行比对
        List<BaseControlNumberBean> controls = bgn.getBaseControlNumbers();
//        Log.e("TAG", "controls = " + controls.size());
        if (controls.size() > 0) {
            filterResultBean(result, controls, 0, resultBeans);
            resultBeans.sort(Comparator.comparingLong(ResultBean::getId));
        } else {
            resultBeans.addAll(result);
        }
        return resultBeans;
    }

    private static void filterResultBean(List<ResultBean> base, List<BaseControlNumberBean> filter, int position, List<ResultBean> resultData) {
        if (position == filter.size()) {
            resultData.addAll(base);
            return;
        }
        BaseControlNumberBean bcn = filter.get(position);
        List<Integer> printRules = bcn.getPrintNumber();

        for (int j = 0; j < printRules.size(); j++) {
            int rule = printRules.get(j);//拿到生成规则
            //得到符合规则的数据
//            List<ResultBean> result = new ArrayList<>(getContains(base, bcn.getBaseNumber(), rule));
//
//            filterResultBean(result, filter, position + 1, resultData);
        }
        FILTER_COUNT++;
        Log.i("筛选次数：" + FILTER_COUNT);
    }



    public static int FILTER_COUNT = 0;

    private static List<ResultBean> getContains(List<ResultBean> data, List<Integer> baseData, int rule) {
        List<ResultBean> resultData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ResultBean children = data.get(i);
//            int[] intArr = children.getData();
            int[] intArr = new int[1];
            int containCount = 0;
            for (int j = 0; j < intArr.length; j++) {
                if (baseData.contains(intArr[j])) {
                    containCount++;
                }
            }
            if (containCount == rule) {
                resultData.add(children);
            }
        }
        return resultData;
    }

    public static class RunnableThread implements Runnable {

        private String path;
        private String outPath;

        public RunnableThread(String path, String outPath) {
            this.path = path;
            this.outPath = outPath;
        }

        @Override
        public void run() {
            try {
                generate(path, outPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
