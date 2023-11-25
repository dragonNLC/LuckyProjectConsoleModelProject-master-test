package com.dragon.lucky.command14;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.FileReadHelper;
import com.dragon.lucky.utils.Log;
import com.dragon.lucky.utils.Utils;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GenerateRunner {

    private static GenerateRunner sInstance;
    private CommandBean mCommand;

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

    private CallbackListener mCallBackListener;

    public void run(CommandBean command, CallbackListener callbackListener) throws IOException {
        this.mCallBackListener = callbackListener;
        this.mCommand = command;
        if (!FileReadHelper.checkFileExists(command.getInputPath())) {
            Log.i("预加载文件不存在");
            return;
        }
        List<String> inputData = FileReadHelper.readFile(command.getInputPath());
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
                List<Integer> numbers = new ArrayList<>();
                for (int j = 0; j < splitLine.length; j++) {
                    if (Utils.isNumeric(splitLine[j])) {
                        numbers.add(Integer.parseInt(splitLine[j]));
                    }
                }
                List<NumberBean> numberData = new ArrayList<>();
                if (generateSizeArr != null) {
                    for (int j = 0; j < generateSizeArr.length; j++) {
                        numberData.add(new NumberBean(numbers, generateSizeArr[j]));
                        Log.i("generateSizeArr[j] = " + generateSizeArr[j]);
                    }
                    inputResult.add(new LineBean(numberData));
                } else {
                    numberData.add(new NumberBean(numbers, generateSize));
                    inputResult.add(new LineBean(numberData));
                }
            }
        }

        //拿到基础数之后，开始按照需要生成对应的数据队列
        for (int i = 0; i < inputResult.size(); i++) {
            LineBean nb = inputResult.get(i);
            List<NumberBean> numberData = nb.getData();
            for (int j = 0; j < numberData.size(); j++) {
                NumberBean number = numberData.get(j);
                List<NumberDataBean> result = new ArrayList<>();
                int[] resultArr = new int[number.getGenerateSize()];
                if (number.getGenerateSize() != 0) {
                    Utils.combine_increase_for_number(number.getData(), 0, resultArr, number.getGenerateSize(), number.getGenerateSize(), number.getData().size(), result);
                }
                number.setGenerateData(result);
//                Log.i("result = " + result.toString());
//                Log.i("result = " + result.toString());
            }
        }
        LineBean lastNumber = inputResult.remove(inputResult.size() - 1);
        Log.i("数据生成完毕");
        //开始向下合并
        //拿到数据进行判断是否合适使用
        NumberBean firstNumber = append(inputResult);

        List<NumberDataBean> filterResultData = new ArrayList<>();
//        Log.i("firstNumber.getGenerateData().size() = " + firstNumber.getGenerateData().size());
        for (int i = 0; i < firstNumber.getGenerateData().size(); i++) {
            NumberDataBean ndb = firstNumber.getGenerateData().get(i);
            if (ndb.getData().size() == command.getFirstFilterDataLen()) {
                filterResultData.add(ndb);
//                Log.i("data = " + ndb.getData().toString());
            }
        }
        firstNumber.setGenerateData(filterResultData);

        /////////////////////////////////////////////////
        List<NumberDataBean> resultData = new ArrayList<>();
        List<LineBean> tempLines = new ArrayList<>();
        tempLines.add(lastNumber);

        NumberBean lastGenerateResultNumber = appendChildren(firstNumber, tempLines);
        resultData = lastGenerateResultNumber.getGenerateData();

        List<NumberDataBean> filterResultData2 = new ArrayList<>();
        for (int i = 0; i < resultData.size(); i++) {
            NumberDataBean ndb = resultData.get(i);
            if (ndb.getData().size() == command.getSecondFilterDataLen()) {
                filterResultData2.add(ndb);//得到结果
            }
        }
        Log.i("第五阶段处理完毕");
        /////////////////////////////////////////////////
        if (mCommand.getCheckData() != null && mCommand.getCheckData().length > 0) {
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

            /*for (int i = 0; i < filterResultData2.size(); i++) {
                filterResultData2.get(i).getData().sort(Integer::compareTo);
                sb.append(filterResultData2.get(i).getData().toString());
                sb.append("\n");
            }*/
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFileForRun14(mCommand.getOutputPath(), sb.toString(), filterResultData2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mCallBackListener != null) {
                mCallBackListener.onCompile();
            } else {
                Log.i("所有数据处理完成！");
            }
        } else {//直接输出
            StringBuilder sb = new StringBuilder();
            sb.append("-----------------------结果：" + filterResultData2.size() + "条-----------------------------");
            sb.append("\n");
            /*for (int i = 0; i < filterResultData2.size(); i++) {
                sb.append(filterResultData2.get(i).getData().toString());
                sb.append("\n");
            }*/
            if (mCommand.getOutputPath() != null && !mCommand.getOutputPath().equals("")) {
                try {
                    FileReadHelper.writeToFileForRun14(mCommand.getOutputPath(), sb.toString(), filterResultData2);
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

    private NumberBean append(List<LineBean> data) {
        Log.i("开始合并数 = " + data.size());
        LineBean lineBean = data.remove(0);
        List<NumberBean> results = new ArrayList<>();
        for (int i = 0; i < lineBean.getData().size(); i++) {
            NumberBean nb = lineBean.getData().get(i);
            results.add(appendChildren(nb, data));
        }
        if (results.size() > 1) {
            appendChild(results);
        }
        return results.get(0);
    }

    private NumberBean appendChildren(NumberBean numbers, List<LineBean> data) {
        LineBean l1 = data.remove(0);

        List<NumberBean> l1Numbers = l1.getData();

        List<NumberBean> appendData = new ArrayList<>();

        for (int i = 0; i < l1Numbers.size(); i++) {
            List<NumberBean> template = new ArrayList<>();
            template.add(numbers);
            template.add(l1Numbers.get(i));
            appendChild(template);
            appendData.add(template.get(0));
            Log.i("template = " + template.size());
        }


        List<NumberDataBean> appendNumberData = new ArrayList<>();
        for (int i = 0; i < appendData.size(); i++) {
            //开始合并数
            appendNumberData.addAll(appendData.get(i).getGenerateData());
        }


        //去重
        List<NumberDataBean> collectNumber = new ArrayList<>();
        for (int i = 0; i < appendNumberData.size(); i++) {
            NumberDataBean numberBean = appendNumberData.get(i);
            if (collectNumber.size() == 0) {
                collectNumber.add(numberBean);
                continue;
            }
            Iterator<NumberDataBean> iterator = collectNumber.iterator();
            boolean contain = false;
            while (iterator.hasNext()) {
//                        Log.i("处理下一个");
                NumberDataBean nb = iterator.next();
                if (nb.getData().size() != numberBean.getData().size()) {
                    break;
                }
                int existsCount = 0;
                for (int j = 0; j < nb.getData().size(); j++) {
                    boolean exists = false;
                    for (int k = 0; k < numberBean.getData().size(); k++) {
                        if (nb.getData().get(j).equals(numberBean.getData().get(k))) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        existsCount++;
                    }
                }
                if (existsCount == nb.getData().size()) {
                    contain = true;
//                    Log.i("contain2 = " + numberBean.getData().toString() + "\ncontain1 = " + nb.getData().toString());
                }
            }
            if (!contain) {
                collectNumber.add(numberBean);
            }//得到结果
        }
        Log.i("结果数：" + collectNumber.size());

        if (data.size() <= 0) {
            Log.i("第二阶段处理完毕");
            return new NumberBean(collectNumber);
        } else {
            Log.i("第一阶段进入循环：" + data.size());
            NumberBean temp = new NumberBean(collectNumber);
            return appendChildren(temp, new ArrayList<>(data));
        }
    }

    private void appendChild(List<NumberBean> data) {
        NumberBean n1 = data.remove(0);
        NumberBean n2 = data.remove(0);
        List<NumberDataBean> d1 = n1.getGenerateData();
        List<NumberDataBean> d2 = n2.getGenerateData();
        Log.i("d1 = " + d1.size());
        Log.i("d2 = " + d2.size());
        NumberBean result = new NumberBean();
        List<NumberDataBean> resultData = new ArrayList<>();
        result.setGenerateData(resultData);
        if (d1.size() == 0) {
            resultData.addAll(d2);
        } else if (d2.size() == 0) {
            resultData.addAll(d1);
        } else {
            for (int i = 0; i < d1.size(); i++) {
                NumberDataBean d1S = d1.get(i);
//                Log.i("d1S = " + d1.size());
//                Log.i("d2S = " + d2.size());
                for (int j = 0; j < d2.size(); j++) {
                    NumberDataBean d2S = d2.get(j);
                    List<Integer> b1 = d1S.getData();
                    List<Integer> b2 = d2S.getData();
//                Log.i("b1 = " + b1.toString());
//                Log.i("b2 = " + b2.toString());
                    List<Integer> d = new ArrayList<>(b1);
                    if (calculateCount % 1000000 == 0) {
//                        Log.i("calculateCount = " + calculateCount);
                    }
                    calculateCount++;
                    for (int k = 0; k < b2.size(); k++) {
                        if (!d.contains(b2.get(k))) {
                            d.add(b2.get(k));//去重了
//                        Log.i("d = " + d.toString());
                        }
                    }
                    resultData.add(new NumberDataBean(d));//将结果赋值进去
                }
            }
        }

        Log.i("开始去重");
        //去重
        List<NumberDataBean> collectNumber = new ArrayList<>();
        for (int i = 0; i < resultData.size(); i++) {
            NumberDataBean numberBean = resultData.get(i);
            if (collectNumber.size() == 0) {
                collectNumber.add(numberBean);
                continue;
            }
            Iterator<NumberDataBean> iterator = collectNumber.iterator();
            boolean contain = false;
            while (iterator.hasNext()) {
//                        Log.i("处理下一个");
                NumberDataBean nb = iterator.next();
                if (nb.getData().size() != numberBean.getData().size()) {
                    break;
                }
                int existsCount = 0;
                for (int j = 0; j < nb.getData().size(); j++) {
                    boolean exists = false;
                    for (int k = 0; k < numberBean.getData().size(); k++) {
                        if (nb.getData().get(j).equals(numberBean.getData().get(k))) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        existsCount++;
                    }
                }
                if (existsCount == nb.getData().size()) {
                    contain = true;
//                    Log.i("contain2 = " + numberBean.getData().toString() + "\ncontain1 = " + nb.getData().toString());
                }
            }
            if (!contain) {
                collectNumber.add(numberBean);
            }//得到结果
        }
        Log.i("结果数：" + collectNumber.size());
        result.setGenerateData(collectNumber);

        data.add(0, result);
        if (data.size() <= 1) {
            Log.i("第三阶段处理完毕");
        } else {
            Log.i("进入循环，剩余计算数： " + data.size());
            appendChild(data);
        }
    }

    private long calculateCount = 0;


    private int printSingleDataIdx(List<NumberDataBean> data, int existsCountStandard) {
        int size = 0;
        for (int i = 0; i < data.size(); i++) {
            NumberDataBean d = data.get(i);
            int existsCount = 0;
            for (int k = 0; k < mCommand.getCheckData().length; k++) {
                for (int j = 0; j < d.getData().size(); j++) {
                    if (mCommand.getCheckData()[k] == d.getData().get(j)) {
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

}
