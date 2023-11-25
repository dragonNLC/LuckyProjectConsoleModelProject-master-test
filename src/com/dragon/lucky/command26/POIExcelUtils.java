package com.dragon.lucky.command26;


import com.dragon.lucky.utils.Log;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

            CellStyle cellStyle = createCellStyle(workbook);
            CellStyle cellGreenStyle = createCellStyle3(workbook);
            int columnIdx = 0;

            Map<Integer, Row> rowMap = new HashMap<>();

            for (int i = 0; i < data.size(); i++) {
                int line = 0;
                OnceGenerateBean onceGenerateBean = data.get(i);
                Row rowX = null;

                if (rowMap.get(line) == null) {
                    rowX = sheet.createRow(line);
                    rowMap.put(line, rowX);
                }
                rowX = rowMap.get(line);

                createStringCell(rowX, columnIdx, cellStyle, onceGenerateBean.getTitle());
                columnIdx++;

                for (int j = 0; j < onceGenerateBean.getData().size(); j++) {
                    columnIdx--;

                    line++;
                    AssistContentBean acv = onceGenerateBean.getData().get(j);
                    if (rowMap.get(line) == null) {
                        rowX = sheet.createRow(line);
                        rowMap.put(line, rowX);
                    }
                    rowX = rowMap.get(line);
                    CellStyle tempCellStyle = null;
                    if (acv.isCheckState()) {
                        tempCellStyle = cellGreenStyle;
                    } else {
                        tempCellStyle = cellStyle;
                    }
                    createStringCell(rowX, columnIdx, tempCellStyle, acv.getTag());
                    columnIdx++;
                    createStringCell(rowX, columnIdx, cellStyle, acv.getCount() + "");
                }
                columnIdx += 2;
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
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);
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
