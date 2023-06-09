package com.dragon.lucky;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Deprecated
public class Main4 {

    public static String inputPath;
    public static String outPath;

    public static final int SIZE_COUNT = 12;//修改多少组创建一个线程

    public static MyCallBackListener myCallBackListener = new MyCallBackListener();//筛选完毕之后的回调方法，进入求并集

    //入口函数
    public static void main(String[] args) throws IOException {

        inputPath = "";
        outPath = "";
        if (args != null && args.length == 2) {
            inputPath = args[0];
            outPath = args[1];
        } else {
            System.out.println("请输入文本位置与输出文件位置，并以空格键隔开...");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String commands = scanner.nextLine();
                String[] commandSplit = commands.split(" ");
                if (commandSplit.length > 1) {
                    inputPath = commandSplit[0];
                    outPath = commandSplit[1];
                    scanner.close();
                    break;
                } else if (inputPath.equals("")) {
                    inputPath = commands;
                } else {
                    outPath = commands;
                    scanner.close();
                    break;
                }
            }
        }
        Utils.METHOD_COUNT = 0;
        new Thread(new RunnableThread(inputPath, outPath))
                .start();
        for (; ; ) {
        }
    }

    //开始生成数据的方法
    private static boolean generate(String path, String outPath) throws IOException {
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
//        Log.i("结果条数：" + resultBeans.size());
//        FileReadHelper.writeToFile(outPath, resultBeans);
//        Log.i("处理完成！");
        return true;
    }

    //获取条件数组列表
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

    //求并集类
    public static class MyCallBackListener implements CallbackListener {

        private CopyOnWriteArrayList<String> threadName;
        private CopyOnWriteArrayList<RunThread> threads;

        public MyCallBackListener() {
            threadName = new CopyOnWriteArrayList<>();
            threads = new CopyOnWriteArrayList<>();
        }

        @Override
        public synchronized void onThreadCompile(String name, RunThread thread) {
//            Log.i("onThreadCompile");
            threadName.remove(name);
            threads.add(thread);
            List<ResultBean> result = new ArrayList<>();
            if (threadName.size() <= 0) {
                if (threads.size() > 1) {
                    //完成了，开始求并集
                    Log.i("正在求并集...");
                    for (int i = 1; i < threads.size(); i++) {
                        if (i == 1) {
                            RunThread singleThread = threads.get(0);
                            RunThread singleThread2 = threads.get(1);
                            result.clear();
                            result.addAll(getContains(singleThread.resultBeans, singleThread2.resultBeans));
                            Log.i("singleThread.resultBeans = " + singleThread.resultBeans.size());
                            Log.i("singleThread2.resultBeans = " + singleThread2.resultBeans.size());
                            Log.i("result = " + result.size());
                        } else {
                            RunThread singleThread2 = threads.get(i);
                            List<ResultBean> temp = getContains(result, singleThread2.resultBeans);
                            Log.i("result.resultBeans = " + result.size());
                            Log.i("singleThread2.resultBeans = " + singleThread2.resultBeans.size());
                            Log.i("temp = " + temp.size());
                            result.clear();
                            result.addAll(temp);
                        }
                    }
                } else {
                    result.addAll(threads.get(0).resultBeans);
                }
                try {
                    FileReadHelper.writeToFile(outPath, result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(threads.size() + " 处理完成：结果数-" + result.size());
            }
        }

        @Override
        public void addThread(String name) {
//            Log.i("addThread");
            threadName.add(name);
        }
    }

    private static List<ResultBean> generateResults(BaseGenerateNumberBean bgn) {
        List<ResultBean> result = new ArrayList<>();
        List<ResultBean> resultBeans = new ArrayList<>();
        int[] resultArr = new int[bgn.getPrintNumber()];
        Utils.combine_increase(bgn.getBaseNumbers(), 0, resultArr, bgn.getPrintNumber(), bgn.getPrintNumber(), bgn.getBaseNumbers().size(), result, new long[1], -1);

//        Log.e("TAG", "result = " + result.size());
        //拿到最终结果数，开始跟条件进行比对
        List<BaseControlNumberBean> controls = bgn.getBaseControlNumbers();
        if (controls.size() > 0) {
            int sizeCount = SIZE_COUNT;
            int size = controls.size() / SIZE_COUNT;
            Log.i("size = " + size);
            if (size > 0) {
                int last = controls.size() % SIZE_COUNT;
                if (last > 0) {
                    size++;
                    for (int i = 0; i < size; i++) {
                        if (i == size - 1) {
                            Thread thread = new RunThread(new ArrayList<>(result), controls.subList(i * sizeCount, controls.size()), myCallBackListener);
                            myCallBackListener.addThread(thread.getName());
                            thread.start();
                        } else {
                            Thread thread = new RunThread(new ArrayList<>(result), controls.subList(i * sizeCount, (i + 1) * sizeCount), myCallBackListener);
                            myCallBackListener.addThread(thread.getName());
                            thread.start();
                        }
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        Thread thread = new RunThread(new ArrayList<>(result), controls.subList(i * sizeCount, (i + 1) * sizeCount), myCallBackListener);
                        myCallBackListener.addThread(thread.getName());
                        thread.start();
                    }
                }
            } else {
                filterResultBean(result, controls, 0, resultBeans);
                resultBeans.sort(Comparator.comparingLong(ResultBean::getId));
                try {
                    FileReadHelper.writeToFile(outPath, resultBeans);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(" 处理完成：结果数-" + resultBeans.size() /*+ "   " + resultBeans.toString()*/);
            }
        } else {
            resultBeans.addAll(result);
            try {
                FileReadHelper.writeToFile(outPath, resultBeans);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(" 处理完成：结果数-" + resultBeans.size()/* + "   " + resultBeans.toString()*/);
        }
        return resultBeans;
    }


    public static int FILTER_COUNT = 0;//记录方法运行次数
    //取得数据然后取并集
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
//        Log.i("筛选次数：" + FILTER_COUNT);
    }

    private static List<ResultBean> getContains(List<ResultBean> data, List<Integer> baseData, int rule) {
        List<ResultBean> resultData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ResultBean children = data.get(i);
//            int[] intArr = children.getData();
//            int containCount = 0;
//            for (int j = 0; j < intArr.length; j++) {
//                if (baseData.contains(intArr[j])) {
//                    containCount++;
//                }
//            }
//            if (containCount == rule) {
//                resultData.add(children);
//            }
        }
        return resultData;
    }

    private static List<ResultBean> getContains(List<ResultBean> data, List<ResultBean> data2) {
        List<ResultBean> resultData = new ArrayList<>();
        /*int id = 0;
        try {
            FileReadHelper.writeToFile("H:\\x1.txt", data);
            FileReadHelper.writeToFile("H:\\x2.txt", data2);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            ResultBean children = data.get(i);
            for (int j = 0; j < data2.size(); j++) {
                ResultBean children2 = data2.get(j);
//                Log.i("equals = " + Arrays.toString(children.getData()) + "    " + Arrays.toString(children2.getData()));
//                Log.i("equals2 = " + Arrays.equals(children.getData(), children2.getData()));
//                sb.append("equals = " + Arrays.toString(children.getData()) + "    " + Arrays.toString(children2.getData()));
//                sb.append("\n");
//                sb.append("equals2 = " + Arrays.equals(children.getData(), children2.getData()));
//                sb.append("\n");
                if (Arrays.equals(children.getData(), children2.getData())) {
//                    Log.i("equals = " + Arrays.toString(children.getData()) + "    " + Arrays.toString(children2.getData()));
                    resultData.add(children);
                    break;
                }
            }
        }
        /*try {
            FileReadHelper.writeToFile("H:\\log.txt", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return resultData;
    }

    //筛选的处理线程
    public static class RunThread extends Thread {

        List<ResultBean> result;
        List<BaseControlNumberBean> controlBean;
        public List<ResultBean> resultBeans = new ArrayList<>();
        CallbackListener callbackListener;

        public RunThread(List<ResultBean> resultBeans, List<BaseControlNumberBean> controlBean, CallbackListener callbackListener) {
            this.result = resultBeans;
            this.controlBean = controlBean;
            this.callbackListener = callbackListener;
        }

        @Override
        public void run() {
            super.run();
            Log.i("线程：" + currentThread().getName());
            filterResultBean(result, controlBean, 0, resultBeans);
            resultBeans.sort(Comparator.comparingLong(ResultBean::getId));
            Log.i("线程结束：" + currentThread().getName() + "   " + resultBeans.size());
            if (callbackListener != null) {
                callbackListener.onThreadCompile(currentThread().getName(), this);
            }
        }
    }

    //开始生成数组的线程
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

    public interface CallbackListener {
        void onThreadCompile(String name, RunThread thread);

        void addThread(String name);
    }


}
