package com.dragon.lucky.command28;

import com.dragon.lucky.utils.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class POIExcelUtils {

    public static void output(String path, List<CollectBean> data, List<String> inputs) {
//先把格式列出来
        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                Log.i("输出路径 = " + path);
                file.createNewFile();
            }
            XSSFSheet sheet = workbook.createSheet(new File(path).getName());
            sheet.setDefaultColumnWidth(3);
            CellStyle cellStyle = createCellStyle(workbook);
            CellStyle cellGreenStyle = createGreenCellStyle(workbook);
            CellStyle cellBlueStyle = createBlueCellStyle(workbook);
            CellStyle cellRedStyle = createRedCellStyle(workbook);

            Row rowX = sheet.createRow(0);

            for (int i = 0; i < inputs.size(); i++) {
                createStringCell(rowX, 3 + i, cellStyle, new File(inputs.get(i)).getName().split("\\.")[0]);
            }

            for (int i = 0; i < data.size(); i++) {
                CollectBean cb = data.get(i);
                rowX = sheet.createRow(i + 1);
                createStringCell(rowX, 0, cellStyle, cb.tag);
                createStringCell(rowX, 1, cellStyle, cb.count + "");
                for (int j = 0; j < inputs.size(); j++) {
                    if (cb.files.contains(inputs.get(j))) {
                        CellStyle tempStyle = null;
                        if (cb.orm.get(inputs.get(j)).tag == 1) {
                            tempStyle = cellRedStyle;
                        } else if (cb.orm.get(inputs.get(j)).tag == 2) {
                            tempStyle = cellBlueStyle;
                        } else {
                            tempStyle = cellGreenStyle;
                        }
                        createStringCell(rowX, j + 1 + 1 + 1, tempStyle, cb.orm.get(inputs.get(j)).count + "");
                    }
                }
            }
            fos = new FileOutputStream(file);
            workbook.write(fos);//完成写入
            fos.flush();
            fos.close();
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

    private static void setCommonCellStyle(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(false);
    }

    private static CellStyle createGreenCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN1.index);
        return style;
    }

    private static CellStyle createBlueCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
        return style;
    }

    private static CellStyle createRedCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        setCommonCellStyle(style);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.RED.index);
        return style;
    }

}
