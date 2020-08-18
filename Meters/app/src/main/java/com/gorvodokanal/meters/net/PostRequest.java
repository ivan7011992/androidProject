package com.gorvodokanal.meters.net;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

public class PostRequest {
    private RequestQueue mQueue;

    public PostRequest(RequestQueue mQueue) {
        this.mQueue = mQueue;

    }

    public void makeRequest(String url, Map<String, String> params, final VolleyJsonCallback callback) {

        try {
            JSONObject jsonData = new JSONObject();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                jsonData.put(entry.getKey(), entry.getValue());
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccess(response);
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("VolleyError", error.getMessage());
                }

            });

            mQueue.add(request);
        } catch(Exception e) {
            Log.d("VolleyError", e.getMessage());
        }
    }

}

