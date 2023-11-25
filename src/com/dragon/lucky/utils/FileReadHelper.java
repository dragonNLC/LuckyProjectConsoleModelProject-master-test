package com.dragon.lucky.utils;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultBean2;
import com.dragon.lucky.command13.NumberBean;
import com.dragon.lucky.command15.NumberDataBean;
import com.dragon.lucky.command18.ReadCallBackListener;
import com.dragon.lucky.command28.CollectBean;
import com.dragon.lucky.command7.ExistsDataBean;
import com.dragon.lucky.command8.GenerateBaseDataBean;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileReadHelper {

    public static long readFileLen(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        long len = fis.available();
        Log.i("len = " + len);
        fis.close();
        return len;
    }

    public static List<String> readFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        if (fis.available() > 1024 * 1024 * 1024) {
            fis.close();
            return readFileForLine(path);
        } else {
            fis.close();
            return readFileWithAll(path);
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public static List<String> readFileWithLimit(String path, int limit, ReadCallBackListener readCallBackListener) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        List<String> contents = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        long i = 0;
        long lastRead = 0;
        while (reader.ready()) {
            if (i >= lastRead + limit) {
                Log.i("已读至：" + i + "行，正在进行处理！");
                //表示已经读到结束了
                lastRead = i;
                if (readCallBackListener != null) {
                    readCallBackListener.onResultGroup(contents);
                }
                contents.clear();
                contents = new ArrayList<>();
            }
            String line = reader.readLine();
            String content = line.trim();
            if (content.contains("#")) {
                if (content.indexOf("#") != content.lastIndexOf("#")) {
                    Log.i("content  " + content);
                    String[] splitContent = content.split("#");
                    if (splitContent.length > 2) {
                        contents.add(content.split("#")[2]);
                    }
                } else {
                    contents.add(content.split("#")[0]);
                }
            } else {
                contents.add(content);
            }
            i++;
            if (i % 100000 == 0) {
                Log.i("i = " + i);
            }
        }
        if (readCallBackListener != null) {
            Log.i("已读至：" + i + "行，正在进行处理！");
            readCallBackListener.onResultGroup(contents);
        }
        Log.i("---------------读取结束！");
//        Log.i("---------------读取结束，本次读取开始位置：" + idx * limit + "---------------");
        fis.close();
        reader.close();
        return contents;
    }

    /*public static List<String> readFileWithLimit(String path, int idx, int limit) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        List<String> contents = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        long i = 0;
        while (reader.ready()) {
            if (i < (idx * limit)) {
                continue;
            }
            if (i > (idx + 1) * limit) {
                //表示已经读到结束了
                break;
            }
            String line = reader.readLine();
            String content = line.trim();
            if (content.contains("#")) {
                if (content.indexOf("#") != content.lastIndexOf("#")) {
                    Log.i("content  " + content);
                    String[] splitContent = content.split("#");
                    if (splitContent.length > 2) {
                        contents.add(content.split("#")[2]);
                    }
                } else {
                    contents.add(content.split("#")[0]);
                }
            } else {
                contents.add(content);
            }
            i++;
            if (i % 100000 == 0) {
                Log.i("i = " + i);
            }
        }
        Log.i("---------------读取结束，本次读取开始位置：" + idx * limit + "---------------");
        fis.close();
        reader.close();
        return contents;
    }*/

    public static List<String> readFileWithAll(String path) throws IOException {
//        Log.i("path = " + path);
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        int len = 0;
        byte[] buffer = new byte[fis.available()];
        StringBuilder sb = new StringBuilder();
        while ((len = fis.read(buffer)) > 0) {
            sb.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
        }
        List<String> contents = new ArrayList<>();
        String[] contentArr = sb.toString().split("\n");
        if (contentArr.length > 0) {
            for (int i = 0; i < contentArr.length; i++) {
                String content = contentArr[i].trim();
                if (content.contains("#")) {
                    if (content.indexOf("#") != content.lastIndexOf("#")) {
                        Log.i("content  " + content);
                        String[] splitContent = content.split("#");
                        if (splitContent.length > 2) {
                            contents.add(content.split("#")[2]);
                        }
                    } else {
                        contents.add(content.split("#")[0]);
                    }
                } else {
                    contents.add(content);
                }
            }
        }
        return contents;
    }

    public static List<String> readFileForLine(String path) throws IOException {
//        Log.i("path = " + path);
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        /*int len = 0;
        byte[] buffer = new byte[fis.available()];
        StringBuilder sb = new StringBuilder();
        while ((len = fis.read(buffer)) > 0) {
            sb.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
        }*/
        List<String> contents = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        long i = 0;
        while (reader.ready()) {
            i++;
            if (i % 1000000 == 0) {
                Log.i("i = " + i);
            }
            String line = reader.readLine();
            String content = line.trim();
            if (content.contains("#")) {
                if (content.indexOf("#") != content.lastIndexOf("#")) {
                    Log.i("content  " + content);
                    String[] splitContent = content.split("#");
                    if (splitContent.length > 2) {
                        contents.add(content.split("#")[2]);
                    }
                } else {
                    contents.add(content.split("#")[0]);
                }
            } else {
                contents.add(content);
            }
        }
        Log.i("---------------读取结束---------------");
        fis.close();
        reader.close();
        return contents;
    }

    public static boolean checkFileExists(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean writeToFile(String path, List<ResultBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        data.sort(new Comparator<ResultBean>() {
            @Override
            public int compare(ResultBean o1, ResultBean o2) {
                return Integer.compare(o2.getCollectCount(), o1.getCollectCount());
            }
        });
        for (int i = 0; i < data.size(); i++) {
//            Log.i(data.get(i).getData().length + "");
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            if (data.get(i).getCollectCount() > 0) {
                fos.write(",[".getBytes());
                fos.write(String.valueOf(data.get(i).getCollectCount()).getBytes());
//                Log.i(i +"  ----data.get(i).getCollectCount() = " + data.get(i).getCollectCount());
                fos.write("]".getBytes());
            } else {
//                Log.i(i +"  ----data.get(i).getCollectCount() = " + data.get(i).getCollectCount());
            }
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile(String path, List<ResultBean> data, int w4, int w5) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        data.sort(new Comparator<ResultBean>() {
            @Override
            public int compare(ResultBean o1, ResultBean o2) {
                return Integer.compare(o2.getCollectCount(), o1.getCollectCount());
            }
        });
        fos.write(("总数：" + data.size()).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中5位：" + w5).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中4位：" + w4).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
//            Log.i(data.get(i).getData().length + "");
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            if (data.get(i).getCollectCount() > 0) {
                fos.write(",[".getBytes());
                fos.write(String.valueOf(data.get(i).getCollectCount()).getBytes());
//                Log.i(i +"  ----data.get(i).getCollectCount() = " + data.get(i).getCollectCount());
                fos.write("]".getBytes());
            } else {
//                Log.i(i +"  ----data.get(i).getCollectCount() = " + data.get(i).getCollectCount());
            }
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile3(String path, String path2, List<GenerateBaseDataBean.AppendData> data, List<GenerateBaseDataBean.BaseData> notFullAppendData, int w4, int w5, int w42, int w52) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(("总数：" + data.size()).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中5位：" + w5).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中4位：" + w4).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
            fos.write(data.get(i).getByteData().toString().getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();

        fos = new FileOutputStream(path2);

        fos.write(("-------------------------------------剩余未使用数据-------------------------------------").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("总数：" + notFullAppendData.size()).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中5位：" + w52).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中4位：" + w42).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < notFullAppendData.size(); i++) {
            fos.write(Arrays.toString(notFullAppendData.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile4(String path, String path2, List<GenerateBaseDataBean.AppendData> data, List<GenerateBaseDataBean.BaseData> notFullAppendData, int w1, int w2, int w3, int w4, int w5, int w12, int w22, int w32, int w42, int w52) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(("总数：" + data.size()).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        float percent5 = (w5 / (float) data.size()) * 100;
        fos.write(("中5位：" + w5 + "     " + String.format("%.2f", percent5) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        float percent4 = (w4 / (float) data.size()) * 100;
        fos.write(("中4位：" + w4 + "     " + String.format("%.2f", percent4) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        float percent3 = (w3 / (float) data.size()) * 100;
        fos.write(("中3位：" + w3 + "     " + String.format("%.2f", percent3) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        float percent2 = (w2 / (float) data.size()) * 100;
        fos.write(("中2位：" + w2 + "     " + String.format("%.2f", percent2) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        float percent1 = (w1 / (float) data.size()) * 100;
        fos.write(("中1位：" + w1 + "     " + String.format("%.2f", percent1) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
            fos.write(data.get(i).getByteData().toString().getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();

        fos = new FileOutputStream(path2);

        fos.write(("-------------------------------------剩余未使用数据-------------------------------------").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("总数：" + notFullAppendData.size()).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        percent5 = (w52 / (float) notFullAppendData.size()) * 100;
        fos.write(("中5位：" + w52 + "     " + String.format("%.2f", percent5) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        percent4 = (w42 / (float) notFullAppendData.size()) * 100;
        fos.write(("中4位：" + w42 + "     " + String.format("%.2f", percent4) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        percent3 = (w32 / (float) notFullAppendData.size()) * 100;
        fos.write(("中3位：" + w32 + "     " + String.format("%.2f", percent3) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        percent2 = (w22 / (float) notFullAppendData.size()) * 100;
        fos.write(("中2位：" + w22 + "     " + String.format("%.2f", percent2) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));

        percent1 = (w12 / (float) notFullAppendData.size()) * 100;
        fos.write(("中1位：" + w12 + "     " + String.format("%.2f", percent1) + "%").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < notFullAppendData.size(); i++) {
            fos.write(Arrays.toString(notFullAppendData.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile(String path, List<ResultBean> data, List<Integer> dataIdx) throws IOException {
        HashSet<Integer> complexData = new HashSet<>(dataIdx);
        dataIdx.clear();
        dataIdx.addAll(complexData);
        dataIdx.sort(Integer::compareTo);
        FileOutputStream fos = new FileOutputStream(path);
        for (int i = 0; i < dataIdx.size(); i++) {
//            Log.i(dataIdx.get(i) + "");
            fos.write(Arrays.toString(data.get(dataIdx.get(i)).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile2(String path, List<ResultBean2> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        for (int i = 0; i < data.size(); i++) {
//            Log.i(data.get(i).getData().length + "");
            fos.write(Arrays.toString(data.get(i).getData().toArray()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile(String path, int w4, int w5, List<Integer> dataIdx, int dataCount, int count, int generateIdx) throws IOException {
        String tempPath = path.split("\\.")[0] + "-" + generateIdx + ".txt";
        FileOutputStream fos = new FileOutputStream(tempPath);
        fos.write(("总数：" + count).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中5位：" + w5).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.write(("中4位：" + w4).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < dataIdx.size(); i++) {
            float percent = dataIdx.get(i) / (float) dataCount;
            fos.write(String.format("%.1f", (percent * 100)).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile(String path, List<Integer> dataIdx, int count, int generateIdx, int size) throws IOException {
        String tempPath = path.split("\\.")[0] + "-" + generateIdx + ".txt";
        FileOutputStream fos;
        if (new File(tempPath).exists()) {
            fos = new FileOutputStream(tempPath, true);
        } else {
            fos = new FileOutputStream(tempPath);
        }
        fos.write(("---------------------------------------" + size + "---------------------------------------").getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < dataIdx.size(); i++) {
            float percent = dataIdx.get(i) / (float) count;
            fos.write(String.format("%.1f", (percent * 100)).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFile2(String path, int size, int dataCount, int allCount, List<ExistsDataBean> existsData, int generateIdx) throws IOException {
        String tempPath = path.split("\\.")[0] + "-" + generateIdx + ".txt";
        FileOutputStream fos;
        if (new File(tempPath).exists()) {
            fos = new FileOutputStream(tempPath, true);
        } else {
            fos = new FileOutputStream(tempPath);
            fos.write(("总数：" + allCount).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
            fos.write(("中4个条数：" + existsData.size()).getBytes(StandardCharsets.UTF_8));
            fos.write("-----".getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < existsData.size(); i++) {
                ExistsDataBean existsDataBean = existsData.get(i);
                fos.write((existsDataBean.getCollectionCount() + "").getBytes(StandardCharsets.UTF_8));
                fos.write("、".getBytes(StandardCharsets.UTF_8));
            }
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        //size:出现n次的数  dataCount:个数
        fos.write((size + "," + dataCount).getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.close();
        return true;
    }

    public static boolean writeToFile(String path, String data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(data.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun3(String path, String strContent, List<ResultBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(strContent.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun14(String path, String strContent, List<com.dragon.lucky.command14.NumberDataBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(strContent.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
            data.get(i).getData().sort(Integer::compareTo);
            fos.write(data.get(i).getData().toString().getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun18(String path, String strContent, List<com.dragon.lucky.command18.NumberDataBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(strContent.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun28(String path, List<CollectBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        for (int i = 0; i < data.size(); i++) {
            fos.write(data.get(i).tag.getBytes(StandardCharsets.UTF_8));
            fos.write(",".getBytes(StandardCharsets.UTF_8));
            fos.write(String.valueOf(data.get(i).count).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean appendToFileHead(String srcPath, String path, String data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(data.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        FileInputStream fis = new FileInputStream(srcPath);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            fos.flush();
        }
        fis.close();
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun18(String path, List<com.dragon.lucky.command18.NumberDataBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, true);
        for (int i = 0; i < data.size(); i++) {
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun15(String path, String dataStr, List<NumberDataBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(dataStr.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
//            Arrays.sort(data.get(i).getData());
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun27(String path, String dataStr, List<com.dragon.lucky.command27.NumberDataBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(dataStr.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
//            Arrays.sort(data.get(i).getData());
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

    public static boolean writeToFileForRun13(String path, String dataStr, List<NumberBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(dataStr.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < data.size(); i++) {
//            Arrays.sort(data.get(i).getData());
            fos.write(data.get(i).getData().toString().getBytes(StandardCharsets.UTF_8));
            fos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        fos.close();
        return true;
    }

}
