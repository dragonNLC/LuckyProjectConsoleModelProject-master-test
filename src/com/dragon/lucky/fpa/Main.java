package com.dragon.lucky.fpa;


import com.dragon.lucky.bean.TwoPoint;
import com.dragon.lucky.command19.RunnerMain19;
import com.dragon.lucky.command3.RunnerMain3;
import com.dragon.lucky.command7.RunnerMain7;
import com.dragon.lucky.fpa.bean.*;
import com.dragon.lucky.fpa.utils.FileReadHelper;
import com.dragon.lucky.fpa.utils.JsoupHelper;
import com.dragon.lucky.fpa.utils.Log;
import com.dragon.lucky.fpa.utils.POIExcelUtils;
import com.dragon.lucky.spider.Main2;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static final String PATH = "C:\\Users\\aptdev\\Desktop\\za\\20231030\\";

    public static void main(String[] args) throws IOException {
//        JsoupHelper.doConnect1();
//        JsoupHelper.doConnect2();
//        JsoupHelper.doConnect3();
//        JsoupHelper.doConnect4();
//        JsoupHelper.doConnect5();
//        JsoupHelper.doConnect6();
//        JsoupHelper.doConnect7();
//        JsoupHelper.doConnect8();
//        JsoupHelper.doConnect9();
//        JsoupHelper.doConnect10();
//        JsoupHelper.doConnect11();
//        JsoupHelper.doConnect13();
        List<PreviewDataBean> data1 = JsoupHelper.doConnect1();
        DMBean data2 = JsoupHelper.doConnect2();
        DMBean data3 = JsoupHelper.doConnect3();
        DMBean data4 = JsoupHelper.doConnect4();
        DMBean data5 = JsoupHelper.doConnect5();
        DMBean data6 = JsoupHelper.doConnect6();
        DMBean data7 = JsoupHelper.doConnect9();
        SMBean data8 = JsoupHelper.doConnect7();
        SMBean data9 = JsoupHelper.doConnect10();
        SMBean data10 = JsoupHelper.doConnect8();
        SMBean data11 = JsoupHelper.doConnect11();

        List<Integer> yls1 = JsoupHelper.doConnect12();
        List<Integer> yls2 = JsoupHelper.doConnect13();

        List<PrintDataBean> allPrint = JsoupHelper.doConnect14(data11.dataItem.get(data11.dataItem.size() - 2).title);
        Log.i("all = " + allPrint.size());


        String path1 = PATH + (Integer.parseInt(data1.get(1).title) + 1) + ".xlsx";
        String path2 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-h.txt";
        String path3 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-u.txt";
        String path4 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + ".txt";
        String path5 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-2h.txt";
        String path6 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-3h.txt";

        String path7 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-2h-out.txt";
        String path8 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-3h-out.txt";
        String path9 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-run19.txt";

        FPAResultBean result = POIExcelUtils.output(path1, path2, path3, path4, path5, path6, data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, yls1, yls2, allPrint);

        //开始跑蓝的数
        FileReadHelper.deleteFile(path7);
        RunnerMain3.run("-p C:\\Users\\aptdev\\Desktop\\za\\D\\L2w.txt" + " -i " + path5 + " -o " + path7);
        for (; ; ) {
            //检测文件是否生成了
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (FileReadHelper.checkFileExists(path7)) {
                break;
            }
        }
        Log.i("2位蓝球文件已生成！");

        FileReadHelper.deleteFile(path8);
        RunnerMain3.run("-p C:\\Users\\aptdev\\Desktop\\za\\D\\L3w.txt" + " -i " + path6 + " -o " + path8);
        for (; ; ) {
            //检测文件是否生成了
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (FileReadHelper.checkFileExists(path8)) {
                break;
            }
        }
        Log.i("3位蓝球文件已生成！");

        FileReadHelper.writeToFile(path9, "-p " + path7 + " -i2 " + path8 + "");

        Main2ResultBean number = Main2.run();
        Log.i("number = " + number.toString());

        List<TwoPoint> redNumber = new ArrayList<>();
        for (int i = 0; i < result.redResult.size(); i++) {
            if (!redNumber.contains(result.redResult.get(i))) {
                redNumber.add(result.redResult.get(i));
            }
        }
        for (int i = 0; i < number.redNumber.size(); i++) {
            if (!redNumber.contains(number.redNumber.get(i))) {
                redNumber.add(number.redNumber.get(i));
            }
        }

        String name = new File(path9).getName().split("\\.")[0];
        String path10 = new File(path9).getParentFile().getAbsolutePath() + File.separator + name + "-" + getDataStr() + ".xls";
        RunnerMain19.run("-m " + path9 + " -mo " + path10, redNumber, result.yellowResult, result.oriResult, number.blueNumber);
        for (; ; ) {
            //检测文件是否生成了
            if (FileReadHelper.checkFileExists(path10)) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("最终蓝球文件已生成！");

        String path11 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-h-run7-5w.txt";
        String path12 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-h-run7-6w.txt";
        String path13 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-h-run7-7w.txt";
        String path14 = PATH + File.separator + (Integer.parseInt(data1.get(1).title) + 1) + "-h-run7-8w.txt";
//        RunnerMain7.run();
        //生成红球文件

    }

    private static String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return sdf.format(new Date());
    }

}
