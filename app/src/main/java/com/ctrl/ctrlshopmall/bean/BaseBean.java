package com.ctrl.ctrlshopmall.bean;

import java.io.Serializable;

/**
 * Created by ctrlc on 2017/11/9.
 */

public class BaseBean implements Serializable {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
