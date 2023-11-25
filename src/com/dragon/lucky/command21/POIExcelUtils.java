package com.dragon.lucky.command21;


import com.dragon.lucky.utils.Log;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by lb on 2018/9/11.
 */

public class POIExcelUtils {

    public static boolean write2File(String output, List<OnceGenerateBean> data) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream fos = null;
        try {
            File file = new File(output);
            if (!file.exists()) {
                Log.i("输出路径 = " + output);
                file.createNewFile();
            }
            XSSFSheet sheet = workbook.createSheet(new File(output).getName());
            sheet.setDefaultColumnWidth(4);
//            sheet.setDefaultRowHeight((short) 12);

            HashMap<String, Integer> z5FQMap = new HashMap<>();
            HashMap<String, Integer> z345FQMap = new HashMap<>();
            CellStyle cellStyle = createCellStyle(workbook);

            int line = 2;
            for (int i = 0; i < data.size(); i++) {
                int columeIdx = 0;
                OnceGenerateBean onceGenerateBean = data.get(i);

                Row rowX = sheet.createRow(line);
//                createStringCell(rowX, line, createCellStyle(workbook), "文件名");

//                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, onceGenerateBean.getTitle());

//                rowX = sheet.createRow(line);
//                createStringCell(rowX, columeIdx + 1, createCellStyle(workbook), "文件总数");

//                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx + 1, cellStyle, onceGenerateBean.getSize() + "");

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, onceGenerateBean.getFqSize() + "位分区");

                columeIdx++;


                List<String> keys = new ArrayList<>(onceGenerateBean.getData7Place().keySet());

                keys.sort(String::compareTo);//排序

                for (int j = 0; j < keys.size(); j++) {
//                    Log.i("keys.get(i) = " + keys.get(j));
                    createStringCell(rowX, columeIdx, cellStyle, keys.get(j));
                    columeIdx++;
                }


                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "各分区总数");

                columeIdx++;
                for (String key :
                        keys) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
                    createStringCell(rowX, columeIdx, cellStyle, partitionBean.getCount() + "");

                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "中5数量");

                columeIdx++;
                for (String key :
                        keys) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
//                    rowX = sheet.createRow(line);
                    createStringCell(rowX, columeIdx, cellStyle, partitionBean.getZ5() + "");

                    //统计本分区中5数
                    if (!z5FQMap.containsKey(key)) {
                        z5FQMap.put(key, 0);
                    }
                    if (partitionBean.getZ5() > 0) {
                        z5FQMap.put(key, z5FQMap.get(key) + 1);
                    }
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "中4数量");

                columeIdx++;
                for (String key :
                        keys) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
//                    rowX = sheet.createRow(line);
                    createStringCell(rowX, columeIdx, cellStyle, partitionBean.getZ4() + "");

                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "中5、4占比");


                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "中3数量");

                columeIdx++;
                for (String key :
                        keys) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
//                    rowX = sheet.createRow(line);
                    createStringCell(rowX, columeIdx, cellStyle, partitionBean.getZ3() + "");

                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "中5、4、3占比");

                columeIdx++;
                for (String key :
                        keys) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
//                    rowX = sheet.createRow(line);
                    float percent = partitionBean.getCount() == 0 ? 0 : ((partitionBean.getZ5() + partitionBean.getZ4() + partitionBean.getZ3()) / (float) partitionBean.getCount()) * 100;
                    float percent2 = onceGenerateBean.getSize() == 0 ? 0 : ((onceGenerateBean.getZ5() + onceGenerateBean.getZ4() + onceGenerateBean.getZ3()) / (float) onceGenerateBean.getSize()) * 100;

                    //统计本分区中5数
                    if (!z345FQMap.containsKey(key)) {
                        z345FQMap.put(key, 0);
                    }

                    if (percent > percent2) {
                        createStringCell(rowX, columeIdx, createCellStyle2(workbook), String.format("%.1f", percent) + "%");
                        z345FQMap.put(key, z345FQMap.get(key) + 1);
                    } else {
                        createStringCell(rowX, columeIdx, cellStyle, String.format("%.1f", percent) + "%");
                    }

                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "中2数量");

                columeIdx++;
                for (String key :
                        keys) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
//                    rowX = sheet.createRow(line);
                    createStringCell(rowX, columeIdx, cellStyle, partitionBean.getZ2() + "");

                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "中1数量");

                columeIdx++;
                for (String key :
                        keys) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
//                    rowX = sheet.createRow(line);
                    createStringCell(rowX, columeIdx, cellStyle, partitionBean.getZ1() + "");

                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "合计占比");

                line++;

                columeIdx = 0;
                rowX = sheet.createRow(line);
                createStringCell(rowX, columeIdx, cellStyle, "合计占文件总数比");

                line++;

                line += 2;
            }


            List<String> z5Keys = new ArrayList<>(z5FQMap.keySet());
            z5Keys.sort(String::compareTo);//排序
            Row rowX = sheet.createRow(0);
            int z5ColumnIdx = 0;
            createStringCell(rowX, z5ColumnIdx, cellStyle, "分区中5个数");
            z5ColumnIdx++;

//            Log.i("z5Keys.size() = " + z5Keys.size());
            for (int i = 0; i < z5Keys.size(); i++) {
                if (z5FQMap.get(z5Keys.get(i)) > 0) {
                    createStringCell(rowX, z5ColumnIdx, createCellStyle3(workbook), z5FQMap.get(z5Keys.get(i)) + "");
                } else {
                    createStringCell(rowX, z5ColumnIdx, cellStyle, z5FQMap.get(z5Keys.get(i)) + "");
                }
                z5ColumnIdx++;
            }


            List<String> z345Keys = new ArrayList<>(z345FQMap.keySet());
            z345Keys.sort(String::compareTo);//排序
            rowX = sheet.createRow(1);
            int z345ColumnIdx = 0;
            createStringCell(rowX, z345ColumnIdx, cellStyle, "当前分区中345比例大于总数比例的期数");
            z345ColumnIdx++;

//            Log.i("z5Keys.size() = " + z5Keys.size());
            for (int i = 0; i < z345Keys.size(); i++) {
                if (z345FQMap.get(z345Keys.get(i)) > 0) {
                    createStringCell(rowX, z345ColumnIdx, createCellStyle4(workbook), z345FQMap.get(z345Keys.get(i)) + "");
                } else {
                    createStringCell(rowX, z345ColumnIdx, cellStyle, z345FQMap.get(z345Keys.get(i)) + "");
                }
                z345ColumnIdx++;
            }


            fos = new FileOutputStream(file);
            workbook.write(fos);//完成写入
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();

            }
            workbook.close();
        }
        return true;
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

    private static CellStyle createCellStyle2(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
//        style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.index);
        return style;
    }

    private static CellStyle createCellStyle3(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
//        style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.index);
        return style;
    }

    private static CellStyle createCellStyle4(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        return style;
    }

    private static void setCommonCellStyle(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(false);
    }

}
