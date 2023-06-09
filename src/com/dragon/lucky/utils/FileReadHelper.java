package com.dragon.lucky.utils;

import com.dragon.lucky.bean.ResultBean;
import com.dragon.lucky.bean.ResultBean2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FileReadHelper {

    public static List<String> readFile(String path) throws IOException {
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

    public static boolean checkFileExists(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean writeToFile(String path, List<ResultBean> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        for (int i = 0; i < data.size(); i++) {
//            Log.i(data.get(i).getData().length + "");
            fos.write(Arrays.toString(data.get(i).getData()).getBytes(StandardCharsets.UTF_8));
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

    public static boolean writeToFile(String path, String data) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(data.getBytes(StandardCharsets.UTF_8));
        fos.write("\n".getBytes(StandardCharsets.UTF_8));
        fos.close();
        return true;
    }

}
