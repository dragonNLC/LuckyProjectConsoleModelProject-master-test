package com.dragon.lucky.command23;


import com.dragon.lucky.command21.OnceGenerateBean;
import com.dragon.lucky.command21.PartitionBean;
import com.dragon.lucky.utils.Log;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by lb on 2018/9/11.
 */

public class ExcelUtils {

    public static boolean write2File(String output, List<AssistContentBean> data, int rowItem) {
        WritableWorkbook writableWorkbook = null;
        try {
            File file = new File(output);
            if (!file.exists()) {
                Log.i("输出路径 = " + output);
                file.createNewFile();
            }
            WritableCellFormat writableCellFormat = getWritable();//设置单元格格式
            writableWorkbook = Workbook.createWorkbook(new File(output));//生成一个Excel
            WritableSheet writableSheet = writableWorkbook.createSheet(new File(output).getName(), 0);//创建一个Sheet
            Label labelX = null;

            int columeIdx = 0;
            writableSheet.setColumnView(columeIdx, 10);//设置单元格宽度
            labelX = new Label(columeIdx, 0, "", getBackgroundColorWritable4());
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 1, "总数", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 3, "中5", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 4, "", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 5, "中4", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 6, "", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 7, "中3", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 8, "345占比", getBackgroundColorWritable());
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 9, "中2", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 10, "2345占比", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 11, "中1", writableCellFormat);
            writableSheet.addCell(labelX);
            labelX = new Label(columeIdx, 12, "总占比", writableCellFormat);
            writableSheet.addCell(labelX);
            Log.i("data.size() = " + data.size());
            columeIdx += 1;

            int skipLine = 0;

            for (int i = 0; i < data.size(); i++) {
                int line = skipLine;
                AssistContentBean onceGenerateBean = data.get(i);
                writableSheet.setColumnView(columeIdx, 10);//设置单元格宽度
//                writableSheet.setRowView(line, 600);

                labelX = new Label(columeIdx, line, onceGenerateBean.title, getBackgroundColorWritable4());
                writableSheet.addCell(labelX);
                line++;

                labelX = new Label(columeIdx, line, onceGenerateBean.size + "", writableCellFormat);
                writableSheet.addCell(labelX);
                line += 2;

                labelX = new Label(columeIdx, line, onceGenerateBean.z5 + "", writableCellFormat);
                writableSheet.addCell(labelX);
                line += 2;

                labelX = new Label(columeIdx, line, onceGenerateBean.z4 + "", writableCellFormat);
                writableSheet.addCell(labelX);
                line += 2;

                labelX = new Label(columeIdx, line, onceGenerateBean.z3 + "", writableCellFormat);
                writableSheet.addCell(labelX);
                line++;

                float percent = ((onceGenerateBean.z5 + onceGenerateBean.z4 + onceGenerateBean.z3) / (float) onceGenerateBean.size) * 100;

                labelX = new Label(columeIdx, line, String.format("%.1f", percent) + "%", getBackgroundColorWritable());
                writableSheet.addCell(labelX);
                line++;

                labelX = new Label(columeIdx, line, onceGenerateBean.z2 + "", writableCellFormat);
                writableSheet.addCell(labelX);
                line++;

                percent = ((onceGenerateBean.z5 + onceGenerateBean.z4 + onceGenerateBean.z3 + onceGenerateBean.z2) / (float) onceGenerateBean.size) * 100;
                labelX = new Label(columeIdx, line, String.format("%.1f", percent) + "%", writableCellFormat);
                writableSheet.addCell(labelX);
                line++;

                labelX = new Label(columeIdx, line, onceGenerateBean.z1 + "", writableCellFormat);
                writableSheet.addCell(labelX);
                line++;

                percent = ((onceGenerateBean.z5 + onceGenerateBean.z4 + onceGenerateBean.z3 + onceGenerateBean.z2 + onceGenerateBean.z1) / (float) onceGenerateBean.size) * 100;
                labelX = new Label(columeIdx, line, String.format("%.1f", percent) + "%", writableCellFormat);
                writableSheet.addCell(labelX);
//                Log.i("line1111 = " + line);

                columeIdx++;

                if (i != 0 && ((i + 1) % (rowItem)) == 0 && i != data.size() - 1) {
                    skipLine = skipLine + 12 + 2;
                    Log.i("columeIdx = " + columeIdx + " i = " + i);
                    Log.i("(i % rowItem - 1) = " + (i % rowItem));
//                    Log.i("skipLine = " + skipLine);
//                    Log.i("line = " + line);

                    columeIdx = 0;
                    labelX = new Label(columeIdx, skipLine, "", getBackgroundColorWritable4());
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 1, "总数", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 3, "中5", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 4, "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 5, "中4", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 6, "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 7, "中3", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 8, "345占比", getBackgroundColorWritable());
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 9, "中2", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 10, "2345占比", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 11, "中1", writableCellFormat);
                    writableSheet.addCell(labelX);
                    labelX = new Label(columeIdx, skipLine + 12, "总占比", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx += 1;
                }
            }
            writableWorkbook.write();//完成写入

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writableWorkbook != null) {
                try {
                    writableWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
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
