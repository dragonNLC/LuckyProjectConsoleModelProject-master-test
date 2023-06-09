package com.dragon.lucky.bean;

public class ControlFooterBean extends ContentBean {

    private boolean enableClick;

    public ControlFooterBean() {
        super(ContentBean.ITEM_TYPE_CONDITION_FOOTER_LAYOUT);
    }

    public boolean isEnableClick() {
        return enableClick;
    }

    public void setEnableClick(boolean enableClick) {
        this.enableClick = enableClick;
    }
}
