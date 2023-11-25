package com.dragon.lucky.command15;

import com.dragon.lucky.bean.ContentBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;

    private CallbackListener mCallBackListener;

    private List<LineBean> allLineData;
    private LineBean mLastNumber;
    private volatile int mThreadCount;

    private long calculateCount = 0;

    private List<ContainThread> mThreads = new ArrayList<>();

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

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        service = Executors.newFixedThreadPool(10);
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        List<String> inputData = FileReadHelper.readFile(command.getInputPath());
        allLineData = new ArrayList<>();
        List<LineBean> inputResult = new ArrayList<>();
        for (int i = 0; i < inputData.size(); i++) {
            String d = inputData.get(i);
            String[] contents = d.split("-");
            d = contents[0];
            int generateSize = 0;
            int[] generateSizeArr = null;
            if (contents[1].contains("、")) {
                String[] contentArr = contents[1].split("、");
                generateSizeArr = new int[contentArr.length];
                for (int j = 0; j < contentArr.length; j++) {
                    generateSizeArr[j] = Integer.parseInt(contentArr[j]);
                }
            } else {
                generateSize = Integer.parseInt(contents[1]);
            }
            String[] splitLine;
            if (d.contains("、")) {
                splitLine = d.replace(" ", "").split("、");
            } else {
                splitLine = d.replace(" ", "").split(",");
            }
            if (splitLine != null && splitLine.length > 0) {
//                    Log.i("line = " + splitLine.length);
                byte[] numbers = new byte[splitLine.length];
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        numbers[j] = Byte.parseByte(splitLine[j]);
                    }
                }
                List<NumberBean> numberData = new ArrayList<>();
                if (generateSizeArr != null) {
                    for (int j = 0; j < generateSizeArr.length; j++) {
                        numberData.add(new NumberBean(numbers, generateSizeArr[j]));
                    }
                    inputResult.add(new LineBean(numberData));
                } else {
                    numberData.add(new NumberBean(numbers, generateSize));
                    inputResult.add(new LineBean(numberData));
                }
            }
        }
        allLineData.addAll(inputResult);

        StringBuilder print = new StringBuilder();
        //拿到基础数之后，开始按照需要生成对应的数据队列
        for (int i = 0; i < inputResult.size(); i++) {
            LineBean nb = inputResult.get(i);
            List<NumberBean> numberData = nb.getData();
            for (int j = 0; j < numberData.size(); j++) {
                NumberBean number = numberData.get(j);
                List<NumberDataBean> result = new ArrayList<>();
                int[] resultArr = new int[number.getGenerateSize()];
                if (number.getGenerateSize() != 0) {
                    Utils.combine_increase_for_number(number.getData(), 0, resultArr, number.getGenerateSize(), number.getGenerateSize(), number.getData().length, result);
                }
                number.setGenerateData(result);
                print.append("生成数据数量:").append(result.size());
                print.append(" ");
            }
        }
        Log.i(print.toString());
        mLastNumber = inputResult.remove(inputResult.size() - 1);
        Log.i("数据生成完毕");
        //开始向下合并

        //拿到数据进行判断是否合适使用
        List<NumberBean> headLineData = inputResult.remove(0).getData();
        for (int i = 0; i < headLineData.size(); i++) {
            List<NumberBean> headSingleLineData = new ArrayList<>();
            headSingleLineData.add(headLineData.get(i));
            append(headSingleLineData, new ArrayList<>(inputResult));
        }
    }

    private synchronized void addThreadCount() {
        mThreadCount++;
        Log.i("生成线程数:" + mThreadCount);
    }

    private ExecutorService service = null;

    private void append(List<NumberBean> numberData, List<LineBean> data) {
//        Log.i("开始递归生成交集队列：" + data.size());
        if (data.size() == 0) {
            //开始合并
            addThreadCount();
            service.submit(new ContainThread(numberData, 1, mThreadCount));
        } else {
            LineBean lineData = data.remove(0);
            List<NumberBean> numbers = lineData.getData();
            for (int j = 0; j < numbers.size(); j++) {
                List<NumberBean> temp = new ArrayList<>();
                if (numberData != null) {
                    temp.addAll(numberData);
                }
                temp.add(numbers.get(j));
                append(temp, new ArrayList<>(data));
            }
        }
    }

    private void append(NumberBean numberData, LineBean lineData) {
        List<NumberBean> numbers = lineData.getData();
        Log.i("numbers.size() = " + numbers.size());
        for (int j = 0; j < numbers.size(); j++) {
            List<NumberBean> temp = new ArrayList<>();
            if (numberData != null) {
                temp.add(numberData);
            }
            temp.add(numbers.get(j));
            //开始合并
            addThreadCount();
            service.submit(new ContainThread(temp, 2, mThreadCount));
        }
    }

    private NumberBean containChild(List<NumberBean> data, int type) {
        List<NumberDataBean> appendNumberData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            //开始合并数
            appendNumberData.addAll(data.get(i).getGenerateData());
        }
        List<NumberDataBean> collectNumber = null;
        if (type == 2) {
            collectNumber = appendNumberData;
        } else {
            Log.i("去重前合并数:" + appendNumberData.size());
            collectNumber = new ArrayList<>(removeDuplicates(appendNumberData));
            Log.i("去重完毕,结果数：" + collectNumber.size());
        }
        return new NumberBean(collectNumber);
    }

    private void appendChild(List<NumberBean> data, int type) {
        NumberBean n1 = data.remove(0);
        NumberBean n2 = data.remove(0);
        List<NumberDataBean> d1 = n1.getGenerateData();
        List<NumberDataBean> d2 = n2.getGenerateData();
//        Log.i("数组1长度 = " + d1.size());
//        Log.i("数组2长度 = " + d2.size());
        NumberBean result = new NumberBean();
        List<NumberDataBean> resultData = new ArrayList<>();
        result.setGenerateData(resultData);
        if (d1.size() == 0) {
            resultData.addAll(d2);
        } else if (d2.size() == 0) {
            resultData.addAll(d1);
        } else {
            if (type == 2) {
                for (int i = 0; i < d1.size(); i++) {
                    NumberDataBean d1S = d1.get(i);
                    for (int j = 0; j < d2.size(); j++) {
                        NumberDataBean d2S = d2.get(j);
                        byte[] b1 = d1S.getData();
                        byte[] b2 = d2S.getData();
                        byte[] d = new byte[b1.length + b2.length];
                        for (int k = 0; k < b1.length; k++) {
                            d[k] = b1[k];
                        }
                        if (calculateCount % 1000000 == 0) {
//                        Log.i("calculateCount = " + calculateCount);
                        }
                        calculateCount++;
                        int addCount = 0;
                        for (int k = 0; k < b2.length; k++) {
                            d[b1.length + addCount] = b2[k];
                            addCount++;
                        }
//                        d = Arrays.copyOf(d, b1.length + addCount);//重新拷贝数据
                        Arrays.sort(d);
                        resultData.add(new NumberDataBean(d));//将结果赋值进去
                    }
                }
            } else {
                for (int i = 0; i < d1.size(); i++) {
                    NumberDataBean d1S = d1.get(i);
                    for (int j = 0; j < d2.size(); j++) {
                        NumberDataBean d2S = d2.get(j);
                        byte[] b1 = d1S.getData();
                        byte[] b2 = d2S.getData();
                        byte[] d = new byte[b1.length + b2.length];
                        for (int k = 0; k < b1.length; k++) {
                            d[k] = b1[k];
                        }
                        if (calculateCount % 1000000 == 0) {
//                        Log.i("calculateCount = " + calculateCount);
                        }
                        calculateCount++;
                        int addCount = 0;
                        for (int k = 0; k < b2.length; k++) {
                            boolean contain = false;
                            for (int l = 0; l < d.length; l++) {
                                if (d[l] == b2[k]) {
                                    contain = true;
                                    break;
                                }
                            }
                            if (!contain) {
                                addCount++;
                                d[b1.length + addCount - 1] = b2[k];
                            }
                        }
                        d = Arrays.copyOf(d, b1.length + addCount);//重新拷贝数据
                        Arrays.sort(d);
                        resultData.add(new NumberDataBean(d));//将结果赋值进去
                    }
                }
            }
        }
//        Log.i(Thread.currentThread().getName() + " 开始去重：" + resultData.size());
        List<NumberDataBean> collectNumber = null;
        if (type == 2) {
            collectNumber = resultData;
        } else {
            collectNumber = new ArrayList<>(removeDuplicates(resultData));
        }
//        Log.i(Thread.currentThread().getName() + " 结果数：" + collectNumber.size());
        result.setGenerateData(collectNumber);
        data.add(0, result);
        if (data.size() <= 1) {
            Log.i(Thread.currentThread().getName() + " 第三阶段处理完毕：" + data.get(0).getGenerateData().size());
        } else {
//            Log.i(Thread.currentThread() + " 进入循环，剩余计算数： " + data.size());
            appendChild(data, type);
        }
    }

    private List<NumberDataBean> removeDuplicates(List<NumberDataBean> resultData) {
        //去重
        /*List<NumberDataBean> collectNumber = new ArrayList<>();
        for (int i = 0; i < resultData.size(); i++) {
            NumberDataBean numberBean = resultData.get(i);
//            Log.i(Arrays.toString(numberBean.getData()));
            if (collectNumber.size() == 0) {
                collectNumber.add(numberBean);
                continue;
            }
            Log.i("运行第 " + i + "个,总长度 " + resultData.size() + " 现有结果数:" + collectNumber.size());
            Iterator<NumberDataBean> iterator = collectNumber.iterator();
            boolean contain = false;
            while (iterator.hasNext()) {
                NumberDataBean nb = iterator.next();
                if (nb.getData().length != numberBean.getData().length) {
                    continue;
                }
                if (Arrays.equals(nb.getData(), numberBean.getData())) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                collectNumber.add(numberBean);
            }//得到结果
        }*/

        List<ContainBean> checkData = new ArrayList<>();
        for (int i = 0; i < resultData.size(); i++) {
            if (i % 1000000 == 0) {
//                Log.i("运行第 " + i + "个,总长度 " + resultData.size());
            }
            checkData.add(new ContainBean(Arrays.toString(resultData.get(i).getData()), i));
        }
        HashSet<ContainBean> filterData = new HashSet<>(checkData);
        List<ContainBean> checkData2 = new ArrayList<>(filterData);

        List<NumberDataBean> collectNumber = new ArrayList<>();
        for (int i = 0; i < checkData2.size(); i++) {
            if (i % 1000000 == 0) {
//                Log.i("运行第 " + i + "个,总长度 " + resultData.size() + "  记录的数据长度:" + collectNumber.size());
            }
//            Log.i(Arrays.toString(resultData.get(checkData2.get(i).getId()).getData()));
            collectNumber.add(resultData.get(checkData2.get(i).getId()));
        }

        return collectNumber;
    }

    private int printSingleDataIdx(List<NumberDataBean> data, int existsCountStandard) {
        int size = 0;
        for (int i = 0; i < data.size(); i++) {
            NumberDataBean d = data.get(i);
            /*if (d.getData().length != 5) {
                continue;
            }*/
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
                size++;
            }
        }
        return size;
    }

    private class ContainThread implements Runnable {

        private List<NumberBean> mData;
        private List<NumberBean> mTempData;
        private int mType;
        private int mId;

        public ContainThread(List<NumberBean> data, int type, int id) {
            this.mData = data;
            this.mType = type;
            this.mId = id;
        }

        @Override
        public void run() {
            try {
                Log.i("休眠时长 = " + mId);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mTempData = new ArrayList<>(mData);
            Log.i("求合集线程正在运行：" + Thread.currentThread().getName());
            int size = 1;
            if (mType == 1) {
                int generateDataSize = 0;
                for (int i = 0; i < mData.size(); i++) {
                    NumberBean nb = mData.get(i);
                    generateDataSize += nb.getGenerateSize();
                    if (nb.getGenerateData().size() != 0) {
                        size *= nb.getGenerateData().size();
                    }
//                    Log.i("size = " +  size);
                }
                Log.i("本次计算的数据长度 = " + generateDataSize + "  数组长度:" + size);
                if (mCommand.getFirstFilterDataLen() != -1 && generateDataSize < mCommand.getFirstFilterDataLen()) {
                    //这种长度的不处理
                    Log.i("数据长度不符合要求,移除计算");
                    mData.clear();
                }
            } else {
                int generateDataSize = mData.get(mData.size() - 1).getGenerateSize() + mCommand.getFirstFilterDataLen();
                /*for (int i = 0; i < mData.size(); i++) {
                    NumberBean nb = mData.get(i);
                    generateDataSize += nb.getGenerateSize();
                    if (nb.getGenerateData().size() != 0) {
                        size *= nb.getGenerateData().size();
                    }
                    Log.i("size = " + size);
                }*/
                Log.i("本次计算的数组长度:" + size);
                if (mCommand.getSecondFilterDataLen() != -1 && generateDataSize != mCommand.getSecondFilterDataLen()) {
                    //这种长度的不处理
                    Log.i("最后一轮计算，数据长度不符合要求,移除计算");
                    mData.clear();
                }
            }
            if (mData.size() > 1) {
                appendChild(mData, mType);
                if (mType == 2) {
                    mData.get(0).setGenerateData(mData.get(0).getGenerateData());
                } else {
                    mData.get(0).setGenerateData(FilterUtils.filterResultBean2(mData.get(0).getGenerateData(), mTempData));
                }
//                mData.get(0).setGenerateData(mData.get(0).getGenerateData());
            }
            if (mType == 1) {
                onContainCompile(this);
            } else {
                onContainLastPartCompile(this);
            }
        }
    }

    private synchronized void onContainCompile(ContainThread containThread) {
        Log.i("合集线程：" + Thread.currentThread().getName() + "运行完毕！");
        mThreadCount--;
        Log.i("剩余数:" + mThreadCount);
        mThreads.add(containThread);

        /*containThread.mData.get(0).getGenerateData().sort(Comparator.comparing(o -> Arrays.toString(o.getData())));
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------结果：" + containThread.mData.get(0).getGenerateData().size() + "条-----------------------------");
        sb.append("\n");
        Log.i("结果数：" + sb.toString());
        if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
            try {
                FileReadHelper.writeToFileForRun15(mCommand.getOutputPath() + containThread.getName() + "-base.txt", sb.toString(), containThread.mData.get(0).getGenerateData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        if (mThreadCount <= 0) {
            //合并结束，进行交集
            List<NumberBean> temp = new ArrayList<>();
            for (int i = 0; i < mThreads.size(); i++) {
                temp.addAll(mThreads.get(i).mData);
            }
            mThreads.clear();
            Log.i("所有合集线程运行完毕");
            NumberBean firstNumber = containChild(temp, 1);
            //得到最终结果
            List<NumberDataBean> filterResultData = new ArrayList<>();
            for (int i = 0; i < firstNumber.getGenerateData().size(); i++) {
                NumberDataBean ndb = firstNumber.getGenerateData().get(i);
                if (mCommand.getFirstFilterDataLen() == -1 || ndb.getData().length == mCommand.getFirstFilterDataLen()) {
                    filterResultData.add(ndb);
//                    Log.i("data = " + ndb.getData().toString());
                }
            }
            firstNumber.setGenerateData(filterResultData);

            /*filterResultData.sort(Comparator.comparing(o -> Arrays.toString(o.getData())));
            StringBuilder sb = new StringBuilder();
            sb.append("-----------------------结果：" + filterResultData.size() + "条-----------------------------");
            sb.append("\n");
            Log.i("结果数：" + sb.toString());
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFileForRun15(mCommand.getOutputPath() + "-base.txt", sb.toString(), filterResultData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

            /////////////////////////////////////////////////
            append(firstNumber, mLastNumber);
        }
    }

    private synchronized void onContainLastPartCompile(ContainThread containThread) {
        Log.i("合集线程：" + Thread.currentThread().getName() + "运行完毕！");
        mThreadCount--;
//        FilterUtils.filterResultBean2(containThread.mData.get(0).getGenerateData(), containThread.mTempData);
        mThreads.add(containThread);
        if (mThreadCount <= 0) {
            service.shutdown();
            Log.i("所有合集线程运行完毕，开始合并数！");
            //合并结束，进行交集
            List<NumberBean> temp = new ArrayList<>();
            for (int i = 0; i < mThreads.size(); i++) {
                temp.addAll(mThreads.get(i).mData);
            }
            NumberBean lastNumber = containChild(temp, 2);

            /////////////////////////////////////////////////
//            List<NumberDataBean> resultData = lastNumber.getGenerateData();

            List<NumberDataBean> filterResultData2 = lastNumber.getGenerateData();
            /*for (int i = 0; i < resultData.size(); i++) {
                NumberDataBean ndb = resultData.get(i);
                if (mCommand.getSecondFilterDataLen() == -1 || ndb.getData().length == mCommand.getSecondFilterDataLen()) {
                    filterResultData2.add(ndb);//得到结果
                }
            }*/
            filterResultData2.sort(Comparator.comparing(o -> Arrays.toString(o.getData())));

//            Log.i("filterResultData2 = " + filterResultData2.size());
//            List<LineBean> lastLine = new ArrayList<>();
//            lastLine.add(mLastNumber);
//            FilterUtils.filterResultBean(filterResultData2, lastLine);
//            Log.i("filterResultData2 = " + filterResultData2.size());

            Log.i("所有数据运算完毕");
            Log.i("正在进行独立过滤运算");
            Log.i("-----------------------结果：" + filterResultData2.size() + "条-----------------------------");
            /////////////////////////////////////////////////
            if (mCommand.getCheckData() != null && mCommand.getCheckData().length > 0) {
//                filterResultData2 = FilterUtils.filterNumber(filterResultData2);
                //开始检查得到的五位里面中4\5位的个数
                int existsData1 = printSingleDataIdx(filterResultData2, 1);//先得到符合的数
                int existsData2 = printSingleDataIdx(filterResultData2, 2);//先得到符合的数
                int existsData3 = printSingleDataIdx(filterResultData2, 3);//先得到符合的数
                int existsData4 = printSingleDataIdx(filterResultData2, 4);//先得到符合的数
                int existsData5 = printSingleDataIdx(filterResultData2, 5);//先得到符合的数
                StringBuilder sb = new StringBuilder();
                sb.append("-----------------------结果：" + filterResultData2.size() + "条-----------------------------");
                sb.append("\n");
                /*sb.append("中“1”位数量：" + existsData1);
                sb.append("\n");
                sb.append("中“2”位数量：" + existsData2);
                sb.append("\n");
                sb.append("中“3”位数量：" + existsData3);
                sb.append("\n");
                sb.append("中“4”位数量：" + existsData4);
                sb.append("\n");
                sb.append("中“5”位数量：" + existsData5);
                sb.append("\n");*/


                float percent1 = (existsData1 / (float) filterResultData2.size()) * 100;
                sb.append("中“1”位数量：").append(existsData1).append("     ").append(String.format("%.2f", percent1)).append("%");
                sb.append("\n");
                float percent2 = (existsData2 / (float) filterResultData2.size()) * 100;
                sb.append("中“2”位数量：").append(existsData2).append("     ").append(String.format("%.2f", percent2)).append("%");
                sb.append("\n");
                float percent3 = (existsData3 / (float) filterResultData2.size()) * 100;
                sb.append("中“3”位数量：").append(existsData3).append("     ").append(String.format("%.2f", percent3)).append("%");
                sb.append("\n");
                float percent4 = (existsData4 / (float) filterResultData2.size()) * 100;
                sb.append("中“4”位数量：").append(existsData4).append("     ").append(String.format("%.2f", percent4)).append("%");
                sb.append("\n");
                float percent5 = (existsData5 / (float) filterResultData2.size()) * 100;
                sb.append("中“5”位数量：").append(existsData5).append("     ").append(String.format("%.2f", percent5)).append("%");
                sb.append("\n");

                sb.append("总数：" + (existsData1 + existsData2 + existsData3 + existsData4 + existsData5));
                sb.append("\n");
                Log.i("结果数：" + sb.toString());
                if (filterResultData2.size() > 3000000) {
                    if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                        try {
                            FileReadHelper.writeToFileForRun15(mCommand.getOutputPath(), sb.toString(), filterResultData2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (int i = 0; i < filterResultData2.size(); i++) {
                        Arrays.sort(filterResultData2.get(i).getData());
                        sb.append(Arrays.toString(filterResultData2.get(i).getData()));
                        sb.append("\n");
                    }
                    if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                        try {
                            FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (mCallBackListener != null) {
                    mCallBackListener.onCompile();
                } else {
                    Log.i("所有数据处理完成！");
                }
            } else {//直接输出
//                filterResultData2 = FilterUtils.filterNumber(filterResultData2);
                StringBuilder sb = new StringBuilder();
                sb.append("-----------------------结果：" + filterResultData2.size() + "条-----------------------------");
                sb.append("\n");
                for (int i = 0; i < filterResultData2.size(); i++) {
                    sb.append(Arrays.toString(filterResultData2.get(i).getData()));
                    sb.append("\n");
                }
                if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                    try {
                        FileReadHelper.writeToFile(mCommand.getOutputPath(), sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (mCallBackListener != null) {
                    mCallBackListener.onCompile();
                } else {
                    Log.i("所有数据处理完成！");
                }
            }
        }
    }

}
