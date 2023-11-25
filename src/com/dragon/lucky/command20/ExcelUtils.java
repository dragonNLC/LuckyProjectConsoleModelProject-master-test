package com.dragon.lucky.command20;


import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.utils.FileReadHelper;
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
import java.util.ArrayList;
import java.util.Comparator;
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
            WritableCellFormat writableGreenBackgroundCellFormat = getBackgroundColorWritable2();//设置单元格格式
            WritableCellFormat writableCellFormat3 = getBackgroundColorWritable3();//设置单元格格式
            WritableCellFormat writableCellFormat4 = getBackgroundColorWritable4();//设置单元格格式
            writableWorkbook = Workbook.createWorkbook(new File(filePath));//生成一个Excel
//            WritableSheet writableSheet = writableWorkbook.createSheet(fileName, 0);//创建一个Sheet
            Label labelX = null;
            int columeIdx = 0;
            int sheetIdx = 0;
            WritableSheet writableSheet = null;
            for (int i = 0; i < dataSet.size(); i++) {
                if (i % 15 == 0) {
                    writableSheet = writableWorkbook.createSheet(fileName + "-" + sheetIdx, sheetIdx);//创建一个Sheet
                    sheetIdx++;
                    columeIdx = 0;
                }
                int line = 0;
                OnceGenerateBean data = dataSet.get(i);

                FileReadHelper.writeToFile(data.getPath(), data.getData());

                writableSheet.setColumnView(columeIdx, 3);//设置单元格宽度
                writableSheet.setRowView(line, 300);
                labelX = new Label(columeIdx, line, data.getTitle(), writableCellFormat);
                writableSheet.addCell(labelX);

                writableSheet.setColumnView(columeIdx, 3);//设置单元格宽度
                writableSheet.setRowView(line + 1, 300);
                labelX = new Label(columeIdx, line + 1, data.getData().size() + "", writableCellFormat3);
                writableSheet.addCell(labelX);

                writableSheet.setColumnView(columeIdx, 3);//设置单元格宽度
                writableSheet.setRowView(line + 2, 300);
                labelX = new Label(columeIdx, line + 2, data.getZ5() + "", writableCellFormat4);
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
                            labelX = new Label(columeIdx + 1 + k, j, bytes[k] + "", writableGreenBackgroundCellFormat);
                        }
                        writableSheet.addCell(labelX);
                    }
                }
                List<Byte> order = new ArrayList<>(data.getCollect().keySet());

                order.sort(new Comparator<Byte>() {
                    @Override
                    public int compare(Byte o1, Byte o2) {
                        return o1.compareTo(o2);
                    }
                });

                addColumnCount += 1;
                for (int j = 0; j < order.size(); j++) {
                    writableSheet.setColumnView(columeIdx + addColumnCount, 3);//设置单元格宽度
                    writableSheet.setColumnView(columeIdx + addColumnCount + 1, 3);//设置单元格宽度
                    writableSheet.setColumnView(columeIdx + addColumnCount + 2, 3);//设置单元格宽度
                    writableSheet.setColumnView(columeIdx + addColumnCount + 3, 3);//设置单元格宽度
                    writableSheet.setRowView(j, 300);
                    boolean exists = false;
                    for (int k = 0; k < data.getCheckData().length; k++) {
                        if (order.get(j) == data.getCheckData()[k]) {
//                            Log.i("order.get(j) == " + order.get(j));
//                            Log.i("data.getCheckData()[k] == " + data.getCheckData()[k]);
                            exists = true;
                            break;
                        }
                    }
//                    labelX = new Label(columeIdx + addColumnCount, j, order.get(j) + "", writableCellFormat);
//                    writableSheet.addCell(labelX);
                    if (!exists) {
                        labelX = new Label(columeIdx + addColumnCount + 1, j, order.get(j) + "", writableCellFormat);
                        writableSheet.addCell(labelX);
                        labelX = new Label(columeIdx + addColumnCount + 2, j, data.getCollect().get(order.get(j)) + "", writableCellFormat);
                        writableSheet.addCell(labelX);
                    } else {
                        labelX = new Label(columeIdx + addColumnCount + 1, j, order.get(j) + "", writableGreenBackgroundCellFormat);
                        writableSheet.addCell(labelX);
                        labelX = new Label(columeIdx + addColumnCount + 2, j, data.getCollect().get(order.get(j)) + "", writableGreenBackgroundCellFormat);
                        writableSheet.addCell(labelX);
                    }
//                    labelX = new Label(columeIdx + addColumnCount + 3, j, order.get(j) + "", writableCellFormat);
//                    writableSheet.addCell(labelX);
                }
                columeIdx += addColumnCount + 4;
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
