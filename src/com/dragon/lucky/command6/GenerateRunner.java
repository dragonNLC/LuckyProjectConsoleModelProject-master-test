package com.dragon.lucky.command6;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.bean.cut.CutResultBean2;
import com.dragon.lucky.command2.GenerateResultBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;
    private List<Integer> compileData = new ArrayList<>();
    private int threadCount;
    private int compileThreadCount;

    private CopyOnWriteArrayList<String> threadName = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<RunThread> threads = new CopyOnWriteArrayList<>();

    private int currentFilterThreadId;//当前执行中的最后一个线程id

//    public static MyCallBackListener myCallBackListener = new MyCallBackListener();//筛选完毕之后的回调方法，进入求并集

    private GenerateRunner() {

    }

    public static GenerateRunner getInstance() {
        if (sInstance == null) {
            synchronized (GenerateRunner.class) {
                if (sInstance == null) {
                    sInstance = new GenerateRunner();
                }
            }
        }
        return sInstance;
    }

    public static List<ResultBean> allGenerateData;
    private int currentGenerateIdx;

    public void run(CommandBean command) throws IOException {
        this.mCommand = command;
        String tempPath = mCommand.getOutputPath().split("\\.")[0] + "-" + 4 + "-鱼腩" + ".txt";
        if (new File(tempPath).exists()) {
            new File(tempPath).delete();
        }
        /*if (CommandAnalysis.getInstance().getCommand().getCheckData() != null) {
            List<ResultBean> printData = new ArrayList<>();
            if (!FileReadHelper.checkFileExists(command.getInputPath())) {
                Log.i("导入文件不存在");
                return;
            }
            List<String> previewData = FileReadHelper.readFile(command.getInputPath());
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
            int count = 0;
            for (int i = 0; i < previewResult.size(); i++) {
                ResultBean data = previewResult.get(i);
                boolean exists = false;
                for (int k = 0; k < mCommand.getCheckData().length; k++) {
                    for (int j = 0; j < data.getData().length; j++) {
                        if (mCommand.getCheckData()[k] == data.getData()[j]) {
                            exists = true;
                            break;
                        } else {
                            exists = false;
                        }
                    }
                    if (!exists) {
                        break;
                    }
                }
                if (exists) {
                    count++;
                    printData.add(data);
                    Log.i("第" + (i + 1) + "行存在对应的数据！存在：" + count + " 条");
                }
            }
            Log.i("校验完毕，结束！");
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), printData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }*/
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
                /*try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), allGenerateData, resultBeans);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(" 处理完成：结果数-" + resultBeans.size() *//*+ "   " + resultBeans.toString()*//*);*/
            }
        } else {
//            resultBeans.addAll(result);
            /*try {
                FileReadHelper.writeToFile(mCommand.getOutputPath(), result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(" 处理完成：结果数-" + result.size()*//* + "   " + resultBeans.toString()*//*);*/
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
                /*try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), allGenerateData, result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(threads.size() + " 处理完成：结果数-" + result.size());*/
            }
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
//            printSingleData(printData);
//            printAllData(printData);
            firstFilterData(printData);
        } else if (currentGenerateIdx == 1) {
            threads.clear();
            compileData.clear();
            currentFilterThreadId = 0;
            currentGenerateIdx++;
//            printSingleData(printData);
//            printAllData(printData);
            secondFilterData(printData);
        } else if (currentGenerateIdx == 2) {
            threads.clear();
            compileData.clear();
            currentFilterThreadId = 0;
            currentGenerateIdx++;
//            printSingleData(printData);
//            printAllData(printData);
            thirdFilterData(printData);
        } else if (currentGenerateIdx == 3) {
            threads.clear();
            compileData.clear();
            currentFilterThreadId = 0;
            currentGenerateIdx++;
//            printSingleData(printData);
//            printAllData(printData);
            fourthFilterData(printData);
            Log.i("所有数据处理完毕！");
        } else {
//            printSingleData(printData);
            List<ExistsDataBean> existsData4 = printSingleDataIdx(printData, 4);//先得到符合的数
            List<ExistsDataBean> existsData5 = printSingleDataIdx(printData, 5);//先得到符合的数
            try {
                FileReadHelper.writeToFile(mCommand.getOutputPath(), printData, existsData4.size(), existsData5.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("所有数据处理完毕！");
        }
    }

    private void firstFilterData(List<ResultBean> previewResult) {
        //结果降位
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            List<ResultBean> tempResult = new ArrayList<>();
            ResultBean rb = previewResult.get(i);
            int[] resultArr = new int[mCommand.getGenerateSize()];
            List<Byte> byteData = new ArrayList<>();
            for (int j = 0; j < rb.getData().length; j++) {
                byteData.add(rb.getData()[j]);
            }
            Utils.combine_increase(byteData, 0, resultArr, mCommand.getGenerateSize(), mCommand.getGenerateSize(), byteData.size(), tempResult, new long[1], mCommand.getMergeCount());
            if (result.size() == 0) {
                for (int j = 0; j < tempResult.size(); j++) {
                    tempResult.get(j).setCollectCount(1);
                }
                result.addAll(tempResult);
            } else {
                List<ResultBean> appendTempResultData = new ArrayList<>();
                for (int j = 0; j < tempResult.size(); j++) {
                    ResultBean temp = tempResult.get(j);
                    boolean exists = false;
                    for (int k = 0; k < result.size(); k++) {
                        ResultBean temp2 = result.get(k);
                        if (Arrays.equals(temp.getData(), temp2.getData())) {
                            exists = true;
                            temp2.setCollectCount(temp2.getCollectCount() + 1);
                            break;
                        }
                    }
                    if (!exists) {
                        temp.setCollectCount(1);
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }
        }
        result.sort((o1, o2) -> Integer.compare(o2.getCollectCount(), o1.getCollectCount()));

        List<ExistsDataBean> existsData4 = printSingleDataIdx(previewResult, 4);//先得到符合的数
        List<ExistsDataBean> existsData5 = printSingleDataIdx(previewResult, 5);//先得到符合的数
        //输出4、5位的个数
        List<Integer> tempIdxData = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            ResultBean r = result.get(i);
            if (Arrays.equals(r.getData(), mCommand.getCheckData())) {//5+0
                tempIdxData.add(i);
                continue;
            }
            int existsDataCount = getExistsDataCount(r.getData(), mCommand.getCheckData());
            if (existsDataCount == 4) {//再求一个一样的就可以了
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 1) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
            if (existsDataCount == 3) {
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 2) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
        }
        //输出符合的5+0、4+1、3+2的数
        try {
            FileReadHelper.writeToFile(mCommand.getOutputPath(), existsData4.size(), existsData5.size(), tempIdxData, result.size(), previewResult.size(), currentGenerateIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int sPercent = Math.round(mCommand.getM1Percent() * result.size());
        int ePercent = Math.round(mCommand.getM1WPercent() * result.size());
        List<ResultBean> filterData = new ArrayList<>();
        if (sPercent != 0) {
            filterData.addAll(result.subList(0, sPercent + 1));
        }
        if (ePercent != 0) {
            filterData.addAll(result.subList(Math.max(ePercent - 1, 0), result.size()));
        }

        //拿到鱼头鱼尾的部分，转换成control
        List<BaseControlNumberBean> bcnData = GenerateHelper.getBaseControlNumbers(filterData, mCommand.getGenerateSize());
        BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
        if (bcnData == null) {
            Log.i("参考数列表数据有误！");
            return;
        } else {
            bgn.setBaseControlNumbers(bcnData);
            Log.i("bcnData = " + bcnData.size());
        }
        allGenerateData = previewResult;
        generateResults(bgn, allGenerateData);
    }

    private void secondFilterData(List<ResultBean> previewResult) {
        //结果降位
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            List<ResultBean> tempResult = new ArrayList<>();
            ResultBean rb = previewResult.get(i);
            int[] resultArr = new int[mCommand.getGenerateSize()];
            List<Byte> byteData = new ArrayList<>();
            for (int j = 0; j < rb.getData().length; j++) {
                byteData.add(rb.getData()[j]);
            }
            Utils.combine_increase(byteData, 0, resultArr, mCommand.getGenerateSize(), mCommand.getGenerateSize(), byteData.size(), tempResult, new long[1], mCommand.getMergeCount());
            if (result.size() == 0) {
                for (int j = 0; j < tempResult.size(); j++) {
                    tempResult.get(j).setCollectCount(1);
                }
                result.addAll(tempResult);
            } else {
                List<ResultBean> appendTempResultData = new ArrayList<>();
                for (int j = 0; j < tempResult.size(); j++) {
                    ResultBean temp = tempResult.get(j);
                    boolean exists = false;
                    for (int k = 0; k < result.size(); k++) {
                        ResultBean temp2 = result.get(k);
                        if (Arrays.equals(temp.getData(), temp2.getData())) {
                            exists = true;
                            temp2.setCollectCount(temp2.getCollectCount() + 1);
                            break;
                        }
                    }
                    if (!exists) {
                        temp.setCollectCount(1);
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }
        }
        result.sort((o1, o2) -> Integer.compare(o2.getCollectCount(), o1.getCollectCount()));

        List<ExistsDataBean> existsData4 = printSingleDataIdx(previewResult, 4);//先得到符合的数
        List<ExistsDataBean> existsData5 = printSingleDataIdx(previewResult, 5);//先得到符合的数
        //输出4、5位的个数
        List<Integer> tempIdxData = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            ResultBean r = result.get(i);
            if (Arrays.equals(r.getData(), mCommand.getCheckData())) {//5+0
                tempIdxData.add(i);
                continue;
            }
            int existsDataCount = getExistsDataCount(r.getData(), mCommand.getCheckData());
            if (existsDataCount == 4) {//再求一个一样的就可以了
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 1) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
            if (existsDataCount == 3) {
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 2) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
        }
        //输出符合的5+0、4+1、3+2的数
        try {
            FileReadHelper.writeToFile(mCommand.getOutputPath(), existsData4.size(), existsData5.size(), tempIdxData, result.size(), previewResult.size(), currentGenerateIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int sPercent = Math.round(mCommand.getM2Percent() * result.size());
        int ePercent = Math.round(mCommand.getM2WPercent() * result.size());
        List<ResultBean> filterData = new ArrayList<>();
        if (sPercent != 0) {
            filterData.addAll(result.subList(0, sPercent + 1));
        }
        if (ePercent != 0) {
            filterData.addAll(result.subList(Math.max(ePercent - 1, 0), result.size()));
        }

        //拿到鱼头鱼尾的部分，转换成control
        List<BaseControlNumberBean> bcnData = GenerateHelper.getBaseControlNumbers(filterData, mCommand.getGenerateSize());
        BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
        if (bcnData == null) {
            Log.i("参考数列表数据有误！");
            return;
        } else {
            bgn.setBaseControlNumbers(bcnData);
        }
        allGenerateData = previewResult;
        generateResults(bgn, allGenerateData);
    }

    private void thirdFilterData(List<ResultBean> previewResult) {
        //结果降位
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            List<ResultBean> tempResult = new ArrayList<>();
            ResultBean rb = previewResult.get(i);
            int[] resultArr = new int[mCommand.getGenerateSize()];
            List<Byte> byteData = new ArrayList<>();
            for (int j = 0; j < rb.getData().length; j++) {
                byteData.add(rb.getData()[j]);
            }
            Utils.combine_increase(byteData, 0, resultArr, mCommand.getGenerateSize(), mCommand.getGenerateSize(), byteData.size(), tempResult, new long[1], mCommand.getMergeCount());
            if (result.size() == 0) {
                for (int j = 0; j < tempResult.size(); j++) {
                    tempResult.get(j).setCollectCount(1);
                }
                result.addAll(tempResult);
            } else {
                List<ResultBean> appendTempResultData = new ArrayList<>();
                for (int j = 0; j < tempResult.size(); j++) {
                    ResultBean temp = tempResult.get(j);
                    boolean exists = false;
                    for (int k = 0; k < result.size(); k++) {
                        ResultBean temp2 = result.get(k);
                        if (Arrays.equals(temp.getData(), temp2.getData())) {
                            exists = true;
                            temp2.setCollectCount(temp2.getCollectCount() + 1);
                            break;
                        }
                    }
                    if (!exists) {
                        temp.setCollectCount(1);
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }
        }
        result.sort((o1, o2) -> Integer.compare(o2.getCollectCount(), o1.getCollectCount()));

        List<ExistsDataBean> existsData4 = printSingleDataIdx(previewResult, 4);//先得到符合的数
        List<ExistsDataBean> existsData5 = printSingleDataIdx(previewResult, 5);//先得到符合的数
        //输出4、5位的个数
        List<Integer> tempIdxData = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            ResultBean r = result.get(i);
            if (Arrays.equals(r.getData(), mCommand.getCheckData())) {//5+0
                tempIdxData.add(i);
                continue;
            }
            int existsDataCount = getExistsDataCount(r.getData(), mCommand.getCheckData());
            if (existsDataCount == 4) {//再求一个一样的就可以了
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 1) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
            if (existsDataCount == 3) {
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 2) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
        }
        //输出符合的5+0、4+1、3+2的数
        try {
            FileReadHelper.writeToFile(mCommand.getOutputPath(), existsData4.size(), existsData5.size(), tempIdxData, result.size(), previewResult.size(), currentGenerateIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int sPercent =  Math.round(mCommand.getM3Percent() * result.size());
        int ePercent =  Math.round(mCommand.getM3WPercent() * result.size());
        List<ResultBean> filterData = new ArrayList<>();
        if (sPercent != 0) {
            filterData.addAll(result.subList(0, sPercent + 1));
        }
        if (ePercent != 0) {
            filterData.addAll(result.subList(Math.max(ePercent - 1, 0), result.size()));
        }

        //拿到鱼头鱼尾的部分，转换成control
        List<BaseControlNumberBean> bcnData = GenerateHelper.getBaseControlNumbers(filterData, mCommand.getGenerateSize());
        BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
        if (bcnData == null) {
            Log.i("参考数列表数据有误！");
            return;
        } else {
            bgn.setBaseControlNumbers(bcnData);
        }
        allGenerateData = previewResult;
        generateResults(bgn, allGenerateData);
    }

    private void fourthFilterData(List<ResultBean> previewResult) {
        //结果降位
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < previewResult.size(); i++) {
            List<ResultBean> tempResult = new ArrayList<>();
            ResultBean rb = previewResult.get(i);
            int[] resultArr = new int[mCommand.getGenerateSize()];
            List<Byte> byteData = new ArrayList<>();
            for (int j = 0; j < rb.getData().length; j++) {
                byteData.add(rb.getData()[j]);
            }
            Utils.combine_increase(byteData, 0, resultArr, mCommand.getGenerateSize(), mCommand.getGenerateSize(), byteData.size(), tempResult, new long[1], mCommand.getMergeCount());
            if (result.size() == 0) {
                for (int j = 0; j < tempResult.size(); j++) {
                    tempResult.get(j).setCollectCount(1);
                }
                result.addAll(tempResult);
            } else {
                List<ResultBean> appendTempResultData = new ArrayList<>();
                for (int j = 0; j < tempResult.size(); j++) {
                    ResultBean temp = tempResult.get(j);
                    boolean exists = false;
                    for (int k = 0; k < result.size(); k++) {
                        ResultBean temp2 = result.get(k);
                        if (Arrays.equals(temp.getData(), temp2.getData())) {
                            exists = true;
                            temp2.setCollectCount(temp2.getCollectCount() + 1);
                            break;
                        }
                    }
                    if (!exists) {
                        temp.setCollectCount(1);
                        appendTempResultData.add(temp);
                    }
                }
                result.addAll(appendTempResultData);//去重完毕
            }
        }
        Map<Integer, List<ResultBean>> collectData = new HashMap<>();
        for (int i = 0; i < result.size(); i++) {
            ResultBean rb = result.get(i);
            if (!collectData.containsKey(rb.getCollectCount())) {
                collectData.put(rb.getCollectCount(), new ArrayList<>());
            }
            List<ResultBean> children = collectData.get(rb.getCollectCount());
            children.add(rb);//n位的归为一类
        }

        result.sort((o1, o2) -> Integer.compare(o2.getCollectCount(), o1.getCollectCount()));

        List<ExistsDataBean> existsData4 = printSingleDataIdx(previewResult, 4);//先得到符合的数
        List<ExistsDataBean> existsData5 = printSingleDataIdx(previewResult, 5);//先得到符合的数
        //输出4、5位的个数
        List<Integer> tempIdxData = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            ResultBean r = result.get(i);
            if (Arrays.equals(r.getData(), mCommand.getCheckData())) {//5+0
                tempIdxData.add(i);
                continue;
            }
            int existsDataCount = getExistsDataCount(r.getData(), mCommand.getCheckData());
            if (existsDataCount == 4) {//再求一个一样的就可以了
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 1) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
            if (existsDataCount == 3) {
                for (int j = 0; j < existsData5.size(); j++) {
                    ExistsDataBean existsDataBean = existsData5.get(j);
                    existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                    if (existsDataCount == 2) {//4+1;
                        tempIdxData.add(i);
                        break;
                    }
                }
            }
        }
        //输出符合的5+0、4+1、3+2的数
        try {
            FileReadHelper.writeToFile(mCommand.getOutputPath(), existsData4.size(), existsData5.size(), tempIdxData, result.size(), previewResult.size(), currentGenerateIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int key :
                collectData.keySet()) {
            List<ResultBean> singleData = collectData.get(key);
//            String path = mCommand.getOutputPath().split("\\.")[0] + currentGenerateIdx + "-鱼腩-" + key + ".txt";
//            try {
//                FileReadHelper.writeToFile(path, singleData);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //输出4、5位的个数
            List<Integer> tempSingleIdxData = new ArrayList<>();
            for (int i = 0; i < singleData.size(); i++) {
                ResultBean r = singleData.get(i);
                if (Arrays.equals(r.getData(), mCommand.getCheckData())) {//5+0
                    tempSingleIdxData.add(i);
                    continue;
                }
                int existsDataCount = getExistsDataCount(r.getData(), mCommand.getCheckData());
                if (existsDataCount == 4) {//再求一个一样的就可以了
                    for (int j = 0; j < existsData5.size(); j++) {
                        ExistsDataBean existsDataBean = existsData5.get(j);
                        existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                        if (existsDataCount == 1) {//4+1;
                            tempSingleIdxData.add(i);
                            break;
                        }
                    }
                }
                if (existsDataCount == 3) {
//                    if (key == 1) {
//                        Log.i("key = " + key + "   i = " + i);
//                    }
                    for (int j = 0; j < existsData5.size(); j++) {
                        ExistsDataBean existsDataBean = existsData5.get(j);
                        existsDataCount = getExistsDataCount(r.getData(), existsDataBean.getData());
                        if (existsDataCount == 2) {//4+1;
                            tempSingleIdxData.add(i);
                            break;
                        } else {
//                            if (key == 1) {
//                                Log.i("   existsDataCount = " + existsDataCount);
//                            }
                        }
                    }
                }
            }
            //输出符合的5+0、4+1、3+2的数
            try {
                FileReadHelper.writeToFile(mCommand.getOutputPath(), tempSingleIdxData, singleData.size(), currentGenerateIdx, key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int sPercent =  Math.round(mCommand.getM4Percent() * result.size());
        int ePercent =  Math.round(mCommand.getM4WPercent() * result.size());
        List<ResultBean> filterData = new ArrayList<>();
        if (sPercent != 0) {
            filterData.addAll(result.subList(0, sPercent + 1));
        }
        if (ePercent != 0) {
            filterData.addAll(result.subList(Math.max(ePercent - 1, 0), result.size()));
        }
        //取中间部分
        List<GenerateConditionBean> generateConditions = mCommand.getmGenerateConditions();
        for (int i = 0; i < generateConditions.size(); i++) {
            GenerateConditionBean grb = generateConditions.get(i);
            List<GenerateConditionBean.Percent> percents = grb.getPercents();
            for (int j = 0; j < percents.size(); j++) {
                GenerateConditionBean.Percent percent = percents.get(j);
                List<ResultBean> tempData = collectData.get(percent.getCount());
                if (tempData != null) {
                    sPercent = (int) (percent.getHeadPercent() * tempData.size());
                    ePercent = (int) (percent.getFootPercent() * tempData.size());
//                    Log.i("sPercent = " + sPercent);
//                    Log.i("ePercent = " + ePercent);
//                    Log.i("percent.getHeadPercent() = " + percent.getHeadPercent());
//                    Log.i("percent.getFootPercent() = " + percent.getFootPercent());
                    filterData.addAll(tempData.subList(Math.max(sPercent - 1, 0), Math.min(ePercent + 1, tempData.size())));//拿到中间部分
                } else {
                    Log.i("percent.getCount() = " + percent.getCount());
                }
            }
        }

        //拿到鱼头鱼尾的部分，转换成control
        List<BaseControlNumberBean> bcnData = GenerateHelper.getBaseControlNumbers(filterData, mCommand.getGenerateSize());
        BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
        if (bcnData == null) {
            Log.i("参考数列表数据有误！");
            return;
        } else {
            bgn.setBaseControlNumbers(bcnData);
        }
        allGenerateData = previewResult;
        generateResults(bgn, allGenerateData);
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
                        existsIdx.add(new ExistsDataBean(i, noExistsData));//取得符合要求的数
                    } else {
                        existsIdx.add(new ExistsDataBean(i, null));//取得符合要求的数
                    }
                }
            }
        }
        return existsIdx;
    }

}
