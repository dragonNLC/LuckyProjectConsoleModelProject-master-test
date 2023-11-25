package com.dragon.lucky.command17;


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

    public static boolean write2File(String output, List<OnceGenerateBean> data) {
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
            int line = 0;
            for (int i = 0; i < data.size(); i++) {
                int columeIdx = 0;
                OnceGenerateBean onceGenerateBean = data.get(i);
                writableSheet.setColumnView(columeIdx, 24);//设置单元格宽度
//                writableSheet.setRowView(line, 600);

                labelX = new Label(columeIdx, line, "文件名", writableCellFormat);
                writableSheet.addCell(labelX);
                labelX = new Label(columeIdx + 1, line, onceGenerateBean.getTitle(), writableCellFormat);
                writableSheet.addCell(labelX);

                labelX = new Label(columeIdx + 2, line, "文件总数", writableCellFormat);
                writableSheet.addCell(labelX);
                labelX = new Label(columeIdx + 3, line, onceGenerateBean.getSize() + "", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "7位分区", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData7Place().keySet()) {
                    labelX = new Label(columeIdx, line, key, writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "各分区总数", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData7Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getCount() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData7Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ5() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中4数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData7Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ4() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5、4占比", writableCellFormat);
                writableSheet.addCell(labelX);


                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中3数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData7Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ3() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5、4、3占比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中2数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData7Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ2() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中1数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData7Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData7Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ1() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "合计占比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "合计占文件总数比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "6位分区", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData6Place().keySet()) {
                    labelX = new Label(columeIdx, line, key, writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "各分区总数", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData6Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData6Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getCount() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData6Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData6Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ5() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中4数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData6Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData6Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ4() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5、4占比", writableCellFormat);
                writableSheet.addCell(labelX);


                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中3数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData6Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData6Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ3() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5、4、3占比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中2数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData6Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData6Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ2() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中1数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData6Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData6Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ1() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "合计占比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "合计占文件总数比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "5位分区", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData5Place().keySet()) {
                    labelX = new Label(columeIdx, line, key, writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "各分区总数", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData5Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData5Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getCount() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData5Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData5Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ5() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中4数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData5Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData5Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ4() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5、4占比", writableCellFormat);
                writableSheet.addCell(labelX);


                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中3数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData5Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData5Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ3() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中5、4、3占比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中2数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData5Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData5Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ2() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "中1数量", writableCellFormat);
                writableSheet.addCell(labelX);
                columeIdx++;
                for (String key :
                        onceGenerateBean.getData5Place().keySet()) {
                    PartitionBean partitionBean = onceGenerateBean.getData5Place().get(key);
                    labelX = new Label(columeIdx, line, partitionBean.getZ1() + "", writableCellFormat);
                    writableSheet.addCell(labelX);
                    columeIdx++;
                }

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "合计占比", writableCellFormat);
                writableSheet.addCell(labelX);

                line++;

                columeIdx = 0;
                labelX = new Label(columeIdx, line, "合计占文件总数比", writableCellFormat);
                writableSheet.addCell(labelX);

                line += 2;
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
