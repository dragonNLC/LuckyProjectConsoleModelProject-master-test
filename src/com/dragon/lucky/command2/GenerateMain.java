package com.dragon.lucky.command2;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.command.CommandBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.*;

public class GenerateMain {

    public static void main(String[] args) throws IOException {
        CommandBean commandBean = analysisCommand(args);
        if (!FileReadHelper.checkFileExists(commandBean.getInputPath())) {
            Log.i("导入文件不存在");
            return;
        }
        List<String> data = FileReadHelper.readFile(commandBean.getInputPath());
        List<ControlBean> cb = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String singleControlLine = data.get(i);
            Log.i("辅助条件" + (i - 1) + "：" + singleControlLine);
            if (singleControlLine.contains("-")) {
                String[] controlLines = singleControlLine.split("-");
                cb.add(new ControlBean(controlLines[0], controlLines[1]));
            }
        }
        List<BaseControlNumberBean> bcnData = getBaseControlNumbers(cb);
        BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
        if (bcnData == null) {
            Log.i("参考数列表数据有误！");
            return;
        } else {
            bgn.setBaseControlNumbers(bcnData);
        }
        List<GenerateResultBean> result = new ArrayList<>();
        for (int i = 0; i < bcnData.size(); i++) {
            BaseControlNumberBean bcn = bcnData.get(i);
            List<Integer> control = bcn.getPrintNumber();
            List<ResultBean2> resultData = new ArrayList<>();
            for (int j = 0; j < control.size(); j++) {
                int c = control.get(j);
                if (c == 0) {//存一个标志位就行
                    resultData.add(new ResultBean2());
                } else if (c > 0) {
                    int[] resultArr = new int[c];
                    Utils.combine_increase2(bcn.getBaseNumber(), 0, resultArr, c, c, bcn.getBaseNumber().size(), resultData, new long[1]);
                }
            }
            result.add(new GenerateResultBean(resultData));
        }
        Log.i("正在进行第一次合并。。。");
        generateLastData(commandBean, result);
    }

    private static void generateLastData(CommandBean commandBean, List<GenerateResultBean> result) {
        GenerateResultBean grb = result.get(0);
        GenerateResultBean grb2 = result.get(1);
        List<ResultBean2> tempData1 = grb.getData();
        List<ResultBean2> tempData2 = grb2.getData();
        List<ResultBean2> lastData = new ArrayList<>();
        List<String> filterString = new ArrayList<>();
        Log.i("正在执行求并集...");
        Log.i("j time ================== " + System.currentTimeMillis() + "     tempData1.size() " + tempData1.size() + "   tempData2.size() " + tempData2.size());
        for (int j = 0; j < tempData1.size(); j++) {
            ResultBean2 td1 = tempData1.get(j);
            for (int k = 0; k < tempData2.size(); k++) {
                ResultBean2 td2 = tempData2.get(k);
                int size = td1.getData().size() + td2.getData().size();
                if (size == 0) {
                    lastData.add(new ResultBean2());
                } else {
                    List<Byte> byteData = new ArrayList<>();
                    if (td1.getData().size() == 0) {
                        byteData.addAll(td2.getData());
                    } else if (td2.getData().size() == 0) {
                        byteData.addAll(td1.getData());
                    } else {
                        byteData.addAll(td1.getData());
                        //去重
//                        Log.i("filter time ================== " + System.currentTimeMillis());
                        for (int i = 0; i < td2.getData().size(); i++) {
                            if (!byteData.contains(td2.getData().get(i))) {
                                byteData.add(td2.getData().get(i));
                            }
                        }
                        //去重
//                        Log.i("filter time ----------------- " + System.currentTimeMillis());
                    }
                    //判断结果数是不是超过了需要的位数
                    if (byteData.size() <= commandBean.getGenerateSize()) {
//                        Log.i("sort time ================== " + System.currentTimeMillis());
                        byteData.sort(Byte::compareTo);
//                        Log.i("sort time ----------------- " + System.currentTimeMillis());
//                        Log.i("contains time ================== " + System.currentTimeMillis());
                        if (!filterString.contains(byteData.toString())) {
                            filterString.add(byteData.toString());
                            lastData.add(new ResultBean2(byteData, -1));
                        }
//                        Log.i("contains time ----------------- " + System.currentTimeMillis());
                    }
                }
            }
        }
        Log.i("j time ----------------- " + System.currentTimeMillis());
        Log.i("求并集结束...");
        result.remove(0);
        result.remove(0);
        //去除ArrayList里面的重数
//        Log.i("本次合并数据结束，正在去重。。。需要运算的数据量：" + lastData.size());
        /*List<ResultBean2> data = new ArrayList<>();
        for (int i = 0; i < lastData.size(); i++) {
            if (!filterString.contains(lastData.get(i).getData().toString())) {
                Log.i("i = " + i + "------" + lastData.size());
                filterString.add(lastData.get(i).getData().toString());
                data.add(lastData.get(i));
            }
        }*/
        result.add(0, new GenerateResultBean(lastData));
//        Log.i("本次去重结束。。。");

        if (result.size() == 1) {
            //处理完成
            Log.i("处理完成，结果数字：" + result.get(0).getData().size());
            //排序
            GenerateResultBean tempGrb = result.get(0);
            List<ResultBean2> tempResultBean = tempGrb.getData();

            List<ResultBean2Temp> tempResultBean2 = new ArrayList<>();
            for (int i = 0; i < tempResultBean.size(); i++) {
                ResultBean2 rb2 = tempResultBean.get(i);
                Log.i("rb2.getData().toString() = " + rb2.getData().toString());
                tempResultBean2.add(new ResultBean2Temp(rb2.getData().toString(), i));
            }
            tempResultBean2.sort(new Comparator<ResultBean2Temp>() {
                @Override
                public int compare(ResultBean2Temp o1, ResultBean2Temp o2) {
                    return o1.getTag().compareTo(o2.getTag());
                }
            });
            List<ResultBean2> printData = new ArrayList<>();
            for (int i = 0; i < tempResultBean2.size(); i++) {
                ResultBean2Temp resultBean2Temp = tempResultBean2.get(i);
                if (tempResultBean.get(resultBean2Temp.getId()).getData().size() < 5) {
                    continue;
                }
                printData.add(tempResultBean.get(resultBean2Temp.getId()));
            }
            try {
                FileReadHelper.writeToFile2(commandBean.getOutputPath(), printData);
            } catch (IOException e) {
                e.printStackTrace();
            }
         //输出结果
        } else {
            Log.i("正在处理下一条结果！");
            generateLastData(commandBean, result);
        }
    }



    /*public static void generateData(List<BaseControlNumberBean> controlNumberBeans, int id, List<GenerateResultBean> result, int mergeCount) {
        BaseControlNumberBean bcn = controlNumberBeans.get(id);
        List<Integer> control = bcn.getPrintNumber();
        for (int j = 0; j < control.size(); j++) {//获取需要生成的数量列表
            GenerateResultBean grb = new GenerateResultBean();
            int controlSize = control.get(j);
            if (controlSize > 0) {
                int[] resultArr = new int[controlSize];
                List<ResultBean> resultData = new ArrayList<>();
                Utils.combine_increase(bcn.getBaseNumber(), 0, resultArr, controlSize, controlSize, bcn.getBaseNumber().size(), resultData, new long[1], mergeCount);
                grb.getData().add(new DataBean(resultData));
            }
            result.add(grb);
        }
        id++;
        if (id < controlNumberBeans.size()) {
            generateData(controlNumberBeans, id, result, mergeCount);
        }
    }*/

    //根据输入内容获取实际指令
    public static CommandBean analysisCommand(String[] args) {
        CommandBean command = null;
        if (args != null && args.length > 0) {
            command = new CommandBean();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-i"://输入原始数据
                        command.setInputPath(args[++i]);
                        break;
                    case "-o"://输出结果文件
                        command.setOutputPath(args[++i]);
                        break;
                    case "-s"://输出结果文件
                        command.setGenerateSize(Integer.parseInt(args[++i]));
                        break;
                }
            }
        } else {
            Log.i("请输入操作指令，并以空格隔开：\n-i(输入原始数据文件)；\n-o（输出结果文件）;\n-s(输出目标数)");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                String commands = scanner.nextLine();
                String[] commandSplit = commands.split(" ");
                scanner.close();
                return analysisCommand(commandSplit);
            }
        }
        return command;
    }

    //获取条件数组列表
    private static List<BaseControlNumberBean> getBaseControlNumbers(List<ControlBean> adapterData) {
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

}
