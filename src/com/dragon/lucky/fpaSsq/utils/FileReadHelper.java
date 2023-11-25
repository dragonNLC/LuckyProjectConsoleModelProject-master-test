package com.dragon.lucky.fpaSsq.utils;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean writeToFile(String path, String data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(data.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.close();
        return true;
    }

    public static boolean writeToFile(String path, InputStream is) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.flush();
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

}
