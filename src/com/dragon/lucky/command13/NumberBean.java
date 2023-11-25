package com.dragon.lucky.command13;

import com.dragon.lucky.bean.ContentBean;
import com.dragon.lucky.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberBean {

    private List<Integer> data;
    private String dataStr;
    private String dataStr2;

    private boolean isChild;
    private boolean remove;

    public static boolean change;

    public NumberBean() {
        data = new ArrayList<>();
    }

    public NumberBean(List<Integer> data) {
        this.data = data;
        dataStr = data.toString();
        dataStr2 = data.toString().replace("[", "").replace("]", "");
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
        dataStr = data.toString();
        dataStr2 = data.toString().replace("[", "").replace("]", "");
    }

    public String getDataStr() {
        return dataStr;
    }

    public String getDataStr2() {
        return dataStr2;
    }

    public void resetDataStr() {
        dataStr = data.toString();
        dataStr2 = data.toString().replace("[", "").replace("]", "");
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    @Override
    public int hashCode() {
        if (!change) {
        return dataStr.hashCode();
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof NumberBean) {
            /*boolean result = ((NumberBean) obj).dataStr.equals(this.dataStr);
            if (result) {
                setCollectCount(getCollectCount() + 1);
                ((ResultBean) obj).setCollectCount(((ResultBean) obj).getCollectCount() + 1);
            }*/
            NumberBean nb = (NumberBean) obj;
            /*if ((nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23]"))
                    && (getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23, 30]"))) {
                Log.i("??????22222222");
            }
            if ((nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23, 30]"))
                    && (getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23]"))) {
                Log.i("??????22222222");
            }*/
            if (nb.getData().size() == this.getData().size()) {
//                Log.i("this.setChild(true)1");
                if (nb.getDataStr().equals(this.getDataStr())) {
//                    Log.i("this.setChild(true)2");
                    this.setChild(true);
                    return true;
                }
            } else {
                /*Log.i("nb.getDataStr() = " + nb.getDataStr());
                if ((nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23]") || nb.getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23, 30]"))
                        || (getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23]") || getDataStr().equals("[1, 11, 12, 13, 14, 15, 18, 21, 23, 30]"))) {
                    Log.i("??????");
                }*/
                List<Integer> largeData = null;
                List<Integer> miniData = null;
                boolean nbMaxThanNumber = false;
                if (nb.getData().size() > this.getData().size()) {
                    nbMaxThanNumber = true;
                    largeData = nb.getData();
                    miniData = this.getData();
                } else {
                    miniData = nb.getData();
                    largeData = this.getData();
                }
                if (largeData.containsAll(miniData)) {
                    if (!nbMaxThanNumber) {
                        nb.setChild(true);
//                        Log.i("nb.setChild(true)");
                    } else {
                        this.setChild(true);
//                        Log.i("this.setChild(true)");
                    }
                    return false;
                }
            }
        }
        return false;
    }

}


