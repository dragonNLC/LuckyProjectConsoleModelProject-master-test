package com.dragon.lucky.command7;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.command2.GenerateResultBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;
import com.sun.org.apache.xpath.internal.operations.Div;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenerateRunner {

    private CommandBean mCommand;
    private List<Integer> compileData = new ArrayList<>();
    private int threadCount;
    private int compileThreadCount;

    private CopyOnWriteArrayList<String> threadName = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<RunThread> threads = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<String> divideThreadName = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<DivideThread> divideThreads = new CopyOnWriteArrayList<>();

    private int currentFilterThreadId;//当前执行中的最后一个线程id

//    public static MyCallBackListener myCallBackListener = new MyCallBackListener();//筛选完毕之后的回调方法，进入求并集

    private GenerateRunner() {

    }

    public static GenerateRunner getInstance() {
        return new GenerateRunner();
    }

    public static List<ResultBean> allGenerateData;
    private int currentGenerateIdx;

    private CallbackListener mCallBackListener;

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        String tempPath = mCommand.getOutputPath().split("\\.")[0] + "-1.txt";
        if (new File(tempPath).exists()) {
            new File(tempPath).delete();
        }
        if (!Utils.isEmpty(command.getPreviewFilePath()) && !Utils.isEmpty(command.getInputPath())) {
            if (!FileReadHelper.checkFileExists(command.getPreviewFilePath())) {
                Log.i("预加载文件不存在");
                return;
            }
            List<String> previewData = FileReadHelper.readFile(command.getPreviewFilePath());
            List<ResultBean> previewResult = new ArrayList<>();
            for (int i = 0; i < previewData.size(); i++) {
                String d = previewData.get(i);
                String line = d.replace("[", "").replace("]", "");
                String[] splitLine = line.split(", ");
                if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                    byte[] splitLineInt = new byte[splitLine.length];
                    for (int j = 0; j < splitLine.length; j++) {
                        if (Utils.isNumeric(splitLine[j])) {
                            splitLineInt[j] = Byte.parseByte(splitLine[j]);
                        }
                    }
                    previewResult.add(new ResultBean(splitLineInt, i, mCommand.getMergeCount()));
                }
            }
            List<String> data = FileReadHelper.readFile(command.getInputPath());
            List<ControlBean> cb = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                String singleControlLine = data.get(i);
                Log.i("辅助条件" + (i - 1) + "：" + singleControlLine);
                if (singleControlLine.contains("-")) {
                    String[] controlLines = singleControlLine.split("-");
                    cb.add(new ControlBean(controlLines[0], controlLines[1]));
                }
//                if (i == 20) {
//                    break;
//                }
            }
            List<BaseControlNumberBean> bcnData = GenerateHelper.getInstance().getBaseControlNumbers(cb);
            BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
            if (bcnData == null) {
                Log.i("参考数列表数据有误！");
                return;
            } else {
                bgn.setBaseControlNumbers(bcnData);
            }
            allGenerateData = previewResult;
            generateResults(bgn, previewResult);
            return;
        }

        if (!Utils.isEmpty(command.getInputPath())) {
            if (!FileReadHelper.checkFileExists(command.getInputPath())) {
                Log.i("输入文件不存在");
                return;
            }
            List<String> data = FileReadHelper.readFile(command.getInputPath());
            BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
            if (data.size() > 1) {
                HeadContentBean hcb = new HeadContentBean(data.get(0), data.get(1));
                String baseNumber = hcb.getControl();
                if (baseNumber.length() <= 0) {
                    Log.i("请输入正确的基础数字");
                    return;
                }
                String basePrintNumber = hcb.getNumber();
                if (basePrintNumber.length() <= 0 || !Utils.isNumeric(basePrintNumber) || Integer.parseInt(basePrintNumber) <= 0) {
                    Log.i("请输入正确的最终选择个数");
                    return;
                }
                Log.i("基本数字：" + baseNumber);
                Log.i("筛选个数：" + basePrintNumber);
                String[] baseNumbers = baseNumber.split("、");
                List<Byte> baseNumberIntegers = new ArrayList<>();
                for (int i = 0; i < baseNumbers.length; i++) {
                    if (Utils.isNumeric(baseNumbers[i])) {
                        Byte baseNumberInteger = Byte.parseByte(baseNumbers[i]);
                        if (!baseNumberIntegers.contains(baseNumberInteger)) {
                            baseNumberIntegers.add(baseNumberInteger);
                        }
                    }
                }
                bgn.setBaseNumbers(baseNumberIntegers);
                bgn.setPrintNumber(Integer.parseInt(basePrintNumber));

                if (bgn.getPrintNumber() > baseNumberIntegers.size()) {
                    Log.i("最终选择个数不能大于基础数据个数！");
                    return;
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
            List<BaseControlNumberBean> bcnData = GenerateHelper.getInstance().getBaseControlNumbers(cb);
            if (bcnData == null) {
                Log.i("参考数列表数据有误！");
                return;
            } else {
                bgn.setBaseControlNumbers(bcnData);
            }

            List<ResultBean> result = new ArrayList<>();
            int[] resultArr = new int[bgn.getPrintNumber()];
            Utils.combine_increase(bgn.getBaseNumbers(), 0, resultArr, bgn.getPrintNumber(), bgn.getPrintNumber(), bgn.getBaseNumbers().size(), result, new long[1], mCommand.getMergeCount());
            allGenerateData = result;
            generateResults(bgn, result);
        }
    }

    ///////////////////////////////////////////////
    private List<Integer> generateResults(BaseGenerateNumberBean bgn, List<ResultBean> result) {
        Log.i("result = " + result.size());
        List<Integer> resultBeans = new ArrayList<>();
//        Log.e("TAG", "result = " + result.size());
        //拿到最终结果数，开始跟条件进行比对
        List<BaseControlNumberBean> controls = bgn.getBaseControlNumbers();
        if (controls.size() > 0) {
            int sizeCount = mCommand.getSizeCount();
            int size = controls.size() / mCommand.getSizeCount();
            Log.i("size = " + size);
            if (size > 0) {
                int last = controls.size() % mCommand.getSizeCount();
                if (last > 0) {
                    size++;
                    for (int i = 0; i < size; i++) {
                        if (i == size - 1) {
                            Thread thread = new RunThread(result, controls.subList(i * sizeCount, controls.size()), this);
                            addThread(thread.getName());
                            thread.start();
                        } else {
                            Thread thread = new RunThread(result, controls.subList(i * sizeCount, (i + 1) * sizeCount), this);
                            addThread(thread.getName());
                            thread.start();
                        }
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        Thread thread = new RunThread(result, controls.subList(i * sizeCount, (i + 1) * sizeCount), this);
                        addThread(thread.getName());
                        thread.start();
                    }
                }
            } else {
                FilterUtils.filterResultBean(result, controls, 0, null, resultBeans);
                printData(GenerateHelper.getResultBeans(allGenerateData, resultBeans));
            }
        } else {
            printData(result);
        }
        return resultBeans;
    }
    ///////////////////////////////////////////////

    public void addThread(String name) {
        threadName.add(name);
    }

    public synchronized void onThreadCompile(String name, RunThread thread) {
        threadName.remove(name);
        threads.add(thread);
        List<Integer> result = new ArrayList<>();
        if (threadName.size() <= 0) {
            if (threads.size() > 1) {
                doContainThread();//筛选的线程超过一个，则需要求并集
            } else {
                result.addAll(threads.get(0).resultBeans);
                printData(GenerateHelper.getResultBeans(allGenerateData, result));
            }
        }
    }

    public void addThread2(String name) {
        divideThreadName.add(name);
    }

    public synchronized void onThreadCompile2(String name, DivideThread thread) {
        divideThreadName.remove(name);
        divideThreads.add(thread);
        Log.i("divideThreadName.size() = " + divideThreadName.size());
        if (divideThreadName.size() <= 0) {
            mergeResultData();
        }
    }

    public synchronized void mergeResultData() {
        Log.i("mergeResultData");
        List<ResultBean> result = new ArrayList<>();
        if (divideThreads.size() > 1) {
            for (int i = 0; i < divideThreads.size(); i++) {
                DivideThread divideThread = divideThreads.get(i);
                if (result.size() == 0) {
                    result.addAll(divideThread.result);
                } else {
                    List<ResultBean> appendTempResultData = new ArrayList<>();
                    for (int j = 0; j < divideThread.result.size(); j++) {
                        ResultBean temp = divideThread.result.get(j);
                        boolean exists = false;
                        for (int k = 0; k < result.size(); k++) {
                            ResultBean temp2 = result.get(k);
                            if (Arrays.equals(temp.getData(), temp2.getData())) {
                                exists = true;
                                temp2.setCollectCount(temp.getCollectCount() + temp2.getCollectCount());
                                break;
                            }
                        }
                        if (!exists) {
                            appendTempResultData.add(temp);
                        }
                    }
                    result.addAll(appendTempResultData);//去重完毕
                }
            }
            Log.i(System.currentTimeMillis() + "---sort");
            result.sort((o1, o2) -> Integer.compare(o2.getCollectCount(), o1.getCollectCount()));
            Log.i(System.currentTimeMillis() + "---sort");
            fourthFilterData(result);
        } else {
            fourthFilterData(divideThreads.get(0).result);
        }
    }

    public void doContainThread(List<ResultMergeBean> lastResultMergeBeans) {
        compileThreadCount = 0;
        threadCount = 0;
        compileData.clear();
        //完成了，开始求并集
        Log.i("正在求下一个并集...当前数据位置：" + currentFilterThreadId + "  总需要求的并集数据总数：" + threads.size());
        List<ResultMergeBean> data = lastResultMergeBeans;
        List<CutResultBean2> cutResultBeans = new ArrayList<>();
        for (int j = 0; j < mCommand.getArrayCount(); j++) {
            CutResultBean2 cutBean = new CutResultBean2(0, data.size());
            cutResultBeans.add(cutBean);
        }
        List<ResultMergeBean> data2 = threads.get(currentFilterThreadId).mergeBeans;
        List<CutResultBean2> cutResultBeans2 = new ArrayList<>();
        int size = data2.size() / mCommand.getArrayCount();
        for (int j = 0; j < mCommand.getArrayCount(); j++) {
            CutResultBean2 cutBean = new CutResultBean2(j * size, j == mCommand.getArrayCount() - 1 ? data2.size() : (j + 1) * size);
            cutResultBeans2.add(cutBean);
        }
        currentFilterThreadId++;

        for (int j = 0; j < cutResultBeans.size(); j++) {
            threadCount++;
            new ContainThread(this, allGenerateData, data, cutResultBeans.get(j).start, cutResultBeans.get(j).end, data2, cutResultBeans2.get(j).start, cutResultBeans2.get(j).end)
                    .start();
        }
    }

    private void doContainThread() {
        //完成了，开始求并集
        Log.i("正在开始求并集...当前数据位置：" + currentFilterThreadId + "  总需要求的并集数据总数：" + threads.size());
        List<ResultMergeBean> data = threads.get(0).mergeBeans;
        List<CutResultBean2> cutResultBeans = new ArrayList<>();
        for (int j = 0; j < mCommand.getArrayCount(); j++) {
            CutResultBean2 cutBean = new CutResultBean2(0, data.size());
            cutResultBeans.add(cutBean);
        }
        currentFilterThreadId++;
        List<ResultMergeBean> data2 = threads.get(1).mergeBeans;
        List<CutResultBean2> cutResultBeans2 = new ArrayList<>();
        int size = data2.size() / mCommand.getArrayCount();
        int last = data2.size() % mCommand.getArrayCount();//得到剩余的数
        int append = 0;
        for (int j = 0; j < mCommand.getArrayCount(); j++) {
            CutResultBean2 cutBean = new CutResultBean2(j * size + append, j == mCommand.getArrayCount() - 1 ? data2.size() : (j + 1) * size + (append == last ? append : ++append));
            cutResultBeans2.add(cutBean);
        }
        currentFilterThreadId++;

        for (int j = 0; j < cutResultBeans.size(); j++) {
            threadCount++;
            new ContainThread(this, allGenerateData, data, cutResultBeans.get(j).start, cutResultBeans.get(j).end, data2, cutResultBeans2.get(j).start, cutResultBeans2.get(j).end)
                    .start();
        }
    }

    private int count;

    public synchronized void containCompileData(List<Integer> data) {
        //求并集的线程结束，需要判断是否还有筛选的数据没有进行并集求解
        compileData.addAll(data);
//        Log.i("单次计算结果数：" + data.size());
        compileThreadCount++;
        if (threadCount <= compileThreadCount) {
            compileData.sort(Integer::compareTo);
            //这里进行判断，是不是已经执行到最后一个线程了
            if (currentFilterThreadId >= threads.size()) {//最后一个了
                /*Log.i("计算完成，结果数：" + compileData.size());
                try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), allGenerateData, compileData);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                printData(GenerateHelper.getResultBeans(allGenerateData, compileData));
            } else {//执行下一次结果
                List<ResultMergeBean> collectMergeBeans = FilterUtils.collectMergeBean(allGenerateData, compileData);
                count++;
                doContainThread(collectMergeBeans);
            }
        }
    }


    private void printData(List<ResultBean> printData) {
        Log.i("------------------------------------本轮计算完成，结果数：" + printData.size() + "------------------------------------");
        if (currentGenerateIdx == 0) {
            threads.clear();
            compileData.clear();
            currentFilterThreadId = 0;
            currentGenerateIdx++;
            generateManyThread(printData);
        }
    }

    private void generateManyThread(List<ResultBean> previewResult) {
        if (previewResult.size() > 0) {
            int size = previewResult.size() / mCommand.getDivideThreadCount();
            Log.i("size = " + size);
            if (size > 0) {
                for (int i = 0; i < mCommand.getDivideThreadCount(); i++) {
                    if (i == mCommand.getDivideThreadCount() - 1) {
                        Thread thread = new DivideThread(previewResult.subList(i * size, previewResult.size()), this, mCommand.getGenerateSize(), mCommand.getMergeCount());
                        addThread2(thread.getName());
                        thread.start();
                    } else {
                        Thread thread = new DivideThread(previewResult.subList(i * size, (i + 1) * size), this, mCommand.getGenerateSize(), mCommand.getMergeCount());
                        addThread2(thread.getName());
                        thread.start();
                    }
                }
            } else {
                Thread thread = new DivideThread(previewResult, this, mCommand.getGenerateSize(), mCommand.getMergeCount());
                addThread2(thread.getName());
                thread.start();
            }
        }
    }

    private void fourthFilterData(List<ResultBean> result) {
        Log.i(System.currentTimeMillis() + "---getCollectCount");
        Map<Integer, List<ResultBean>> collectData = new HashMap<>();
        for (int i = 0; i < result.size(); i++) {
            ResultBean rb = result.get(i);
            if (!collectData.containsKey(rb.getCollectCount())) {
                collectData.put(rb.getCollectCount(), new ArrayList<>());
            }
            List<ResultBean> children = collectData.get(rb.getCollectCount());
            children.add(rb);//n位的归为一类
        }
        Log.i(System.currentTimeMillis() + "---getCollectCount");

        List<ExistsDataBean> existsData4 = printSingleDataIdx(result, 4);//先得到符合的数
        //输出符合的中4的个数
        try {
            FileReadHelper.writeToFile(mCommand.getOutputPath(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //输出符合的中4的个数
        for (int key :
                collectData.keySet()) {
            List<ResultBean> singleData = collectData.get(key);
            //输出符合的5+0、4+1、3+2的数
            try {
                FileReadHelper.writeToFile2(mCommand.getOutputPath(), key, singleData.size(), result.size(), existsData4, currentGenerateIdx);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("所有数据处理完毕！");
        if (mCallBackListener != null) {
            mCallBackListener.onCompile();
        }
    }


    private int getExistsDataCount(byte[] data1, byte[] data2) {
        int count = 0;
        for (int i = 0; i < data1.length; i++) {
            for (int j = 0; j < data2.length; j++) {
                if (data1[i] == data2[j]) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private List<ExistsDataBean> printSingleDataIdx(List<ResultBean> data, int... existsCountStandards) {
        List<ExistsDataBean> existsIdx = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ResultBean d = data.get(i);
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < d.getData().length; j++) {
                    if (mCommand.getCheckData()[k] == d.getData()[j]) {
                        existsCount++;
                        break;
                    }
                }
            }
            for (int j = 0; j < existsCountStandards.length; j++) {
                if (existsCount == existsCountStandards[j]) {
                    if (existsCountStandards[j] == 5) {
                        //取得另外两位数
                        byte[] noExistsData = new byte[2];
                        int noExistsDataId = 0;
                        for (int k = 0; k < d.getData().length; k++) {
                            boolean exists = false;
                            for (int l = 0; l < mCommand.getCheckData().length; l++) {
                                if (d.getData()[k] == mCommand.getCheckData()[l]) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                noExistsData[noExistsDataId] = d.getData()[k];
                                noExistsDataId++;
                            }
                        }
                        existsIdx.add(new ExistsDataBean(i, noExistsData, d.getCollectCount()));//取得符合要求的数
                    } else {
                        existsIdx.add(new ExistsDataBean(i, null, d.getCollectCount()));//取得符合要求的数
                    }
                }
            }
        }
        return existsIdx;
    }

}
