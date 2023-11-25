package com.dragon.lucky.command24;


import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.Log;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


/**
 * Created by lb on 2018/9/11.
 */

public class ExcelUtils {

    public static boolean outputFile(String filePath, String fileName, List<OnceGenerateBean> dataSet) {
        WritableWorkbook writableWorkbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                Log.i("filePath = " + filePath);
                file.createNewFile();
            }
            WritableCellFormat writableCellFormat = getWritable();//设置单元格格式
            writableWorkbook = Workbook.createWorkbook(new File(filePath));//生成一个Excel
            WritableSheet writableSheet = writableWorkbook.createSheet(fileName, 0);//创建一个Sheet
            Label labelX = null;
            int columeIdx = 0;
            for (int i = 0; i < dataSet.size(); i++) {
                int line = 0;
                OnceGenerateBean data = dataSet.get(i);
                writableSheet.setColumnView(columeIdx, 3);//设置单元格宽度
                writableSheet.setRowView(line, 300);
                labelX = new Label(columeIdx, line, data.getTitle(), writableCellFormat);
                writableSheet.addCell(labelX);

                writableSheet.setColumnView(columeIdx, 3);//设置单元格宽度
                writableSheet.setRowView(line + 1, 300);
                labelX = new Label(columeIdx, line + 1, data.getData().size() + "", getBackgroundColorWritable3());
                writableSheet.addCell(labelX);

                writableSheet.setColumnView(columeIdx, 3);//设置单元格宽度
                writableSheet.setRowView(line + 2, 300);
                labelX = new Label(columeIdx, line + 2, data.getZ5() + "", getBackgroundColorWritable4());
                writableSheet.addCell(labelX);


                int addColumnCount = 0;

                for (int j = 0; j < data.getData().size(); j++) {
                    List<ResultBean> resultBeans = data.getData();
                    byte[] bytes = resultBeans.get(j).getData();
                    addColumnCount = Math.max(addColumnCount, bytes.length);
                    byte[] hitBytes = resultBeans.get(j).getHitData();
                    for (int k = 0; k < bytes.length; k++) {
                        writableSheet.setColumnView(columeIdx + 1 + k, 3);//设置单元格宽度
                        writableSheet.setRowView(j, 300);
                        if (hitBytes[k] == 0) {
                            labelX = new Label(columeIdx + 1 + k, j, bytes[k] + "", writableCellFormat);
                        } else {
                            labelX = new Label(columeIdx + 1 + k, j, bytes[k] + "", getBackgroundColorWritable2());
                        }
                        writableSheet.addCell(labelX);
                    }
                }
                columeIdx += addColumnCount + 1;
            }
            writableWorkbook.write();//完成写入
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writableWorkbook != null) {
                Log.i("writableWorkbook = excel生成成功！");
                try {
                    writableWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static WritableCellFormat getWritable() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritable() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.LIGHT_ORANGE);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritable2() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.BRIGHT_GREEN);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritable3() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.PINK2);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritable4() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.GRAY_25);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritable5() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.SKY_BLUE);
        return writableCellFormat;
    }

}
