package com.gorvodokanal.meters.net;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class GetRequest {
    TextView title1;
    private RequestQueue mQueue;

    public GetRequest(RequestQueue mQueue) {
        this.mQueue = mQueue;
    }

    public void makeRequest(String url,final VolleyJsonCallback callback) {

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {//здесь будем распозновать наш json объект
                    callback.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();//метод для стекка ошибок
                }
            });
            mQueue.add(request);
        } catch (Exception e) {
            Log.d("VolleyError", e.getMessage());
        }

    }


}
