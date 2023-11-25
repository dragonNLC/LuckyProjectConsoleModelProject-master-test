package com.dragon.lucky.command19;

import com.dragon.lucky.bean.*;
import com.dragon.lucky.bean.cut.CutResultBean2;
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

    private CallbackListener mCallBackListener;

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!Utils.isEmpty(command.getPreviewFilePath())) {
            if (!FileReadHelper.checkFileExists(command.getPreviewFilePath())) {
                Log.i("预加载文件不存在");
                return;
            }
            List<String> previewData = FileReadHelper.readFile(command.getPreviewFilePath());
            List<ResultBean> previewResult = new ArrayList<>();
            for (int i = 0; i < previewData.size(); i++) {
                String d = previewData.get(i);
//                String line = d.replace("[", "").replace("]", "");
                if (!d.contains("[") || !d.contains("]")) {
                    continue;
                }
                String line = "";
                if (d.contains("],[")) {
                    String[] content = d.split("],\\[");
                    line = content[0].replace("[", "").replace("]", "");
                } else {
                    line = d.replace("[", "").replace("]", "");
                }
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
            if (!Utils.isEmpty(command.getInputPath())) {
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
                List<BaseControlNumberBean> bcnData = getBaseControlNumbers(cb);
                BaseGenerateNumberBean bgn = new BaseGenerateNumberBean();
                if (bcnData == null) {
                    Log.i("参考数列表数据有误！");
                    return;
                } else {
                    bgn.setBaseControlNumbers(bcnData);
                }
                allGenerateData = previewResult;
                generateResults(bgn, previewResult);
            } else {
                printData(previewResult);
            }
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
            List<BaseControlNumberBean> bcnData = getBaseControlNumbers(cb);
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
    private List<Integer> generateResults(BaseGenerateNumberBean bgn, List<ResultBean> result) throws IOException {
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
                /*try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), allGenerateData, resultBeans);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
                printData(getResultBeans(allGenerateData, resultBeans));

                Log.i(" 处理完成：结果数-" + resultBeans.size() /*+ "   " + resultBeans.toString()*/);
            }
        } else {
//            resultBeans.addAll(result);
            /*try {
                FileReadHelper.writeToFile(mCommand.getOutputPath(), result);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            printData(result);
            Log.i(" 处理完成：结果数-" + result.size()/* + "   " + resultBeans.toString()*/);
        }
        return resultBeans;
    }

    ///////////////////////////////////////////////
    public List<ResultBean> getResultBeans(List<ResultBean> data, List<Integer> dataIdx) {
        HashSet<Integer> complexData = new HashSet<>(dataIdx);
        dataIdx.clear();
        dataIdx.addAll(complexData);
//        dataIdx.sort(Integer::compareTo);
        List<ResultBean> result = new ArrayList<>();
        for (int i = 0; i < dataIdx.size(); i++) {
            result.add(data.get(dataIdx.get(i)));
        }
        return result;
    }

    private void printData(List<ResultBean> previewResult) throws IOException {
        OnceGenerateBean onceGenerateBean = new OnceGenerateBean();
        Log.i("校验完毕，出现总条数：" + previewResult.size());
        int existsData1 = printSingleDataIdx(previewResult, 1);//先得到符合的数
        int existsData2 = printSingleDataIdx(previewResult, 2);//先得到符合的数

        onceGenerateBean.setData(previewResult);
        onceGenerateBean.setZ1(existsData1);
        onceGenerateBean.setZ2(existsData2);
        if (!Utils.isEmpty(mCommand.getInputPath())) {
            onceGenerateBean.setTitle(new File(mCommand.getInputPath()).getName().split("\\.")[0]);
        } else  if (!Utils.isEmpty(mCommand.getInputPath2())) {
            onceGenerateBean.setTitle(new File(mCommand.getInputPath2()).getName().split("\\.")[0]);
        } else  if (!Utils.isEmpty(mCommand.getPreviewFilePath())) {
            onceGenerateBean.setTitle(new File(mCommand.getPreviewFilePath()).getName().split("\\.")[0]);
        }

        if (!Utils.isEmpty(mCommand.getInputPath2())) {
            List<String> inputString2 = FileReadHelper.readFile(mCommand.getInputPath2());
            List<ResultBean> inputData2 = new ArrayList<>();
            for (int i = 0; i < inputString2.size(); i++) {
                String d = inputString2.get(i);
                if (!d.contains("[") || !d.contains("]")) {
                    continue;
                }
                String line = "";
                if (d.contains("],[")) {
                    String[] content = d.split("],\\[");
                    line = content[0].replace("[", "").replace("]", "");
                } else {
                    line = d.replace("[", "").replace("]", "");
                }
                String[] splitLine = line.split(", ");
                if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                    byte[] splitLineInt = new byte[splitLine.length];
                    for (int j = 0; j < splitLine.length; j++) {
                        if (Utils.isNumeric(splitLine[j])) {
                            splitLineInt[j] = Byte.parseByte(splitLine[j]);
                        }
                    }
                    inputData2.add(new ResultBean(splitLineInt, i, mCommand.getMergeCount()));
                }
            }

            List<ResultBean> generateData = new ArrayList<>();
            //拿到基础数之后，开始按照需要生成对应的数据队列
            for (int i = 0; i < inputData2.size(); i++) {
                ResultBean nb = inputData2.get(i);
                List<ResultBean> tempData = new ArrayList<>();
                int[] resultArr = new int[2];//生成2位的数据
                Utils.combine_increase_for_number_for_run19(nb.getData(), 0, resultArr, 2, 2, nb.getData().length, tempData);
                for (int j = 0; j < tempData.size(); j++) {
                    tempData.get(j).setCollectCount(1);
                }
                //降位成功，开始统计个数
                generateData.addAll(tempData);
            }

            //去重，统计数量

            HashSet<ResultBean> filterData = new HashSet<>(generateData);
            List<ResultBean> checkGenerateData = new ArrayList<>(filterData);
            //拿到数据，开始统计数
            for (int i = 0; i < previewResult.size(); i++) {
                Iterator<ResultBean> iterator = checkGenerateData.iterator();
                while (iterator.hasNext()) {
                    ResultBean resultBean = iterator.next();
                    if (Arrays.equals(previewResult.get(i).getData(), resultBean.getData())) {
                        previewResult.get(i).setCollectCount(resultBean.getCollectCount());
                        iterator.remove();
                    }
                }
            }
            //得到剩余的3位降位数据跟两位带尾数的数据，传给剩下的数
            existsData1 = printSingleDataIdx(checkGenerateData, 1);//先得到符合的数
            existsData2 = printSingleDataIdx(checkGenerateData, 2);//先得到符合的数

            onceGenerateBean.setData2(checkGenerateData);
            onceGenerateBean.setZ21(existsData1);
            onceGenerateBean.setZ22(existsData2);
//            Log.i("existsData1 = " + existsData1);
//            Log.i("existsData2 = " + existsData2);
        }

        if (mCallBackListener != null) {
            mCallBackListener.onCompile(onceGenerateBean);
        }
    }

    private int printSingleDataIdx(List<ResultBean> data, int existsCountStandard) {
        int size = 0;
        if (mCommand.getCheckData() != null) {
            for (int i = 0; i < data.size(); i++) {
                ResultBean d = data.get(i);
                byte[] hitData = new byte[d.getData().length];
                int existsCount = 0;
                for (int j = 0; j < d.getData().length; j++) {
                    for (int k = 0; k < mCommand.getCheckData().length; k++) {
                        if (mCommand.getCheckData()[k] == d.getData()[j]) {
                            existsCount++;
                            hitData[j] = 1;
                            break;
                        }
                    }
                }
                d.setHitData(hitData);
                if (existsCount == existsCountStandard) {
                    size++;
                }

            }
        }
        return size;
    }

    //获取条件数组列表
    private List<BaseControlNumberBean> getBaseControlNumbers(List<ControlBean> adapterData) {
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
    private BaseControlNumberBean getBaseControlNumber(ControlBean cb) {
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

    public void addThread(String name) {
        threadName.add(name);
    }

    private int currentFilterThreadId;//当前执行中的最后一个线程id

    public synchronized void onThreadCompile(String name, RunThread thread) throws IOException {
//            Log.i("onThreadCompile");
        threadName.remove(name);
        threads.add(thread);
        List<Integer> result = new ArrayList<>();
        if (threadName.size() <= 0) {
            if (threads.size() > 1) {
                doContainThread();//筛选的线程超过一个，则需要求并集
            } else {
                result.addAll(threads.get(0).resultBeans);
                /*try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), allGenerateData, result);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                printData(getResultBeans(allGenerateData, result));
                Log.i(threads.size() + " 处理完成：结果数-" + result.size());
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
        /*for (int i = 0; i < lastResultMergeBeans.size(); i++) {
            if (lastResultMergeBeans.get(i).mergeHead.equals("[4, 5, 6]")) {
                for (int j = 0; j < lastResultMergeBeans.get(i).mergeResult.size(); j++) {
                    Log.i(threads.get(currentFilterThreadId).getName() + "    lastResultMergeBeans = ：" + lastResultMergeBeans.get(i).mergeResult.get(j) + "   ");
                }
            }
        }*/

        List<ResultMergeBean> data2 = threads.get(currentFilterThreadId).mergeBeans;
        List<CutResultBean2> cutResultBeans2 = new ArrayList<>();
        int size = data2.size() / mCommand.getArrayCount();
//        int last = data2.size() % mCommand.getArrayCount();
        /*if (last > 0) {
            size++;
        }*/
        for (int j = 0; j < mCommand.getArrayCount(); j++) {
            CutResultBean2 cutBean = new CutResultBean2(j * size, j == mCommand.getArrayCount() - 1 ? data2.size() : (j + 1) * size);
//            CutResultBean2 cutBean = new CutResultBean2(j * size, Math.min((j + 1) * size, data2.size()));
            cutResultBeans2.add(cutBean);
//            try {
//                FileReadHelper.writeToFile("H:\\1\\" + threads.get(currentFilterThreadId).getName() + ".txt", compileData);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        /*for (int i = 0; i < threads.get(currentFilterThreadId).mergeBeans.size(); i++) {
            if (threads.get(currentFilterThreadId).mergeBeans.get(i).mergeHead.equals("[4, 5, 6]")) {
                for (int j = 0; j < threads.get(currentFilterThreadId).mergeBeans.get(i).mergeResult.size(); j++) {
                    Log.i(threads.get(currentFilterThreadId).getName() + "    collectMergeBeans = ：" + threads.get(currentFilterThreadId).mergeBeans.get(i).mergeResult.get(j) + "   ");
                }
            }
        }*/
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
//        int last = data2.size() % mCommand.getArrayCount();
//        if (last > 0) {
//            size++;
//        }
        int last = data2.size() % mCommand.getArrayCount();//得到剩余的数
        int append = 0;
        for (int j = 0; j < mCommand.getArrayCount(); j++) {
//            CutResultBean2 cutBean = new CutResultBean2(j * size, Math.min((j + 1) * size, data2.size()));
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

    public synchronized void containCompileData(List<Integer> data) throws IOException {
        //求并集的线程结束，需要判断是否还有筛选的数据没有进行并集求解
        compileData.addAll(data);
        Log.i("单次计算结果数：" + data.size());
        compileThreadCount++;
//        Log.i("compileThreadCount：" + compileThreadCount);
//        Log.i("threadCount：" + threadCount);
        if (threadCount <= compileThreadCount) {
//            compileData.sort(Integer::compareTo);
            //这里进行判断，是不是已经执行到最后一个线程了
//            Log.i("currentFilterThreadId：" + currentFilterThreadId);
//            Log.i("threads.size()：" + threads.size());
            if (currentFilterThreadId >= threads.size()) {//最后一个了
                Log.i("计算完成，结果数：" + compileData.size());
                /*try {
                    FileReadHelper.writeToFile(mCommand.getOutputPath(), allGenerateData, compileData);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                printData(getResultBeans(allGenerateData, compileData));
            } else {//执行下一次结果
//                if (currentFilterThreadId >= 5 && currentFilterThreadId < 9) {
//                for (int i = 0; i < compileData.size(); i++) {
//                    Log.i("compileData = ：" + Arrays.toString(compileData.get(i).getData()) + "   " + "   " + compileData.get(i).getMergeArr());
//                    }
//                }
                List<ResultMergeBean> collectMergeBeans = FilterUtils.collectMergeBean(allGenerateData, compileData);
//                if (currentFilterThreadId >= 5 && currentFilterThreadId < 9) {
//                for (int i = 0; i < collectMergeBeans.size(); i++) {
//                    if (collectMergeBeans.get(i).mergeHead.equals("[4, 5, 6]")) {
//                        for (int j = 0; j < collectMergeBeans.get(i).mergeResult.size(); j++) {
//                            Log.i("collectMergeBeans = ：" + collectMergeBeans.get(i).mergeResult.get(j) + "   ");
//                        }
//                    }
//                    }
//                }
                /*try {
                    FileReadHelper.writeToFile("H:\\2\\" + count + ".txt", compileData);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                count++;
                doContainThread(collectMergeBeans);
            }
        }
    }

}
