package com.dragon.lucky.fpaSsq.utils;

import com.dragon.lucky.fpaSsq.bean.DMBean;
import com.dragon.lucky.fpaSsq.bean.PreviewDataBean;
import com.dragon.lucky.fpaSsq.bean.PrintDataBean;
import com.dragon.lucky.fpaSsq.bean.SMBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsoupHelper {

    /**
     * https://zx.500.com/ssq/zhuanjiashahao.php?expectnum=100
     * 全区杀号
     * 第三行
     */
    public static List<PreviewDataBean> doConnect1() {
//        Log.i("正在解析网页...");
        String url = "https://zx.500.com/ssq/zhuanjiashahao.php?expectnum=100";
        List<PreviewDataBean> previewData = new ArrayList<>();
        float[] percent = new float[10];
        try {
            Document document = Jsoup.connect(url)
                    .get();
            Element itemElement = document.getElementsByClass("killnub_table").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");
            Elements tdPercentElements = itemElements.get(2).getElementsByTag("td");

            for (int i = 1; i < tdPercentElements.size() - 1; i++) {
                Element element = tdPercentElements.get(i);
                if (element.text().contains("%")) {
                    float tdData = Float.parseFloat(element.text().replace("%", "")) / 100;
                    percent[i - 1] = tdData;
                } else {
                    float tdData = Float.parseFloat(element.text());
                    percent[i - 1] = tdData;
                }
            }

            for (int i = 4; i < itemElements.size(); i++) {
                PreviewDataBean preview = new PreviewDataBean();
                previewData.add(preview);
                List<PreviewDataBean.PreviewSingleDataBean> previewSingleData = new ArrayList<>();
                List<PreviewDataBean.PreviewSingleDataBean> previewSingleData2 = new ArrayList<>();
                preview.setPreviewData(previewSingleData);
                preview.setPreviewData2(previewSingleData2);
                if (i == 4) {//最新的
                    Elements tdElements = itemElements.get(i).getElementsByTag("td");
                    for (int j = 2; j < 7; j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        String html = element.html();
                        previewSingleData.add(new PreviewDataBean.PreviewSingleDataBean(Byte.parseByte(html), false));
                    }
                    for (int j = 7; j < tdElements.size(); j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        if (element != null) {
                            String html = element.html();
                            previewSingleData2.add(new PreviewDataBean.PreviewSingleDataBean(Byte.parseByte(html), false));
                        }
                    }
                } else {
                    Elements tdElements = itemElements.get(i).getElementsByTag("td");
                    Element titleElement = tdElements.get(0);
                    Element contentElement = tdElements.get(1);
                    int[] byteData = new int[7];
                    String contentText = contentElement.text();
                    String[] contentTexts = contentText.split(" ");
                    for (int j = 0; j < byteData.length; j++) {
                        byteData[j] = Integer.parseInt(contentTexts[j]);
                    }
                    preview.setData(byteData);
                    preview.setTitle(titleElement.html());
                    for (int j = 2; j < 7; j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        boolean isTrue = element.hasClass("nub-red");
                        String html = element.html();
                        previewSingleData.add(new PreviewDataBean.PreviewSingleDataBean(Integer.parseInt(html), isTrue));
                    }
                    for (int j = 7; j < tdElements.size() - 1; j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        boolean isTrue = element.hasClass("nub-blue");
                        String html = element.html();
                        previewSingleData2.add(new PreviewDataBean.PreviewSingleDataBean(Integer.parseInt(html), isTrue));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return previewData;
    }

    /**
     * https://expert.78500.cn/ssq/hqsm/
     * 前区杀号
     * 第四行
     */
    public static DMBean doConnect2() {
//        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/hqsm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        DMBean result = new DMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            List<DMBean.DMItemBean> items = new ArrayList<>();
            List<DMBean.DMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                DMBean.DMItemBean item = new DMBean.DMItemBean();
                items.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < printNumbersStr2.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr2[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.hitData = tdStateElement.hasClass("tflag") ? 1 : 0;
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                DMBean.DMTypeBean type = new DMBean.DMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/ssq/lqsm/
     * 后区杀号
     */
    public static DMBean doConnect3() {
//        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/lqsm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        DMBean result = new DMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");


            List<DMBean.DMItemBean> items = new ArrayList<>();
            List<DMBean.DMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                DMBean.DMItemBean item = new DMBean.DMItemBean();
                items.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < printNumbersStr2.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr2[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.hitData = tdStateElement.hasClass("tflag") ? 1 : 0;
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                DMBean.DMTypeBean type = new DMBean.DMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://zst.cjcp.cn/shdd/ssq-qianqu-100.html
     * 前区杀号
     * 第五行
     */
    public static DMBean doConnect4() {
//        Log.i("正在解析网页...");
        String url = "https://zst.cjcp.cn/shdd/ssq-qianqu-100.html";
        Map<String, String> map = new HashMap<>();
        map.put("queryCount", "100");

        DMBean result = new DMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("charttab").get(0).lastElementChild();
            Elements itemElements = itemElement.getElementsByTag("tr");

            List<DMBean.DMItemBean> items = new ArrayList<>();
            List<DMBean.DMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size() - 6; i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                DMBean.DMItemBean item = new DMBean.DMItemBean();
                items.add(item);

                if (i == itemElements.size() - 1 - 6) {//最后一期
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < printNumbersStr2.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr2[j]);
                    }

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j).firstElementChild();
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.hitData = tdStateElement.html().contains("正确") ? 1 : 0;
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }

            for (int i = itemElements.size() - 6; i < itemElements.size(); i++) {
                DMBean.DMTypeBean type = new DMBean.DMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemElements.get(i).getElementsByTag("td");
                type.data = new float[11];
                int idx = 0;
                for (int j = 2; j < itemTdCollectionChildElements.size(); j += 2) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[idx++] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[idx++] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://zst.cjcp.cn/shdd/ssq-houqu-100.html
     * 后区杀号
     */
    public static DMBean doConnect5() {
//        Log.i("正在解析网页...");
        String url = "https://zst.cjcp.cn/shdd/ssq-houqu-100.html";
        Map<String, String> map = new HashMap<>();
        map.put("queryCount", "100");

        DMBean result = new DMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("charttab").get(0).lastElementChild();
            Elements itemElements = itemElement.getElementsByTag("tr");

            List<DMBean.DMItemBean> items = new ArrayList<>();
            List<DMBean.DMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size() - 6; i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                DMBean.DMItemBean item = new DMBean.DMItemBean();
                items.add(item);

                if (i == itemElements.size() - 1 - 6) {//最后一期
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < printNumbersStr2.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr2[j]);
                    }

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j).firstElementChild();
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.hitData = tdStateElement.html().contains("正确") ? 1 : 0;
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }

            for (int i = itemElements.size() - 6; i < itemElements.size(); i++) {
                DMBean.DMTypeBean type = new DMBean.DMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemElements.get(i).getElementsByTag("td");
                type.data = new float[11];
                int idx = 0;
                for (int j = 2; j < itemTdCollectionChildElements.size(); j += 2) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[idx++] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[idx++] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/ssq/hqdm/
     * 前区定胆
     * 第六行
     */
    public static DMBean doConnect6() {
//        Log.i("正在解析网页...");https://expert.78500.cn/ssq/hqdm/
        String url = "https://expert.78500.cn/ssq/hqdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        DMBean result = new DMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            List<DMBean.DMItemBean> items = new ArrayList<>();
            List<DMBean.DMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                DMBean.DMItemBean item = new DMBean.DMItemBean();
                items.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < printNumbersStr2.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr2[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.hitData = tdStateElement.hasClass("tflag") ? 0 : 1;
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                DMBean.DMTypeBean type = new DMBean.DMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/ssq/hqsdm/
     * 前区三胆
     * 第七行
     */
    public static SMBean doConnect7() {
//        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/hqsdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        SMBean result = new SMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");
            List<SMBean.SMItemBean> items = new ArrayList<>();
            List<SMBean.SMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                SMBean.SMItemBean item = new SMBean.SMItemBean();
                items.add(item);
                List<SMBean.SMDataBean> subItems = new ArrayList<>();
                item.itemData = subItems;
                Element tdNumber = tdElements.get(0);
                item.title = tdNumber.html();
//                Log.i("当前期数：" + tdNumber.html());

                for (int j = 2; j < tdElements.size() - 1; j++) {
                    Element tdElement = tdElements.get(j);
                    SMBean.SMDataBean subItem = new SMBean.SMDataBean();
                    subItems.add(subItem);
                    String tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
                    String[] tdHtmlContentStr = tdHtml.split(",");
                    subItem.data = new int[tdHtmlContentStr.length];
                    subItem.hitData = new int[tdHtmlContentStr.length];
                    for (int k = 0; k < subItem.data.length; k++) {
                        subItem.data[k] = Integer.parseInt(tdHtmlContentStr[k]);
                    }
                    if (tdElement.hasAttr("style")) {
                        //有标色的一个解法
                        Elements tdRedTextColorElements = tdElement.getElementsByTag("b");

                        for (int k = 0; k < subItem.data.length; k++) {
                            for (int l = 0; l < tdRedTextColorElements.size(); l++) {
                                if (tdRedTextColorElements.get(l).text().contains(",")) {
                                    String[] tdReds = tdRedTextColorElements.get(l).text().split(",");
                                    for (int m = 0; m < tdReds.length; m++) {
                                        int tempText = Integer.parseInt(tdReds[m]);
                                        if (tempText == subItem.data[k]) {
                                            subItem.hitData[k] = 1;
                                            break;
                                        }
                                    }
                                } else {
                                    int tempText = Integer.parseInt(tdRedTextColorElements.get(l).text());
                                    if (tempText == subItem.data[k]) {
                                        subItem.hitData[k] = 1;
                                        break;
                                    }
                                }
                            }
                        }
//                        Log.i("选中数（全对） = " + tdHtml);
                    }
//                    Log.i("\n");
                }
                if (!tdElements.hasClass("nowissue")) {//最后一期
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < item.redData.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < item.blueData.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr[j]);
                    }
                    Element tdLastNumber = tdElements.get(tdElements.size() - 1);
                    item.hitCount = Integer.parseInt(tdLastNumber.text());
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                SMBean.SMTypeBean type = new SMBean.SMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/ssq/hqssm/
     * 前区杀三码
     * 第八行
     */
    public static SMBean doConnect8() {
//        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/hqssm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        SMBean result = new SMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");
            List<SMBean.SMItemBean> items = new ArrayList<>();
            List<SMBean.SMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                SMBean.SMItemBean item = new SMBean.SMItemBean();
                items.add(item);
                List<SMBean.SMDataBean> subItems = new ArrayList<>();
                item.itemData = subItems;
                Element tdNumber = tdElements.get(0);
                item.title = tdNumber.html();
//                Log.i("当前期数：" + tdNumber.html());

                for (int j = 2; j < tdElements.size() - 1; j++) {
                    Element tdElement = tdElements.get(j);
                    SMBean.SMDataBean subItem = new SMBean.SMDataBean();
                    subItems.add(subItem);
                    String tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
                    String[] tdHtmlContentStr = tdHtml.split(",");
                    subItem.data = new int[tdHtmlContentStr.length];
                    subItem.hitData = new int[tdHtmlContentStr.length];
                    for (int k = 0; k < subItem.data.length; k++) {
                        subItem.data[k] = Integer.parseInt(tdHtmlContentStr[k]);
                    }
                    if (tdElement.hasAttr("style")) {
                        //有标色的一个解法
                        Elements tdRedTextColorElements = tdElement.getElementsByTag("b");
                        for (int k = 0; k < subItem.data.length; k++) {
                            for (int l = 0; l < tdRedTextColorElements.size(); l++) {
                                if (tdRedTextColorElements.get(l).text().contains(",")) {
                                    String[] tdReds = tdRedTextColorElements.get(l).text().split(",");
                                    for (int m = 0; m < tdReds.length; m++) {
                                        int tempText = Integer.parseInt(tdReds[m]);
                                        if (tempText == subItem.data[k]) {
                                            subItem.hitData[k] = 1;
                                            break;
                                        }
                                    }
                                } else {
                                    int tempText = Integer.parseInt(tdRedTextColorElements.get(l).text());
                                    if (tempText == subItem.data[k]) {
                                        subItem.hitData[k] = 1;
                                        break;
                                    }
                                }
                            }
                        }
//                        Log.i("选中数（全对） = " + tdHtml);
                    }
                }
                if (!tdElements.hasClass("nowissue")) {//最后一期
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < item.redData.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < item.blueData.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr[j]);
                    }
                    Element tdLastNumber = tdElements.get(tdElements.size() - 1);
                    item.hitCount = Integer.parseInt(tdLastNumber.text());
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                SMBean.SMTypeBean type = new SMBean.SMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/ssq/lqdm/
     * 后区定胆
     */
    public static DMBean doConnect9() {
//        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/lqdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");
        DMBean result = new DMBean();
        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            List<DMBean.DMItemBean> items = new ArrayList<>();
            List<DMBean.DMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                DMBean.DMItemBean item = new DMBean.DMItemBean();
                items.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<DMBean.DMDataBean> subItems = new ArrayList<>();
                    item.itemData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
//                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        DMBean.DMDataBean subItem = new DMBean.DMDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.hitData = tdStateElement.hasClass("tflag") ? 0 : 1;
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                DMBean.DMTypeBean type = new DMBean.DMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * https://expert.78500.cn/ssq/lqsdm/
     * 后区三胆
     */
    public static SMBean doConnect10() {
//        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/lqsdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        SMBean result = new SMBean();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");
            List<SMBean.SMItemBean> items = new ArrayList<>();
            List<SMBean.SMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                SMBean.SMItemBean item = new SMBean.SMItemBean();
                items.add(item);
                List<SMBean.SMDataBean> subItems = new ArrayList<>();
                item.itemData = subItems;
                Element tdNumber = tdElements.get(0);
                item.title = tdNumber.html();
//                Log.i("当前期数：" + tdNumber.html());

                for (int j = 2; j < tdElements.size() - 1; j++) {
                    Element tdElement = tdElements.get(j);
                    SMBean.SMDataBean subItem = new SMBean.SMDataBean();
                    subItems.add(subItem);
                    String tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
                    String[] tdHtmlContentStr = tdHtml.split(",");
                    subItem.data = new int[tdHtmlContentStr.length];
                    subItem.hitData = new int[tdHtmlContentStr.length];
                    for (int k = 0; k < subItem.data.length; k++) {
                        subItem.data[k] = Integer.parseInt(tdHtmlContentStr[k]);
                    }
                    if (tdElement.hasAttr("style")) {
                        //有标色的一个解法
                        Elements tdRedTextColorElements = tdElement.getElementsByTag("b");
                        for (int k = 0; k < subItem.data.length; k++) {
                            for (int l = 0; l < tdRedTextColorElements.size(); l++) {
                                if (tdRedTextColorElements.get(l).text().contains(",")) {
                                    String[] tdReds = tdRedTextColorElements.get(l).text().split(",");
                                    for (int m = 0; m < tdReds.length; m++) {
                                        int tempText = Integer.parseInt(tdReds[m]);
                                        if (tempText == subItem.data[k]) {
                                            subItem.hitData[k] = 1;
                                            break;
                                        }
                                    }
                                } else {
                                    int tempText = Integer.parseInt(tdRedTextColorElements.get(l).text());
                                    if (tempText == subItem.data[k]) {
                                        subItem.hitData[k] = 1;
                                        break;
                                    }
                                }
                            }
                        }
//                        Log.i("选中数（全对） = " + tdHtml);
                    }
//                    Log.i("\n");
                }
                if (!tdElements.hasClass("nowissue")) {//最后一期
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < item.redData.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < item.blueData.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr[j]);
                    }
                    Element tdLastNumber = tdElements.get(tdElements.size() - 1);
                    item.hitCount = Integer.parseInt(tdLastNumber.text());
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                SMBean.SMTypeBean type = new SMBean.SMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/ssq/lqssm/
     * 后区杀三码
     */
    public static SMBean doConnect11() {
//        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/lqssm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        SMBean result = new SMBean();
        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");
            List<SMBean.SMItemBean> items = new ArrayList<>();
            List<SMBean.SMTypeBean> typeItems = new ArrayList<>();
            result.dataItem = items;
            result.typesItem = typeItems;

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                SMBean.SMItemBean item = new SMBean.SMItemBean();
                items.add(item);
                List<SMBean.SMDataBean> subItems = new ArrayList<>();
                item.itemData = subItems;
                Element tdNumber = tdElements.get(0);
                item.title = tdNumber.html();
//                Log.i("当前期数：" + tdNumber.html());

                for (int j = 2; j < tdElements.size() - 1; j++) {
                    Element tdElement = tdElements.get(j);
                    SMBean.SMDataBean subItem = new SMBean.SMDataBean();
                    subItems.add(subItem);
                    String tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
                    String[] tdHtmlContentStr = tdHtml.split(",");
                    subItem.data = new int[tdHtmlContentStr.length];
                    subItem.hitData = new int[tdHtmlContentStr.length];
                    for (int k = 0; k < subItem.data.length; k++) {
                        subItem.data[k] = Integer.parseInt(tdHtmlContentStr[k]);
                    }
                    if (tdElement.hasAttr("style")) {
                        //有标色的一个解法
                        Elements tdRedTextColorElements = tdElement.getElementsByTag("b");

                        for (int k = 0; k < subItem.data.length; k++) {
                            for (int l = 0; l < tdRedTextColorElements.size(); l++) {
                                if (tdRedTextColorElements.get(l).text().contains(",")) {
                                    String[] tdReds = tdRedTextColorElements.get(l).text().split(",");
                                    for (int m = 0; m < tdReds.length; m++) {
                                        int tempText = Integer.parseInt(tdReds[m]);
                                        if (tempText == subItem.data[k]) {
                                            subItem.hitData[k] = 1;
                                            break;
                                        }
                                    }
                                } else {
                                    int tempText = Integer.parseInt(tdRedTextColorElements.get(l).text());
                                    if (tempText == subItem.data[k]) {
                                        subItem.hitData[k] = 1;
                                        break;
                                    }
                                }
                            }
                        }
//                        Log.i("选中数（全对） = " + tdHtml);
                    }
//                    Log.i("\n");
                }
                if (!tdElements.hasClass("nowissue")) {//最后一期
                    Element redNumber = tdElements.get(1).firstElementChild();
//                    Log.i("本期出数：" + redNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
//                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.redData = new int[printNumbersStr.length];
                    for (int j = 0; j < item.redData.length; j++) {
                        item.redData[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    String[] printNumbersStr2 = blueNumber.html().split(" ");
                    item.blueData = new int[printNumbersStr2.length];
                    for (int j = 0; j < item.blueData.length; j++) {
                        item.blueData[j] = Integer.parseInt(printNumbersStr[j]);
                    }
                    Element tdLastNumber = tdElements.get(tdElements.size() - 1);
                    if (tdLastNumber.text().equals("全对")) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(tdLastNumber.text());
                    }
                }
            }

            Element itemCollectionElement = document.getElementsByClass("tabgame").get(0).lastElementChild();
            Elements itemCollectionChildElements = itemCollectionElement.getElementsByTag("tr");
            for (int i = 1; i < itemCollectionChildElements.size(); i++) {
                SMBean.SMTypeBean type = new SMBean.SMTypeBean();
                typeItems.add(type);
                Elements itemTdCollectionChildElements = itemCollectionChildElements.get(i).getElementsByTag("td");
                type.data = new float[itemTdCollectionChildElements.size() - 2];
                for (int j = 1; j < itemTdCollectionChildElements.size() - 1; j++) {
                    Element itemTdCollectionChildElement = itemTdCollectionChildElements.get(j);
                    if (itemTdCollectionChildElement.text().contains("%")) {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text().replace("%", "")) / 100;
                        type.data[j - 1] = tdData;
                    } else {
                        float tdData = Float.parseFloat(itemTdCollectionChildElement.text());
                        type.data[j - 1] = tdData;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://www.cjcp.cn/zoushitu/cjwssq/hqfenqu-03-2-100.html
     * 前区走势
     */
    public static List<Integer> doConnect12() {
//        Log.i("正在解析网页...");
        String url = "https://www.cjcp.cn/zoushitu/cjwssq/hqfenqu-03-2-100.html";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<Integer> result = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementById("pagedata");
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < 33; i++) {
                int count = 0;
                for (int j = itemElements.size() - 1; j >= 0; j--) {
                    Element element = itemElements.get(j);
                    Elements numberElements = element.getElementsByTag("td");
                    Element numberElement = numberElements.get(i + 3);
//                    Log.i("numberElement = " + numberElement.html());
                    if (!numberElement.hasClass("qiu_red") && !numberElement.hasClass("qiu_red_02")) {//不中的
                        count++;
                    } else {
                        break;
                    }
                }
                result.add(count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < result.size(); i++) {
//            Log.i((i + 1) + "--------" + result.get(i));
//        }
        return result;
    }

    /**
     * https://www.cjcp.cn/zoushitu/cjwssq/lqzonghe-2-100.html
     * 后区走势
     */
    public static List<Integer> doConnect13() {
//        Log.i("正在解析网页...");
        String url = "https://www.cjcp.cn/zoushitu/cjwssq/lqzonghe-2-100.html";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<Integer> result = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementById("pagedata");
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < 17; i++) {
                int count = 0;
                for (int j = itemElements.size() - 1; j >= 0; j--) {
                    Element element = itemElements.get(j);
                    Elements numberElements = element.getElementsByTag("td");
                    Element numberElement = numberElements.get(i + 3);
//                    Log.i("numberElement = " + element.html());
                    if (!numberElement.hasClass("qiu_blue")) {//不中的
                        count++;
                    } else {
                        break;
                    }
                }
                result.add(count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < result.size(); i++) {
//            Log.i((i + 1) + "--------" + result.get(i));
//        }
        return result;
    }

    /**
     * https://zst.cjcp.cn/cjwssq/view/ssqzonghe_content.html
     * 开奖历史走势
     */
    public static List<PrintDataBean> doConnect14(String endQi) throws IOException {
        String url = "https://zst.cjcp.cn/cjwssq/view/ssqzonghe_content.html";
        Map<String, String> map = new HashMap<>();
        map.put("startqi", "2007001");
        map.put("endqi", endQi);
        map.put("searchType", "1");
        List<PrintDataBean> result = new ArrayList<>();

        URL u = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestMethod("POST");
        urlConnection.setUseCaches(true);
        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setConnectTimeout(3000);
        urlConnection.connect();
        Log.i("连接成功！");

        OutputStream os = urlConnection.getOutputStream();

        StringBuffer sBuffer = new StringBuffer();
        for (String key : map.keySet()) {
            sBuffer.append(key + "=" + map.get(key) + "&");
        }

        os.write(sBuffer.toString().getBytes());
        os.flush();

        Log.i("输出成功！");
        if (urlConnection.getResponseCode() == 200) {
            InputStream is = urlConnection.getInputStream();
            FileReadHelper.writeToFile("C:\\Users\\aptdev\\Desktop\\za\\20231030-2\\out.html", is);
            Log.i("读取成功！");
        }
        urlConnection.disconnect();

        try {
            Document document = Jsoup.parse(new File("C:\\Users\\aptdev\\Desktop\\za\\20231030-2\\out.html"));

//            Log.i("document = " + document.outerHtml());
            Element itemElement = document.getElementById("info");
            Elements itemElements = itemElement.getElementsByTag("tr");
            Log.i("itemElements = " + itemElements.size());
            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
//                Log.i("tdElements = " + tdElements.text());
//                Log.i("tdElements = " + tdElements.size());
                PrintDataBean ptb = new PrintDataBean();
                result.add(ptb);//拿到数据
                int[] redBall = new int[6];
                int[] blueBall = new int[1];
                ptb.setRedBalls(redBall);
                ptb.setBlueBalls(blueBall);
                ptb.setTitle(tdElements.get(2).text());
                int redIdx = 0;
                for (int j = 2; j < 35; j++) {
                    Element tdElement = tdElements.get(j);
                    if (tdElement.hasClass("z_font_red")) {
                        redBall[redIdx] = Integer.parseInt(tdElement.text());
                        redIdx++;
                    }
                }
                int blueIdx = 0;
                for (int j = 35; j < 51; j++) {
                    Element tdElement = tdElements.get(j);
                    if (tdElement.hasClass("z_font_ls")) {
                        blueBall[blueIdx] = Integer.parseInt(tdElement.text());
                        blueIdx++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
