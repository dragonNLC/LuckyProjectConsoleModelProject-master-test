package com.dragon.lucky.command18;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.dragon.lucky.utils.Log;
import jxl.Workbook;
import jxl.biff.FontRecord;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


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

            int line = 0;
            for (int i = 0; i < dataSet.size(); i++) {
                int columeIdx = 0;
                OnceGenerateBean data = dataSet.get(i);
                writableSheet.setColumnView(columeIdx, 4);//设置单元格宽度
                writableSheet.setRowView(line, 300);
                Label labelX = new Label(columeIdx, line, data.getTitle(), writableCellFormat);
                writableSheet.addCell(labelX);
                writableSheet.mergeCells(columeIdx, line, columeIdx + 6, line);
                line++;
                List<GenerateBean> generateData = data.getData();

                int tempColumnWidth = 0;


                Label label = null;
                for (int j = 0; j < generateData.size(); j++) {
                    int maxColumnSize = 0;

                    GenerateBean generate = generateData.get(j);
                    List<NumberDataBean> content = generate.getData();

                    for (int k = 0; k < content.size(); k++) {
                        maxColumnSize = Math.max(maxColumnSize, content.get(k).getData().length);
                    }

                    for (int k = 0; k < content.size(); k++) {
                        if (content.get(k).getData().length < maxColumnSize) {
                            byte[] byteData = new byte[maxColumnSize];
                            for (int l = 0; l < content.get(k).getData().length; l++) {
                                byteData[l] = content.get(k).getData()[l];
                            }
                            content.get(k).setData(byteData);

                            byte[] hitByteData = new byte[maxColumnSize];
                            for (int l = 0; l < content.get(k).getDataHitIdx().length; l++) {
                                hitByteData[l] = content.get(k).getDataHitIdx()[l];
                            }
                            content.get(k).setDataHitIdx(hitByteData);
                        }
                    }

                    for (int k = 0; k < content.size(); k++) {
                        NumberDataBean singleContent = content.get(k);
                        for (int l = 0; l < singleContent.getData().length; l++) {
                            writableSheet.setColumnView(columeIdx + l, 4);//设置单元格宽度
                            writableSheet.setRowView(line + k, 300);
                            if (singleContent.getDataHitIdx()[l] == 1) {
                                label = new Label(columeIdx + l, line + k, Byte.toString(singleContent.getData()[l]), getBackgroundColorWritable2());
                            } else {
                                if (singleContent.getData()[l] == 0) {
                                    label = new Label(columeIdx + l, line + k, "", writableCellFormat);
                                } else {
                                    label = new Label(columeIdx + l, line + k, Byte.toString(singleContent.getData()[l]), writableCellFormat);
                                }
                            }
                            writableSheet.addCell(label);
                        }
                        tempColumnWidth = singleContent.getData().length;

//                        Log.i("tempColumnWidth = " + tempColumnWidth);

                        writableSheet.setColumnView(columeIdx + tempColumnWidth, 10);//设置单元格宽度
                        writableSheet.setRowView(line + k, 300);
                        if (singleContent.getHitCount() == 4) {
                            label = new Label(columeIdx + tempColumnWidth, line + k, "中" + singleContent.getHitCount(), getBackgroundColorWritable());
                            writableSheet.addCell(label);
                        } else if (singleContent.getHitCount() == 3) {
                            label = new Label(columeIdx + tempColumnWidth, line + k, "中" + singleContent.getHitCount(), getBackgroundColorWritable5());
                            writableSheet.addCell(label);
                        } else if (singleContent.getHitCount() == 5) {
                            label = new Label(columeIdx + tempColumnWidth, line + k, "中" + singleContent.getHitCount(), getBackgroundColorWritable3());
                            writableSheet.addCell(label);
                        } else {
                            label = new Label(columeIdx + tempColumnWidth, line + k, "中" + singleContent.getHitCount(), writableCellFormat);
                            writableSheet.addCell(label);
                        }

                        writableSheet.setColumnView(columeIdx + tempColumnWidth + 1, 5);//设置单元格宽度
                        writableSheet.setRowView(line + k, 300);
                        label = new Label(columeIdx + tempColumnWidth + 1, line + k, "", getBackgroundColorWritable4());
                        writableSheet.addCell(label);
                    }

                    for (int l = 0; l < tempColumnWidth + 1; l++) {
                        writableSheet.setColumnView(columeIdx + l, 4);//设置单元格宽度
                        writableSheet.setRowView(line + generate.getGenerateSize(), 200);
                        label = new Label(columeIdx + l, line + generate.getGenerateSize(), "", getBackgroundColorWritable4());
                        writableSheet.addCell(label);
                    }


//                    writableSheet.setColumnView(columeIdx + tempColumnWidth + 1, 5);//设置单元格宽度
//                    writableSheet.setRowView(line + content.size(), 300);
//                    label = new Label(columeIdx + tempColumnWidth + 1, line + content.size(), "", getBackgroundColorWritable4());
//                    writableSheet.addCell(label);

                    for (int k = 0; k < generate.getGenerateSize() - content.size() + 2; k++) {
                        writableSheet.setColumnView(columeIdx + tempColumnWidth + 1, 2);//设置单元格宽度
                        if (k == generate.getGenerateSize() - content.size()) {
                            writableSheet.setRowView(line + content.size() + k, 200);
                        } else {
                            writableSheet.setRowView(line + content.size() + k, 300);
                        }
                        label = new Label(columeIdx + tempColumnWidth + 1, line + content.size() + k, "", getBackgroundColorWritable4());
                        writableSheet.addCell(label);
                    }
                    columeIdx += tempColumnWidth + 2;

                    /*writableSheet.setColumnView(columeIdx, 30);//设置单元格宽度
                    writableSheet.setRowView(line + 1, 300);
                    Label label = new Label(columeIdx, line + 1, "", writableCellFormat);
                    writableSheet.addCell(label);
                    writableSheet.setColumnView(columeIdx + 1, 10);//设置单元格宽度
                    writableSheet.setRowView(line + 1, 300);
                    label = new Label(columeIdx + 1, line + 1, "", writableCellFormat);
                    writableSheet.addCell(label);*/
//                    line++;

                    /*if (content.size() < generate.getGenerateSize()) {
                        for (int k = 0; k < generate.getGenerateSize() - content.size(); k++) {
                            writableSheet.setColumnView(columeIdx, 5);//设置单元格宽度
                            writableSheet.setRowView(line + k, 300);
                            label = new Label(columeIdx, line + k, "", writableCellFormat);
                            writableSheet.addCell(label);

                            writableSheet.setColumnView(columeIdx + 1, 10);//设置单元格宽度
                            writableSheet.setRowView(line + k, 300);
                            label = new Label(columeIdx + 1, line + k, "", writableCellFormat);
                            writableSheet.addCell(label);
                        }
                    }*/
//                    line += (generate.getGenerateSize() - content.size());
                }
                line += 6;
//                columeIdx += tempColumnWidth + 1;
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
        writableCellFormat.setBackground(Colour.RED);
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
