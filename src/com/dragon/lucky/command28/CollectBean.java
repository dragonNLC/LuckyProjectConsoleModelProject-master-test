package com.dragon.lucky.command28;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectBean {

    public String tag;
    public int count;
    public List<String> files;
    public Map<String, CollectOrderTagBean> orm;

    public CollectBean() {
        this.files = new ArrayList<>();
        this.orm = new HashMap<>();
    }

    public CollectBean(String tag, int count) {
        this.tag = tag;
        this.count = count;
        this.files = new ArrayList<>();
        this.orm = new HashMap<>();
    }

    public CollectBean(String tag, int count, List<String> files) {
        this.tag = tag;
        this.count = count;
        this.files = files;
        this.orm = new HashMap<>();
    }

    public static class CollectOrderTagBean {
        public int count;
        public int tag;

        public CollectOrderTagBean(int count, int tag) {
            this.count = count;
            this.tag = tag;
        }
    }

}
