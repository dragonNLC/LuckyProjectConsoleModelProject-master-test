package com.dragon.lucky.command19;


import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.TwoPoint;
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

    public static boolean outputFile(String filePath, String fileName, List<OnceGenerateBean> dataSet, List<TwoPoint> redNumber, List<TwoPoint> yellowNumber, List<TwoPoint> oriNumber, List<TwoPoint> blueNumber) {
        WritableWorkbook writableWorkbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                Log.i("filePath = " + filePath);
                file.createNewFile();
            }
            WritableCellFormat writableCellFormat = getWritable();//设置单元格格式
            WritableCellFormat greenWritableCellFormat = getBackgroundColorWritable2();//设置单元格格式
            WritableCellFormat redWritableCellFormat = getBackgroundColorWritable6();//设置单元格格式
            WritableCellFormat yellowWritableCellFormat = getBackgroundColorWritableYellow();//设置单元格格式
            WritableCellFormat oriWritableCellFormat = getBackgroundColorWritableOri();//设置单元格格式
            WritableCellFormat blueWritableCellFormat = getBackgroundColorWritableBlue();//设置单元格格式
            writableWorkbook = Workbook.createWorkbook(new File(filePath));//生成一个Excel
            WritableSheet writableSheet = writableWorkbook.createSheet(fileName, 0);//创建一个Sheet
            Label labelX = null;
            int columeIdx = 0;
            for (int i = 0; i < dataSet.size(); i++) {
                int line = 0;
                OnceGenerateBean data = dataSet.get(i);
                writableSheet.setColumnView(columeIdx, 6);//设置单元格宽度
                writableSheet.setRowView(line, 300);
                labelX = new Label(columeIdx, line, data.getTitle(), writableCellFormat);
                writableSheet.addCell(labelX);

                writableSheet.setColumnView(columeIdx, 6);//设置单元格宽度
                writableSheet.setRowView(line + 1, 300);
                labelX = new Label(columeIdx, line + 1, data.getData().size() + "", getBackgroundColorWritable3());
                writableSheet.addCell(labelX);

                writableSheet.setColumnView(columeIdx, 6);//设置单元格宽度
                writableSheet.setRowView(line + 2, 300);
                labelX = new Label(columeIdx, line + 2, data.getZ2() + "", getBackgroundColorWritable4());
                writableSheet.addCell(labelX);

                writableSheet.setColumnView(columeIdx, 6);//设置单元格宽度
                writableSheet.setRowView(line + 3, 300);
                labelX = new Label(columeIdx, line + 3, data.getZ1() + "", getBackgroundColorWritable5());
                writableSheet.addCell(labelX);

                int addColumnCount = 0;
                int addCollectCount = 0;

                List<ResultBean> resultBeans = data.getData();

                int maxCollectCount = 0;
                for (int j = 0; j < resultBeans.size(); j++) {
                    maxCollectCount = Math.max(maxCollectCount, resultBeans.get(j).getCollectCount());
                }
                for (int j = 0; j < resultBeans.size(); j++) {
                    byte[] bytes = resultBeans.get(j).getData();
                    addColumnCount = Math.max(addColumnCount, bytes.length);
                    byte[] hitBytes = resultBeans.get(j).getHitData();
                    for (int k = 0; k < bytes.length; k++) {
                        writableSheet.setColumnView(columeIdx + 1 + k, 3);//设置单元格宽度
                        writableSheet.setRowView(j, 300);
                        WritableCellFormat tempCellStyle = null;

                        if (hitBytes == null || hitBytes[k] == 0) {
                            tempCellStyle = writableCellFormat;
                        } else {
                            tempCellStyle = greenWritableCellFormat;
                        }
                        if (blueNumber != null) {
                            for (int l = 0; l < blueNumber.size(); l++) {
                                if (blueNumber.get(l).a == bytes[k]) {
                                    tempCellStyle = blueWritableCellFormat;
                                    break;
                                }
                                if (blueNumber.get(l).b == bytes[k]) {
                                    tempCellStyle = blueWritableCellFormat;
                                    break;
                                }
                            }
                        }
                        if (oriNumber != null) {
                            for (int l = 0; l < oriNumber.size(); l++) {
                                int checkCount = 0;
                                for (int m = 0; m < bytes.length; m++) {
                                    if (bytes[m] == oriNumber.get(l).a) {
                                        checkCount++;
                                    }
                                    if (bytes[m] == oriNumber.get(l).b) {
                                        checkCount++;
                                    }
                                }
                                if (checkCount > 1) {
                                    tempCellStyle = oriWritableCellFormat;
                                }
                            }
                        }
                        if (yellowNumber != null) {
                            for (int l = 0; l < yellowNumber.size(); l++) {
                                int checkCount = 0;
                                for (int m = 0; m < bytes.length; m++) {
                                    if (bytes[m] == yellowNumber.get(l).a) {
                                        checkCount++;
                                    }
                                    if (bytes[m] == yellowNumber.get(l).b) {
                                        checkCount++;
                                    }
                                }
                                if (checkCount > 1) {
                                    tempCellStyle = yellowWritableCellFormat;
                                }
                            }
                        }
                        if (redNumber != null) {
                            for (int l = 0; l < redNumber.size(); l++) {
                                int checkCount = 0;
                                for (int m = 0; m < bytes.length; m++) {
                                    if (bytes[m] == redNumber.get(l).a) {
                                        checkCount++;
                                    }
                                    if (bytes[m] == redNumber.get(l).b) {
                                        checkCount++;
                                    }
                                }
                                if (checkCount > 1) {
                                    tempCellStyle = redWritableCellFormat;
                                }
                                if (checkCount > 0 && redNumber.get(l).b == 0 && redNumber.get(l).a == bytes[k]) {
                                    tempCellStyle = redWritableCellFormat;
                                }
                            }
                        }
                        if (resultBeans.get(j).getCollectCount() == maxCollectCount) {
                            tempCellStyle = redWritableCellFormat;
                        }
                        labelX = new Label(columeIdx + 1 + k, j, bytes[k] + "", tempCellStyle);
                        writableSheet.addCell(labelX);
                    }
//                    Log.i("data.getData2() = " + (data.getData2() == null));
//                    Log.i("data.getData2().size() = " + (data.getData2().size()));
                    if (data.getData2() != null && data.getData2().size() > 0) {
                        writableSheet.setColumnView(columeIdx + 1 + bytes.length, 3);//设置单元格宽度
                        writableSheet.setRowView(j, 300);
                        if (resultBeans.get(j).getCollectCount() == 0) {
                            labelX = new Label(columeIdx + 1 + bytes.length, j, resultBeans.get(j).getCollectCount() + "", getBackgroundColorWritable6());
                        } else {
                            labelX = new Label(columeIdx + 1 + bytes.length, j, resultBeans.get(j).getCollectCount() + "", getBackgroundColorWritable4());
                        }
                        writableSheet.addCell(labelX);
                        addCollectCount = 2;

                        writableSheet.setColumnView(columeIdx + 2 + bytes.length, 3);//设置单元格宽度
                        writableSheet.setRowView(j, 300);
                        labelX = new Label(columeIdx + 2 + bytes.length, j, "", writableCellFormat);
                        writableSheet.addCell(labelX);
                    }
                }

                int lineAddCount = 0;
                if (resultBeans.size() < 4) {
                    lineAddCount = 4;
                } else {
                    lineAddCount = resultBeans.size();
                }


                for (int j = 0; j < data.getData2().size(); j++) {

                }

                if (data.getData2() != null && data.getData2().size() > 0) {

                    writableSheet.setColumnView(columeIdx, 6);//设置单元格宽度
                    writableSheet.setRowView(lineAddCount + 1, 300);
                    labelX = new Label(columeIdx, lineAddCount + 1, data.getData2().size() + "", getBackgroundColorWritable3());
                    writableSheet.addCell(labelX);

                    writableSheet.setColumnView(columeIdx, 6);//设置单元格宽度
                    writableSheet.setRowView(lineAddCount + 2, 300);
                    labelX = new Label(columeIdx, lineAddCount + 2, data.getZ2() + "", getBackgroundColorWritable4());
                    writableSheet.addCell(labelX);

                    writableSheet.setColumnView(columeIdx, 6);//设置单元格宽度
                    writableSheet.setRowView(lineAddCount + 3, 300);
                    labelX = new Label(columeIdx, lineAddCount + 3, data.getZ1() + "", getBackgroundColorWritable5());
                    writableSheet.addCell(labelX);

                    List<ResultBean> resultBeans2 = data.getData2();
                    maxCollectCount = 0;
                    for (int j = 0; j < resultBeans2.size(); j++) {
                        maxCollectCount = Math.max(maxCollectCount, resultBeans2.get(j).getCollectCount());
                    }
                    for (int j = 0; j < resultBeans2.size(); j++) {
                        byte[] bytes = resultBeans2.get(j).getData();
                        addColumnCount = Math.max(addColumnCount, bytes.length);
                        byte[] hitBytes = resultBeans2.get(j).getHitData();
                        for (int k = 0; k < bytes.length; k++) {
                            writableSheet.setColumnView(columeIdx + 1 + k, 3);//设置单元格宽度
                            writableSheet.setRowView(j + lineAddCount + 1, 300);
                            WritableCellFormat tempCellStyle = null;
                            if (hitBytes == null || hitBytes[k] == 0) {
                                tempCellStyle = writableCellFormat;
                            } else {
                                tempCellStyle = greenWritableCellFormat;
                            }
                            if (blueNumber != null) {
                                for (int l = 0; l < blueNumber.size(); l++) {
                                    if (blueNumber.get(l).a == bytes[k]) {
                                        tempCellStyle = blueWritableCellFormat;
                                        break;
                                    }
                                    if (blueNumber.get(l).b == bytes[k]) {
                                        tempCellStyle = blueWritableCellFormat;
                                        break;
                                    }
                                }
                            }
                            if (oriNumber != null) {
                                for (int l = 0; l < oriNumber.size(); l++) {
                                    int checkCount = 0;
                                    for (int m = 0; m < bytes.length; m++) {
                                        if (bytes[m] == oriNumber.get(l).a) {
                                            checkCount++;
                                        }
                                        if (bytes[m] == oriNumber.get(l).b) {
                                            checkCount++;
                                        }
                                    }
                                    if (checkCount > 1) {
                                        tempCellStyle = oriWritableCellFormat;
                                    }
                                }
                            }
                            if (yellowNumber != null) {
                                for (int l = 0; l < yellowNumber.size(); l++) {
                                    int checkCount = 0;
                                    for (int m = 0; m < bytes.length; m++) {
                                        if (bytes[m] == yellowNumber.get(l).a) {
                                            checkCount++;
                                        }
                                        if (bytes[m] == yellowNumber.get(l).b) {
                                            checkCount++;
                                        }
                                    }
                                    if (checkCount > 1) {
                                        tempCellStyle = yellowWritableCellFormat;
                                    }
                                }
                            }
                            if (redNumber != null) {
                                for (int l = 0; l < redNumber.size(); l++) {
                                    int checkCount = 0;
                                    for (int m = 0; m < bytes.length; m++) {
                                        if (bytes[m] == redNumber.get(l).a) {
                                            checkCount++;
                                        }
                                        if (bytes[m] == redNumber.get(l).b) {
                                            checkCount++;
                                        }
                                    }
                                    if (checkCount > 1) {
                                        tempCellStyle = redWritableCellFormat;
                                    }
                                    if (checkCount > 0 && redNumber.get(l).b == 0 && redNumber.get(l).a == bytes[k]) {
                                        tempCellStyle = redWritableCellFormat;
                                    }
                                }
                            }

                            if (resultBeans2.get(j).getCollectCount() == maxCollectCount) {
                                tempCellStyle = redWritableCellFormat;
                            }

                            labelX = new Label(columeIdx + 1 + k, j + lineAddCount + 1, bytes[k] + "", tempCellStyle);

                            writableSheet.addCell(labelX);
                        }
                        if (data.getData2() != null && data.getData2().size() > 0) {
                            writableSheet.setColumnView(columeIdx + 1 + bytes.length, 3);//设置单元格宽度
                            writableSheet.setRowView(j + resultBeans.size() + 1, 300);
                            if (resultBeans2.get(j).getCollectCount() == 0) {
                                labelX = new Label(columeIdx + 1 + bytes.length, j + lineAddCount + 1, resultBeans2.get(j).getCollectCount() + "", getBackgroundColorWritable6());
                            } else {
                                labelX = new Label(columeIdx + 1 + bytes.length, j + lineAddCount + 1, resultBeans2.get(j).getCollectCount() + "", getBackgroundColorWritable4());
                            }
                            writableSheet.addCell(labelX);
                            addCollectCount = 2;
                        }
                    }
                }

                columeIdx += addColumnCount + 1 + addCollectCount;
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

    private static WritableCellFormat getBackgroundColorWritable6() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.RED);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritableYellow() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.YELLOW);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritableOri() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.ORANGE);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritableBlue() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.SKY_BLUE);
        return writableCellFormat;
    }

}
