package com.dragon.lucky.fpa.utils;

import com.dragon.lucky.bean.TwoPoint;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.dragon.lucky.fpa.bean.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class POIExcelUtils {

    public static FPAResultBean output(String path, String path2, String path3, String path4, String path5, String path6, List<PreviewDataBean> data1, DMBean data2, DMBean data3, DMBean data4, DMBean data5, DMBean data6, DMBean data7, SMBean data8, SMBean data9, SMBean data10, SMBean data11, List<Integer> yls1, List<Integer> yls2, List<PrintDataBean> allPrint) {
        //先把格式列出来

        FPAResultBean result = new FPAResultBean();

        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                Log.i("输出路径 = " + path);
                file.createNewFile();
            }
            String name = (Integer.parseInt(data1.get(1).title) + 1) + "";
            XSSFSheet sheet = workbook.createSheet(name);
            sheet.setDefaultColumnWidth(3);
            CellStyle cellStyle = createCellStyle(workbook);
            CellStyle cellGoldStyle = createGoldCellStyle(workbook);
            CellStyle cellGreenStyle = createGreenCellStyle(workbook);
            CellStyle cellPinkStyle = createPinkCellStyle(workbook);
            CellStyle cellYellowStyle = createYellowCellStyle(workbook);
            CellStyle cellPink2Style = createPink2CellStyle(workbook);
            int columnIdx = 0;

            Row rowX = sheet.createRow(0);
            createStringCell(rowX, columnIdx, cellStyle, name);

            rowX = sheet.createRow(1);
            for (int i = 1; i < 36; i++) {
                createStringCell(rowX, columnIdx + i - 1, cellStyle, i + "");
            }
            columnIdx = 36 + 1;

            for (int i = 1; i < 13; i++) {
                createStringCell(rowX, columnIdx + i - 1, cellStyle, i + "");
            }
            columnIdx = 0;

            ////////////////////////////////////////////////222222///////////////////////////////////////////////////////
            rowX = sheet.createRow(2);
            for (int i = 1; i < 36; i++) {
                createStringCell(rowX, columnIdx + i - 1, cellGoldStyle, yls1.get(i - 1) + "");
            }
            columnIdx = 36 + 1;

            for (int i = 1; i < 13; i++) {
                createStringCell(rowX, columnIdx + i - 1, cellGoldStyle, yls2.get(i - 1) + "");
            }
            columnIdx = 0;
            ////////////////////////////////////////////////222222///////////////////////////////////////////////////////

            ////////////////////////////////////////////////333333///////////////////////////////////////////////////////
            List<Integer> maxIdx = new ArrayList<>();
            int maxData = 0;
            List<Integer> checkIdx = new ArrayList<>();//最近一期错误的
            List<Integer> check100Idx = new ArrayList<>();//第100期错误的

            for (int j = 0; j < data1.get(data1.size() - 1).getPreviewData().size(); j++) {
                PreviewDataBean.PreviewSingleDataBean psd = data1.get(data1.size() - 1).getPreviewData().get(j);
                if (!psd.isTrue) {
                    check100Idx.add(data1.get(0).getPreviewData().get(j).data);
                }
            }

            for (int i = 1; i < data1.size(); i++) {
                boolean isCheck = false;
                for (int j = 0; j < data1.get(i).getPreviewData().size(); j++) {
                    PreviewDataBean.PreviewSingleDataBean psd = data1.get(i).getPreviewData().get(j);
                    if (!psd.isTrue) {
                        isCheck = true;
                        checkIdx.add(data1.get(0).getPreviewData().get(j).data);
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 5; i++) {
                int count = 0;
                for (int j = 1; j < data1.size(); j++) {
                    PreviewDataBean itemBean = data1.get(j);
                    if (itemBean.getPreviewData().get(i).isTrue) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count > maxData) {
                    maxData = count;
                    maxIdx.clear();
                    maxIdx.add(data1.get(0).getPreviewData().get(i).data);
                } else if (count == maxData) {
                    maxIdx.add(data1.get(0).getPreviewData().get(i).data);
                }
            }

            data1.get(0).previewData.sort(new Comparator<PreviewDataBean.PreviewSingleDataBean>() {
                @Override
                public int compare(PreviewDataBean.PreviewSingleDataBean o1, PreviewDataBean.PreviewSingleDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            rowX = sheet.createRow(3);
            int typeIdx = 0;

            List<Integer> inputInts = new ArrayList<>();

            inputInts.clear();
            PrintRedBallUtils.append("#一网  #");
            PrintRedBallUtils.append3("#一网  #");
            for (int i = 0; i < data1.get(0).previewData.size(); i++) {
                if (!inputInts.contains(data1.get(0).previewData.get(i).data)) {
                    inputInts.add(data1.get(0).previewData.get(i).data);
                    PrintRedBallUtils.append(data1.get(0).previewData.get(i).data);
                    PrintRedBallUtils.append("、");
                    PrintRedBallUtils.append3(data1.get(0).previewData.get(i).data);
                    PrintRedBallUtils.append3("、");
                }
            }
            PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
            PrintRedBallUtils.append("-3");
            PrintRedBallUtils.append("\n");
            PrintRedBallUtils.deleteCharAt3(PrintRedBallUtils.length3() - 1);
            PrintRedBallUtils.append3("-1、2");
            PrintRedBallUtils.append3("\n");

            PrintRedBallUtils.append("#一网加粗#");
            if (checkIdx.size() > 0) {
                checkIdx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < checkIdx.size(); i++) {
                    PrintRedBallUtils.append(checkIdx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (checkIdx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#一网底#");
            if (check100Idx.size() > 0) {
                check100Idx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < check100Idx.size(); i++) {
                    PrintRedBallUtils.append(check100Idx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (check100Idx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            for (int i = 1; i < 36; i++) {
                CellStyle tempCellStyle;
                /*if (checkIdx.contains((Integer) i) && checkIdx.size() == 1) {
                    if (!PrintUtils.cx.contains(i)) {
                        PrintUtils.cx.add(i);
                    }

                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    tempCellStyle.setFont(font);
                } else */
                if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createGreenCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createGreenCellStyle(workbook);
                        tempCellStyle.setFont(font);
                    } else {
                        tempCellStyle = cellGreenStyle;
                    }
                }
                if (data1.get(0).previewData.size() > typeIdx && data1.get(0).previewData.get(typeIdx).data == i) {
                    typeIdx++;
                    String content = "X";
                    if (maxIdx.contains(i)) {
                        content = "X" + maxData;
                        if (!PrintRedBallUtils.zdld.contains(i)) {
                            PrintRedBallUtils.zdld.add(i);
                        }
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content);
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 36 + 1;

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            for (int j = 0; j < data1.get(data1.size() - 1).getPreviewData2().size(); j++) {
                PreviewDataBean.PreviewSingleDataBean psd = data1.get(data1.size() - 1).getPreviewData2().get(j);
                if (!psd.isTrue) {
                    check100Idx.add(data1.get(0).getPreviewData2().get(j).data);

                    PrintBlueBallUtils._1wd.add(data1.get(0).getPreviewData2().get(j).data);
                }
            }

            for (int i = 1; i < data1.size(); i++) {
                boolean isCheck = false;
                for (int j = 0; j < data1.get(i).getPreviewData2().size(); j++) {
                    PreviewDataBean.PreviewSingleDataBean psd = data1.get(i).getPreviewData2().get(j);
                    if (!psd.isTrue) {
                        isCheck = true;
                        checkIdx.add(data1.get(0).getPreviewData2().get(j).data);
                        PrintBlueBallUtils._1wc.add(data1.get(0).getPreviewData2().get(j).data);
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 5; i++) {
                int count = 0;
                for (int j = 1; j < data1.size(); j++) {
                    PreviewDataBean itemBean = data1.get(j);
                    if (itemBean.getPreviewData2().get(i).isTrue) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count > maxData) {
                    maxData = count;
                    maxIdx.clear();
                    maxIdx.add(data1.get(0).getPreviewData2().get(i).data);
                } else if (count == maxData) {
                    maxIdx.add(data1.get(0).getPreviewData2().get(i).data);
                }
            }

            data1.get(0).previewData2.sort(new Comparator<PreviewDataBean.PreviewSingleDataBean>() {
                @Override
                public int compare(PreviewDataBean.PreviewSingleDataBean o1, PreviewDataBean.PreviewSingleDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            typeIdx = 0;
            for (int i = 1; i < 13; i++) {
                CellStyle tempCellStyle;
                /*if (checkIdx.contains((Integer) i) && checkIdx.size() == 1) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    tempCellStyle.setFont(font);
                } else */
                if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createGreenCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createGreenCellStyle(workbook);
                        tempCellStyle.setFont(font);
                    } else {
                        tempCellStyle = cellGreenStyle;
                    }
                }
                if (data1.get(0).previewData2.size() > typeIdx && data1.get(0).previewData2.get(typeIdx).data == i) {
                    typeIdx++;
                    String content = "X";
                    if (maxIdx.contains(i)) {
                        content = "X" + maxData;
                        if (!PrintBlueBallUtils.zdld.contains(i)) {
                            PrintBlueBallUtils.zdld.add(i);
                        }
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content);
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 0;
            /////////////////////////////////////////////////333333//////////////////////////////////////////////////////

            //////////////////////////////////////////////////444444/////////////////////////////////////////////////////
            rowX = sheet.createRow(4);
            Map<Integer, Integer> collect = new HashMap<>();
            for (int i = 0; i < data2.dataItem.get(data2.dataItem.size() - 1).itemData.size(); i++) {
                if (!collect.containsKey(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data)) {
                    collect.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                collect.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, collect.get(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data) + 1);
            }

            Map<Integer, Integer> collect2 = new HashMap<>();
            for (int i = 0; i < data3.dataItem.get(data3.dataItem.size() - 1).itemData.size(); i++) {
                if (!collect2.containsKey(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data)) {
                    collect2.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                collect2.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, collect2.get(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data) + 1);
            }

            List<Integer> deleteLine = new ArrayList<>();
            List<Integer> collect4X = new ArrayList<>();

            deleteLine.clear();
            collect4X.clear();
            collect4X.add(data2.dataItem.get(data2.dataItem.size() - 2).hitCount);
            collect4X.add(data2.dataItem.get(data2.dataItem.size() - 3).hitCount);
            collect4X.add(data2.dataItem.get(data2.dataItem.size() - 4).hitCount);
            collect4X.add(data2.dataItem.get(data2.dataItem.size() - 5).hitCount);
            for (int j = data2.dataItem.size() - 3; j > -1; j--) {
                if (deleteLine.size() > 0) {
                    break;
                }
                if (j - 4 < 0) {
                    break;
                }
                if (collect4X.get(0) != data2.dataItem.get(j).hitCount) {
                    continue;
                }
                if (collect4X.get(1) != data2.dataItem.get(j - 1).hitCount) {
                    continue;
                }
                if (collect4X.get(2) != data2.dataItem.get(j - 2).hitCount) {
                    continue;
                }
                if (collect4X.get(3) != data2.dataItem.get(j - 3).hitCount) {
                    continue;
                }
                //对了，拿到哪个数
                for (int k = 0; k < data2.dataItem.get(j + 1).itemData.size(); k++) {
                    DMBean.DMDataBean dmb = data2.dataItem.get(j + 1).itemData.get(k);
                    if (dmb.hitData != 1) {
                        deleteLine.add(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(k).data);
                    }
                }
            }

            //找最近三期有相同的对错次数的
            if (deleteLine.size() <= 0) {
                collect4X.clear();
                collect4X.add(data2.dataItem.get(data2.dataItem.size() - 2).hitCount);
                collect4X.add(data2.dataItem.get(data2.dataItem.size() - 3).hitCount);
                collect4X.add(data2.dataItem.get(data2.dataItem.size() - 4).hitCount);
                for (int j = data2.dataItem.size() - 3; j > -1; j--) {
                    if (deleteLine.size() > 0) {
                        break;
                    }
                    if (j - 3 < 0) {
                        break;
                    }
                    if (collect4X.get(0) != data2.dataItem.get(j).hitCount) {
                        continue;
                    }
                    if (collect4X.get(1) != data2.dataItem.get(j - 1).hitCount) {
                        continue;
                    }
                    if (collect4X.get(2) != data2.dataItem.get(j - 2).hitCount) {
                        continue;
                    }
                    //对了，拿到哪个数
                    for (int k = 0; k < data2.dataItem.get(j + 1).itemData.size(); k++) {
                        DMBean.DMDataBean dmb = data2.dataItem.get(j + 1).itemData.get(k);
                        if (dmb.hitData != 1) {
                            deleteLine.add(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(k).data);
                        }
                    }
                }
            }

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            for (int j = 0; j < data2.dataItem.get(0).itemData.size(); j++) {
                DMBean.DMDataBean dmi = data2.dataItem.get(0).itemData.get(j);
                if (dmi.hitData != 1) {
                    check100Idx.add(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(j).data);
                }
            }

            for (int i = data2.dataItem.size() - 2; i > -1; i--) {
                boolean isCheck = false;
                for (int j = 0; j < data2.dataItem.get(i).itemData.size(); j++) {
                    DMBean.DMDataBean dmi = data2.dataItem.get(i).itemData.get(j);
                    if (dmi.hitData != 1) {
                        isCheck = true;
                        checkIdx.add(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(j).data);
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                int count = 0;
                for (int j = data2.dataItem.size() - 2; j > -1; j--) {
                    DMBean.DMItemBean dmi = data2.dataItem.get(j);
                    if (dmi.itemData.get(i).hitData == 1) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count > maxData) {
                    maxData = count;
                    maxIdx.clear();
                    maxIdx.add(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data);
                } else if (count == maxData) {
                    maxIdx.add(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data);
                }
            }

            data2.dataItem.get(data2.dataItem.size() - 1).itemData.sort(new Comparator<DMBean.DMDataBean>() {
                @Override
                public int compare(DMBean.DMDataBean o1, DMBean.DMDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            inputInts.clear();
            PrintRedBallUtils.append("#二网  #");
            PrintRedBallUtils.append3("#二网  #");
            for (int i = 0; i < data2.dataItem.get(data2.dataItem.size() - 1).itemData.size(); i++) {
                if (!inputInts.contains(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data)) {
                    inputInts.add(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append("、");
                    PrintRedBallUtils.append3(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append3("、");
                }
            }
            PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
            PrintRedBallUtils.append("-4");
            PrintRedBallUtils.append("\n");
            PrintRedBallUtils.deleteCharAt3(PrintRedBallUtils.length3() - 1);
            PrintRedBallUtils.append3("-2、3");
            PrintRedBallUtils.append3("\n");

            PrintRedBallUtils.append("#二网加粗#");
            if (checkIdx.size() > 0) {
                checkIdx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < checkIdx.size(); i++) {
                    PrintRedBallUtils.append(checkIdx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (checkIdx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#二网中#");
            if (deleteLine.size() > 0) {
                deleteLine.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < deleteLine.size(); i++) {
                    PrintRedBallUtils.append(deleteLine.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (deleteLine.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#二网底#");
            if (check100Idx.size() > 0) {
                check100Idx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < check100Idx.size(); i++) {
                    PrintRedBallUtils.append(check100Idx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (check100Idx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            for (int i = 1; i < 36; i++) {
                CellStyle tempCellStyle;
                if ((checkIdx.contains((Integer) i) && checkIdx.size() == 1) || (check100Idx.contains((Integer) i) && check100Idx.size() == 1) || (deleteLine.contains((Integer) i) && deleteLine.size() == 1)) {
                    if (!PrintRedBallUtils.cx.contains(i)) {
                        PrintRedBallUtils.cx.add(i);
                    }

                    Font font = workbook.createFont();
                    if (checkIdx.contains((Integer) i)) {
                        font.setBold(true);
                    }
                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createPinkCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createPinkCellStyle(workbook);
                        if (deleteLine.contains((Integer) i)) {
                            font.setStrikeout(true);
                        }
                        tempCellStyle.setFont(font);
                    } else {
                        if (deleteLine.contains((Integer) i)) {
                            Font font = workbook.createFont();
                            tempCellStyle = createPinkCellStyle(workbook);
                            font.setStrikeout(true);
                            tempCellStyle.setFont(font);
                        } else {
                            tempCellStyle = cellPinkStyle;
                        }
                    }
                }
                if (collect.containsKey(i)) {
                    int count = collect.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count >= 4) {
                        content.append(count);
                        content.append("X");
                    }
                    if (count < 4) {
                        for (int j = 0; j < count; j++) {
                            content.append("X");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        if (!PrintRedBallUtils.zdld.contains(i)) {
                            PrintRedBallUtils.zdld.add(i);
                        }
                        content.append(maxData);
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 36 + 1;

            deleteLine.clear();
            collect4X.clear();
            collect4X.add(data3.dataItem.get(data3.dataItem.size() - 2).hitCount);
            collect4X.add(data3.dataItem.get(data3.dataItem.size() - 3).hitCount);
            collect4X.add(data3.dataItem.get(data3.dataItem.size() - 4).hitCount);
            collect4X.add(data3.dataItem.get(data3.dataItem.size() - 5).hitCount);
//            Log.i("collect4X = " + collect4X.toString());
//            boolean allSuccess = false;
            for (int j = data3.dataItem.size() - 3; j > -1; j--) {
                if (deleteLine.size() > 0) {
                    break;
                }
//                Log.i("j - 4 = " + (j - 4));
                if (j - 4 < 0) {
                    break;
                }
//                Log.i("collect4X.get(0) = " + collect4X.get(0));
//                Log.i("data3.dataItem.get(j).hitCount = " + data3.dataItem.get(j).hitCount);
                if (collect4X.get(0) != data3.dataItem.get(j).hitCount) {
                    continue;
                }
//                Log.i("1111");
                if (collect4X.get(1) != data3.dataItem.get(j - 1).hitCount) {
                    continue;
                }
//                Log.i("2222");
                if (collect4X.get(2) != data3.dataItem.get(j - 2).hitCount) {
                    continue;
                }
//                Log.i("3333");
                if (collect4X.get(3) != data3.dataItem.get(j - 3).hitCount) {
                    continue;
                }
//                Log.i("2222222222222222222222" + data3.dataItem.get(j + 1).title);
                //对了，拿到哪个数
                for (int k = 0; k < data3.dataItem.get(j + 1).itemData.size(); k++) {
                    DMBean.DMDataBean dmb = data3.dataItem.get(j + 1).itemData.get(k);
                    if (dmb.hitData != 1) {
                        deleteLine.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(k).data);
                    }
                }
//                allSuccess = true;
//                Log.i("deleteLine = " + deleteLine.size());
            }

            //找最近三期有相同的对错次数的
            if (deleteLine.size() <= 0) {
                collect4X.clear();
                collect4X.add(data3.dataItem.get(data3.dataItem.size() - 2).hitCount);
                collect4X.add(data3.dataItem.get(data3.dataItem.size() - 3).hitCount);
                collect4X.add(data3.dataItem.get(data3.dataItem.size() - 4).hitCount);
                for (int j = data3.dataItem.size() - 3; j > -1; j--) {
                    if (deleteLine.size() > 0) {
                        break;
                    }
                    if (j - 3 < 0) {
                        break;
                    }
                    if (collect4X.get(0) != data3.dataItem.get(j).hitCount) {
                        continue;
                    }
                    if (collect4X.get(1) != data3.dataItem.get(j - 1).hitCount) {
                        continue;
                    }
                    if (collect4X.get(2) != data3.dataItem.get(j - 2).hitCount) {
                        continue;
                    }
                    //对了，拿到哪个数
                    for (int k = 0; k < data3.dataItem.get(j + 1).itemData.size(); k++) {
                        DMBean.DMDataBean dmb = data3.dataItem.get(j + 1).itemData.get(k);
                        if (dmb.hitData != 1) {
                            deleteLine.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(k).data);

                            PrintBlueBallUtils._2wz.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(k).data);
                        }
                    }
                }
            }

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            for (int j = 0; j < data3.dataItem.get(0).itemData.size(); j++) {
                DMBean.DMDataBean dmi = data3.dataItem.get(0).itemData.get(j);
                if (dmi.hitData != 1) {
                    check100Idx.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(j).data);

                    PrintBlueBallUtils._2wd.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(j).data);
                }
            }

            for (int i = data3.dataItem.size() - 2; i > -1; i--) {
                boolean isCheck = false;
                for (int j = 0; j < data3.dataItem.get(i).itemData.size(); j++) {
                    DMBean.DMDataBean dmi = data3.dataItem.get(i).itemData.get(j);
                    if (dmi.hitData != 1) {
                        isCheck = true;
                        checkIdx.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(j).data);

                        PrintBlueBallUtils._2wc.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(j).data);
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                int count = 0;
                for (int j = data3.dataItem.size() - 2; j > -1; j--) {
                    DMBean.DMItemBean dmi = data3.dataItem.get(j);
                    if (dmi.itemData.get(i).hitData == 1) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count > maxData) {
                    maxData = count;
                    maxIdx.clear();
                    maxIdx.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data);
                } else if (count == maxData) {
                    maxIdx.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data);
                }
            }

            data3.dataItem.get(data3.dataItem.size() - 1).itemData.sort(new Comparator<DMBean.DMDataBean>() {
                @Override
                public int compare(DMBean.DMDataBean o1, DMBean.DMDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            for (int i = 1; i < 13; i++) {
                CellStyle tempCellStyle;
                if (checkIdx.contains((Integer) i) && checkIdx.size() == 1 || (check100Idx.contains((Integer) i) && check100Idx.size() == 1) || (deleteLine.contains((Integer) i) && deleteLine.size() == 1)) {
                    if (!PrintBlueBallUtils.bx.contains(i)) {
                        PrintBlueBallUtils.bx.add(i);
                    }

                    Font font = workbook.createFont();

                    if (checkIdx.contains((Integer) i)) {
                        font.setBold(true);
                    }

                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);

                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createPinkCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createPinkCellStyle(workbook);
                        if (deleteLine.contains((Integer) i)) {
                            font.setStrikeout(true);
                        }
                        tempCellStyle.setFont(font);
                    } else {
                        if (deleteLine.contains((Integer) i)) {
                            Font font = workbook.createFont();
                            tempCellStyle = createPinkCellStyle(workbook);
                            font.setStrikeout(true);
                            tempCellStyle.setFont(font);
                        } else {
                            tempCellStyle = cellPinkStyle;
                        }
                    }
                }
                if (collect2.containsKey(i)) {
                    int count = collect2.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count >= 3) {
                        content.append(count);
                        content.append("X");
                    }
                    if (count < 3) {
                        for (int j = 0; j < count; j++) {
                            content.append("X");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        content.append(maxData);
                        if (!PrintBlueBallUtils.zdld.contains(i)) {
                            PrintBlueBallUtils.zdld.add(i);
                        }
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 0;
            ///////////////////////////////////////////////////444444////////////////////////////////////////////////////

            ////////////////////////////////////////////////////5555555///////////////////////////////////////////////////
            rowX = sheet.createRow(5);
            collect = new HashMap<>();
            for (int i = 0; i < data4.dataItem.get(data4.dataItem.size() - 1).itemData.size(); i++) {
                if (!collect.containsKey(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data)) {
                    collect.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                collect.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, collect.get(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data) + 1);
            }

            collect2 = new HashMap<>();
            for (int i = 0; i < data5.dataItem.get(data5.dataItem.size() - 1).itemData.size(); i++) {
                if (!collect2.containsKey(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data)) {
                    collect2.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                collect2.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, collect2.get(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data) + 1);
            }


            deleteLine.clear();
            collect4X.clear();
            collect4X.add(data4.dataItem.get(data4.dataItem.size() - 2).hitCount);
            collect4X.add(data4.dataItem.get(data4.dataItem.size() - 3).hitCount);
            collect4X.add(data4.dataItem.get(data4.dataItem.size() - 4).hitCount);
            collect4X.add(data4.dataItem.get(data4.dataItem.size() - 5).hitCount);
            for (int j = data4.dataItem.size() - 3; j > -1; j--) {
                if (deleteLine.size() > 0) {
                    break;
                }
                if (j - 4 < 0) {
                    break;
                }
                if (collect4X.get(0) != data4.dataItem.get(j).hitCount) {
                    continue;
                }
                if (collect4X.get(1) != data4.dataItem.get(j - 1).hitCount) {
                    continue;
                }
                if (collect4X.get(2) != data4.dataItem.get(j - 2).hitCount) {
                    continue;
                }
                if (collect4X.get(3) != data4.dataItem.get(j - 3).hitCount) {
                    continue;
                }
                //对了，拿到哪个数
                for (int k = 0; k < data4.dataItem.get(j + 1).itemData.size(); k++) {
                    DMBean.DMDataBean dmb = data4.dataItem.get(j + 1).itemData.get(k);
                    if (dmb.hitData != 1) {
                        deleteLine.add(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(k).data);
                    }
                }
            }

            //找最近三期有相同的对错次数的
            if (deleteLine.size() <= 0) {
                collect4X.clear();
                collect4X.add(data4.dataItem.get(data4.dataItem.size() - 2).hitCount);
                collect4X.add(data4.dataItem.get(data4.dataItem.size() - 3).hitCount);
                collect4X.add(data4.dataItem.get(data4.dataItem.size() - 4).hitCount);
                for (int j = data4.dataItem.size() - 3; j > -1; j--) {
                    if (deleteLine.size() > 0) {
                        break;
                    }
                    if (j - 3 < 0) {
                        break;
                    }
                    if (collect4X.get(0) != data4.dataItem.get(j).hitCount) {
                        continue;
                    }
                    if (collect4X.get(1) != data4.dataItem.get(j - 1).hitCount) {
                        continue;
                    }
                    if (collect4X.get(2) != data4.dataItem.get(j - 2).hitCount) {
                        continue;
                    }
                    //对了，拿到哪个数
                    for (int k = 0; k < data4.dataItem.get(j + 1).itemData.size(); k++) {
                        DMBean.DMDataBean dmb = data4.dataItem.get(j + 1).itemData.get(k);
                        if (dmb.hitData != 1) {
                            deleteLine.add(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(k).data);
                        }
                    }
                }
            }

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            for (int j = 0; j < data4.dataItem.get(0).itemData.size(); j++) {
                DMBean.DMDataBean dmi = data4.dataItem.get(0).itemData.get(j);
                if (dmi.hitData != 1) {
                    check100Idx.add(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(j).data);
                }
            }

            for (int i = data4.dataItem.size() - 2; i > -1; i--) {
                boolean isCheck = false;
                for (int j = 0; j < data4.dataItem.get(i).itemData.size(); j++) {
                    DMBean.DMDataBean dmi = data4.dataItem.get(i).itemData.get(j);
                    if (dmi.hitData != 1) {
                        isCheck = true;
                        checkIdx.add(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(j).data);
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                int count = 0;
                for (int j = data4.dataItem.size() - 2; j > -1; j--) {
                    DMBean.DMItemBean dmi = data4.dataItem.get(j);
                    if (dmi.itemData.get(i).hitData == 1) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count > maxData) {
                    maxData = count;
                    maxIdx.clear();
                    maxIdx.add(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data);
                } else if (count == maxData) {
                    maxIdx.add(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data);
                }
            }

            data4.dataItem.get(data4.dataItem.size() - 1).itemData.sort(new Comparator<DMBean.DMDataBean>() {
                @Override
                public int compare(DMBean.DMDataBean o1, DMBean.DMDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            inputInts.clear();
            PrintRedBallUtils.append("#三网  #");
            PrintRedBallUtils.append3("#三网  #");
            for (int i = 0; i < data4.dataItem.get(data4.dataItem.size() - 1).itemData.size(); i++) {
                if (!inputInts.contains(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data)) {
                    inputInts.add(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append("、");
                    PrintRedBallUtils.append3(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append3("、");
                }
            }
            PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
            PrintRedBallUtils.append("-4");
            PrintRedBallUtils.append("\n");
            PrintRedBallUtils.deleteCharAt3(PrintRedBallUtils.length3() - 1);
            PrintRedBallUtils.append3("-2、3");
            PrintRedBallUtils.append3("\n");

            PrintRedBallUtils.append("#三网加粗#");
            if (checkIdx.size() > 0) {
                checkIdx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < checkIdx.size(); i++) {
                    PrintRedBallUtils.append(checkIdx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (checkIdx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#三网中#");
            if (deleteLine.size() > 0) {
                deleteLine.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < deleteLine.size(); i++) {
                    PrintRedBallUtils.append(deleteLine.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (deleteLine.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#三网底#");
            if (check100Idx.size() > 0) {
                check100Idx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < check100Idx.size(); i++) {
                    PrintRedBallUtils.append(check100Idx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (check100Idx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");


            for (int i = 1; i < 36; i++) {
                CellStyle tempCellStyle;
                if (checkIdx.contains((Integer) i) && checkIdx.size() == 1 || (check100Idx.contains((Integer) i) && check100Idx.size() == 1) || (deleteLine.contains((Integer) i) && deleteLine.size() == 1)) {
                    if (!PrintRedBallUtils.cx.contains(i)) {
                        PrintRedBallUtils.cx.add(i);
                    }

                    Font font = workbook.createFont();
                    if (checkIdx.contains((Integer) i)) {
                        font.setBold(true);
                    }
                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createYellowCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createYellowCellStyle(workbook);
                        if (deleteLine.contains((Integer) i)) {
                            font.setStrikeout(true);
                        }
                        tempCellStyle.setFont(font);
                    } else {
                        if (deleteLine.contains((Integer) i)) {
                            Font font = workbook.createFont();
                            tempCellStyle = createYellowCellStyle(workbook);
                            font.setStrikeout(true);
                            tempCellStyle.setFont(font);
                        } else {
                            tempCellStyle = cellYellowStyle;
                        }
                    }
                }
                if (collect.containsKey(i)) {
                    int count = collect.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count >= 4) {
                        content.append(count);
                        content.append("X");
                    }
                    if (count < 4) {
                        for (int j = 0; j < count; j++) {
                            content.append("X");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        if (!PrintRedBallUtils.zdld.contains(i)) {
                            PrintRedBallUtils.zdld.add(i);
                        }
                        content.append(maxData);
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 36 + 1;

            deleteLine.clear();
            collect4X.clear();
            collect4X.add(data5.dataItem.get(data5.dataItem.size() - 2).hitCount);
            collect4X.add(data5.dataItem.get(data5.dataItem.size() - 3).hitCount);
            collect4X.add(data5.dataItem.get(data5.dataItem.size() - 4).hitCount);
            collect4X.add(data5.dataItem.get(data5.dataItem.size() - 5).hitCount);
            for (int j = data5.dataItem.size() - 3; j > -1; j--) {
                if (deleteLine.size() > 0) {
                    break;
                }
                if (j - 4 < 0) {
                    break;
                }
//                Log.i("c21 = " + data5.dataItem.get(j).hitCount);
                if (collect4X.get(0) != data5.dataItem.get(j).hitCount) {
                    continue;
                }
//                Log.i("c22 = " + data5.dataItem.get(j).title);
                if (collect4X.get(1) != data5.dataItem.get(j - 1).hitCount) {
                    continue;
                }
//                Log.i("c23 = " + data5.dataItem.get(j).title);
                if (collect4X.get(2) != data5.dataItem.get(j - 2).hitCount) {
                    continue;
                }
//                Log.i("c24 = " + data5.dataItem.get(j).title);
                if (collect4X.get(3) != data5.dataItem.get(j - 3).hitCount) {
                    continue;
                }
//                Log.i("c2 = " + collect4X.toString() + " j = " + j);
//                Log.i("c2 = " + data5.dataItem.get(j).title);
//                Log.i("c2 = " + data5.dataItem.get(j).hitCount);
//                Log.i("c2 = " + data5.dataItem.get(j - 1).hitCount);
//                Log.i("c23 = " + data5.dataItem.get(j - 2).hitCount);
                //对了，拿到哪个数
                for (int k = 0; k < data5.dataItem.get(j + 1).itemData.size(); k++) {
                    DMBean.DMDataBean dmb = data5.dataItem.get(j + 1).itemData.get(k);
                    if (dmb.hitData != 1) {
                        deleteLine.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(k).data);
                    }
                }
            }

            //找最近三期有相同的对错次数的
            if (deleteLine.size() <= 0) {
                collect4X.clear();
                collect4X.add(data5.dataItem.get(data5.dataItem.size() - 2).hitCount);
                collect4X.add(data5.dataItem.get(data5.dataItem.size() - 3).hitCount);
                collect4X.add(data5.dataItem.get(data5.dataItem.size() - 4).hitCount);
                for (int j = data5.dataItem.size() - 3; j > -1; j--) {
                    if (deleteLine.size() > 0) {
                        break;
                    }
                    if (j - 3 < 0) {
                        break;
                    }
                    if (collect4X.get(0) != data5.dataItem.get(j).hitCount) {
                        continue;
                    }
                    if (collect4X.get(1) != data5.dataItem.get(j - 1).hitCount) {
                        continue;
                    }
                    if (collect4X.get(2) != data5.dataItem.get(j - 2).hitCount) {
                        continue;
                    }
                    //对了，拿到哪个数
                    for (int k = 0; k < data5.dataItem.get(j + 1).itemData.size(); k++) {
                        DMBean.DMDataBean dmb = data5.dataItem.get(j + 1).itemData.get(k);
                        if (dmb.hitData != 1) {
//                            Log.i("c2 = " + dmb.data + data5.dataItem.get(j + 1).title);
                            deleteLine.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(k).data);

                            PrintBlueBallUtils._3wz.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(k).data);
                        }
                    }
                }
            }

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            for (int j = 0; j < data5.dataItem.get(0).itemData.size(); j++) {
                DMBean.DMDataBean dmi = data5.dataItem.get(0).itemData.get(j);
                if (dmi.hitData != 1) {
                    check100Idx.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(j).data);

                    PrintBlueBallUtils._3wd.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(j).data);
                }
            }

            for (int i = data5.dataItem.size() - 2; i > -1; i--) {
                boolean isCheck = false;
                for (int j = 0; j < data5.dataItem.get(i).itemData.size(); j++) {
                    DMBean.DMDataBean dmi = data5.dataItem.get(i).itemData.get(j);
                    if (dmi.hitData != 1) {
                        isCheck = true;
                        checkIdx.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(j).data);

                        PrintBlueBallUtils._3wc.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(j).data);
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                int count = 0;
                for (int j = data5.dataItem.size() - 2; j > -1; j--) {
                    DMBean.DMItemBean dmi = data5.dataItem.get(j);
                    if (dmi.itemData.get(i).hitData == 1) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count > maxData) {
                    maxData = count;
                    maxIdx.clear();
                    maxIdx.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data);
                } else if (count == maxData) {
                    maxIdx.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data);
                }
            }

            data5.dataItem.get(data5.dataItem.size() - 1).itemData.sort(new Comparator<DMBean.DMDataBean>() {
                @Override
                public int compare(DMBean.DMDataBean o1, DMBean.DMDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            for (int i = 1; i < 13; i++) {
                CellStyle tempCellStyle;
                if (checkIdx.contains((Integer) i) && checkIdx.size() == 1 || (check100Idx.contains((Integer) i) && check100Idx.size() == 1) || (deleteLine.contains((Integer) i) && deleteLine.size() == 1)) {
                    if (!PrintBlueBallUtils.bx.contains(i)) {
                        PrintBlueBallUtils.bx.add(i);
                    }

                    Font font = workbook.createFont();
                    if (checkIdx.contains((Integer) i)) {
                        font.setBold(true);
                    }
                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createYellowCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createYellowCellStyle(workbook);
                        if (deleteLine.contains((Integer) i)) {
                            font.setStrikeout(true);
                        }
                        tempCellStyle.setFont(font);
                    } else {
                        if (deleteLine.contains((Integer) i)) {
                            Font font = workbook.createFont();
                            tempCellStyle = createYellowCellStyle(workbook);
                            font.setStrikeout(true);
                            tempCellStyle.setFont(font);
                        } else {
                            tempCellStyle = cellYellowStyle;
                        }
                    }
                }
                if (collect2.containsKey(i)) {
                    int count = collect2.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count >= 3) {
                        content.append(count);
                        content.append("X");
                    }
                    if (count < 3) {
                        for (int j = 0; j < count; j++) {
                            content.append("X");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        content.append(maxData);
                        if (!PrintBlueBallUtils.zdld.contains(i)) {
                            PrintBlueBallUtils.zdld.add(i);
                        }
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 0;
            /////////////////////////////////////////////////5555555///////////////////////////////////////////////////////

            /////////////////////////////////////////////////66666666//////////////////////////////////////////////////////
            rowX = sheet.createRow(6);
            collect = new HashMap<>();
            for (int i = 0; i < data6.dataItem.get(data6.dataItem.size() - 1).itemData.size(); i++) {
                if (!collect.containsKey(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data)) {
                    collect.put(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                collect.put(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data, collect.get(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data) + 1);
            }

            collect2 = new HashMap<>();
            for (int i = 0; i < data7.dataItem.get(data7.dataItem.size() - 1).itemData.size(); i++) {
                if (!collect2.containsKey(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data)) {
                    collect2.put(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                collect2.put(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data, collect2.get(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data) + 1);
            }


            deleteLine.clear();
            collect4X.clear();
            collect4X.add(data6.dataItem.get(data6.dataItem.size() - 2).hitCount);
            collect4X.add(data6.dataItem.get(data6.dataItem.size() - 3).hitCount);
            collect4X.add(data6.dataItem.get(data6.dataItem.size() - 4).hitCount);
            collect4X.add(data6.dataItem.get(data6.dataItem.size() - 5).hitCount);
            for (int j = data6.dataItem.size() - 3; j > -1; j--) {
                if (deleteLine.size() > 0) {
                    break;
                }
                if (j - 4 < 0) {
                    break;
                }
                if (collect4X.get(0) != data6.dataItem.get(j).hitCount) {
                    continue;
                }
                if (collect4X.get(1) != data6.dataItem.get(j - 1).hitCount) {
                    continue;
                }
                if (collect4X.get(2) != data6.dataItem.get(j - 2).hitCount) {
                    continue;
                }
                if (collect4X.get(3) != data6.dataItem.get(j - 3).hitCount) {
                    continue;
                }
                //对了，拿到哪个数
                for (int k = 0; k < data6.dataItem.get(j + 1).itemData.size(); k++) {
                    DMBean.DMDataBean dmb = data6.dataItem.get(j + 1).itemData.get(k);
                    if (dmb.hitData != 1) {
                        deleteLine.add(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(k).data);
                    }
                }
            }

            //找最近三期有相同的对错次数的
            if (deleteLine.size() <= 0) {
                collect4X.clear();
                collect4X.add(data6.dataItem.get(data6.dataItem.size() - 2).hitCount);
                collect4X.add(data6.dataItem.get(data6.dataItem.size() - 3).hitCount);
                collect4X.add(data6.dataItem.get(data6.dataItem.size() - 4).hitCount);
                for (int j = data6.dataItem.size() - 3; j > -1; j--) {
                    if (deleteLine.size() > 0) {
                        break;
                    }
                    if (j - 3 < 0) {
                        break;
                    }
                    if (collect4X.get(0) != data6.dataItem.get(j).hitCount) {
                        continue;
                    }
                    if (collect4X.get(1) != data6.dataItem.get(j - 1).hitCount) {
                        continue;
                    }
                    if (collect4X.get(2) != data6.dataItem.get(j - 2).hitCount) {
                        continue;
                    }
                    /*Log.i("c2 = " + collect4X.toString() + " j = " + j);
                    Log.i("c2 = " + data6.dataItem.get(j).title);
                    Log.i("c2 = " + data6.dataItem.get(j).hitCount);
                    Log.i("c2 = " + data6.dataItem.get(j - 1).hitCount);
                    Log.i("c2 = " + data6.dataItem.get(j - 2).hitCount);*/
                    //对了，拿到哪个数
                    for (int k = 0; k < data6.dataItem.get(j + 1).itemData.size(); k++) {
                        DMBean.DMDataBean dmb = data6.dataItem.get(j + 1).itemData.get(k);
                        if (dmb.hitData != 1) {
//                            Log.i("c2 = " + dmb.data + data6.dataItem.get(j + 1).title);
                            deleteLine.add(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(k).data);
                        }
                    }
                }
            }

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            for (int j = 0; j < data6.dataItem.get(0).itemData.size(); j++) {
                DMBean.DMDataBean dmi = data6.dataItem.get(0).itemData.get(j);
                if (dmi.hitData != 1) {
                    check100Idx.add(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(j).data);
                }
            }

            for (int i = data6.dataItem.size() - 2; i > -1; i--) {
                boolean isCheck = false;
                for (int j = 0; j < data6.dataItem.get(i).itemData.size(); j++) {
                    DMBean.DMDataBean dmi = data6.dataItem.get(i).itemData.get(j);
                    if (dmi.hitData != 1) {
                        isCheck = true;
                        checkIdx.add(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(j).data);
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                int count = 0;
                for (int j = data6.dataItem.size() - 2; j > -1; j--) {
                    DMBean.DMItemBean dmi = data6.dataItem.get(j);
                    if (dmi.itemData.get(i).hitData == 1) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count > maxData) {
                    maxData = count;
                    maxIdx.clear();
                    maxIdx.add(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data);
                } else if (count == maxData) {
                    maxIdx.add(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data);
                }
            }

            data6.dataItem.get(data6.dataItem.size() - 1).itemData.sort(new Comparator<DMBean.DMDataBean>() {
                @Override
                public int compare(DMBean.DMDataBean o1, DMBean.DMDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            inputInts.clear();
            PrintRedBallUtils.append("#胆V  #");
            PrintRedBallUtils.append2("#胆V  #");
            for (int i = 0; i < data6.dataItem.get(data6.dataItem.size() - 1).itemData.size(); i++) {
                if (!inputInts.contains(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data)) {
                    inputInts.add(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append2(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data);
                    PrintRedBallUtils.append("、");
                    PrintRedBallUtils.append2("、");
                }
            }
            PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
            PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
            PrintRedBallUtils.append("-4");
            PrintRedBallUtils.append2("-0");
            PrintRedBallUtils.append("\n");
            PrintRedBallUtils.append2("\n");

            PrintRedBallUtils.append("#胆V加粗#");
            if (checkIdx.size() > 0) {
                checkIdx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < checkIdx.size(); i++) {
                    PrintRedBallUtils.append(checkIdx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (checkIdx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#胆V中#");
            if (deleteLine.size() > 0) {
                deleteLine.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < deleteLine.size(); i++) {
                    PrintRedBallUtils.append(deleteLine.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (deleteLine.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#胆V底#");
            if (check100Idx.size() > 0) {
                check100Idx.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < check100Idx.size(); i++) {
                    PrintRedBallUtils.append(check100Idx.get(i));
                    PrintRedBallUtils.append("、");
                }
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (check100Idx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            Map<Integer, Integer> x2 = new HashMap<>();
            Map<Integer, Integer> tempX2 = new HashMap<>();
            for (int i = 0; i < data1.get(0).previewData.size(); i++) {
                if (!x2.containsKey(data1.get(0).previewData.get(i).data)) {
                    x2.put(data1.get(0).previewData.get(i).data, 0);
                }
                if (!tempX2.containsKey(data1.get(0).previewData.get(i).data)) {
                    tempX2.put(data1.get(0).previewData.get(i).data, 0);
                } else {
                    continue;
                }
                x2.put(data1.get(0).previewData.get(i).data, x2.get(data1.get(0).previewData.get(i).data) + 1);
            }
            tempX2.clear();
            for (int i = 0; i < data2.dataItem.get(data2.dataItem.size() - 1).itemData.size(); i++) {
                if (!x2.containsKey(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data)) {
                    x2.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX2.containsKey(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX2.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x2.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, x2.get(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data) + 1);
            }
            tempX2.clear();
            for (int i = 0; i < data4.dataItem.get(data4.dataItem.size() - 1).itemData.size(); i++) {
                if (!x2.containsKey(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data)) {
                    x2.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX2.containsKey(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX2.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x2.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, x2.get(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data) + 1);
            }
            /*for (int i = 0; i < data6.dataItem.get(data6.dataItem.size() - 1).itemData.size(); i++) {
                if (!x2.containsKey(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data)) {
                    x2.put(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                x2.put(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data, x2.get(data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(i).data) + 1);
            }*/

            PrintRedBallUtils.append("#2X    #");
            final int[] count = {0};
            x2.keySet().stream().sorted(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            }).forEach(new Consumer<Integer>() {
                @Override
                public void accept(Integer key) {
                    if (x2.get(key) == 2) {
                        count[0]++;
                        PrintRedBallUtils.append(key);
                        PrintRedBallUtils.append("、");
                    }
//                    Log.i(key + "-" + x2.get(key));
                }
            });
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            Map<Integer, Integer> x3 = new HashMap<>();
            Map<Integer, Integer> tempX3 = new HashMap<>();
            for (int i = 0; i < data1.get(0).previewData.size(); i++) {
                if (!x3.containsKey(data1.get(0).previewData.get(i).data)) {
                    x3.put(data1.get(0).previewData.get(i).data, 0);
                }
                if (!tempX3.containsKey(data1.get(0).previewData.get(i).data)) {
                    tempX3.put(data1.get(0).previewData.get(i).data, 0);
                } else {
                    continue;
                }
                x3.put(data1.get(0).previewData.get(i).data, x3.get(data1.get(0).previewData.get(i).data) + 1);
            }
            tempX3.clear();
            for (int i = 0; i < data2.dataItem.get(data2.dataItem.size() - 1).itemData.size(); i++) {
                if (!x3.containsKey(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data)) {
                    x3.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX3.containsKey(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX3.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x3.put(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data, x3.get(data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(i).data) + 1);
            }
            tempX3.clear();
            for (int i = 0; i < data4.dataItem.get(data4.dataItem.size() - 1).itemData.size(); i++) {
                if (!x3.containsKey(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data)) {
                    x3.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX3.containsKey(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX3.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x3.put(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data, x3.get(data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(i).data) + 1);
            }


            PrintRedBallUtils.append("#3X    #");
            count[0] = 0;

            x3.keySet().stream().sorted(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            }).forEach(new Consumer<Integer>() {
                @Override
                public void accept(Integer key) {
                    if (x3.get(key) == 3) {
                        count[0]++;
                        PrintRedBallUtils.append(key);
                        PrintRedBallUtils.append("、");
                    }
                }
            });

            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            for (int i = 1; i < 36; i++) {
                CellStyle tempCellStyle;
                if (checkIdx.contains((Integer) i) && checkIdx.size() == 1 || (check100Idx.contains((Integer) i) && check100Idx.size() == 1) || (deleteLine.contains((Integer) i) && deleteLine.size() == 1)) {
                    if (!PrintRedBallUtils.cx.contains(i)) {
                        PrintRedBallUtils.cx.add(i);
                    }

                    Font font = workbook.createFont();
                    if (checkIdx.contains((Integer) i)) {
                        font.setBold(true);
                    }
                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createPink2CellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createPink2CellStyle(workbook);
                        if (deleteLine.contains((Integer) i)) {
                            font.setStrikeout(true);
                        }
                        tempCellStyle.setFont(font);
                    } else {
                        if (deleteLine.contains((Integer) i)) {
                            Font font = workbook.createFont();
                            tempCellStyle = createPink2CellStyle(workbook);
                            font.setStrikeout(true);
                            tempCellStyle.setFont(font);
                        } else {
                            tempCellStyle = cellPink2Style;
                        }
                    }
                }
                if (collect.containsKey(i)) {
                    count[0] = collect.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count[0] >= 3) {
                        content.append(count[0]);
                        content.append("V");
                    }
                    if (count[0] < 3) {
                        for (int j = 0; j < count[0]; j++) {
                            content.append("V");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        if (!PrintRedBallUtils.zdld.contains(i)) {
                            PrintRedBallUtils.zdld.add(i);
                        }
                        content.append(maxData);
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 36 + 1;

            PrintRedBallUtils.append("#最多连对#");
            PrintRedBallUtils.zdld.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.zdld.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.zdld.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.zdld.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.zdld.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            List<Integer> tempNumber = new ArrayList<>();
            for (int i = 1; i < 36; i++) {
                tempNumber.add(i);
                PrintRedBallUtils.wcwd.add(i);
                PrintRedBallUtils.wc1d.add(i);
                PrintRedBallUtils.wc2d.add(i);
                PrintRedBallUtils._1cwd.add(i);
                PrintRedBallUtils._1c1d.add(i);
                PrintRedBallUtils._1c2d.add(i);
                PrintRedBallUtils._2cwd.add(i);
                PrintRedBallUtils._2c1d.add(i);
                PrintRedBallUtils._2c2d.add(i);
                PrintRedBallUtils.ncnd.add(i);
            }

            PrintRedBallUtils.nul.addAll(tempNumber);
            PrintRedBallUtils.nulV.addAll(tempNumber);
            PrintRedBallUtils.nulC.addAll(tempNumber);
            PrintRedBallUtils.nulD.addAll(tempNumber);
            PrintRedBallUtils.nulAll.addAll(tempNumber);
            PrintRedBallUtils.wc.addAll(tempNumber);
            PrintRedBallUtils.wd.addAll(tempNumber);


            List<Integer> tempNumber2 = new ArrayList<>();
            for (int i = 1; i < 13; i++) {
                tempNumber2.add(i);
            }
            PrintBlueBallUtils._wc.addAll(tempNumber2);
            PrintBlueBallUtils._wd.addAll(tempNumber2);

            for (int i = 0; i < 13; i++) {
                for (int j = 0; j < data9.dataItem.get(data9.dataItem.size() - 1).itemData.size(); j++) {
                    for (int k = 0; k < data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(j).data.length; k++) {
                        if (data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(j).data[k] == i) {
                            PrintBlueBallUtils._wc.remove((Integer) i);
                        }
                    }
                }
            }

            for (int i = 0; i < 13; i++) {
                for (int j = 0; j < data11.dataItem.get(data11.dataItem.size() - 1).itemData.size(); j++) {
                    for (int k = 0; k < data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(j).data.length; k++) {
                        if (data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(j).data[k] == i) {
                            PrintBlueBallUtils._wd.remove((Integer) i);
                        }
                    }
                }
            }

            for (int i = 1; i < 36; i++) {
                for (int j = 0; j < data1.get(0).getPreviewData().size(); j++) {
                    if (data1.get(0).getPreviewData().get(j).data == i) {
                        tempNumber.remove((Integer) i);
                        PrintRedBallUtils.nul.remove((Integer) i);
                        PrintRedBallUtils.nulV.remove((Integer) i);
                        PrintRedBallUtils.nulC.remove((Integer) i);
                        PrintRedBallUtils.nulD.remove((Integer) i);
                        PrintRedBallUtils.nulAll.remove((Integer) i);
                        break;
                    }
                }
                for (int j = 0; j < data2.dataItem.get(data2.dataItem.size() - 1).itemData.size(); j++) {
                    if (data2.dataItem.get(data2.dataItem.size() - 1).itemData.get(j).data == i) {
                        tempNumber.remove((Integer) i);
                        PrintRedBallUtils.nul.remove((Integer) i);
                        PrintRedBallUtils.nulV.remove((Integer) i);
                        PrintRedBallUtils.nulC.remove((Integer) i);
                        PrintRedBallUtils.nulD.remove((Integer) i);
                        PrintRedBallUtils.nulAll.remove((Integer) i);
                        break;
                    }
                }
                for (int j = 0; j < data4.dataItem.get(data4.dataItem.size() - 1).itemData.size(); j++) {
                    if (data4.dataItem.get(data4.dataItem.size() - 1).itemData.get(j).data == i) {
                        tempNumber.remove((Integer) i);
                        PrintRedBallUtils.nul.remove((Integer) i);
                        PrintRedBallUtils.nulV.remove((Integer) i);
                        PrintRedBallUtils.nulC.remove((Integer) i);
                        PrintRedBallUtils.nulD.remove((Integer) i);
                        PrintRedBallUtils.nulAll.remove((Integer) i);
                        break;
                    }
                }
                for (int j = 0; j < data6.dataItem.get(data6.dataItem.size() - 1).itemData.size(); j++) {
                    if (data6.dataItem.get(data6.dataItem.size() - 1).itemData.get(j).data == i) {
                        tempNumber.remove((Integer) i);
                        PrintRedBallUtils.nulV.remove((Integer) i);
                        PrintRedBallUtils.nulC.remove((Integer) i);
                        PrintRedBallUtils.nulD.remove((Integer) i);
                        PrintRedBallUtils.nulAll.remove((Integer) i);
                        break;
                    }
                }
                for (int j = 0; j < data8.dataItem.get(data8.dataItem.size() - 1).itemData.size(); j++) {
                    for (int k = 0; k < data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(j).data.length; k++) {
                        if (data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(j).data[k] == i) {
                            PrintRedBallUtils.nulAll.remove((Integer) i);

                            PrintRedBallUtils.nulD.remove((Integer) i);

                            PrintRedBallUtils.wc.remove((Integer) i);
                            PrintRedBallUtils.wcwd.remove((Integer) i);

                            PrintRedBallUtils.wc1d.remove((Integer) i);
                            PrintRedBallUtils.wc2d.remove((Integer) i);
                            break;
                        }
                    }
                }
                for (int j = 0; j < data10.dataItem.get(data10.dataItem.size() - 1).itemData.size(); j++) {
                    for (int k = 0; k < data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(j).data.length; k++) {
                        if (data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(j).data[k] == i) {
                            PrintRedBallUtils.nulAll.remove((Integer) i);
                            PrintRedBallUtils.nulC.remove((Integer) i);

                            PrintRedBallUtils.wd.remove((Integer) i);
                            PrintRedBallUtils.wcwd.remove((Integer) i);

                            PrintRedBallUtils._1cwd.remove((Integer) i);
                            PrintRedBallUtils._2cwd.remove((Integer) i);
                            break;
                        }
                    }
                }
            }

            for (int j = 0; j < PrintRedBallUtils.wcwd.size(); j++) {
                PrintRedBallUtils.wc1d.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils.wc2d.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils._1cwd.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils._1c1d.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils._1c2d.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils._2cwd.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils._2c1d.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils._2c2d.remove(PrintRedBallUtils.wcwd.get(j));
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils.wcwd.get(j));
            }

            deleteLine.clear();
            collect4X.clear();
            collect4X.add(data7.dataItem.get(data7.dataItem.size() - 2).hitCount);
            collect4X.add(data7.dataItem.get(data7.dataItem.size() - 3).hitCount);
            collect4X.add(data7.dataItem.get(data7.dataItem.size() - 4).hitCount);
            collect4X.add(data7.dataItem.get(data7.dataItem.size() - 5).hitCount);
            for (int j = data7.dataItem.size() - 3; j > -1; j--) {
                if (deleteLine.size() > 0) {
                    break;
                }
                if (j - 4 < 0) {
                    break;
                }
                if (collect4X.get(0) != data7.dataItem.get(j).hitCount) {
                    continue;
                }
                if (collect4X.get(1) != data7.dataItem.get(j - 1).hitCount) {
                    continue;
                }
                if (collect4X.get(2) != data7.dataItem.get(j - 2).hitCount) {
                    continue;
                }
                if (collect4X.get(3) != data7.dataItem.get(j - 3).hitCount) {
                    continue;
                }
                //对了，拿到哪个数
                for (int k = 0; k < data7.dataItem.get(j + 1).itemData.size(); k++) {
                    DMBean.DMDataBean dmb = data7.dataItem.get(j + 1).itemData.get(k);
                    if (dmb.hitData != 1) {
                        deleteLine.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(k).data);
                    }
                }
            }

            //找最近三期有相同的对错次数的
            if (deleteLine.size() <= 0) {
                collect4X.clear();
                collect4X.add(data7.dataItem.get(data7.dataItem.size() - 2).hitCount);
                collect4X.add(data7.dataItem.get(data7.dataItem.size() - 3).hitCount);
                collect4X.add(data7.dataItem.get(data7.dataItem.size() - 4).hitCount);
                for (int j = data7.dataItem.size() - 3; j > -1; j--) {
                    if (deleteLine.size() > 0) {
                        break;
                    }
                    if (j - 3 < 0) {
                        break;
                    }
                    if (collect4X.get(0) != data7.dataItem.get(j).hitCount) {
                        continue;
                    }
                    if (collect4X.get(1) != data7.dataItem.get(j - 1).hitCount) {
                        continue;
                    }
                    if (collect4X.get(2) != data7.dataItem.get(j - 2).hitCount) {
                        continue;
                    }
                    //对了，拿到哪个数
                    for (int k = 0; k < data7.dataItem.get(j + 1).itemData.size(); k++) {
                        DMBean.DMDataBean dmb = data7.dataItem.get(j + 1).itemData.get(k);
                        if (dmb.hitData != 1) {
                            deleteLine.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(k).data);
                            if (!PrintBlueBallUtils._dvz.contains(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(k).data)) {
                                PrintBlueBallUtils._dvz.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(k).data);
                            }
                        }
                    }
                }
            }

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            for (int j = 0; j < data7.dataItem.get(0).itemData.size(); j++) {
                DMBean.DMDataBean dmi = data7.dataItem.get(0).itemData.get(j);
                if (dmi.hitData != 1) {
                    check100Idx.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(j).data);
                    if (!PrintBlueBallUtils._dvd.contains(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(j).data)) {
                        PrintBlueBallUtils._dvd.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(j).data);
                    }
                }
            }

            for (int i = data7.dataItem.size() - 2; i > -1; i--) {
                boolean isCheck = false;
                for (int j = 0; j < data7.dataItem.get(i).itemData.size(); j++) {
                    DMBean.DMDataBean dmi = data7.dataItem.get(i).itemData.get(j);
                    if (dmi.hitData != 1) {
                        isCheck = true;
                        checkIdx.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(j).data);
                        if (!PrintBlueBallUtils._dvc.contains(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(j).data)) {
                            PrintBlueBallUtils._dvc.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(j).data);
                        }
                    }
                }
                if (isCheck) {
                    break;
                }
            }

            for (int i = 0; i < 10; i++) {
                count[0] = 0;
                for (int j = data7.dataItem.size() - 2; j > -1; j--) {
                    DMBean.DMItemBean dmi = data7.dataItem.get(j);
                    if (dmi.itemData.get(i).hitData == 1) {
                        count[0]++;
                    } else {
                        break;
                    }
                }
                if (count[0] > maxData) {
                    maxData = count[0];
                    maxIdx.clear();
                    maxIdx.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data);
                } else if (count[0] == maxData) {
                    maxIdx.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data);
                }
            }

            data7.dataItem.get(data7.dataItem.size() - 1).itemData.sort(new Comparator<DMBean.DMDataBean>() {
                @Override
                public int compare(DMBean.DMDataBean o1, DMBean.DMDataBean o2) {
                    return Integer.compare(o1.data, o2.data);
                }
            });

            for (int i = 1; i < 13; i++) {
                CellStyle tempCellStyle;
                if (checkIdx.contains((Integer) i) && checkIdx.size() == 1 || (check100Idx.contains((Integer) i) && check100Idx.size() == 1) || (deleteLine.contains((Integer) i) && deleteLine.size() == 1)) {
                    if (!PrintBlueBallUtils.bx.contains(i)) {
                        PrintBlueBallUtils.bx.add(i);
                    }

                    Font font = workbook.createFont();
                    if (checkIdx.contains((Integer) i)) {
                        font.setBold(true);
                    }
                    tempCellStyle = createCellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else if (checkIdx.contains((Integer) i)) {
                    Font font = workbook.createFont();
                    font.setBold(true);
                    tempCellStyle = createPink2CellStyle(workbook);
                    if (check100Idx.contains((Integer) i)) {
                        font.setUnderline(HSSFFont.U_SINGLE);
                    }
                    if (deleteLine.contains((Integer) i)) {
                        font.setStrikeout(true);
                    }
                    tempCellStyle.setFont(font);
                } else {
                    if (check100Idx.contains((Integer) i)) {
                        Font font = workbook.createFont();
                        font.setUnderline(HSSFFont.U_SINGLE);
                        tempCellStyle = createPink2CellStyle(workbook);
                        if (deleteLine.contains((Integer) i)) {
                            font.setStrikeout(true);
                        }
                        tempCellStyle.setFont(font);
                    } else {
                        if (deleteLine.contains((Integer) i)) {
                            Font font = workbook.createFont();
                            tempCellStyle = createPink2CellStyle(workbook);
                            font.setStrikeout(true);
                            tempCellStyle.setFont(font);
                        } else {
                            tempCellStyle = cellPink2Style;
                        }
                    }
                }
                if (collect2.containsKey(i)) {
                    count[0] = collect2.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count[0] >= 3) {
                        content.append(count[0]);
                        content.append("V");
                    }
                    if (count[0] < 3) {
                        for (int j = 0; j < count[0]; j++) {
                            content.append("V");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        content.append(maxData);
                        if (!PrintBlueBallUtils.zdld.contains(i)) {
                            PrintBlueBallUtils.zdld.add(i);
                        }
                    }
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, tempCellStyle, "");
                }
            }
            columnIdx = 0;
            ///////////////////////////////////////////////66666666////////////////////////////////////////////////////////

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            ////////////////////////////////////////////////777777777///////////////////////////////////////////////////////
            rowX = sheet.createRow(7);
            collect = new HashMap<>();
            for (int i = 0; i < data8.dataItem.get(data8.dataItem.size() - 1).itemData.size(); i++) {
                for (int j = 0; j < data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(i).data.length; j++) {
                    if (!collect.containsKey(data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(i).data[j])) {
                        collect.put(data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(i).data[j], 0);
                    }
                    collect.put(data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(i).data[j], collect.get(data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(i).data[j]) + 1);
                }
            }

            collect2 = new HashMap<>();
            for (int i = 0; i < data9.dataItem.get(data9.dataItem.size() - 1).itemData.size(); i++) {
                for (int j = 0; j < data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data.length; j++) {
                    if (!collect2.containsKey(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j])) {
                        collect2.put(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j], 0);
                    }
                    collect2.put(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j], collect2.get(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j]) + 1);
                }
            }

            for (int i = 1; i < 36; i++) {
                if (collect.containsKey(i)) {
                    count[0] = collect.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count[0] != 1) {
                        PrintRedBallUtils.nulC.remove((Integer) i);

                        PrintRedBallUtils._1cwd.remove((Integer) i);
                        PrintRedBallUtils._1c1d.remove((Integer) i);
                        PrintRedBallUtils._1c2d.remove((Integer) i);
                    } else {
                        PrintRedBallUtils._1c.add((Integer) i);
                    }
                    if (count[0] != 2) {

                        PrintRedBallUtils._2cwd.remove((Integer) i);
                        PrintRedBallUtils._2c1d.remove((Integer) i);
                        PrintRedBallUtils._2c2d.remove((Integer) i);
                    } else {
                        PrintRedBallUtils._2c.add((Integer) i);
                    }

                    if (count[0] != 3) {
                    } else {
                        PrintRedBallUtils._3c.add((Integer) i);
                    }
                    if (count[0] != 4) {
                    } else {
                        PrintRedBallUtils._4c.add((Integer) i);
                    }
                    if (count[0] >= 3) {
                        content.append(count[0]);
                        content.append("C");
                    }
                    if (count[0] < 3) {
                        for (int j = 0; j < count[0]; j++) {
                            content.append("C");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        content.append(maxData);
                    }
                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, content.toString());
                } else {
                    PrintRedBallUtils._1cwd.remove((Integer) i);
                    PrintRedBallUtils._1c1d.remove((Integer) i);
                    PrintRedBallUtils._1c2d.remove((Integer) i);

                    PrintRedBallUtils._2cwd.remove((Integer) i);
                    PrintRedBallUtils._2c1d.remove((Integer) i);
                    PrintRedBallUtils._2c2d.remove((Integer) i);

                    PrintRedBallUtils.nulC.remove((Integer) i);

                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, "");
                }
            }
            columnIdx = 36 + 1;

            for (int i = 1; i < 13; i++) {
                if (collect2.containsKey(i)) {
                    count[0] = collect2.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count[0] >= 3) {
                        content.append(count[0]);
                        content.append("C");
                    }
                    if (count[0] == 3) {
                        PrintBlueBallUtils._3c.add(i);
                    }
                    if (count[0] == 4) {
                        PrintBlueBallUtils._4c.add(i);
                    }
                    if (count[0] == 5) {
                        PrintBlueBallUtils._5c.add(i);
                    }
                    if (count[0] < 3) {
                        for (int j = 0; j < count[0]; j++) {
                            content.append("C");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        content.append(maxData);
                    }
                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, "");
                }
            }
            columnIdx = 0;
            ////////////////////////////////////////////////777777777///////////////////////////////////////////////////////

            maxIdx.clear();
            maxData = 0;

            checkIdx.clear();
            check100Idx.clear();

            /////////////////////////////////////////////////888888888//////////////////////////////////////////////////////
            rowX = sheet.createRow(8);
            collect = new HashMap<>();
            for (int i = 0; i < data10.dataItem.get(data10.dataItem.size() - 1).itemData.size(); i++) {
                for (int j = 0; j < data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(i).data.length; j++) {
                    if (!collect.containsKey(data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(i).data[j])) {
                        collect.put(data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(i).data[j], 0);
                    }
                    collect.put(data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(i).data[j], collect.get(data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(i).data[j]) + 1);
                }
            }

            collect2 = new HashMap<>();
            for (int i = 0; i < data11.dataItem.get(data11.dataItem.size() - 1).itemData.size(); i++) {
                for (int j = 0; j < data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data.length; j++) {
                    if (!collect2.containsKey(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j])) {
                        collect2.put(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j], 0);
                    }
                    collect2.put(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j], collect2.get(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j]) + 1);
                }
            }

            for (int i = 1; i < 36; i++) {
                if (collect.containsKey(i)) {
                    count[0] = collect.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count[0] != 1) {
                        PrintRedBallUtils.nulD.remove((Integer) i);

                        PrintRedBallUtils.wc1d.remove((Integer) i);
                        PrintRedBallUtils._1c1d.remove((Integer) i);
                        PrintRedBallUtils._2c1d.remove((Integer) i);
                    } else {
                        PrintRedBallUtils._1d.add((Integer) i);
                    }
                    if (count[0] != 2) {

                        PrintRedBallUtils.wc2d.remove((Integer) i);
                        PrintRedBallUtils._1c2d.remove((Integer) i);
                        PrintRedBallUtils._2c2d.remove((Integer) i);
                    } else {
                        PrintRedBallUtils._2d.add((Integer) i);
                    }
                    if (count[0] != 3) {
                        PrintRedBallUtils._3d.remove((Integer) i);
                    } else {
                        PrintRedBallUtils._3d.add((Integer) i);
                    }
                    if (count[0] != 4) {
                        PrintRedBallUtils._4d.remove((Integer) i);
                    } else {
                        PrintRedBallUtils._4d.add((Integer) i);
                    }
                    if (count[0] >= 3) {
                        content.append(count[0]);
                        content.append("D");
                    }
                    if (count[0] < 3) {
                        for (int j = 0; j < count[0]; j++) {
                            content.append("D");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        content.append(maxData);
                    }
                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, content.toString());
                } else {
                    PrintRedBallUtils.wc1d.remove((Integer) i);
                    PrintRedBallUtils._1c1d.remove((Integer) i);
                    PrintRedBallUtils._2c1d.remove((Integer) i);

                    PrintRedBallUtils.wc2d.remove((Integer) i);
                    PrintRedBallUtils._1c2d.remove((Integer) i);
                    PrintRedBallUtils._2c2d.remove((Integer) i);

                    PrintRedBallUtils.nulD.remove((Integer) i);

                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, "");
                }
            }
            columnIdx = 36 + 1;

            for (int i = 1; i < 13; i++) {
                if (collect2.containsKey(i)) {
                    count[0] = collect2.get(i);
                    StringBuilder content = new StringBuilder();
                    if (count[0] >= 3) {
                        content.append(count[0]);
                        content.append("D");
                    }

                    if (count[0] == 3) {
                        PrintBlueBallUtils._3d.add(i);
                    }
                    if (count[0] == 4) {
                        PrintBlueBallUtils._4d.add(i);
                    }
                    if (count[0] == 5) {
                        PrintBlueBallUtils._5d.add(i);
                    }

                    if (count[0] < 3) {
                        for (int j = 0; j < count[0]; j++) {
                            content.append("D");
                        }
                    }
                    if (maxIdx.contains(i)) {
                        content.append(maxData);
                    }
                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, content.toString());
                } else {
                    createStringCell(rowX, columnIdx + i - 1, cellYellowStyle, "");
                }
            }
            /////////////////////////////////////////////888888888//////////////////////////////////////////////////////////

            fos = new FileOutputStream(file);
            workbook.write(fos);//完成写入
            fos.flush();
            fos.close();


            PrintRedBallUtils.append("#无C    #");
            PrintRedBallUtils.wc.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.wc.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.wc.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.wc.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.wc.size() >= PrintRedBallUtils._1c.size()) {
                    PrintRedBallUtils.append("-5");

                    PrintRedBallUtils.append("\n");
                    PrintRedBallUtils.append("#无C    #");
                    PrintRedBallUtils.append2("#无C    #");
                    for (int z = 0; z < PrintRedBallUtils.wc.size(); z++) {
                        PrintRedBallUtils.append(PrintRedBallUtils.wc.get(z));
                        PrintRedBallUtils.append2(PrintRedBallUtils.wc.get(z));
                        PrintRedBallUtils.append("、");
                        PrintRedBallUtils.append2("、");
                    }
                    PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                    PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
                    PrintRedBallUtils.append("-0");
                    PrintRedBallUtils.append2("-0");
                    PrintRedBallUtils.append2("\n");
                } else {
                    PrintRedBallUtils.append("-5");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#1C    #");
            PrintRedBallUtils.append2("#1C    #");
            PrintRedBallUtils._1c.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._1c.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._1c.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._1c.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.wc.size() < PrintRedBallUtils._1c.size()) {
                    PrintRedBallUtils.append("-5");

                    PrintRedBallUtils.append("\n");
                    PrintRedBallUtils.append("#1C    #");
                    PrintRedBallUtils.append2("#1C    #");
                    for (int z = 0; z < PrintRedBallUtils._1c.size(); z++) {
                        PrintRedBallUtils.append(PrintRedBallUtils._1c.get(z));
                        PrintRedBallUtils.append2(PrintRedBallUtils._1c.get(z));
                        PrintRedBallUtils.append("、");
                        PrintRedBallUtils.append2("、");
                    }
                    PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                    PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
                    PrintRedBallUtils.append("-0");
                    PrintRedBallUtils.append2("-0");
                    PrintRedBallUtils.append2("\n");
                } else {
                    PrintRedBallUtils.append("-5");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#2C    #");
            PrintRedBallUtils._2c.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._2c.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._2c.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._2c.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._2c.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#3C    #");
            PrintRedBallUtils._3c.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._3c.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._3c.get(z));
                PrintRedBallUtils.append("、");
            }
            for (int z = 0; z < PrintRedBallUtils._4c.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._4c.get(z));
                PrintRedBallUtils.append("、");
            }
            if ((PrintRedBallUtils._3c.size() + PrintRedBallUtils._4c.size()) > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if ((PrintRedBallUtils._3c.size() + PrintRedBallUtils._4c.size()) > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            for (int z = 0; z < data8.dataItem.get(data8.dataItem.size() - 1).itemData.size(); z++) {
                PrintRedBallUtils.append("#C");
                PrintRedBallUtils.append(z + 1);
                PrintRedBallUtils.append("    #");
                for (int i = 0; i < data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(z).data.length; i++) {
                    PrintRedBallUtils.append(data8.dataItem.get(data8.dataItem.size() - 1).itemData.get(z).data[i]);
                    PrintRedBallUtils.append("、");
                }
                if (data8.dataItem.get(data8.dataItem.size() - 1).itemData.size() > 0) {
                    PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                    if (data8.dataItem.get(data8.dataItem.size() - 1).itemData.size() > 2) {
                        PrintRedBallUtils.append("-3");
                    } else {
                        PrintRedBallUtils.append("-2");
                    }
                }
                PrintRedBallUtils.append("\n");
            }


            PrintRedBallUtils.append("#无D    #");
            PrintRedBallUtils.wd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.wd.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.wd.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.wd.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.wd.size() >= PrintRedBallUtils._1d.size()) {
                    PrintRedBallUtils.append("-5");

                    PrintRedBallUtils.append("\n");
                    PrintRedBallUtils.append("#无D    #");
                    PrintRedBallUtils.append2("#无D    #");
                    for (int z = 0; z < PrintRedBallUtils.wd.size(); z++) {
                        PrintRedBallUtils.append(PrintRedBallUtils.wd.get(z));
                        PrintRedBallUtils.append2(PrintRedBallUtils.wd.get(z));
                        PrintRedBallUtils.append("、");
                        PrintRedBallUtils.append2("、");
                    }
                    PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                    PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
                    PrintRedBallUtils.append("-0");
                    PrintRedBallUtils.append2("-0");
                    PrintRedBallUtils.append2("\n");
                } else {
                    PrintRedBallUtils.append("-5");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#1D    #");
            PrintRedBallUtils._1d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._1d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._1d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._1d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.wd.size() < PrintRedBallUtils._1d.size()) {
                    PrintRedBallUtils.append("-5");

                    PrintRedBallUtils.append("\n");
                    PrintRedBallUtils.append("#1D    #");
                    PrintRedBallUtils.append2("#1D    #");
                    for (int z = 0; z < PrintRedBallUtils._1d.size(); z++) {
                        PrintRedBallUtils.append(PrintRedBallUtils._1d.get(z));
                        PrintRedBallUtils.append2(PrintRedBallUtils._1d.get(z));
                        PrintRedBallUtils.append("、");
                        PrintRedBallUtils.append2("、");
                    }
                    PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                    PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
                    PrintRedBallUtils.append("-0");
                    PrintRedBallUtils.append2("-0");
                    PrintRedBallUtils.append2("\n");
                } else {
                    PrintRedBallUtils.append("-5");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#2D    #");
            PrintRedBallUtils._2d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._2d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._2d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._2d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._2d.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#3D    #");
            PrintRedBallUtils._3d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._3d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._3d.get(z));
                PrintRedBallUtils.append("、");
            }
            for (int z = 0; z < PrintRedBallUtils._4d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._4d.get(z));
                PrintRedBallUtils.append("、");
            }
            if ((PrintRedBallUtils._3d.size() + PrintRedBallUtils._4d.size()) > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if ((PrintRedBallUtils._3d.size() + PrintRedBallUtils._4d.size()) > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            for (int z = 0; z < data10.dataItem.get(data10.dataItem.size() - 1).itemData.size(); z++) {
                PrintRedBallUtils.append("#D");
                PrintRedBallUtils.append(z + 1);
                PrintRedBallUtils.append("    #");
                for (int i = 0; i < data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(z).data.length; i++) {
                    PrintRedBallUtils.append(data10.dataItem.get(data10.dataItem.size() - 1).itemData.get(z).data[i]);
                    PrintRedBallUtils.append("、");
                }
                if (data10.dataItem.get(data10.dataItem.size() - 1).itemData.size() > 0) {
                    PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                    if (data10.dataItem.get(data10.dataItem.size() - 1).itemData.size() > 2) {
                        PrintRedBallUtils.append("-3");
                    } else {
                        PrintRedBallUtils.append("-2");
                    }
                }
                PrintRedBallUtils.append("\n");
            }

            for (int i = 0; i < PrintRedBallUtils.wcwd.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils.wcwd.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils.wc1d.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils.wc1d.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils.wc2d.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils.wc2d.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils._1cwd.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils._1cwd.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils._1c1d.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils._1c1d.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils._1c2d.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils._1c2d.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils._2cwd.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils._2cwd.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils._2c1d.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils._2c1d.get(i));
            }

            for (int i = 0; i < PrintRedBallUtils._2c2d.size(); i++) {
                PrintRedBallUtils.ncnd.remove(PrintRedBallUtils._2c2d.get(i));
            }

            PrintRedBallUtils.append("#无C无D  #");
            PrintRedBallUtils.wcwd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.wcwd.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.wcwd.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.wcwd.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.wcwd.size() > 2) {
                    PrintRedBallUtils.append("-4");
                } else {
                    PrintRedBallUtils.append("-4");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#无C1D  #");
            PrintRedBallUtils.wc1d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.wc1d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.wc1d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.wc1d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.wc1d.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#无C2D  #");
            PrintRedBallUtils.wc2d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.wc2d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.wc2d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.wc2d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.wc2d.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#1C无D  #");
            PrintRedBallUtils._1cwd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._1cwd.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._1cwd.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._1cwd.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._1cwd.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#1C1D  #");
            PrintRedBallUtils._1c1d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._1c1d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._1c1d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._1c1d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._1c1d.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#1C2D  #");
            PrintRedBallUtils._1c2d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._1c2d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._1c2d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._1c2d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._1c2d.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#2C无D  #");
            PrintRedBallUtils._2cwd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._2cwd.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._2cwd.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._2cwd.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._2cwd.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#2C1D  #");
            PrintRedBallUtils._2c1d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._2c1d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._2c1d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._2c1d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._2c1d.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#2C2D  #");
            PrintRedBallUtils._2c2d.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils._2c2d.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils._2c2d.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils._2c2d.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils._2c2d.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            /*PrintUtils.append("#NCND  #");
            PrintUtils.ncnd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintUtils.ncnd.size(); z++) {
                PrintUtils.append(PrintUtils.ncnd.get(z));
                PrintUtils.append("、");
            }
            if (PrintUtils.ncnd.size() > 0) {
                PrintUtils.deleteCharAt(PrintUtils.length() - 1);
                if (PrintUtils.ncnd.size() > 2) {
                    PrintUtils.append("-3");
                } else {
                    PrintUtils.append("-2");
                }
            }
            PrintUtils.append("\n");*/

            PrintRedBallUtils.append("#空    #");
            PrintRedBallUtils.append2("#空    #");
            PrintRedBallUtils.append3("#空    #");
            PrintRedBallUtils.nul.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.nul.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.nul.get(z));
                PrintRedBallUtils.append("、");
                PrintRedBallUtils.append2(PrintRedBallUtils.nul.get(z));
                PrintRedBallUtils.append2("、");
                PrintRedBallUtils.append3(PrintRedBallUtils.nul.get(z));
                PrintRedBallUtils.append3("、");
            }
            if (PrintRedBallUtils.nul.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
                PrintRedBallUtils.deleteCharAt3(PrintRedBallUtils.length3() - 1);
                if (PrintRedBallUtils.nul.size() > 2) {
                    PrintRedBallUtils.append("-5");
                    PrintRedBallUtils.append2("-0");
                } else {
                    PrintRedBallUtils.append("-5");
                    PrintRedBallUtils.append2("-0");
                }
                PrintRedBallUtils.append3("-4");
            }
            PrintRedBallUtils.append("\n");
            PrintRedBallUtils.append2("\n");
            PrintRedBallUtils.append3("\n");

            PrintRedBallUtils.append("#空+v    #");
            PrintRedBallUtils.nulV.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.nulV.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.nulV.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.nulV.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.nulV.size() > 2) {
                    PrintRedBallUtils.append("-4");
                } else {
                    PrintRedBallUtils.append("-4");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#全空    #");
            PrintRedBallUtils.nulAll.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.nulAll.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.nulAll.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.nulAll.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.nulAll.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#空C    #");
            PrintRedBallUtils.nulC.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.nulC.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.nulC.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.nulC.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.nulC.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#空D    #");
            PrintRedBallUtils.nulD.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.nulD.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.nulD.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.nulD.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.nulD.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            PrintRedBallUtils.append("#错杀    #");
            PrintRedBallUtils.cx.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintRedBallUtils.cx.size(); z++) {
                PrintRedBallUtils.append(PrintRedBallUtils.cx.get(z));
                PrintRedBallUtils.append("、");
            }
            if (PrintRedBallUtils.cx.size() > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (PrintRedBallUtils.cx.size() > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-3");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗0    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 0) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗1    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 1) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗2    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 2) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗3    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 3) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗4    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 4) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗5    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 5) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗6    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 6) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗7    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 7) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗8    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 8) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗9    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 9) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗10    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 10) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗11    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 11) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗12    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 12) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗13    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 13) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗14    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 14) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#遗15    #");
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) == 15) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-3");
                } else {
                    PrintRedBallUtils.append("-2");
                }
            }
            PrintRedBallUtils.append("\n");

            count[0] = 0;
            PrintRedBallUtils.append("#大遗");
            PrintRedBallUtils.append("    #");
            if (yls1.size() > 3) {
                PrintRedBallUtils.append2("#大遗");
                PrintRedBallUtils.append2("    #");
            } else {
                PrintRedBallUtils.append2("#大遗   ");
            }
            for (int z = 0; z < yls1.size(); z++) {
                if (yls1.get(z) >= 10) {
                    PrintRedBallUtils.append((z + 1));
                    PrintRedBallUtils.append("、");
                    PrintRedBallUtils.append2((z + 1));
                    PrintRedBallUtils.append2("、");
                    count[0]++;
                }
            }
            if (count[0] > 0) {
                PrintRedBallUtils.deleteCharAt(PrintRedBallUtils.length() - 1);
                PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
                if (count[0] > 3) {
                    PrintRedBallUtils.append2("-4");
                    PrintRedBallUtils.append2("\n");

                    PrintRedBallUtils.append2("#大遗");
                    PrintRedBallUtils.append2("    #");
                    for (int z = 0; z < yls1.size(); z++) {
                        if (yls1.get(z) >= 10) {
                            PrintRedBallUtils.append2((z + 1));
                            PrintRedBallUtils.append2("、");
                        }
                    }
                    PrintRedBallUtils.deleteCharAt2(PrintRedBallUtils.length2() - 1);
                    PrintRedBallUtils.append2("-0");
                    PrintRedBallUtils.append2("\n");

                } else {
                    PrintRedBallUtils.append2("    #");
                }
                if (count[0] > 2) {
                    PrintRedBallUtils.append("-4");
                } else {
                    PrintRedBallUtils.append("-4");
                }
            }
            PrintRedBallUtils.append("\n");

            FileReadHelper.writeToFile(path2, PrintRedBallUtils.dataToString2());
            FileReadHelper.writeToFile(path3, PrintRedBallUtils.dataToString3());
            FileReadHelper.writeToFile(path4, PrintRedBallUtils.dataToString());
            //输出红球参数

            count[0] = 0;
            ///输出蓝球参数
            PrintBlueBallUtils.append("#必杀    #");
            for (int z = 0; z < PrintBlueBallUtils.bx.size(); z++) {
                if (!result.redResult.contains(PrintBlueBallUtils.bx.get(z))) {
                    result.redResult.add(new TwoPoint(PrintBlueBallUtils.bx.get(z), 0));
                }
                PrintBlueBallUtils.append((PrintBlueBallUtils.bx.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append2("-2");
                } else {
                    PrintBlueBallUtils.append2("-1");
                }
                if (count[0] > 5) {
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append3("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#一网    #");
            for (int z = 0; z < data1.get(0).getPreviewData2().size(); z++) {
                PrintBlueBallUtils.append((data1.get(0).getPreviewData2().get(z).data));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#一网粗    #");
            PrintBlueBallUtils._1wc.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._1wc.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._1wc.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#一网中    #");
            PrintBlueBallUtils._1wz.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._1wz.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._1wz.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#一网底    #");
            PrintBlueBallUtils._1wd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._1wd.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._1wd.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#二网    #");

            List<Integer> pn = new ArrayList<>();

            for (int z = 0; z < data3.dataItem.get(data3.dataItem.size() - 1).itemData.size(); z++) {
                if (pn.contains((data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(z).data))) {
                    continue;
                }
                pn.add(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(z).data);
                PrintBlueBallUtils.append((data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(z).data));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#二网粗    #");
            PrintBlueBallUtils._2wc.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._2wc.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._2wc.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#二网中    #");
            PrintBlueBallUtils._2wz.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._2wz.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._2wz.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#二网底    #");
            PrintBlueBallUtils._2wd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._2wd.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._2wd.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#三网    #");
            pn.clear();
            for (int z = 0; z < data5.dataItem.get(data5.dataItem.size() - 1).itemData.size(); z++) {
                if (pn.contains((data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(z).data))) {
                    continue;
                }
                pn.add(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(z).data);
                PrintBlueBallUtils.append((data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(z).data));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#三网粗    #");
            PrintBlueBallUtils._3wc.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._3wc.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._3wc.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#三网中    #");
            PrintBlueBallUtils._3wz.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._3wz.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._3wz.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            count[0] = 0;
            PrintBlueBallUtils.append("#三网底    #");
            PrintBlueBallUtils._3wd.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils._3wd.size(); z++) {
                PrintBlueBallUtils.append((PrintBlueBallUtils._3wd.get(z)));
                PrintBlueBallUtils.append("、");
                count[0]++;
            }
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");


            x2.clear();
            tempX2.clear();
            for (int i = 0; i < data1.get(0).previewData2.size(); i++) {
                if (!x2.containsKey(data1.get(0).previewData2.get(i).data)) {
                    x2.put(data1.get(0).previewData2.get(i).data, 0);
                }
                if (!tempX2.containsKey(data1.get(0).previewData2.get(i).data)) {
                    tempX2.put(data1.get(0).previewData2.get(i).data, 0);
                } else {
                    continue;
                }
                x2.put(data1.get(0).previewData2.get(i).data, x2.get(data1.get(0).previewData2.get(i).data) + 1);
            }
            tempX2.clear();
            for (int i = 0; i < data3.dataItem.get(data3.dataItem.size() - 1).itemData.size(); i++) {
                if (!x2.containsKey(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data)) {
                    x2.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX2.containsKey(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX2.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x2.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, x2.get(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data) + 1);
            }
            tempX2.clear();
            for (int i = 0; i < data5.dataItem.get(data5.dataItem.size() - 1).itemData.size(); i++) {
                if (!x2.containsKey(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data)) {
                    x2.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX2.containsKey(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX2.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x2.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, x2.get(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data) + 1);
            }

            PrintBlueBallUtils.append("#2X    #");
            count[0] = 0;
            x2.keySet().stream().sorted(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            }).forEach(new Consumer<Integer>() {
                @Override
                public void accept(Integer key) {
                    if (x2.get(key) == 2) {
                        count[0]++;
                        PrintBlueBallUtils.append(key);
                        PrintBlueBallUtils.append("、");
                    }
//                    Log.i(key + "-" + x2.get(key));
                }
            });
            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            x3.clear();
            tempX3.clear();
            for (int i = 0; i < data1.get(0).previewData2.size(); i++) {
                if (!x3.containsKey(data1.get(0).previewData2.get(i).data)) {
                    x3.put(data1.get(0).previewData2.get(i).data, 0);
                }
                if (!tempX3.containsKey(data1.get(0).previewData2.get(i).data)) {
                    tempX3.put(data1.get(0).previewData2.get(i).data, 0);
                } else {
                    continue;
                }
                x3.put(data1.get(0).previewData2.get(i).data, x3.get(data1.get(0).previewData2.get(i).data) + 1);
            }
            tempX3.clear();
            for (int i = 0; i < data3.dataItem.get(data3.dataItem.size() - 1).itemData.size(); i++) {
                if (!x3.containsKey(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data)) {
                    x3.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX3.containsKey(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX3.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x3.put(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data, x3.get(data3.dataItem.get(data3.dataItem.size() - 1).itemData.get(i).data) + 1);
            }
            tempX3.clear();
            for (int i = 0; i < data5.dataItem.get(data5.dataItem.size() - 1).itemData.size(); i++) {
                if (!x3.containsKey(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data)) {
                    x3.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, 0);
                }
                if (!tempX3.containsKey(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data)) {
                    tempX3.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, 0);
                } else {
                    continue;
                }
                x3.put(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data, x3.get(data5.dataItem.get(data5.dataItem.size() - 1).itemData.get(i).data) + 1);
            }


            PrintBlueBallUtils.append("#3X    #");
            count[0] = 0;

            x3.keySet().stream().sorted(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            }).forEach(new Consumer<Integer>() {
                @Override
                public void accept(Integer key) {
                    if (x3.get(key) == 3) {
                        count[0]++;
                        PrintBlueBallUtils.append(key);
                        PrintBlueBallUtils.append("、");
                    }
                }
            });

            if (count[0] > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (count[0] > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                }
            }
            PrintBlueBallUtils.append("\n");


            PrintBlueBallUtils.append("#最多连对#");
            PrintBlueBallUtils.zdld.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            for (int z = 0; z < PrintBlueBallUtils.zdld.size(); z++) {
                PrintBlueBallUtils.append(PrintBlueBallUtils.zdld.get(z));
                PrintBlueBallUtils.append("、");
            }
            if (PrintBlueBallUtils.zdld.size() > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.zdld.size() > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            inputInts.clear();
            PrintBlueBallUtils.append("#胆V  #");
            for (int i = 0; i < data7.dataItem.get(data7.dataItem.size() - 1).itemData.size(); i++) {
                if (!inputInts.contains(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data)) {
                    inputInts.add(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data);
                    PrintBlueBallUtils.append(data7.dataItem.get(data7.dataItem.size() - 1).itemData.get(i).data);
                    PrintBlueBallUtils.append("、");
                }
            }
            PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
            PrintBlueBallUtils.append("-3");
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#胆V加粗#");
            if (PrintBlueBallUtils._dvc.size() > 0) {
                PrintBlueBallUtils._dvc.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._dvc.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._dvc.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._dvc.size() > 2) {
                    PrintBlueBallUtils.append2("-2");
                    PrintBlueBallUtils.append3("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#胆V中#");
            if (PrintBlueBallUtils._dvz.size() > 0) {
                PrintBlueBallUtils._dvz.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._dvz.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._dvz.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._dvz.size() > 2) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#胆V底#");
            if (PrintBlueBallUtils._dvd.size() > 0) {
                PrintBlueBallUtils._dvd.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._dvd.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._dvd.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._dvd.size() > 2) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#无C#");
            if (PrintBlueBallUtils._wc.size() > 0) {
                PrintBlueBallUtils._wc.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._wc.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._wc.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._wc.size() > 2) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#3C#");
            if (PrintBlueBallUtils._3c.size() > 0) {
                PrintBlueBallUtils._3c.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._3c.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._3c.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._3c.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#4C#");
            if (PrintBlueBallUtils._4c.size() > 0) {
                PrintBlueBallUtils._4c.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._4c.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._4c.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._4c.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#5C#");
            if (PrintBlueBallUtils._5c.size() > 0) {
                PrintBlueBallUtils._5c.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._5c.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._5c.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._5c.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");


            /////1c
            for (int i = 0; i < 10; i++) {
                SMBean.SMItemBean sm = data9.dataItem.get(data9.dataItem.size() - 2);
                SMBean.SMItemBean sm2 = data9.dataItem.get(data9.dataItem.size() - 1);
                int checkCount = 0;
                for (int j = 0; j < sm.itemData.get(i).hitData.length; j++) {
                    PrintBlueBallUtils.c10X.get(i).add(sm2.itemData.get(i).data[j]);
                    if (sm.itemData.get(i).hitData[j] == 1) {
                        checkCount++;
                    }
                }
                PrintBlueBallUtils.c10X.get(i).sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                if (checkCount >= 2) {
                    if (i == 0) {
                        PrintBlueBallUtils._c1.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c1.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c1.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c1.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 1) {
                        PrintBlueBallUtils._c2.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c2.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c2.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c2.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 2) {
                        PrintBlueBallUtils._c3.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c3.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c3.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c3.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 3) {
                        PrintBlueBallUtils._c4.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c4.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c4.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c4.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 4) {
                        PrintBlueBallUtils._c5.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c5.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c5.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c5.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 5) {
                        PrintBlueBallUtils._c6.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c6.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c6.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c6.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 6) {
                        PrintBlueBallUtils._c7.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c7.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c7.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c7.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 7) {
                        PrintBlueBallUtils._c8.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c8.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c8.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c8.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 8) {
                        PrintBlueBallUtils._c9.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c9.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c9.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c9.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    } else if (i == 9) {
                        PrintBlueBallUtils._c10.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._c10.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._c10.add(sm2.itemData.get(i).data[2]);
                        PrintBlueBallUtils._c10.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o1.compareTo(o2);
                            }
                        });
                    }
                }
            }

            PrintBlueBallUtils.append("#C1" + PrintBlueBallUtils.c1.toString() + "#");
            if (PrintBlueBallUtils._c1.size() > 0) {
                PrintBlueBallUtils._c1.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c1.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c1.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c1.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c1.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c1.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c1.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c1.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C2" + PrintBlueBallUtils.c2.toString() + "#");
            if (PrintBlueBallUtils._c2.size() > 0) {
                PrintBlueBallUtils._c2.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c2.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c2.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c2.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c2.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c2.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c2.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c2.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C3" + PrintBlueBallUtils.c3.toString() + "#");
            if (PrintBlueBallUtils._c3.size() > 0) {
                PrintBlueBallUtils._c3.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c3.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c3.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c3.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c3.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c3.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c3.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c3.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C4" + PrintBlueBallUtils.c4.toString() + "#");
            if (PrintBlueBallUtils._c4.size() > 0) {
                PrintBlueBallUtils._c4.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c4.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c4.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c4.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c4.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c4.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c4.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c4.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C5" + PrintBlueBallUtils.c5.toString() + "#");
            if (PrintBlueBallUtils._c5.size() > 0) {
                PrintBlueBallUtils._c5.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c5.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c5.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c5.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c5.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c5.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c5.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c5.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C6" + PrintBlueBallUtils.c6.toString() + "#");
            if (PrintBlueBallUtils._c6.size() > 0) {
                PrintBlueBallUtils._c6.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c6.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c6.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c6.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c6.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c6.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c6.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c6.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C7" + PrintBlueBallUtils.c7.toString() + "#");
            if (PrintBlueBallUtils._c7.size() > 0) {
                PrintBlueBallUtils._c7.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c7.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c7.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c7.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c7.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c7.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c7.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c7.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C8" + PrintBlueBallUtils.c8.toString() + "#");
            if (PrintBlueBallUtils._c8.size() > 0) {
                PrintBlueBallUtils._c8.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c8.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c8.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c8.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c8.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c8.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c8.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c8.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C9" + PrintBlueBallUtils.c9.toString() + "#");
            if (PrintBlueBallUtils._c9.size() > 0) {
                PrintBlueBallUtils._c9.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c9.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c9.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c9.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c9.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c9.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c9.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c9.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#C10" + PrintBlueBallUtils.c10.toString() + "#");
            if (PrintBlueBallUtils._c10.size() > 0) {
                PrintBlueBallUtils._c10.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._c10.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._c10.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._c10.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.c10.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.c10.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.c10.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.c10.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            List<SplitBean> splits = new ArrayList<>();
            SMBean.SMItemBean sm = data9.dataItem.get(data9.dataItem.size() - 1);
            for (int i = 0; i < sm.itemData.size(); i++) {
                SMBean.SMDataBean smd = sm.itemData.get(i);
                List<Integer> tmp = new ArrayList<>();
                for (int j = 0; j < smd.data.length; j++) {
                    tmp.add(smd.data[j]);
                }
                tmp.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                Utils.combine_increase(tmp, 0, new int[2], 2, 2, smd.data.length, splits);
            }
            for (int i = 0; i < splits.size(); i++) {
                splits.get(i).setCollectCount(1);
            }

            HashSet<SplitBean> set = new HashSet<>(splits);
            splits = new ArrayList<>(set);
            if (splits.size() > 0) {
                for (int i = 0; i < splits.size(); i++) {
                    if (splits.get(i).getCollectCount() <= 1) {
                        continue;
                    }
                    PrintBlueBallUtils.append2("#---#");
                    for (int j = 0; j < splits.get(i).getData().length; j++) {
                        PrintBlueBallUtils.append2(splits.get(i).getData()[j]);
                        PrintBlueBallUtils.append2("、");
                    }
                    result.redResult.add(new TwoPoint(splits.get(i).getData()[0], splits.get(i).getData()[1]));

                    PrintBlueBallUtils.deleteCharAt2(PrintBlueBallUtils.length() - 1);
                    if (splits.get(i).getData().length > 3) {
                        PrintBlueBallUtils.append2("-2");
                    } else {
                        PrintBlueBallUtils.append2("-2");
                    }
                    PrintBlueBallUtils.append2("\n");
                }
            }


            PrintBlueBallUtils.append("#无D#");
            if (PrintBlueBallUtils._wd.size() > 0) {
                PrintBlueBallUtils._wd.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._wd.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._wd.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._wd.size() > 2) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#3D#");
            if (PrintBlueBallUtils._3d.size() > 0) {
                PrintBlueBallUtils._3d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._3d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._3d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._3d.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#4D#");
            if (PrintBlueBallUtils._4d.size() > 0) {
                PrintBlueBallUtils._4d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._4d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._4d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._4d.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#5D#");
            if (PrintBlueBallUtils._5d.size() > 0) {
                PrintBlueBallUtils._5d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._5d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._5d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._5d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");


            /////1d
            for (int i = 0; i < 10; i++) {
                sm = data11.dataItem.get(data11.dataItem.size() - 2);
                SMBean.SMItemBean sm2 = data11.dataItem.get(data11.dataItem.size() - 1);
                int checkCount = 0;
                for (int k = 0; k < sm.itemData.get(i).data.length; k++) {
                    PrintBlueBallUtils.d10x.get(i).add(sm2.itemData.get(i).data[k]);
                    for (int j = 0; j < sm.blueData.length; j++) {
                        if (sm.itemData.get(i).data[k] == sm.blueData[j]) {
                            checkCount++;
                        }
                    }
                }
                PrintBlueBallUtils.d10x.get(i).sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });

                if (checkCount >= 2) {
                    if (i == 0) {
                        PrintBlueBallUtils._d1.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d1.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d1.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 1) {
                        PrintBlueBallUtils._d2.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d2.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d2.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 2) {
                        PrintBlueBallUtils._d3.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d3.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d3.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 3) {
                        PrintBlueBallUtils._d4.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d4.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d4.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 4) {
                        PrintBlueBallUtils._d5.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d5.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d5.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 5) {
                        PrintBlueBallUtils._d6.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d6.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d6.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 6) {
                        PrintBlueBallUtils._d7.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d7.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d7.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 7) {
                        PrintBlueBallUtils._d8.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d8.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d8.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 8) {
                        PrintBlueBallUtils._d9.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d9.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d9.add(sm2.itemData.get(i).data[2]);
                    } else if (i == 9) {
                        PrintBlueBallUtils._d10.add(sm2.itemData.get(i).data[0]);
                        PrintBlueBallUtils._d10.add(sm2.itemData.get(i).data[1]);
                        PrintBlueBallUtils._d10.add(sm2.itemData.get(i).data[2]);
                    }
                }
            }


            PrintBlueBallUtils.append("#D1" + PrintBlueBallUtils.d1.toString() + "#");
            if (PrintBlueBallUtils._d1.size() > 0) {
                PrintBlueBallUtils._d1.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d1.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d1.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d1.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d1.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d1.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d1.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d1.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D2" + PrintBlueBallUtils.d2.toString() + "#");
            if (PrintBlueBallUtils._d2.size() > 0) {
                PrintBlueBallUtils._d2.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d2.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d2.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d2.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d2.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d2.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d2.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d2.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D3" + PrintBlueBallUtils.d3.toString() + "#");
            if (PrintBlueBallUtils._d3.size() > 0) {
                PrintBlueBallUtils._d3.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d3.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d3.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d3.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d3.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d3.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d3.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d3.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D4" + PrintBlueBallUtils.d4.toString() + "#");
            if (PrintBlueBallUtils._d4.size() > 0) {
                PrintBlueBallUtils._d4.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d4.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d4.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d4.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d4.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d4.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d4.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d4.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D5" + PrintBlueBallUtils.d5.toString() + "#");
            if (PrintBlueBallUtils._d5.size() > 0) {
                PrintBlueBallUtils._d5.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d5.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d5.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d5.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d5.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d5.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d5.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d5.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D6" + PrintBlueBallUtils.d6.toString() + "#");
            if (PrintBlueBallUtils._d6.size() > 0) {
                PrintBlueBallUtils._d6.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d6.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d6.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d6.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d6.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d6.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d6.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d6.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D7" + PrintBlueBallUtils.d7.toString() + "#");
            if (PrintBlueBallUtils._d7.size() > 0) {
                PrintBlueBallUtils._d7.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d7.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d7.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d7.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d7.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d7.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d7.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d7.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D8" + PrintBlueBallUtils.d8.toString() + "#");
            if (PrintBlueBallUtils._d8.size() > 0) {
                PrintBlueBallUtils._d8.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d8.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d8.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d8.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d8.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d8.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d8.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d8.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D9" + PrintBlueBallUtils.d9.toString() + "#");
            if (PrintBlueBallUtils._d9.size() > 0) {
                PrintBlueBallUtils._d9.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d9.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d9.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d9.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d9.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d9.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d9.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d9.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#D10" + PrintBlueBallUtils.d10.toString() + "#");
            if (PrintBlueBallUtils._d10.size() > 0) {
                PrintBlueBallUtils._d10.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._d10.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._d10.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._d10.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            } else {
                PrintBlueBallUtils.d10.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils.d10.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils.d10.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils.d10.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-3");
                }
            }
            PrintBlueBallUtils.append("\n");

            splits = new ArrayList<>();
            sm = data11.dataItem.get(data11.dataItem.size() - 1);
            for (int i = 0; i < sm.itemData.size(); i++) {
                SMBean.SMDataBean smd = sm.itemData.get(i);
                List<Integer> tmp = new ArrayList<>();
                for (int j = 0; j < smd.data.length; j++) {
                    tmp.add(smd.data[j]);
                }
                tmp.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                Utils.combine_increase(tmp, 0, new int[2], 2, 2, smd.data.length, splits);
            }
            for (int i = 0; i < splits.size(); i++) {
                splits.get(i).setCollectCount(1);
            }

            set = new HashSet<>(splits);
            splits = new ArrayList<>(set);
            if (splits.size() > 0) {
                for (int i = 0; i < splits.size(); i++) {
                    if (splits.get(i).getCollectCount() <= 1) {
                        continue;
                    }
                    PrintBlueBallUtils.append2("#---#");
                    for (int j = 0; j < splits.get(i).getData().length; j++) {
                        PrintBlueBallUtils.append2(splits.get(i).getData()[j]);
                        PrintBlueBallUtils.append2("、");
                    }
                    result.redResult.add(new TwoPoint(splits.get(i).getData()[0], splits.get(i).getData()[1]));

                    PrintBlueBallUtils.deleteCharAt2(PrintBlueBallUtils.length() - 1);
                    if (splits.get(i).getData().length > 3) {
                        PrintBlueBallUtils.append2("-2");
                    } else {
                        PrintBlueBallUtils.append2("-2");
                    }
                    PrintBlueBallUtils.append2("\n");
                }
            }

            Map<Integer, Integer> _c = new HashMap<>();
            for (int i = 0; i < data9.dataItem.get(data9.dataItem.size() - 1).itemData.size(); i++) {
                for (int j = 0; j < data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data.length; j++) {
                    if (!_c.containsKey(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j])) {
                        _c.put(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j], 0);
                    }
                    _c.put(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j], _c.get(data9.dataItem.get(data9.dataItem.size() - 1).itemData.get(i).data[j]) + 1);
                }
            }
            Map<Integer, Integer> _d = new HashMap<>();
            for (int i = 0; i < data11.dataItem.get(data11.dataItem.size() - 1).itemData.size(); i++) {
                for (int j = 0; j < data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data.length; j++) {
                    if (!_d.containsKey(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j])) {
                        _d.put(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j], 0);
                    }
                    _d.put(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j], _d.get(data11.dataItem.get(data11.dataItem.size() - 1).itemData.get(i).data[j]) + 1);
                }
            }

            for (int i = 1; i < 13; i++) {
                if (_c.containsKey(i) && _c.get(i) == 1 && _d.containsKey(i) && _d.get(i) == 1) {
                    PrintBlueBallUtils._1c1d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 1 && _d.containsKey(i) && _d.get(i) == 2) {
                    PrintBlueBallUtils._1c2d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 1 && _d.containsKey(i) && _d.get(i) == 3) {
                    PrintBlueBallUtils._1c3d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 2 && _d.containsKey(i) && _d.get(i) == 1) {
                    PrintBlueBallUtils._2c1d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 2 && _d.containsKey(i) && _d.get(i) == 2) {
                    PrintBlueBallUtils._2c2d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 2 && _d.containsKey(i) && _d.get(i) == 3) {
                    PrintBlueBallUtils._2c3d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 3 && _d.containsKey(i) && _d.get(i) == 1) {
                    PrintBlueBallUtils._3c1d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 3 && _d.containsKey(i) && _d.get(i) == 2) {
                    PrintBlueBallUtils._3c2d.add(i);
                }
                if (_c.containsKey(i) && _c.get(i) == 3 && _d.containsKey(i) && _d.get(i) == 3) {
                    PrintBlueBallUtils._3c3d.add(i);
                }
            }

            PrintBlueBallUtils.append("#1C1D#");
            if (PrintBlueBallUtils._1c1d.size() > 0) {
                PrintBlueBallUtils._1c1d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._1c1d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._1c1d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._1c1d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#1C2D#");
            if (PrintBlueBallUtils._1c2d.size() > 0) {
                PrintBlueBallUtils._1c2d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._1c2d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._1c2d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._1c2d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#1C3D#");
            if (PrintBlueBallUtils._1c3d.size() > 0) {
                PrintBlueBallUtils._1c3d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._1c3d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._1c3d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._1c3d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#2C1D#");
            if (PrintBlueBallUtils._2c1d.size() > 0) {
                PrintBlueBallUtils._2c1d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._2c1d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._2c1d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._2c1d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#2C2D#");
            if (PrintBlueBallUtils._2c2d.size() > 0) {
                PrintBlueBallUtils._2c2d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._2c2d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._2c2d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._2c2d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#2C3D#");
            if (PrintBlueBallUtils._2c3d.size() > 0) {
                PrintBlueBallUtils._2c3d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._2c3d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._2c3d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._2c3d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#3C1D#");
            if (PrintBlueBallUtils._3c1d.size() > 0) {
                PrintBlueBallUtils._3c1d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._3c1d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._3c1d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._3c1d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#3C2D#");
            if (PrintBlueBallUtils._3c2d.size() > 0) {
                PrintBlueBallUtils._3c2d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._3c2d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._3c2d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._3c2d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            PrintBlueBallUtils.append("#3C3D#");
            if (PrintBlueBallUtils._3c3d.size() > 0) {
                PrintBlueBallUtils._3c3d.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
                for (int i = 0; i < PrintBlueBallUtils._3c3d.size(); i++) {
                    PrintBlueBallUtils.append(PrintBlueBallUtils._3c3d.get(i));
                    PrintBlueBallUtils.append("、");
                }
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (PrintBlueBallUtils._3c3d.size() > 3) {
                    PrintBlueBallUtils.append("-2");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            Map<Integer, Integer> collectFilterNumber = new HashMap<>();
            for (int i = 0; i < 12; i++) {
                PrintBlueBallUtils.append2("#前" + (i + 1) + "期#");
                for (int j = 0; j < 2; j++) {
                    PrintBlueBallUtils.append2(data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData[j]);
                    PrintBlueBallUtils.append2("、");

                    if (!collectFilterNumber.containsKey(data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData[j])) {
                        collectFilterNumber.put(data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData[j], 0);
                    }
                    collectFilterNumber.put(data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData[j], collectFilterNumber.get(data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData[j]) + 1);
                }
                result.yellowResult.add(new TwoPoint(data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData[0], data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData[1]));

                PrintBlueBallUtils.deleteCharAt2(PrintBlueBallUtils.length() - 1);
                if (data2.dataItem.get(data2.dataItem.size() - 2 - i).blueData.length > 3) {
                    PrintBlueBallUtils.append2("-2");
                } else {
                    PrintBlueBallUtils.append2("-2");
                }
                PrintBlueBallUtils.append2("\n");
            }


            List<Integer> tempNumber3 = new ArrayList<>(tempNumber2);
            int maxCount = 0;
            for (Integer key :
                    collectFilterNumber.keySet()) {
                tempNumber3.remove(key);
                maxCount = Math.max(maxCount, collectFilterNumber.get(key));
            }

            PrintBlueBallUtils.append("#前统次0#");
            count[0] = 0;
            for (int i = 0; i < tempNumber3.size(); i++) {
                PrintBlueBallUtils.append(tempNumber3.get(i));
                PrintBlueBallUtils.append("、");
            }
            if (tempNumber3.size() > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (tempNumber3.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            for (int i = 1; i < maxCount + 1; i++) {
                if (i >= 4) {
                    PrintBlueBallUtils.append("#前统次");
                    for (int j = i; j < maxCount + 1; j++) {
                        PrintBlueBallUtils.append(j);
                        PrintBlueBallUtils.append("、");
                    }
                    PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);

                    PrintBlueBallUtils.append("#");
                    count[0] = 0;

                    List<Integer> tmp = new ArrayList<>();
                    for (int j = i; j < maxCount + 1; j++) {
                        for (Integer key :
                                collectFilterNumber.keySet()) {
                            if (j == collectFilterNumber.get(key)) {
                                tmp.add(key);
                            }
                        }
                    }
                    tmp.sort(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o1.compareTo(o2);
                        }
                    });

                    for (int j = 0; j < tmp.size(); j++) {
                        PrintBlueBallUtils.append(tmp.get(j));
                        PrintBlueBallUtils.append("、");
                        count[0]++;
                    }

                    if (count[0] > 0) {
                        PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                        if (count[0] > 2) {
                            PrintBlueBallUtils.append("-2");
                        } else {
                            PrintBlueBallUtils.append("-2");
                        }
                    }
                    PrintBlueBallUtils.append("\n");
                    break;
                } else {
                    PrintBlueBallUtils.append("#前统次" + i + "#");
                    count[0] = 0;
                    for (Integer key :
                            collectFilterNumber.keySet()) {
                        if (i == collectFilterNumber.get(key)) {
                            PrintBlueBallUtils.append(key);
                            PrintBlueBallUtils.append("、");
                            count[0]++;
                        }
                    }
                    if (count[0] > 0) {
                        PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                        if (count[0] > 2) {
                            PrintBlueBallUtils.append2("-3");
                            PrintBlueBallUtils.append3("-3");
                        } else {
                            PrintBlueBallUtils.append2("-2");
                            PrintBlueBallUtils.append3("-2");
                        }
                    }
                    PrintBlueBallUtils.append("\n");
                }
            }


            List<PrintDataBean> filterPrintData = new ArrayList<>();
            int[] blueData = data2.dataItem.get(data2.dataItem.size() - 2).blueData;
//            Log.i("blueData = " + Arrays.toString(blueData));
            for (int i = 0; i < allPrint.size() - 1; i++) {
//                Log.i("allPrint.get(i).getBlueBalls() = " + Arrays.toString(allPrint.get(i).getBlueBalls()));
                if (Arrays.equals(allPrint.get(i).getBlueBalls(), blueData) && i != allPrint.size() - 1) {//相同的，拿下一期的数
                    filterPrintData.add(allPrint.get(i + 1));
                }
            }

            collectFilterNumber.clear();
            int start = 0;
            if (filterPrintData.size() > 12) {
                start = filterPrintData.size() - 12;
            }
            for (int i = start; i < filterPrintData.size(); i++) {
                PrintBlueBallUtils.append2("#旧" + (i - (filterPrintData.size() - 13)) + "#");
                for (int j = 0; j < 2; j++) {
                    PrintBlueBallUtils.append2(filterPrintData.get(i).getBlueBalls()[j]);
                    if (!collectFilterNumber.containsKey(filterPrintData.get(i).getBlueBalls()[j])) {
                        collectFilterNumber.put(filterPrintData.get(i).getBlueBalls()[j], 0);
                    }
                    collectFilterNumber.put(filterPrintData.get(i).getBlueBalls()[j], collectFilterNumber.get(filterPrintData.get(i).getBlueBalls()[j]) + 1);
                    PrintBlueBallUtils.append2("、");
                }
                result.oriResult.add(new TwoPoint(filterPrintData.get(i).getBlueBalls()[0], filterPrintData.get(i).getBlueBalls()[1]));

                PrintBlueBallUtils.deleteCharAt2(PrintBlueBallUtils.length() - 1);
                if (filterPrintData.get(i).getBlueBalls().length > 3) {
                    PrintBlueBallUtils.append2("-2");
                } else {
                    PrintBlueBallUtils.append2("-2");
                }
                PrintBlueBallUtils.append2("\n");
            }

            tempNumber3 = new ArrayList<>(tempNumber2);
            maxCount = 0;
            for (Integer key :
                    collectFilterNumber.keySet()) {
                tempNumber3.remove(key);
                maxCount = Math.max(maxCount, collectFilterNumber.get(key));
            }

            Log.i("collectFilterNumber = " + collectFilterNumber.toString());

            PrintBlueBallUtils.append("#旧统次0#");
            count[0] = 0;
            for (int i = 0; i < tempNumber3.size(); i++) {
                PrintBlueBallUtils.append(tempNumber3.get(i));
                PrintBlueBallUtils.append("、");
            }
            if (tempNumber3.size() > 0) {
                PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                if (tempNumber3.size() > 3) {
                    PrintBlueBallUtils.append("-3");
                } else {
                    PrintBlueBallUtils.append("-2");
                }
            }
            PrintBlueBallUtils.append("\n");

            for (int i = 1; i < maxCount + 1; i++) {
                /*PrintBlueBallUtils.append("#旧统次" + i + "#");
                count[0] = 0;
                for (Integer key :
                        collectFilterNumber.keySet()) {
                    if (i == collectFilterNumber.get(key)) {
                        PrintBlueBallUtils.append(key);
                        PrintBlueBallUtils.append("、");
                        count[0]++;
                    }
                }
                if (count[0] > 0) {
                    PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                    if (count[0] > 3) {
                        PrintBlueBallUtils.append("-3");
                    } else {
                        PrintBlueBallUtils.append("-2");
                    }
                }
                PrintBlueBallUtils.append("\n");*/

                if (i >= 4) {
                    PrintBlueBallUtils.append("#旧统次");
                    for (int j = i; j < maxCount + 1; j++) {
                        PrintBlueBallUtils.append(j);
                        PrintBlueBallUtils.append("、");
                    }
                    PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);

                    PrintBlueBallUtils.append("#");
                    count[0] = 0;

                    List<Integer> tmp = new ArrayList<>();
                    for (int j = i; j < maxCount + 1; j++) {
                        for (Integer key :
                                collectFilterNumber.keySet()) {
                            if (j == collectFilterNumber.get(key)) {
                                tmp.add(key);
                            }
                        }
                    }
                    tmp.sort(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o1.compareTo(o2);
                        }
                    });

                    for (int j = 0; j < tmp.size(); j++) {
                        PrintBlueBallUtils.append(tmp.get(j));
                        PrintBlueBallUtils.append("、");
                        count[0]++;
                    }

                    if (count[0] > 0) {
                        PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                        if (count[0] > 2) {
                            PrintBlueBallUtils.append("-2");
                        } else {
                            PrintBlueBallUtils.append("-2");
                        }
                    }
                    PrintBlueBallUtils.append("\n");
                    break;
                } else {
                    PrintBlueBallUtils.append("#旧统次" + i + "#");
                    count[0] = 0;
                    for (Integer key :
                            collectFilterNumber.keySet()) {
                        if (i == collectFilterNumber.get(key)) {
                            PrintBlueBallUtils.append(key);
                            PrintBlueBallUtils.append("、");
                            count[0]++;
                        }
                    }
                    if (count[0] > 0) {
                        PrintBlueBallUtils.deleteCharAt(PrintBlueBallUtils.length() - 1);
                        if (count[0] > 2) {
                            PrintBlueBallUtils.append2("-3");
                            PrintBlueBallUtils.append3("-3");
                        } else {
                            PrintBlueBallUtils.append2("-2");
                            PrintBlueBallUtils.append3("-2");
                        }
                    }
                    PrintBlueBallUtils.append("\n");
                }
            }

            FileReadHelper.writeToFile(path5, PrintBlueBallUtils.dataToString());
            FileReadHelper.writeToFile(path6, PrintBlueBallUtils.dataToString3());
            //输出蓝球参数
            //需要唤起

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
        return result;
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

    private static CellStyle createGreenCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN1.index);
        return style;
    }

    private static CellStyle createPinkCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.PINK1.index);
        return style;
    }

    private static CellStyle createGoldCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GOLD.index);
        return style;
    }

    private static CellStyle createYellowCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.YELLOW.index);
        return style;
    }

    private static CellStyle createPink2CellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.PINK.index);
        return style;
    }

    private static void setCommonCellStyle(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(false);
    }

}
