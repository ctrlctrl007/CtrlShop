package com.ctrl.ctrlshopmall.bean;


/**
 * Created by ctrlc on 2017/11/23.
 */

public class User extends BaseBean {
    private  String email;
    private  String logo_url;
    private  String username;
    private  String mobi;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobi() {
        return mobi;
    }

    public void setMobi(String mobi) {
        this.mobi = mobi;
    }
}
