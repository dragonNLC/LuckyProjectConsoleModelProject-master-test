package com.dragon.lucky.spiderSsq;

import com.dragon.lucky.bean.TwoPoint;
import com.dragon.lucky.fpaSsq.bean.Main2ResultBean;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.dragon.lucky.spiderSsq.bean.DateDataBean;
import com.dragon.lucky.spiderSsq.bean.FrequencyBean;
import com.dragon.lucky.spiderSsq.bean.NumberBean;
import com.dragon.lucky.spiderSsq.bean.SubItemBean;
import com.dragon.lucky.spiderSsq.utils.JsoupHelper;
import com.dragon.lucky.spiderSsq.utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class Main2 {

    public static final String WZ1_CACHE_PATH = "C:\\Users\\aptdev\\Desktop\\za\\20231030-2\\wz1.txt";
    public static final String WZ2_CACHE_PATH = "C:\\Users\\aptdev\\Desktop\\za\\20231030-2\\wz2.txt";
    public static String PRINT_CACHE_PATH = "C:\\Users\\aptdev\\Desktop\\za\\20231030-2\\结果.xlsx";
    public static final String STATISTICS_NAME = "统计";

    public static Main2ResultBean run() {
        List<DateDataBean> wz1Data = JsoupHelper.doConnect();
        List<DateDataBean> wz2Data = JsoupHelper.doConnect2();

        wz1Data.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getTitle()), Integer.parseInt(o1.getTitle())));
        DateDataBean currentDDB1 = wz1Data.get(0);
        wz2Data.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getTitle()), Integer.parseInt(o1.getTitle())));
        DateDataBean currentDDB2 = wz2Data.get(0);

        for (DateDataBean item :
                wz1Data) {
            for (int j = 0; j < item.subItemNumbers.size(); j++) {
                SubItemBean sib = item.subItemNumbers.get(j);
                for (int k = 0; k < sib.subNumbers.size(); k++) {
                    if (!item.collect.containsKey(Integer.parseInt(sib.subNumbers.get(k)))) {
                        item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), 0);
                    }
                    item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), item.collect.get(Integer.parseInt(sib.subNumbers.get(k))) + 1);
                }
            }
            for (int i = 1; i < 17; i++) {
                item.collectList.add(new DateDataBean.CollectionBean(i, item.collect.getOrDefault(i, 0)));
            }
            item.collectList.sort(new Comparator<DateDataBean.CollectionBean>() {
                @Override
                public int compare(DateDataBean.CollectionBean o1, DateDataBean.CollectionBean o2) {
                    return Integer.compare(o2.count, o1.count);
                }
            });
        }

        for (DateDataBean item :
                wz2Data) {
            for (int j = 0; j < item.subItemNumbers.size(); j++) {
                SubItemBean sib = item.subItemNumbers.get(j);
                for (int k = 0; k < sib.subNumbers.size(); k++) {
                    if (!item.collect.containsKey(Integer.parseInt(sib.subNumbers.get(k)))) {
                        item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), 0);
                    }
                    item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), item.collect.get(Integer.parseInt(sib.subNumbers.get(k))) + 1);
                }
            }
            for (int i = 1; i < 17; i++) {
                item.collectList.add(new DateDataBean.CollectionBean(i, item.collect.getOrDefault(i, 0)));
            }
            item.collectList.sort(new Comparator<DateDataBean.CollectionBean>() {
                @Override
                public int compare(DateDataBean.CollectionBean o1, DateDataBean.CollectionBean o2) {
                    return Integer.compare(o2.count, o1.count);
                }
            });
        }
        Main2ResultBean number = new Main2ResultBean();

        try {
            //////////////////////第二页，统计数据///////////////////////////////
            Map<Integer, NumberBean> wz1DataCollects = new HashMap<>();
            for (int i = 0; i < wz1Data.size(); i++) {
                DateDataBean ddb = wz1Data.get(i);
                for (int j = 1; j < 17; j++) {
                    if (!ddb.collect.containsKey(j)) {
                        ddb.collect.put(j, 0);
                    }
                    int value = ddb.collect.get(j);
                    if (!wz1DataCollects.containsKey(j)) {
                        wz1DataCollects.put(j, new NumberBean(j));
                    }
                    NumberBean nb = wz1DataCollects.get(j);
                    if (!nb.frequencies.containsKey(value)) {
                        nb.frequencies.put(value, new FrequencyBean(value));
                    }
                    FrequencyBean fb = nb.frequencies.get(value);
                    fb.setCount(fb.getCount() + 1);

                    boolean isTarget = false;
                    if (ddb.printNumbers != null && ddb.printNumbers.size() > 0) {
                        for (int k = 0; k < ddb.printNumbers.size(); k++) {
                            if (j == Integer.parseInt(ddb.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }
                    if (isTarget) {
                        fb.setZ(fb.getZ() + 1);
                    }
                }
            }

            Map<Integer, NumberBean> wz2DataCollects = new HashMap<>();
            for (int i = 0; i < wz2Data.size(); i++) {
                DateDataBean ddb = wz2Data.get(i);
                for (int j = 1; j < 17; j++) {
                    if (!ddb.collect.containsKey(j)) {
                        ddb.collect.put(j, 0);
                    }
                    int value = ddb.collect.get(j);
                    if (!wz2DataCollects.containsKey(j)) {
                        wz2DataCollects.put(j, new NumberBean(j));
                    }
                    NumberBean nb = wz2DataCollects.get(j);
                    if (!nb.frequencies.containsKey(value)) {
                        nb.frequencies.put(value, new FrequencyBean(value));
                    }
                    FrequencyBean fb = nb.frequencies.get(value);
                    fb.setCount(fb.getCount() + 1);

                    boolean isTarget = false;
                    if (ddb.printNumbers != null && ddb.printNumbers.size() > 0) {
                        for (int k = 0; k < ddb.printNumbers.size(); k++) {
                            if (j == Integer.parseInt(ddb.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }
                    if (isTarget) {
                        fb.setZ(fb.getZ() + 1);
                    }
                }
            }

            Map<Integer, NumberBean> wz12DataCollects = new HashMap<>();
            for (int i = 0; i < wz1Data.size(); i++) {
                DateDataBean ddb = wz1Data.get(i);
                DateDataBean ddb2 = wz2Data.get(i);
                for (int j = 1; j < 17; j++) {
//                    Log.i("j == " + j);
                    if (!ddb.collect.containsKey(j)) {
                        ddb.collect.put(j, 0);
                    }
                    if (!ddb2.collect.containsKey(j)) {
                        ddb2.collect.put(j, 0);
                    }
                    int value = ddb.collect.get(j) + ddb2.collect.get(j);
                    if (!wz12DataCollects.containsKey(j)) {
                        wz12DataCollects.put(j, new NumberBean(j));
                    }
                    NumberBean nb = wz12DataCollects.get(j);
                    if (!nb.frequencies.containsKey(value)) {
                        nb.frequencies.put(value, new FrequencyBean(value));
                    }
                    FrequencyBean fb = nb.frequencies.get(value);
                    fb.setCount(fb.getCount() + 1);

                    boolean isTarget = false;
                    if (ddb.printNumbers != null && ddb.printNumbers.size() > 0) {
                        for (int k = 0; k < ddb.printNumbers.size(); k++) {
                            if (j == Integer.parseInt(ddb.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }
                    if (isTarget) {
                        fb.setZ(fb.getZ() + 1);
                    }
                }
            }
            int maxNumber = 0;
            for (int i = 1; i < 17; i++) {
                NumberBean nb = wz1DataCollects.get(i);
                for (int key = 0; key < 11; key++) {
                    boolean isGreen = false;
                    if (currentDDB1.collect.containsKey(i) && currentDDB1.collect.get(i).equals(key)) {
                        isGreen = true;
                    }

                    if (nb.getFrequencies().containsKey(key)) {
                        FrequencyBean fb = nb.getFrequencies().get(key);
                        if (isGreen) {
                            if (fb.getZ() == maxNumber) {
                                number.redNumber.add(new TwoPoint(i, 0));
                            } else if (fb.getZ() > maxNumber) {
                                number.redNumber.clear();
                                number.redNumber.add(new TwoPoint(i, 0));
                            }
                            maxNumber = Math.max(maxNumber, fb.getZ());
                            if (fb.getZ() == 0) {
                                number.blueNumber.add(new TwoPoint(i, 0));
                            }
                        }
                    }
                }
            }

            for (int i = 1; i < 17; i++) {
                NumberBean nb = wz2DataCollects.get(i);
                for (int key = 0; key < 11; key++) {
                    boolean isGreen = false;
                    if (currentDDB2.collect.containsKey(i) && currentDDB2.collect.get(i).equals(key)) {
                        isGreen = true;
                    }

                    if (nb.getFrequencies().containsKey(key)) {
                        FrequencyBean fb = nb.getFrequencies().get(key);
                        if (isGreen) {
                            if (fb.getZ() == maxNumber) {
                                number.redNumber.add(new TwoPoint(i, 0));
                            } else if (fb.getZ() > maxNumber) {
                                number.redNumber.clear();
                                number.redNumber.add(new TwoPoint(i, 0));
                            }
                            maxNumber = Math.max(maxNumber, fb.getZ());
                            if (fb.getZ() == 0) {
                                number.blueNumber.add(new TwoPoint(i, 0));
                            }
                        }
                    }
                }
            }

            for (int i = 1; i < 17; i++) {
                NumberBean nb = wz12DataCollects.get(i);
                for (int key = 0; key < 11; key++) {
                    boolean isGreen = false;
                    int count = 0;
                    if (currentDDB1.collect.containsKey(i)/* && currentDDB1.collect.get(i).equals(key)*/) {
                        count = currentDDB1.collect.get(i);
                    }

                    if (currentDDB2.collect.containsKey(i)/* && currentDDB1.collect.get(i).equals(key)*/) {
                        count += currentDDB2.collect.get(i);
                    }

                    if (count == key) {
                        isGreen = true;
                    }
                    if (nb.getFrequencies().containsKey(key)) {
                        FrequencyBean fb = nb.getFrequencies().get(key);
                        if (isGreen) {
                            if (fb.getZ() == maxNumber) {
                                number.redNumber.add(new TwoPoint(i, 0));
                            } else if (fb.getZ() > maxNumber) {
                                number.redNumber.clear();
                                number.redNumber.add(new TwoPoint(i, 0));
                            }
                            maxNumber = Math.max(maxNumber, fb.getZ());
                            if (fb.getZ() == 0) {
                                number.blueNumber.add(new TwoPoint(i, 0));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return number;
    }


    public static void main(String[] args) {
        List<DateDataBean> wz1Data = JsoupHelper.doConnect();
        List<DateDataBean> wz2Data = JsoupHelper.doConnect2();

        ////////////////////////拿到两个网站的数据，开始保存到本地////////////////////////

        wz1Data.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getTitle()), Integer.parseInt(o1.getTitle())));
        DateDataBean currentDDB1 = wz1Data.get(0);
        wz2Data.sort((o1, o2) -> Integer.compare(Integer.parseInt(o2.getTitle()), Integer.parseInt(o1.getTitle())));
        DateDataBean currentDDB2 = wz2Data.get(0);
        ////////////////////////拿到两个网站的数据，开始保存到本地////////////////////////

        File out = new File(PRINT_CACHE_PATH);
        PRINT_CACHE_PATH = out.getParentFile().getAbsolutePath() + File.separator + currentDDB1.getTitle() + "-" + out.getName();

        ////////////////////////统计两个网站的数据////////////////////////
        for (DateDataBean item :
                wz1Data) {
            for (int j = 0; j < item.subItemNumbers.size(); j++) {
                SubItemBean sib = item.subItemNumbers.get(j);
                for (int k = 0; k < sib.subNumbers.size(); k++) {
                    if (!item.collect.containsKey(Integer.parseInt(sib.subNumbers.get(k)))) {
                        item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), 0);
                    }
                    item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), item.collect.get(Integer.parseInt(sib.subNumbers.get(k))) + 1);
                }
            }
            for (int i = 1; i < 17; i++) {
                item.collectList.add(new DateDataBean.CollectionBean(i, item.collect.getOrDefault(i, 0)));
            }
            item.collectList.sort(new Comparator<DateDataBean.CollectionBean>() {
                @Override
                public int compare(DateDataBean.CollectionBean o1, DateDataBean.CollectionBean o2) {
                    return Integer.compare(o2.count, o1.count);
                }
            });
        }

        for (DateDataBean item :
                wz2Data) {
            for (int j = 0; j < item.subItemNumbers.size(); j++) {
                SubItemBean sib = item.subItemNumbers.get(j);
                for (int k = 0; k < sib.subNumbers.size(); k++) {
                    if (!item.collect.containsKey(Integer.parseInt(sib.subNumbers.get(k)))) {
                        item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), 0);
                    }
                    item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), item.collect.get(Integer.parseInt(sib.subNumbers.get(k))) + 1);
                }
            }
            for (int i = 1; i < 17; i++) {
                item.collectList.add(new DateDataBean.CollectionBean(i, item.collect.getOrDefault(i, 0)));
            }
            item.collectList.sort(new Comparator<DateDataBean.CollectionBean>() {
                @Override
                public int compare(DateDataBean.CollectionBean o1, DateDataBean.CollectionBean o2) {
                    return Integer.compare(o2.count, o1.count);
                }
            });
        }
        ////////////////////////统计两个网站的数据////////////////////////


        //输出到excel表上
        XSSFWorkbook workbook = new XSSFWorkbook();

        FileOutputStream fos = null;
        CellStyle cellStyle = createCellStyle(workbook);
        CellStyle cellStyleRed = createRedBackgroundCellStyle(workbook);
        CellStyle cellStyleGreen = createGreenBackgroundCellStyle(workbook);
        CellStyle cellStyleGrey = createGrayBackgroundCellStyle(workbook);
        CellStyle cellStyleYellow = createYellowBackgroundCellStyle(workbook);
        CellStyle cellStyleBlue = createBlueBackgroundCellStyle(workbook);
        CellStyle cellGreenBackgroundStyle = createGreenBackgroundCellStyle(workbook);
        try {
            File file = new File(PRINT_CACHE_PATH);
            if (file.exists()) {
                file.delete();
                Log.i("输出路径 = " + PRINT_CACHE_PATH);
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            XSSFSheet sheet = workbook.createSheet("汇总");
            sheet.setDefaultColumnWidth(2);

            Map<Integer, Row> rowMap = new HashMap<>();
            int columnIdx = 0;
            //////////////////////输出第一个网站的统计数据///////////////////////////////
            for (int i = 0; i < wz1Data.size(); i++) {
//                Map<Integer, Integer> collect1 = new HashMap<>();
//                collect1Map.put(i, collect1);
                int line = 0;
                Row rowX = null;
                if (rowMap.get(line) == null) {
                    rowX = sheet.createRow(line);
                    rowMap.put(line, rowX);
                }
                rowX = rowMap.get(line);

                DateDataBean item = wz1Data.get(i);
                /*for (int j = 0; j < item.subItemNumbers.size(); j++) {
                    SubItemBean sib = item.subItemNumbers.get(j);
                    for (int k = 0; k < sib.subNumbers.size(); k++) {
                        if (!collect1.containsKey(Integer.parseInt(sib.subNumbers.get(k)))) {
                            collect1.put(Integer.parseInt(sib.subNumbers.get(k)), 0);
                        }
                        collect1.put(Integer.parseInt(sib.subNumbers.get(k)), collect1.get(Integer.parseInt(sib.subNumbers.get(k))) + 1);
                    }
                }*/
                createStringCell(rowX, columnIdx, cellStyle, item.getTitle());

                for (int j = 0; j < item.collectList.size(); j++) {
                    line++;
                    if (rowMap.get(line) == null) {
                        rowX = sheet.createRow(line);
                        rowMap.put(line, rowX);
                    }
                    rowX = rowMap.get(line);

                    boolean isTarget = false;
                    if (item.printNumbers != null && item.printNumbers.size() > 0) {
                        for (int k = 0; k < item.printNumbers.size(); k++) {
                            if (item.collectList.get(j).getId() == Integer.parseInt(item.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }

                    String value = String.valueOf(item.collectList.get(j).getId());
                    if (isTarget) {
                        createStringCell(rowX, columnIdx, cellGreenBackgroundStyle, value);
                    } else {
                        createStringCell(rowX, columnIdx, cellStyle, value);
                    }
                    columnIdx++;

                    value = "0";
                    if (item.collect.containsKey((Integer) item.collectList.get(j).getId())) {
                        value = String.valueOf(item.collect.get((Integer) item.collectList.get(j).getId()));
                    }

                    if (isTarget) {
                        createStringCell(rowX, columnIdx, cellGreenBackgroundStyle, value);
                    } else {
                        createStringCell(rowX, columnIdx, cellStyle, value);
                    }
                    if (j != 15) {
                        columnIdx--;
                    }
                }
                columnIdx++;
                columnIdx++;
            }
            //////////////////////输出第一个网站的统计数据///////////////////////////////
            columnIdx = 0;
            //////////////////////输出第二个网站的统计数据///////////////////////////////
            for (int i = 0; i < wz2Data.size(); i++) {
                int line = 18;
                Row rowX = null;
                if (rowMap.get(line) == null) {
                    rowX = sheet.createRow(line);
                    rowMap.put(line, rowX);
                }
                rowX = rowMap.get(line);

                DateDataBean item = wz2Data.get(i);

                /*for (int j = 0; j < item.subItemNumbers.size(); j++) {
                    SubItemBean sib = item.subItemNumbers.get(j);
                    for (int k = 0; k < sib.subNumbers.size(); k++) {
                        if (!item.collect.containsKey(Integer.parseInt(sib.subNumbers.get(k)))) {
                            item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), 0);
                        }
                        item.collect.put(Integer.parseInt(sib.subNumbers.get(k)), item.collect.get(Integer.parseInt(sib.subNumbers.get(k))) + 1);
                    }
                }*/

                createStringCell(rowX, columnIdx, cellStyle, item.getTitle());

                for (int j = 0; j < item.collectList.size(); j++) {
                    line++;
                    if (rowMap.get(line) == null) {
                        rowX = sheet.createRow(line);
                        rowMap.put(line, rowX);
                    }
                    rowX = rowMap.get(line);

                    boolean isTarget = false;
                    if (item.printNumbers != null && item.printNumbers.size() > 0) {
                        for (int k = 0; k < item.printNumbers.size(); k++) {
                            if (item.collectList.get(j).getId() == Integer.parseInt(item.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }
                    String value = String.valueOf(item.collectList.get(j).getId());
                    if (isTarget) {
                        createStringCell(rowX, columnIdx, cellGreenBackgroundStyle, value);
                    } else {
                        createStringCell(rowX, columnIdx, cellStyle, value);
                    }
                    columnIdx++;

                    value = "0";
                    if (item.collect.containsKey((Integer) item.collectList.get(j).getId())) {
                        value = String.valueOf(item.collect.get((Integer) item.collectList.get(j).getId()));
                    }
                    if (isTarget) {
                        createStringCell(rowX, columnIdx, cellGreenBackgroundStyle, value);
                    } else {
                        createStringCell(rowX, columnIdx, cellStyle, value);
                    }
                    if (j != 15) {
                        columnIdx--;
                    }
                }
                columnIdx++;
                columnIdx++;
            }
            //////////////////////输出第二个网站的统计数据///////////////////////////////

            Map<Integer, List<DateDataBean.CollectionBean>> map = new HashMap<>();

            columnIdx = 0;
            //////////////////////两个网站的统计数据///////////////////////////////
            int size = Math.max(wz1Data.size(), wz2Data.size());

            for (int i = 0; i < size; i++) {
                map.put(i, new ArrayList<>());

                for (int j = 1; j < 17; j++) {
                    int v1 = 0;
                    int v2 = 0;

                    if (wz2Data.get(i).collect.containsKey((Integer) j)) {
                        v2 = wz2Data.get(i).collect.get((Integer) j);
                    }
                    if (wz1Data.get(i).collect.containsKey((Integer) j)) {
                        v1 = wz1Data.get(i).collect.get((Integer) j);
                    }
                    map.get(i).add(new DateDataBean.CollectionBean(j, v1 + v2));
                }
                map.get(i).sort(new Comparator<DateDataBean.CollectionBean>() {
                    @Override
                    public int compare(DateDataBean.CollectionBean o1, DateDataBean.CollectionBean o2) {
                        return Integer.compare(o2.getCount(), o1.getCount());
                    }
                });
            }

//            Log.i("wz1Data.size() = " + wz1Data.size());
//            Log.i("wz2Data.size() = " + wz2Data.size());
            for (int i = 0; i < size; i++) {
                int line = 36;
                Row rowX = null;
                List<DateDataBean.CollectionBean> mergeCollection = map.get(i);
                for (int j = 0; j < mergeCollection.size(); j++) {
                    if (rowMap.get(line) == null) {
                        rowX = sheet.createRow(line);
                        rowMap.put(line, rowX);
                    }
                    rowX = rowMap.get(line);

                    boolean isTarget = false;
                    if (wz1Data.get(i).printNumbers != null && wz1Data.get(i).printNumbers.size() > 0) {
                        for (int k = 0; k < wz1Data.get(i).printNumbers.size(); k++) {
                            if (mergeCollection.get(j).getId() == Integer.parseInt(wz1Data.get(i).printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }

                    String value = String.valueOf(mergeCollection.get(j).getId());
                    if (isTarget) {
                        createStringCell(rowX, columnIdx, cellGreenBackgroundStyle, value);
                    } else {
                        createStringCell(rowX, columnIdx, cellStyle, value);
                    }
                    columnIdx++;

                    value = mergeCollection.get(j).getCount() + "";

                    /*int v1 = 0;
                    int v2 = 0;

                    if (wz2Data.get(i).collect.containsKey((Integer) j)) {
                        v2 = wz2Data.get(i).collect.get((Integer) j);
//                        Log.i("v2 = " + v2);
                    }
                    if (wz1Data.get(i).collect.containsKey((Integer) j)) {
                        v1 = wz1Data.get(i).collect.get((Integer) j);
//                        Log.i("v1 = " + v1);
                    }
                    value = String.valueOf((v1 + v2));*/

                    if (isTarget) {
                        createStringCell(rowX, columnIdx, cellGreenBackgroundStyle, value);
                    } else {
                        createStringCell(rowX, columnIdx, cellStyle, value);
                    }
                    if (j != 15) {
                        columnIdx--;
                    }
                    line++;
                }
                columnIdx++;
                columnIdx++;
            }
            //////////////////////两个网站的统计数据///////////////////////////////


            //////////////////////第二页，统计数据///////////////////////////////
            Map<Integer, NumberBean> wz1DataCollects = new HashMap<>();
            for (int i = 0; i < wz1Data.size(); i++) {
                DateDataBean ddb = wz1Data.get(i);
                for (int j = 1; j < 17; j++) {
//                    Log.i("j == " + j);
                    if (!ddb.collect.containsKey(j)) {
                        ddb.collect.put(j, 0);
                    }
                    int value = ddb.collect.get(j);
                    if (!wz1DataCollects.containsKey(j)) {
                        wz1DataCollects.put(j, new NumberBean(j));
                    }
                    NumberBean nb = wz1DataCollects.get(j);
                    if (!nb.frequencies.containsKey(value)) {
                        nb.frequencies.put(value, new FrequencyBean(value));
                    }
                    FrequencyBean fb = nb.frequencies.get(value);
                    fb.setCount(fb.getCount() + 1);

                    boolean isTarget = false;
                    if (ddb.printNumbers != null && ddb.printNumbers.size() > 0) {
                        for (int k = 0; k < ddb.printNumbers.size(); k++) {
                            if (j == Integer.parseInt(ddb.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }
                    if (isTarget) {
                        fb.setZ(fb.getZ() + 1);
                    }
                }
            }
            /*int maxCount = 0;
            for (int id :
                    wz1DataCollects.keySet()) {
                Map<Integer, FrequencyBean> fqs = wz1DataCollects.get(id).frequencies;

            }*/

            Map<Integer, NumberBean> wz2DataCollects = new HashMap<>();
            for (int i = 0; i < wz2Data.size(); i++) {
                DateDataBean ddb = wz2Data.get(i);
                for (int j = 1; j < 17; j++) {
//                    Log.i("j == " + j);
                    if (!ddb.collect.containsKey(j)) {
                        ddb.collect.put(j, 0);
                    }
                    int value = ddb.collect.get(j);
                    if (!wz2DataCollects.containsKey(j)) {
                        wz2DataCollects.put(j, new NumberBean(j));
                    }
                    NumberBean nb = wz2DataCollects.get(j);
                    if (!nb.frequencies.containsKey(value)) {
                        nb.frequencies.put(value, new FrequencyBean(value));
                    }
                    FrequencyBean fb = nb.frequencies.get(value);
                    fb.setCount(fb.getCount() + 1);

                    boolean isTarget = false;
                    if (ddb.printNumbers != null && ddb.printNumbers.size() > 0) {
                        for (int k = 0; k < ddb.printNumbers.size(); k++) {
                            if (j == Integer.parseInt(ddb.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }
                    if (isTarget) {
                        fb.setZ(fb.getZ() + 1);
                    }
                }
            }

            Map<Integer, NumberBean> wz12DataCollects = new HashMap<>();
            for (int i = 0; i < wz1Data.size(); i++) {
                DateDataBean ddb = wz1Data.get(i);
                DateDataBean ddb2 = wz2Data.get(i);
                for (int j = 1; j < 17; j++) {
//                    Log.i("j == " + j);
                    if (!ddb.collect.containsKey(j)) {
                        ddb.collect.put(j, 0);
                    }
                    if (!ddb2.collect.containsKey(j)) {
                        ddb2.collect.put(j, 0);
                    }
                    int value = ddb.collect.get(j) + ddb2.collect.get(j);
                    if (!wz12DataCollects.containsKey(j)) {
                        wz12DataCollects.put(j, new NumberBean(j));
                    }
                    NumberBean nb = wz12DataCollects.get(j);
                    if (!nb.frequencies.containsKey(value)) {
                        nb.frequencies.put(value, new FrequencyBean(value));
                    }
                    FrequencyBean fb = nb.frequencies.get(value);
                    fb.setCount(fb.getCount() + 1);

                    boolean isTarget = false;
                    if (ddb.printNumbers != null && ddb.printNumbers.size() > 0) {
                        for (int k = 0; k < ddb.printNumbers.size(); k++) {
                            if (j == Integer.parseInt(ddb.printNumbers.get(k))) {
                                isTarget = true;
                                break;
                            }
                        }
                    }
                    if (isTarget) {
                        fb.setZ(fb.getZ() + 1);
                    }
                }
            }
            /*for (int i = 0; i < wz2Data.size(); i++) {
                DateDataBean ddb = wz2Data.get(i);
                for (int j = 1; j < 13; j++) {
//                    Log.i("j == " + j);
                    if (!ddb.collect.containsKey(j)) {
                        ddb.collect.put(j, 0);
                    }
                    int value = ddb.collect.get(j);
                    if (!wz12DataCollects.containsKey(j)) {
                        wz12DataCollects.put(j, new NumberBean(j));
                    }
                    NumberBean nb = wz12DataCollects.get(j);
                    if (!nb.frequencies.containsKey(value)) {
                        nb.frequencies.put(value, new FrequencyBean(value));
                    }
                    FrequencyBean fb = nb.frequencies.get(value);
                    fb.setCount(fb.getCount() + 1);

                    boolean isTarget = false;
                    for (int k = 0; k < ddb.printNumbers.size(); k++) {
                        if (j == Integer.parseInt(ddb.printNumbers.get(k))) {
                            isTarget = true;
                            break;
                        }
                    }
                    if (isTarget) {
                        fb.setZ(fb.getZ() + 1);
                    }
                }
            }*/

            XSSFSheet sheet2 = workbook.createSheet("统计");
            sheet2.setDefaultColumnWidth(3);
            Map<Integer, Row> rowMap2 = new HashMap<>();
            columnIdx = 0;
            for (int i = 1; i < 17; i++) {
                int line = 0;
                Row rowX = null;

                if (rowMap2.get(line) == null) {
                    rowX = sheet2.createRow(line);
                    rowMap2.put(line, rowX);
                }
                rowX = rowMap2.get(line);

                createStringCell(rowX, columnIdx, cellStyle, String.valueOf(i));
                columnIdx++;

                NumberBean nb = wz1DataCollects.get(i);
                for (int key = 0; key < 11; key++) {
                    /*if (key == 0) {
                        continue;
                    }*/
                    int tempColumn = columnIdx;
                    if (rowMap2.get(line) == null) {
                        rowX = sheet2.createRow(line);
                        rowMap2.put(line, rowX);
                    }
                    rowX = rowMap2.get(line);

                    boolean isGreen = false;
                    if (currentDDB1.collect.containsKey(i) && currentDDB1.collect.get(i).equals(key)) {
                        isGreen = true;
                    }

                    if (nb.getFrequencies().containsKey(key)) {
                        FrequencyBean fb = nb.getFrequencies().get(key);
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(key));
                        } else {
                            createStringCell(rowX, tempColumn, cellStyleGrey, String.valueOf(key));
                        }

                        tempColumn++;
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(fb.getCount()));
                        } else {
                            createStringCell(rowX, tempColumn, cellStyleYellow, String.valueOf(fb.getCount()));
                        }

                        tempColumn++;
                        if (fb.getZ() == 0) {
                            createStringCell(rowX, tempColumn, cellStyleRed, String.valueOf(fb.getZ()));
                        } else {
                            if (isGreen) {
                                createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(fb.getZ()));
                            } else {
                                createStringCell(rowX, tempColumn, cellStyleBlue, String.valueOf(fb.getZ()));
                            }
                        }

                        float percent = fb.getZ() / ((float) fb.getCount()) * 100;
                        tempColumn++;
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.format("%.1f", percent) + "%");
                        } else {
                            createStringCell(rowX, tempColumn, cellStyle, String.format("%.1f", percent) + "%");
                        }
                        line++;
                    } else {
                        createStringCell(rowX, tempColumn, cellStyleGrey, String.valueOf(key));
                        line++;
                    }
                }

                columnIdx += 4;
            }

            columnIdx = 0;
            for (int i = 1; i < 17; i++) {
                int line = 12;
                Row rowX = null;

                if (rowMap2.get(line) == null) {
                    rowX = sheet2.createRow(line);
                    rowMap2.put(line, rowX);
                }
                rowX = rowMap2.get(line);

                createStringCell(rowX, columnIdx, cellStyle, String.valueOf(i));
                columnIdx++;

                NumberBean nb = wz2DataCollects.get(i);
                for (int key = 0; key < 11; key++) {
                    /*if (key == 0) {
                        continue;
                    }*/
                    int tempColumn = columnIdx;
                    if (rowMap2.get(line) == null) {
                        rowX = sheet2.createRow(line);
                        rowMap2.put(line, rowX);
                    }
                    rowX = rowMap2.get(line);

                    boolean isGreen = false;
                    if (currentDDB2.collect.containsKey(i) && currentDDB2.collect.get(i).equals(key)) {
                        isGreen = true;
                    }

                    if (nb.getFrequencies().containsKey(key)) {
                        FrequencyBean fb = nb.getFrequencies().get(key);
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(key));
                        } else {
                            createStringCell(rowX, tempColumn, cellStyleGrey, String.valueOf(key));
                        }

                        tempColumn++;
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(fb.getCount()));
                        } else {
                            createStringCell(rowX, tempColumn, cellStyleYellow, String.valueOf(fb.getCount()));
                        }

                        tempColumn++;
                        if (fb.getZ() == 0) {
                            createStringCell(rowX, tempColumn, cellStyleRed, String.valueOf(fb.getZ()));
                        } else {
                            if (isGreen) {
                                createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(fb.getZ()));
                            } else {
                                createStringCell(rowX, tempColumn, cellStyleBlue, String.valueOf(fb.getZ()));
                            }
                        }

                        float percent = fb.getZ() / ((float) fb.getCount()) * 100;
                        tempColumn++;
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.format("%.1f", percent) + "%");
                        } else {
                            createStringCell(rowX, tempColumn, cellStyle, String.format("%.1f", percent) + "%");
                        }

                        line++;
                    } else {
                        createStringCell(rowX, tempColumn, cellStyleGrey, String.valueOf(key));
                        line++;
                    }
                }

                columnIdx += 4;
            }

            columnIdx = 0;
            for (int i = 1; i < 17; i++) {
                int line = 24;
                Row rowX = null;

                if (rowMap2.get(line) == null) {
                    rowX = sheet2.createRow(line);
                    rowMap2.put(line, rowX);
                }
                rowX = rowMap2.get(line);

                createStringCell(rowX, columnIdx, cellStyle, String.valueOf(i));
                columnIdx++;

                NumberBean nb = wz12DataCollects.get(i);
                for (int key = 0; key < 11; key++) {
                    /*if (key == 0) {
                        continue;
                    }*/
                    int tempColumn = columnIdx;
                    if (rowMap2.get(line) == null) {
                        rowX = sheet2.createRow(line);
                        rowMap2.put(line, rowX);
                    }
                    rowX = rowMap2.get(line);

                    boolean isGreen = false;

                    int count = 0;
                    if (currentDDB1.collect.containsKey(i)/* && currentDDB1.collect.get(i).equals(key)*/) {
                        count = currentDDB1.collect.get(i);
                    }

                    if (currentDDB2.collect.containsKey(i)/* && currentDDB1.collect.get(i).equals(key)*/) {
                        count += currentDDB2.collect.get(i);
                    }

                    if (count == key) {
                        isGreen = true;
                    }

                    if (nb.getFrequencies().containsKey(key)) {
                        FrequencyBean fb = nb.getFrequencies().get(key);
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(key));
                        } else {
                            createStringCell(rowX, tempColumn, cellStyleGrey, String.valueOf(key));
                        }

                        tempColumn++;
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(fb.getCount()));
                        } else {
                            createStringCell(rowX, tempColumn, cellStyleYellow, String.valueOf(fb.getCount()));
                        }

                        tempColumn++;
                        if (fb.getZ() == 0) {
                            createStringCell(rowX, tempColumn, cellStyleRed, String.valueOf(fb.getZ()));
                        } else {
                            if (isGreen) {
                                createStringCell(rowX, tempColumn, cellStyleGreen, String.valueOf(fb.getZ()));
                            } else {
                                createStringCell(rowX, tempColumn, cellStyleBlue, String.valueOf(fb.getZ()));
                            }
                        }

                        float percent = fb.getZ() / ((float) fb.getCount()) * 100;
                        tempColumn++;
                        if (isGreen) {
                            createStringCell(rowX, tempColumn, cellStyleGreen, String.format("%.1f", percent) + "%");
                        } else {
                            createStringCell(rowX, tempColumn, cellStyle, String.format("%.1f", percent) + "%");
                        }

                        line++;
                    } else {
                        createStringCell(rowX, tempColumn, cellStyleGrey, String.valueOf(key));
                        line++;
                    }
                }

                columnIdx += 4;
            }

            //////////////////////第二页，统计数据///////////////////////////////
            Log.i("输出完毕！");

            fos = new FileOutputStream(file);
            workbook.write(fos);//完成写入
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createStringCell(Row row, int cellIndex, CellStyle cellStyle, String cellValue) {
        Cell cell = row.createCell(cellIndex, CellType.STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(cellValue);
    }

    private static CellStyle createCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        return style;
    }

    private static CellStyle createGreenBackgroundCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);
        return style;
    }

    private static CellStyle createRedBackgroundCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.RED.index);
        return style;
    }

    private static CellStyle createBlueBackgroundCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.index);
        return style;
    }

    private static CellStyle createGrayBackgroundCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        return style;
    }

    private static CellStyle createYellowBackgroundCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
        return style;
    }

    private static void setCommonCellStyle(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(false);
    }

}
