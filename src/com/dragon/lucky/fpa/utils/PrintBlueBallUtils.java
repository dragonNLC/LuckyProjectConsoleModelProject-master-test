package com.dragon.lucky.fpa.utils;

import java.util.ArrayList;
import java.util.List;

public class PrintBlueBallUtils {

    private static StringBuilder stringBuilder = new StringBuilder();
    private static StringBuilder stringBuilder3 = new StringBuilder();

    public static List<Integer> bx = new ArrayList<>();
    public static List<Integer> _1wc = new ArrayList<>();
    public static List<Integer> _1wz = new ArrayList<>();
    public static List<Integer> _1wd = new ArrayList<>();
    public static List<Integer> _2wc = new ArrayList<>();
    public static List<Integer> _2wz = new ArrayList<>();
    public static List<Integer> _2wd = new ArrayList<>();
    public static List<Integer> _3wc = new ArrayList<>();
    public static List<Integer> _3wz = new ArrayList<>();
    public static List<Integer> _3wd = new ArrayList<>();
    public static List<Integer> _2x = new ArrayList<>();
    public static List<Integer> _3x = new ArrayList<>();
    public static List<Integer> zdld = new ArrayList<>();
    public static List<Integer> _dv = new ArrayList<>();
    public static List<Integer> _dvc = new ArrayList<>();
    public static List<Integer> _dvz = new ArrayList<>();
    public static List<Integer> _dvd = new ArrayList<>();
    public static List<Integer> _wc = new ArrayList<>();
    public static List<Integer> _3c = new ArrayList<>();
    public static List<Integer> _4c = new ArrayList<>();
    public static List<Integer> _5c = new ArrayList<>();
    public static List<Integer> _wd = new ArrayList<>();
    public static List<Integer> _3d = new ArrayList<>();
    public static List<Integer> _4d = new ArrayList<>();
    public static List<Integer> _5d = new ArrayList<>();

    public static List<Integer> _c1 = new ArrayList<>();
    public static List<Integer> c1 = new ArrayList<>();
    public static List<Integer> _c2 = new ArrayList<>();
    public static List<Integer> c2 = new ArrayList<>();
    public static List<Integer> _c3 = new ArrayList<>();
    public static List<Integer> c3 = new ArrayList<>();
    public static List<Integer> _c4 = new ArrayList<>();
    public static List<Integer> c4 = new ArrayList<>();
    public static List<Integer> _c5 = new ArrayList<>();
    public static List<Integer> c5 = new ArrayList<>();
    public static List<Integer> _c6 = new ArrayList<>();
    public static List<Integer> c6 = new ArrayList<>();
    public static List<Integer> _c7 = new ArrayList<>();
    public static List<Integer> c7 = new ArrayList<>();
    public static List<Integer> _c8 = new ArrayList<>();
    public static List<Integer> c8 = new ArrayList<>();
    public static List<Integer> _c9 = new ArrayList<>();
    public static List<Integer> c9 = new ArrayList<>();
    public static List<Integer> _c10 = new ArrayList<>();
    public static List<Integer> c10 = new ArrayList<>();
    public static List<List<Integer>> c10X = new ArrayList<>();
    static {
        c10X.add(c1);
        c10X.add(c2);
        c10X.add(c3);
        c10X.add(c4);
        c10X.add(c5);
        c10X.add(c6);
        c10X.add(c7);
        c10X.add(c8);
        c10X.add(c9);
        c10X.add(c10);
    }

    public static List<Integer> _d1 = new ArrayList<>();
    public static List<Integer> d1 = new ArrayList<>();
    public static List<Integer> _d2 = new ArrayList<>();
    public static List<Integer> d2 = new ArrayList<>();
    public static List<Integer> _d3 = new ArrayList<>();
    public static List<Integer> d3 = new ArrayList<>();
    public static List<Integer> _d4 = new ArrayList<>();
    public static List<Integer> d4 = new ArrayList<>();
    public static List<Integer> _d5 = new ArrayList<>();
    public static List<Integer> d5 = new ArrayList<>();
    public static List<Integer> _d6 = new ArrayList<>();
    public static List<Integer> d6 = new ArrayList<>();
    public static List<Integer> _d7 = new ArrayList<>();
    public static List<Integer> d7 = new ArrayList<>();
    public static List<Integer> _d8 = new ArrayList<>();
    public static List<Integer> d8 = new ArrayList<>();
    public static List<Integer> _d9 = new ArrayList<>();
    public static List<Integer> d9 = new ArrayList<>();
    public static List<Integer> _d10 = new ArrayList<>();
    public static List<Integer> d10 = new ArrayList<>();
    public static List<List<Integer>> d10x = new ArrayList<>();

    static {
        d10x.add(d1);
        d10x.add(d2);
        d10x.add(d3);
        d10x.add(d4);
        d10x.add(d5);
        d10x.add(d6);
        d10x.add(d7);
        d10x.add(d8);
        d10x.add(d9);
        d10x.add(d10);
    }
    public static List<Integer> _1c1d = new ArrayList<>();
    public static List<Integer> _1c2d = new ArrayList<>();
    public static List<Integer> _1c3d = new ArrayList<>();
    public static List<Integer> _2c1d = new ArrayList<>();
    public static List<Integer> _2c2d = new ArrayList<>();
    public static List<Integer> _2c3d = new ArrayList<>();
    public static List<Integer> _3c1d = new ArrayList<>();
    public static List<Integer> _3c2d = new ArrayList<>();
    public static List<Integer> _3c3d = new ArrayList<>();

    public static void append(int data) {
        stringBuilder.append(data);
        append3(data);
    }

    public static void append(String data) {
        stringBuilder.append(data);
        append3(data);
    }

    public static void deleteCharAt(int len) {
        stringBuilder.deleteCharAt(len);
        deleteCharAt3(stringBuilder3.length() - 1);
    }

    public static void append2(int data) {
        stringBuilder.append(data);
    }

    public static void append2(String data) {
        stringBuilder.append(data);
    }

    public static void deleteCharAt2(int len) {
        stringBuilder.deleteCharAt(len);
    }

    public static int length() {
        return stringBuilder.length();
    }

    public static String dataToString() {
        return stringBuilder.toString();
    }

    public static void append3(int data) {
        stringBuilder3.append(data);
    }

    public static void append3(String data) {
        stringBuilder3.append(data);
    }

    public static void deleteCharAt3(int len) {
        stringBuilder3.deleteCharAt(len);
    }

    public static int length3() {
        return stringBuilder3.length();
    }

    public static String dataToString3() {
        return stringBuilder3.toString();
    }

}
