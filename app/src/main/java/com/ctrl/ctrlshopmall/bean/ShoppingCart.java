package com.ctrl.ctrlshopmall.bean;

import java.io.Serializable;

/**
 * Created by ctrlc on 2017/11/14.
 */

public class ShoppingCart extends Ware implements Serializable {
    private int count;
    private boolean isChecked = false;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
