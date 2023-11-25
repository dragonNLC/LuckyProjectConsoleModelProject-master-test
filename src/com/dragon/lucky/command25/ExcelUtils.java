package com.dragon.lucky.command25;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dragon.lucky.utils.Log;
import jxl.Workbook;
import jxl.biff.FontRecord;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.Label;


/**
 * Created by lb on 2018/9/11.
 */

public class ExcelUtils {

    public static Map<String, Integer> MONEY_MAP = new HashMap<>();

    static {
        MONEY_MAP.put("7-0", -42);
        MONEY_MAP.put("7-1", -42);
        MONEY_MAP.put("7-2", -42);
        MONEY_MAP.put("7-3", -12);
        MONEY_MAP.put("7-4", 318);
        MONEY_MAP.put("7-5", 11008);

        MONEY_MAP.put("8-0", -112);
        MONEY_MAP.put("8-1", -112);
        MONEY_MAP.put("8-2", -112);
        MONEY_MAP.put("8-3", -62);
        MONEY_MAP.put("8-4", 408);
        MONEY_MAP.put("8-5", 11538);

        MONEY_MAP.put("9-0", -252);
        MONEY_MAP.put("9-1", -252);
        MONEY_MAP.put("9-2", -252);
        MONEY_MAP.put("9-3", -177);
        MONEY_MAP.put("9-4", 448);
        MONEY_MAP.put("9-5", 12048);

        MONEY_MAP.put("10-0", -504);
        MONEY_MAP.put("10-1", -504);
        MONEY_MAP.put("10-2", -504);
        MONEY_MAP.put("10-3", -399);
        MONEY_MAP.put("10-4", 396);
        MONEY_MAP.put("10-5", 12496);

        MONEY_MAP.put("11-0", -924);
        MONEY_MAP.put("11-1", -924);
        MONEY_MAP.put("11-2", -924);
        MONEY_MAP.put("11-3", -784);
        MONEY_MAP.put("11-4", 196);
        MONEY_MAP.put("11-5", 12826);

        MONEY_MAP.put("12-0", -1584);
        MONEY_MAP.put("12-1", -1584);
        MONEY_MAP.put("12-2", -1584);
        MONEY_MAP.put("12-3", -1404);
        MONEY_MAP.put("12-4", -224);
        MONEY_MAP.put("12-5", 12966);

        MONEY_MAP.put("13-0", -2574);
        MONEY_MAP.put("13-1", -2574);
        MONEY_MAP.put("13-2", -2574);
        MONEY_MAP.put("13-3", -2349);
        MONEY_MAP.put("13-4", -954);
        MONEY_MAP.put("13-5", 12826);

        MONEY_MAP.put("14-0", -4004);
        MONEY_MAP.put("14-1", -4004);
        MONEY_MAP.put("14-2", -4004);
        MONEY_MAP.put("14-3", -3729);
        MONEY_MAP.put("14-4", -2104);
        MONEY_MAP.put("14-5", 12296);

        MONEY_MAP.put("15-0", -6006);
        MONEY_MAP.put("15-1", -6006);
        MONEY_MAP.put("15-2", -6006);
        MONEY_MAP.put("15-3", -5676);
        MONEY_MAP.put("15-4", -3806);
        MONEY_MAP.put("15-5", 11244);
    }

    public static boolean outputFile(String filePath, String fileName, List<OnceGenerateBean> dataSet) {
        WritableWorkbook writableWorkbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                Log.i("filePath = " + filePath);
                file.createNewFile();
            }
            WritableCellFormat writableCellFormat = getWritable();//设置单元格格式
            WritableCellFormat writableCellFormatBolder = getWritableBolder();//设置单元格格式
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
                            WritableCellFormat format = null;
                            Log.i(i + "-" + j + "-" + k + "-" + l);
                            if (singleContent.getDataHitIdx()[l] == 1) {
                                if (singleContent.getDeprecatedDataIdx()[l] == 1) {
                                    format = getBackgroundColorWritable2DeLine();
                                    if (singleContent.getDataPosMap().get(j + "-" + k)[l] == 1) {
                                        ((WritableFont) format.getFont()).setBoldStyle(WritableFont.BOLD);
                                    }
                                    label = new Label(columeIdx + l, line + k, Byte.toString(singleContent.getData()[l]), format);
                                } else {
                                    format = getBackgroundColorWritable2();
                                    if (singleContent.getDataPosMap().get(j + "-" + k) != null && singleContent.getDataPosMap().get(j + "-" + k)[l] == 1) {
                                        ((WritableFont) format.getFont()).setBoldStyle(WritableFont.BOLD);
                                    }
                                    label = new Label(columeIdx + l, line + k, Byte.toString(singleContent.getData()[l]), format);
                                }
                            } else {
                                if (singleContent.getDeprecatedDataIdx()[l] == 1) {
                                    if (singleContent.getData()[l] == 0) {
                                        format = getWritable2DeLine();
                                        if (singleContent.getDataPosMap().get(j + "-" + k)[l] == 1) {
                                            ((WritableFont) format.getFont()).setBoldStyle(WritableFont.BOLD);
                                        }
                                        label = new Label(columeIdx + l, line + k, "", format);
                                    } else {
                                        format = getWritable2DeLine();
                                        if (singleContent.getDataPosMap().get(j + "-" + k)[l] == 1) {
                                            ((WritableFont) format.getFont()).setBoldStyle(WritableFont.BOLD);
                                        }
                                        label = new Label(columeIdx + l, line + k, Byte.toString(singleContent.getData()[l]), format);
                                    }
                                } else {
                                    if (singleContent.getData()[l] == 0) {
                                        format = writableCellFormat;
                                        if (singleContent.getDataPosMap().get(j + "-" + k)[l] == 1) {
                                            format = writableCellFormatBolder;
                                        }
                                        label = new Label(columeIdx + l, line + k, "", format);
                                    } else {
                                        format = writableCellFormat;
                                        if (singleContent.getDataPosMap().get(j + "-" + k) != null && singleContent.getDataPosMap().get(j + "-" + k)[l] == 1) {
                                            format = writableCellFormatBolder;
                                        }
                                        label = new Label(columeIdx + l, line + k, Byte.toString(singleContent.getData()[l]), format);
                                    }
                                }
                            }
                            writableSheet.addCell(label);
                        }
                        tempColumnWidth = singleContent.getData().length;

//                        Log.i("tempColumnWidth = " + tempColumnWidth);

                        writableSheet.setColumnView(columeIdx + tempColumnWidth, 10);//设置单元格宽度
                        writableSheet.setRowView(line + k, 300);
                        /*if (singleContent.getHitCount() == 4) {
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
                        }*/
                        if (singleContent.getHitCount() == 4) {
                            label = new Label(columeIdx + tempColumnWidth, line + k, MONEY_MAP.get(singleContent.getData().length + "-" + singleContent.getHitCount()) + "", getBackgroundColorWritable());
                            writableSheet.addCell(label);
                        } else if (singleContent.getHitCount() == 3) {
                            label = new Label(columeIdx + tempColumnWidth, line + k, MONEY_MAP.get(singleContent.getData().length + "-" + singleContent.getHitCount()) + "", getBackgroundColorWritable5());
                            writableSheet.addCell(label);
                        } else if (singleContent.getHitCount() == 5) {
                            label = new Label(columeIdx + tempColumnWidth, line + k, MONEY_MAP.get(singleContent.getData().length + "-" + singleContent.getHitCount()) + "", getBackgroundColorWritable3());
                            writableSheet.addCell(label);
                        } else {
                            label = new Label(columeIdx + tempColumnWidth, line + k, MONEY_MAP.get(singleContent.getData().length + "-" + singleContent.getHitCount()) + "", writableCellFormat);
                            writableSheet.addCell(label);
                        }
                        tempColumnWidth += 1;

                        writableSheet.setColumnView(columeIdx + tempColumnWidth, 10);//设置单元格宽度
                        writableSheet.setRowView(line + k, 300);
                        label = new Label(columeIdx + tempColumnWidth, line + k, String.format("%.2f", (singleContent.getZ345Percent() * 100)), writableCellFormat);
                        writableSheet.addCell(label);


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

        WritableFont font = new WritableFont(writableCellFormat.getFont());
        writableCellFormat.setFont(font);
        return writableCellFormat;
    }

    private static WritableCellFormat getWritableBolder() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);

        WritableFont font = new WritableFont(writableCellFormat.getFont());
        font.setBoldStyle(WritableFont.BOLD);
        writableCellFormat.setFont(font);
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

        WritableFont font = new WritableFont(writableCellFormat.getFont());
        writableCellFormat.setFont(font);

        return writableCellFormat;
    }

    private static WritableCellFormat getWritable2DeLine() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);

        WritableFont font = new WritableFont(writableCellFormat.getFont());
        font.setStruckout(true);
        writableCellFormat.setFont(font);
        return writableCellFormat;
    }

    private static WritableCellFormat getBackgroundColorWritable2DeLine() throws WriteException {
        WritableCellFormat writableCellFormat = new WritableCellFormat();
        writableCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        writableCellFormat.setAlignment(Alignment.CENTRE);
        writableCellFormat.setBackground(Colour.BRIGHT_GREEN);

        WritableFont font = new WritableFont(writableCellFormat.getFont());
        font.setStruckout(true);

        writableCellFormat.setFont(font);
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
