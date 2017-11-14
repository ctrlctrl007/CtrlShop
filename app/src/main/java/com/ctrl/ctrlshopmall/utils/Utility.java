package com.ctrl.ctrlshopmall.utils;

import com.ctrl.ctrlshopmall.bean.Banner;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctrlc on 2017/11/9.
 */

public class Utility {

    public static List<Banner> handleBannerResponse(String response){
        List<Banner> banners = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(response);
            for(int i = 0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                Banner banner = new Gson().fromJson(object.toString(),Banner.class);
                banners.add(banner);
            }
            return banners;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
