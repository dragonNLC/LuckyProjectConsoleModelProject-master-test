package com.dragon.lucky.spider.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.dragon.lucky.spider.bean.DateDataBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CacheUtils {

    public static List<DateDataBean> mergeContent(List<DateDataBean> first, List<DateDataBean> second) {
        if (first == null || first.size() == 0) {
            return second;
        }
        if (second == null || second.size() == 0) {
            return first;
        }
        List<DateDataBean> result = new ArrayList<>(first);
        for (int i = 0; i < second.size(); i++) {
            boolean add = true;
            for (int j = 0; j < first.size(); j++) {
                if (second.get(i).title.equals(first.get(j).title)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                result.add(second.get(i));
            }
        }
        return result;
    }

    public static List<DateDataBean> readCache(String path) {
        Gson gson = new Gson();
        String content = FileHelper.readFile(path);
        return gson.fromJson(content, getGSonType());
    }

    public static void cacheW(String path, List<DateDataBean> data) {
        Gson gson = new Gson();
        FileHelper.writeToFile(path, gson.toJson(data));
    }

    public static Type getGSonType() {
        return new TypeToken<List<DateDataBean>>() {
        }.getType();
    }

}
