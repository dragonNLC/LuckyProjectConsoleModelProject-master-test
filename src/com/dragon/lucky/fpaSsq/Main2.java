package com.dragon.lucky.fpaSsq;

import com.dragon.lucky.fpaSsq.bean.DMBean;
import com.dragon.lucky.fpaSsq.bean.PreviewDataBean;
import com.dragon.lucky.fpaSsq.bean.SMBean;
import com.dragon.lucky.fpaSsq.utils.JsoupHelper;

import java.util.List;

public class Main2 {

    public static void main(String[] args) {
        List<PreviewDataBean> data1 = JsoupHelper.doConnect1();
        DMBean data2 = JsoupHelper.doConnect2();
        DMBean data3 = JsoupHelper.doConnect3();
        DMBean data4 = JsoupHelper.doConnect4();
        DMBean data5 = JsoupHelper.doConnect5();
        DMBean data6 = JsoupHelper.doConnect6();
        DMBean data7 = JsoupHelper.doConnect9();
        SMBean data8 = JsoupHelper.doConnect7();
        SMBean data9 = JsoupHelper.doConnect10();
        SMBean data10 = JsoupHelper.doConnect8();
        SMBean data11 = JsoupHelper.doConnect11();


    }

}
