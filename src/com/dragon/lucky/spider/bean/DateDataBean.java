package com.dragon.lucky.spider.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateDataBean {

    public String title;
    public List<String> printNumbers;
    public List<SubItemBean> subItemNumbers;
    public Map<Integer, Integer> collect;
    public List<CollectionBean> collectList;

    public DateDataBean() {
        this.collect = new HashMap<>();
        this.collectList = new ArrayList<>();
    }

    public DateDataBean(String title, List<String> printNumbers, List<SubItemBean> subItemNumbers) {
        this.title = title;
        this.printNumbers = printNumbers;
        this.subItemNumbers = subItemNumbers;
        this.collect = new HashMap<>();
        this.collectList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPrintNumbers() {
        return printNumbers;
    }

    public void setPrintNumbers(List<String> printNumbers) {
        this.printNumbers = printNumbers;
    }

    public List<SubItemBean> getSubItemNumbers() {
        return subItemNumbers;
    }

    public void setSubItemNumbers(List<SubItemBean> subItemNumbers) {
        this.subItemNumbers = subItemNumbers;
    }

    public Map<Integer, Integer> getCollect() {
        return collect;
    }

    public void setCollect(Map<Integer, Integer> collect) {
        this.collect = collect;
    }

    public static class CollectionBean {

        public int id;
        public int count;

        public CollectionBean(int id, int count) {
            this.id = id;
            this.count = count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
