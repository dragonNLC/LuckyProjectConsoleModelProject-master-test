package com.dragon.lucky.fpaSsq.utils;

import java.util.ArrayList;
import java.util.List;

public class PrintRedBallUtils {

    private static StringBuilder stringBuilder = new StringBuilder();
    private static StringBuilder stringBuilder2 = new StringBuilder();
    private static StringBuilder stringBuilder3 = new StringBuilder();

    public static List<Integer> nul = new ArrayList<>();
    public static List<Integer> nulV = new ArrayList<>();
    public static List<Integer> nulAll = new ArrayList<>();
    public static List<Integer> nulC = new ArrayList<>();
    public static List<Integer> nulD = new ArrayList<>();
    public static List<Integer> cx = new ArrayList<>();
    public static List<Integer> zdld = new ArrayList<>();
    public static List<Integer> wc = new ArrayList<>();
    public static List<Integer> _1c = new ArrayList<>();
    public static List<Integer> _2c = new ArrayList<>();
    public static List<Integer> _3c = new ArrayList<>();
    public static List<Integer> _4c = new ArrayList<>();
    public static List<Integer> wd = new ArrayList<>();
    public static List<Integer> _1d = new ArrayList<>();
    public static List<Integer> _2d = new ArrayList<>();
    public static List<Integer> _3d = new ArrayList<>();
    public static List<Integer> _4d = new ArrayList<>();

    public static List<Integer> wcwd = new ArrayList<>();
    public static List<Integer> wc1d = new ArrayList<>();
    public static List<Integer> wc2d = new ArrayList<>();
    public static List<Integer> _1cwd = new ArrayList<>();
    public static List<Integer> _1c1d = new ArrayList<>();
    public static List<Integer> _1c2d = new ArrayList<>();
    public static List<Integer> _2cwd = new ArrayList<>();
    public static List<Integer> _2c1d = new ArrayList<>();
    public static List<Integer> _2c2d = new ArrayList<>();
    public static List<Integer> ncnd = new ArrayList<>();

    public static void append(int data) {
        stringBuilder.append(data);
    }

    public static void append(String data) {
        stringBuilder.append(data);
    }

    public static int length() {
        return stringBuilder.length();
    }

    public static String dataToString() {
        return stringBuilder.toString();
    }

    public static void deleteCharAt(int len) {
        stringBuilder.deleteCharAt(len);
    }

    public static void append2(int data) {
        stringBuilder2.append(data);
    }

    public static void append2(String data) {
        stringBuilder2.append(data);
    }

    public static void deleteCharAt2(int len) {
        stringBuilder2.deleteCharAt(len);
    }

    public static int length2() {
        return stringBuilder2.length();
    }

    public static String dataToString2() {
        return stringBuilder2.toString();
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
