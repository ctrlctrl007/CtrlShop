package com.ctrl.ctrlshopmall.msg;

/**
 * Created by ctrlc on 2017/11/23.
 */

public class LoginResMsg<T> extends BaseResMsg {

    private String token;
    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
