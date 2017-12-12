package com.ctrl.ctrlshopmall.utils;

import android.content.Context;

import com.ctrl.ctrlshopmall.bean.Banner;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ctrlc on 2017/11/9.
 */

public class Utility {

    /**
     * 判断是否是正确手机号
     */
   public static boolean checkPhone(String phone){
       String rule = "^1(3|5|7|8|4)\\d{9}";
       Pattern p = Pattern.compile(rule);
       Matcher m = p.matcher(phone);
       return m.matches();
   }
}
