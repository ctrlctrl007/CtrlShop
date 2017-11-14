package com.ctrl.ctrlshopmall.bean;

/**
 * Created by ctrlc on 2017/11/13.
 */

public class Category extends BaseBean{

    public Category() {
    }

    public Category(String name) {

        this.name = name;
    }

    public Category(long id ,String name) {
        this.setId(id);
        this.name = name;
    }
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
