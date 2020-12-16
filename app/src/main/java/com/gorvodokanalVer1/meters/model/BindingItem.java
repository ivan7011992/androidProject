package com.gorvodokanalVer1.meters.model;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class BindingItem {
    private String loginls;



    public BindingItem(JSONObject row) throws JSONException {

        loginls =  row.getString("LOGIN");




    }


    public String getLoginls() {
        return loginls;
    }
}
