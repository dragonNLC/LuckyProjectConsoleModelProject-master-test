package com.dragon.lucky.bean;


public class ContentBean {

    public static final int ITEM_TYPE_INPUT_HEAD_LAYOUT = 1;
    public static final int ITEM_TYPE_CONDITION_LAYOUT = 2;
    public static final int ITEM_TYPE_CONDITION_FOOTER_LAYOUT = 3;
    public static final int ITEM_TYPE_RESULT_HEAD_LAYOUT = 4;
    public static final int ITEM_TYPE_RESULT_LAYOUT = 5;

    private int itemType;

    public ContentBean(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

}
