package com.gorvodokanalVer1.meters.model;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class BindingItem {
    private String loginls;
    private  String user_id;



    public BindingItem(JSONObject row) throws JSONException {

        loginls =  row.getString("LOGIN");
        user_id = row.getString("ID");

    }


    public String getLoginls() {
        return loginls;
    }

    public String getUser_id() {
        return user_id;
    }
}
