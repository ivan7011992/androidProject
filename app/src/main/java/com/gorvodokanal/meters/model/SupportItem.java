package com.gorvodokanal.meters.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;


public class SupportItem {
    private String date_question;
    private String question;
    private String date_response;
    private String response;






    public SupportItem(JSONObject row) throws JSONException {

        date_question =  row.getString("date_question");
        question =  row.getString("question");
        date_response =  row.getString("date_response");
        response =  row.getString("response");




    }

    public String getDate_question() {
         date_question = date_question.substring(0, Math.min(date_question.length(), 10));
        return date_question;
    }

    public String getQuestion() {
        return question;
    }

    public String getDate_response() {
        date_response =  date_response.substring(0, Math.min(date_response.length(), 10));
        return date_response;
    }

    public String getResponse() {
        return response;
    }
}





