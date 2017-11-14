package com.ctrl.ctrlshopmall.bean;

import java.io.Serializable;

/**
 * Created by ctrlc on 2017/11/9.
 */

public class BaseBean implements Serializable {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
