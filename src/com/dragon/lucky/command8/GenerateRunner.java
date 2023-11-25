package com.dragon.lucky.command8;

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

    private CommandBean mCommand;
    private List<GenerateBaseDataBean.BaseData> compileData = new ArrayList<>();
    private int threadCount;
    private int compileThreadCount;

    private CopyOnWriteArrayList<String> threadName = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<RunThread> threads = new CopyOnWriteArrayList<>();

    private int currentFilterThreadId;//当前执行中的最后一个线程id

    private long startTime;

    private GenerateRunner() {

    }

    public static GenerateRunner getInstance() {
        return new GenerateRunner();
    }

    private int currentGenerateIdx;

    private CallbackListener mCallBackListener;

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        startTime = System.currentTimeMillis();
        this.mCommand = command;
        String tempPath = mCommand.getOutputPath().split("\\.")[0] + "-1.txt";
        if (new File(tempPath).exists()) {
            new File(tempPath).delete();
        }
        List<GenerateBaseDataBean> generateData = new ArrayList<>();
        if (mCommand.getInputPath() != null && mCommand.getInputPath().size() > 0) {
            List<String> data = new ArrayList<>();
            for (int i = 0; i < mCommand.getInputPath().size(); i++) {
                GenerateBaseDataBean generateDat = new GenerateBaseDataBean();
                String path = mCommand.getInputPath().get(i);
                if (!FileReadHelper.checkFileExists(path)) {
                    Log.i("预加载文件不存在");
                    return;
                }
                List<String> previewData = FileReadHelper.readFile(path);
                List<GenerateBaseDataBean.BaseData> previewResult = new ArrayList<>();
                for (int j = 0; j < previewData.size(); j++) {
                    String d = previewData.get(j);
                    String[] contents = d.split("],\\[");
                    String line = contents[0].replace("[", "").replace("]", "");
                    String count = contents[1].replace("[", "").replace("]", "");
                    int id = 0;
                    if (!data.contains(line)) {
                        data.add(line);
                    }
                    id = data.indexOf(line);
                    String[] splitLine = line.split(", ");
                    if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                        byte[] splitLineInt = new byte[splitLine.length];
                        for (int k = 0; k < splitLine.length; k++) {
                            if (Utils.isNumeric(splitLine[k])) {
                                splitLineInt[k] = Byte.parseByte(splitLine[k]);
                            }
                        }
                        previewResult.add(new GenerateBaseDataBean.BaseData(splitLineInt, Integer.parseInt(count), id));
                    }
                }
                String singleOutPath = new File(mCommand.getOutputPath()).getParentFile().getAbsolutePath() + File.separator + new File(path).getName().split("\\.")[0] + "-out.txt";
                generateDat.setmPath(singleOutPath);
                generateDat.setData(previewResult);
                generateData.add(generateDat);
            }
        }
        if (generateData.size() > 0) {
            for (int i = 0; i < generateData.size(); i++) {
                String path = mCommand.getFilterPath().get(i);
                if (!FileReadHelper.checkFileExists(path)) {
                    Log.i("预加载文件不存在");
                    return;
                }
                List<String> previewData = FileReadHelper.readFile(path);
                for (int j = 0; j < previewData.size(); j++) {
                    String d = previewData.get(j);
                    String[] contents = d.split("-");
                    if (contents != null && contents.length > 0) {
//                    Log.i("line = " + splitLine.length);
                        generateData.get(i).getFilterData().add(new GenerateBaseDataBean.FilterData(Integer.parseInt(contents[0]), Integer.parseInt(contents[1])));
                    }
                }
            }
        }
        //开始计算
        generateResults(generateData);
    }

    ///////////////////////////////////////////////
    private void generateResults(List<GenerateBaseDataBean> generateData) {
        for (int i = 0; i < generateData.size(); i++) {
            Thread thread = new RunThread(generateData.get(i), this, generateData.get(i).getmPath());
            addThread(thread.getName());
            thread.start();
        }
    }
    ///////////////////////////////////////////////

    public void addThread(String name) {
        threadName.add(name);
    }

    public synchronized void onThreadCompile(String name, RunThread thread) {
        threadName.remove(name);
        threads.add(thread);
        if (threadName.size() <= 0) {
            if (threads.size() > 1) {
                doContainThread();//筛选的线程超过一个，则需要求并集
            } else {
                List<GenerateBaseDataBean.BaseData> result = new ArrayList<>(threads.get(0).mResultBean);
                printData(result);//开始统计数字
            }
        }
    }

    private void doContainThread() {
        //完成了，开始求并集
        Log.i("正在开始求并集...当前数据位置：" + currentFilterThreadId + "  总需要求的并集数据总数：" + threads.size());
        List<GenerateBaseDataBean.BaseData> data = threads.get(0).mResultBean;
        currentFilterThreadId++;
        List<GenerateBaseDataBean.BaseData> data2 = threads.get(1).mResultBean;
        currentFilterThreadId++;
        new ContainThread(this, data, data2)
                .start();
    }

    public void doContainThread(List<GenerateBaseDataBean.BaseData> lastResultMergeBeans) {
        compileThreadCount = 0;
        threadCount = 0;
        //完成了，开始求并集
        Log.i("正在求下一个并集...当前数据位置：" + currentFilterThreadId + "  总需要求的并集数据总数：" + threads.size());
        List<GenerateBaseDataBean.BaseData> data = lastResultMergeBeans;
        List<GenerateBaseDataBean.BaseData> data2 = threads.get(currentFilterThreadId).mResultBean;
        currentFilterThreadId++;
        new ContainThread(this, data, data2)
                .start();
    }

    public synchronized void containCompileData(List<GenerateBaseDataBean.BaseData> data) {
        //求并集的线程结束，需要判断是否还有筛选的数据没有进行并集求解
        Log.i("单次计算结果数：" + data.size());
        compileThreadCount++;
        if (threadCount <= compileThreadCount) {
            //这里进行判断，是不是已经执行到最后一个线程了
            if (currentFilterThreadId >= threads.size()) {//最后一个了
                printData(data);
            } else {//执行下一次结果
                doContainThread(data);
            }
        }
    }


    private void printData(List<GenerateBaseDataBean.BaseData> printData) {//统计数字并排序
        Log.i("------------------------------------本轮计算完成，结果数：" + printData.size() + "------------------------------------");
        if (currentGenerateIdx == 0) {
            threads.clear();
            compileData.clear();
            currentFilterThreadId = 0;
            currentGenerateIdx++;

            //删除了数字之后，开始往上合并
            append(printData);
        }
    }

    private void append(List<GenerateBaseDataBean.BaseData> inputData) {
        Log.i("按顺序去除!");
        List<Byte> data = new ArrayList<>();
        for (int i = 0; i < inputData.size(); i++) {
            GenerateBaseDataBean.BaseData baseData = inputData.get(i);
            for (int j = 0; j < baseData.getData().length; j++) {
                if (!data.contains(baseData.getData()[j])) {
                    data.add(baseData.getData()[j]);
                }
            }
        }
        data.sort(Byte::compareTo);
        //统计完之后，开始每4个合成一个,并

        ////////////////////////////按顺序去除数据，需要使用时请去除此段代码注释////////////////////////////////////////
        /*List<GenerateBaseDataBean.BaseData> baseData = new ArrayList<>();
        int dataSize = inputData.get(0).getData().length;
        for (int i = 0; i < data.size() - dataSize + 1; i++) {
            byte[] tempData = new byte[dataSize];
            for (int j = 0; j < dataSize; j++) {
                tempData[j] = data.get(i + j);
            }
            baseData.add(new GenerateBaseDataBean.BaseData(tempData, 0, -1));
        }
        inputData = ContainThread.getNotContain(inputData, baseData);*///去掉剩下的这些数
        ////////////////////////////按顺序去除数据，需要使用时请去除此段代码注释////////////////////////////////////////

        Log.i("升位!");
        //升位
        for (int i = 0; i < inputData.size(); i++) {
            GenerateBaseDataBean.BaseData tempBaseData = inputData.get(i);
            tempBaseData.getAppendData().add(new GenerateBaseDataBean.AppendData(tempBaseData.getData()));
            Log.i("i = " + i);
            for (int j = 0; j < i; j++) {
                GenerateBaseDataBean.BaseData tempBaseData2 = inputData.get(j);
                boolean exists = false;
                for (int k = 0; k < tempBaseData2.getAppendData().size(); k++) {
//                    Log.i("tempBaseData2.getAppendData().size() = " + tempBaseData2.getAppendData().size());
                    GenerateBaseDataBean.AppendData tempBaseData2AppendData = tempBaseData2.getAppendData().get(k);
                    ExistsBean existsBean = new ExistsBean();
                    if (tempBaseData2AppendData.getByteData().size() == tempBaseData.getData().length) {//开始合成n+1位
                        //先判定两个数是否只是一个数不同
                        int notExistsCount = getNotExistsDataCount(tempBaseData2AppendData.getByteData(), tempBaseData.getData(), existsBean);
                        if (notExistsCount == 1) {//只有一位不同的,加入
                            tempBaseData2AppendData.getByteData().add(existsBean.getNotExistsByte());
                            tempBaseData2AppendData.getByteData().sort(Byte::compareTo);
                            tempBaseData2AppendData.setAppendCount(tempBaseData2AppendData.getAppendCount() + 1);
                            exists = true;
                            break;
                        }
                    } else {//
                        int notExistsCount = equalsData(tempBaseData2AppendData.getByteData(), tempBaseData.getData());
                        if (notExistsCount == 0) {//都相同
                            tempBaseData2AppendData.setAppendCount(tempBaseData2AppendData.getAppendCount() + 1);
                            exists = true;
                            break;
                        }
                    }
                }
                if (!exists) {//如果不存在的话，尝试跟基础数进行比对，看看能否再合成一个
                    ExistsBean existsBean = new ExistsBean();
                    int notExistsCount = getNotExistsDataCount(tempBaseData.getData(), tempBaseData2.getData(), existsBean);
                    if (notExistsCount == 1) {//只有一位不同的,加入，否则就不管
                        GenerateBaseDataBean.AppendData appendData = new GenerateBaseDataBean.AppendData(tempBaseData.getData(), existsBean.getNotExistsByte());
                        appendData.getByteData().sort(Byte::compareTo);
                        tempBaseData2.getAppendData().add(appendData);
                    }
                }
                //与过往的数据进行判定,是否存在合成n+1位的结果
            }
        }
        List<GenerateBaseDataBean.AppendData> fullDataAppend = new ArrayList<>();
        int appendFullCount = 0;
        if (inputData.size() > 0) {
            appendFullCount = calculateAppendFullCount(inputData.get(0).getData().length + 1);
            //判定结束,开始统计哪些数没有合成五位数
            for (int i = 0; i < inputData.size(); i++) {
                GenerateBaseDataBean.BaseData base = inputData.get(i);
                for (int j = 0; j < base.getAppendData().size(); j++) {
                    GenerateBaseDataBean.AppendData appendData = base.getAppendData().get(j);
                    if (appendData.getAppendCount() == appendFullCount) {//这组中了
                        fullDataAppend.add(appendData);
                    }
                }
            }
        }
        //得到的五位数,跟baseData对比,将里面没有出现的数全部剔除
        if (fullDataAppend.size() == 0) {//升位失败
            Log.i("所有数据处理完毕，耗费时间：'" + (System.currentTimeMillis() - startTime) / 1000 + "'秒");
            if (mCallBackListener != null) {
                mCallBackListener.onCompile();
            }
        } else {
            Log.i("本轮计算结束，开始输出结果----" + inputData.size());
            List<GenerateBaseDataBean.BaseData> notFullAppendData = new ArrayList<>();
            for (int i = 0; i < inputData.size(); i++) {
                GenerateBaseDataBean.BaseData allBaseData = inputData.get(i);
                boolean exists = false;
                for (int j = 0; j < fullDataAppend.size(); j++) {
                    int notExistsCount = equalsData(fullDataAppend.get(j).getByteData(), allBaseData.getData());
                    if (notExistsCount == 0) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    notFullAppendData.add(allBaseData);
                }
            }
            //开始检查得到的五位里面中4\5位的个数
            List<GenerateBaseDataBean.AppendData> existsData4 = printSingleDataIdx(fullDataAppend, 4);//先得到符合的数
            List<GenerateBaseDataBean.AppendData> existsData5 = printSingleDataIdx(fullDataAppend, 5);//先得到符合的数
            List<GenerateBaseDataBean.BaseData> existsData4_2 = printSingleDataIdx2(notFullAppendData, 4);//先得到符合的数
            List<GenerateBaseDataBean.BaseData> existsData5_2 = printSingleDataIdx2(notFullAppendData, 5);//先得到符合的数
            //输出中四位\五位个数\总数\合成五位的数\剩余4位的数
            String outputFilePath = mCommand.getOutputPath().split("\\.")[0] + "-" + appendFullCount + "w.txt";
            String outputFilePath2 = mCommand.getOutputPath().split("\\.")[0] + "-" + appendFullCount + "w-" + (appendFullCount - 1) + "w.txt";
            try {
                FileReadHelper.writeToFile3(outputFilePath, outputFilePath2, fullDataAppend, notFullAppendData, existsData4.size(), existsData5.size(), existsData4_2.size(), existsData5_2.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //append转换为baseData,再往上合并
            append(convertAppend2BaseData(fullDataAppend));
        }
    }

    private List<GenerateBaseDataBean.BaseData> convertAppend2BaseData(List<GenerateBaseDataBean.AppendData> appendData) {
        List<GenerateBaseDataBean.BaseData> result = new ArrayList<>();
        for (int i = 0; i < appendData.size(); i++) {
            result.add(new GenerateBaseDataBean.BaseData(appendData.get(i).getByteData(), 1));
        }
        return result;
    }

    private int calculateAppendFullCount(int count) {
        return calculateAppendCount(count) / calculateAppendCount(count - 1);
    }

    private int calculateAppendCount(int count) {
        if (count == 1) {
            return count;
        }
        return count * calculateAppendCount(count - 1);
    }

    private List<GenerateBaseDataBean.AppendData> printSingleDataIdx(List<GenerateBaseDataBean.AppendData> data, int existsCountStandard) {
        List<GenerateBaseDataBean.AppendData> resultData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            GenerateBaseDataBean.AppendData d = data.get(i);
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < d.getByteData().size(); j++) {
                    if (mCommand.getCheckData()[k] == d.getByteData().get(j)) {
                        existsCount++;
                        break;
                    }
                }
            }
            if (existsCount == existsCountStandard) {
                resultData.add(d);
            }
        }
        return resultData;
    }

    private List<GenerateBaseDataBean.BaseData> printSingleDataIdx2(List<GenerateBaseDataBean.BaseData> data, int existsCountStandard) {
        List<GenerateBaseDataBean.BaseData> resultData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            GenerateBaseDataBean.BaseData d = data.get(i);
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < d.getData().length; j++) {
                    if (mCommand.getCheckData()[k] == d.getData()[j]) {
                        existsCount++;
                        break;
                    }
                }
            }
            if (existsCount == existsCountStandard) {
                resultData.add(d);
            }
        }
        return resultData;
    }

    private int getNotExistsDataCount(List<Byte> data1, byte[] data2, ExistsBean existsBean) {
        int count = 0;
        int notExistsCount = 0;
        for (int i = 0; i < data2.length; i++) {
            boolean exists = false;
            for (int j = 0; j < data1.size(); j++) {
                if (data2[i] == data1.get(j)) {
                    count++;
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                if (existsBean != null) {
                    existsBean.setNotExistsByte(data2[i]);
                }
                notExistsCount++;
            }
            if (notExistsCount > 1) {//超过两个不同，直接退出了
                return data1.size();
            }
        }
        return data1.size() - count;
    }

    private int getNotExistsDataCount(byte[] data1, byte[] data2, ExistsBean existsBean) {
        int count = 0;
        int notExistsCount = 0;
        for (int i = 0; i < data2.length; i++) {
            boolean exists = false;
            for (int j = 0; j < data1.length; j++) {
                if (data2[i] == data1[j]) {
                    count++;
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                if (existsBean != null) {
                    existsBean.setNotExistsByte(data2[i]);
                }
                notExistsCount++;
            }
            if (notExistsCount > 1) {//超过两个不同，直接退出了
                return data1.length;
            }
        }
        return data1.length - count;
    }

    private int equalsData(List<Byte> data1, byte[] data2) {
        int count = 0;
        for (int i = 0; i < data2.length; i++) {
            boolean exists = false;
            for (int j = 0; j < data1.size(); j++) {
                if (data2[i] == data1.get(j)) {
                    count++;
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                return data2.length;
            }
        }
        return data2.length - count;
    }

}
