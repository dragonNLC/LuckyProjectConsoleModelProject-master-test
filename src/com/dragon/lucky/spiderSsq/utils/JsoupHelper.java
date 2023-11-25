package com.dragon.lucky.spiderSsq.utils;

import com.dragon.lucky.spiderSsq.bean.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class JsoupHelper {

    /**
     * https://zx.500.com/dlt/zhuanjiashahao.php?expectnum=100
     * 全区杀号
     */
    public static List<PreviewDataBean> doConnect3() {
        Log.i("正在解析网页...");
        String url = "https://zx.500.com/dlt/zhuanjiashahao.php?expectnum=100";
        List<PreviewDataBean> previewData = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url)
                    .get();
            Element itemElement = document.getElementsByClass("killnub_table").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 4; i < itemElements.size(); i++) {
                PreviewDataBean preview = new PreviewDataBean();
                List<PreviewDataBean.PreviewSingleDataBean> previewSingleData = new ArrayList<>();
                List<PreviewDataBean.PreviewSingleDataBean> previewSingleData2 = new ArrayList<>();
                if (i == 4) {
                    Elements tdElements = itemElements.get(i).getElementsByTag("td");
                    for (int j = 2; j < 7; j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        String html = element.html();
                        previewSingleData.add(new PreviewDataBean.PreviewSingleDataBean(Byte.decode(html), false));
                    }
                    for (int j = 7; j < tdElements.size(); j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        if (element != null) {
                            String html = element.html();
//                            Log.i("html = " + html);
                            previewSingleData2.add(new PreviewDataBean.PreviewSingleDataBean(Byte.decode(html), false));
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
                    /*Log.i("contentElement = " + contentElement.data());
                    Log.i("contentElement = " + contentElement.val());
                    Log.i("contentElement = " + contentElement.html());
                    Log.i("contentElement = " + contentElement.text());*/

                    preview.setTitle(titleElement.html());
                    for (int j = 2; j < 7; j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        boolean isTrue = !element.hasClass("nub-yellow");
                        String html = element.html();
                        previewSingleData.add(new PreviewDataBean.PreviewSingleDataBean(Integer.parseInt(html), isTrue));
                    }
                    for (int j = 7; j < tdElements.size() - 1; j++) {
                        Element element = tdElements.get(j).firstElementChild();
                        boolean isTrue = !element.hasClass("nub-yellow");
                        String html = element.html();
//                            Log.i("html = " + html);
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
     * https://expert.78500.cn/dlt/hqsm/
     * 前区杀号
     */
    public static List<HQSMBean> doConnect4() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/dlt/hqsm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<HQSMBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                HQSMBean item = new HQSMBean();
                result.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<HQSMBean.HQSMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        HQSMBean.HQSMSingleDataBean subItem = new HQSMBean.HQSMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<HQSMBean.HQSMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
                    Log.i("本期出数：" + redNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.data = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.data[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        HQSMBean.HQSMSingleDataBean subItem = new HQSMBean.HQSMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.isTrue = tdStateElement.hasClass("tflag");
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }
//            String title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/dlt/lqsm/
     * 后区杀号
     */
    public static List<LQSMBean> doConnect5() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/dlt/lqsm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<LQSMBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                LQSMBean item = new LQSMBean();
                result.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<LQSMBean.LQSMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        LQSMBean.LQSMSingleDataBean subItem = new LQSMBean.LQSMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<LQSMBean.LQSMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).lastElementChild();
                    Log.i("本期出数：" + redNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.data = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.data[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        LQSMBean.LQSMSingleDataBean subItem = new LQSMBean.LQSMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.isTrue = tdStateElement.hasClass("tflag");
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://zst.cjcp.cn/shdd/dlt-qianqu-100.html
     * 前区杀号
     */
    public static List<CJWBean> doConnect6() {
        Log.i("正在解析网页...");
        String url = "https://zst.cjcp.cn/shdd/dlt-qianqu-100.html";
        Map<String, String> map = new HashMap<>();
        map.put("queryCount", "100");

        List<CJWBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("charttab").get(0).lastElementChild();
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size() - 6; i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                CJWBean item = new CJWBean();
                result.add(item);

                if (i == itemElements.size() - 1 - 6) {//最后一期
                    List<CJWBean.CJWSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        CJWBean.CJWSingleDataBean subItem = new CJWBean.CJWSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<CJWBean.CJWSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
                    Log.i("本期出数：" + redNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.data = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.data[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        CJWBean.CJWSingleDataBean subItem = new CJWBean.CJWSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j).firstElementChild();
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.isTrue = tdStateElement.attr("alt").equals("正确");
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://zst.cjcp.cn/shdd/dlt-houqu-100.html
     * 后区杀号
     */
    public static List<CJWBean> doConnect7() {
        Log.i("正在解析网页...");
        String url = "https://zst.cjcp.cn/shdd/dlt-houqu-100.html";
        Map<String, String> map = new HashMap<>();
        map.put("queryCount", "100");

        List<CJWBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("charttab").get(0).lastElementChild();
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size() - 6; i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                CJWBean item = new CJWBean();
                result.add(item);

                if (i == itemElements.size() - 1 - 6) {//最后一期
                    List<CJWBean.CJWSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        CJWBean.CJWSingleDataBean subItem = new CJWBean.CJWSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<CJWBean.CJWSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).lastElementChild();
                    Log.i("本期出数：" + redNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.data = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.data[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    for (int j = 3; j < tdElements.size() - 1; j += 3) {
                        CJWBean.CJWSingleDataBean subItem = new CJWBean.CJWSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j).firstElementChild();
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.isTrue = tdStateElement.attr("alt").equals("正确");
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/dlt/hqdm/
     * 前区定胆
     */
    public static List<HQDMBean> doConnect8() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/dlt/hqdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<HQDMBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                HQDMBean item = new HQDMBean();
                result.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<HQDMBean.HQDMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        HQDMBean.HQDMSingleDataBean subItem = new HQDMBean.HQDMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<HQDMBean.HQDMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).firstElementChild();
                    Log.i("本期出数：" + redNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.data = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.data[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        HQDMBean.HQDMSingleDataBean subItem = new HQDMBean.HQDMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.isTrue = tdStateElement.hasClass("tflag");
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }
//            String title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/dlt/hqsdm/
     * 前区三胆
     */
    public static List<DateDataBean> doConnect10() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/dlt/hqsdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<DateDataBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
//                    .get();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");

                if (tdElements.hasClass("nowissue")) {//最后一期不要，跳过
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdElement.text());
                            Elements tdRedTextColorElements = tdElement.getElementsByTag("b");
                            Log.i("tdRedTextColorElements = " + tdRedTextColorElements.size());
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                            Log.i("选中数（不对） = " + tdElement.text());
                            Elements tdRedTextColorElements = tdElement.getElementsByTag("b");
                            Log.i("tdRedTextColorElements = " + tdRedTextColorElements.size());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                } else {
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = blueNumber.html().split(" ");
                    item.printNumbers = Arrays.asList(printNumbersStr);

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdHtml);
                            Elements tdRedTextColorElements = tdElement.getElementsByTag("b");
                            Log.i("tdRedTextColorElements = " + tdRedTextColorElements.size());
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                            Log.i("选中数（不对） = " + tdElement.text());
                            Elements tdRedTextColorElements = tdElement.getElementsByTag("b");
                            Log.i("tdRedTextColorElements = " + tdRedTextColorElements.size());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                }

            }
//            String title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/dlt/hqssm/
     * 后区杀三码
     */
    public static List<DateDataBean> doConnect11() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/dlt/hqssm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<DateDataBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
//                    .get();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");

                if (tdElements.hasClass("nowissue")) {//最后一期不要，跳过
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdHtml);
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                } else {
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = blueNumber.html().split(" ");
                    item.printNumbers = Arrays.asList(printNumbersStr);

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdHtml);
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                }

            }
//            String title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/dlt/lqdm/
     * 后区定胆
     */
    public static List<LQDMBean> doConnect9() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/dlt/lqdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<LQDMBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");
                LQDMBean item = new LQDMBean();
                result.add(item);

                if (i == itemElements.size() - 1) {//最后一期
                    List<LQDMBean.LQDMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        LQDMBean.LQDMSingleDataBean subItem = new LQDMBean.LQDMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        subItem.data = Integer.parseInt(tdHtml);
                    }
                } else {
                    List<LQDMBean.LQDMSingleDataBean> subItems = new ArrayList<>();
                    item.previewData = subItems;

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element redNumber = tdElements.get(1).lastElementChild();
                    Log.i("本期出数：" + redNumber.html());

                    String[] printNumbersStr = redNumber.html().split(" ");
                    item.data = new int[printNumbersStr.length];
                    for (int j = 0; j < printNumbersStr.length; j++) {
                        item.data[j] = Integer.parseInt(printNumbersStr[j]);
                    }

                    for (int j = 2; j < tdElements.size() - 1; j += 2) {
                        LQDMBean.LQDMSingleDataBean subItem = new LQDMBean.LQDMSingleDataBean();
                        subItems.add(subItem);
                        Element tdElement = tdElements.get(j);
                        String tdHtml = tdElement.html();
                        Element tdStateElement = tdElements.get(j + 1);
                        subItem.data = Integer.parseInt(tdHtml);
                        subItem.isTrue = tdStateElement.hasClass("tflag");
                    }
                    Element lastTdElement = tdElements.get(tdElements.size() - 1);

                    if ("全对".equals(lastTdElement.text())) {
                        item.hitCount = 10;
                    } else {
                        item.hitCount = Integer.parseInt(lastTdElement.text());
                    }
                }
            }
//            String title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * https://expert.78500.cn/ssq/lqsdm/
     * 后区三胆
     */
    public static List<DateDataBean> doConnect() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/lqsdm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<DateDataBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
//                    .get();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");

                if (tdElements.hasClass("nowissue")) {//最后一期不要，跳过
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray") || tdElement.hasClass("alltrue")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdHtml);
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                } else {
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = blueNumber.html().split(" ");
                    item.printNumbers = Arrays.asList(printNumbersStr);

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray") || tdElement.hasClass("alltrue")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdHtml);
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                }

            }
//            String title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * https://expert.78500.cn/ssq/lqssm/
     * 后区杀三码
     */
    public static List<DateDataBean> doConnect2() {
        Log.i("正在解析网页...");
        String url = "https://expert.78500.cn/ssq/lqssm/";
        Map<String, String> map = new HashMap<>();
        map.put("issue", "100");

        List<DateDataBean> result = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url)
                    .data(map)
                    .post();
//                    .get();
            Element itemElement = document.getElementsByClass("tRhover").get(0);
            Elements itemElements = itemElement.getElementsByTag("tr");

            for (int i = 0; i < itemElements.size(); i++) {
                Element trElement = itemElements.get(i);
                Elements tdElements = trElement.getElementsByTag("td");


                if (tdElements.hasClass("nowissue")) {//最后一期不要，跳过
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray") || tdElement.hasClass("alltrue")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdHtml);
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                } else {
                    DateDataBean item = new DateDataBean();
                    result.add(item);

                    Element tdNumber = tdElements.get(0);
                    item.title = tdNumber.html();
                    Log.i("当前期数：" + tdNumber.html());
                    Element blueNumber = tdElements.get(1).lastElementChild();
                    Log.i("本期出数：" + blueNumber.html());

                    String[] printNumbersStr = blueNumber.html().split(" ");
                    item.printNumbers = Arrays.asList(printNumbersStr);

                    List<SubItemBean> subItems = new ArrayList<>();
                    item.subItemNumbers = subItems;

                    for (int j = 2; j < tdElements.size(); j++) {
                        Element tdElement = tdElements.get(j);

                        if (tdElement.hasClass("tdgray") || tdElement.hasClass("alltrue")) {
                            continue;
                        }
                        SubItemBean subItem = new SubItemBean();

                        String tdHtml;
                        if (tdElement.hasAttr("style")) {
                            //有标色的一个解法
                            tdHtml = tdElement.html().replace("<font color=\"red\"><b>", "").replace("</b></font>", "");
//                        String[] splitTdHtml = tdHtml.split(",");
                            Log.i("选中数（全对） = " + tdHtml);
                        } else {
                            tdHtml = tdElement.html();
                            Log.i("选中数（不对） = " + tdElement.html());
                        }
                        subItem.subNumbers = Arrays.asList(tdHtml.split(","));
                        subItems.add(subItem);
                        Log.i("\n");
                    }
                }
            }
//            String title = document.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
