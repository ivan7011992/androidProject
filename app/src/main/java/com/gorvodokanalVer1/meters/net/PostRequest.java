package com.gorvodokanalVer1.meters.net;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class PostRequest {
    private RequestQueue mQueue;

    public PostRequest(RequestQueue mQueue) {
        this.mQueue = mQueue;

    }

    public void makeRequest(String url, Map<String, Object> params, final VolleyJsonSuccessCallback callback,VolleyJsonErrorCallback errorCallback) {

        try {
            JSONObject jsonData = convertToJson(params);


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
                    if(errorCallback!= null){
                    errorCallback.onError(error);
                    }
                }

            });

            mQueue.add(request);
        } catch (Exception e) {
            Log.d("VolleyError", e.getMessage());
        }
    }

    private JSONObject convertToJson(Map<String, Object> params) throws JSONException {
        JSONObject jsonData = new JSONObject();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof Map) {
                jsonData.put(entry.getKey(), convertToJson((Map) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                JSONArray array = new JSONArray();
                for (String obj : (List<String>) entry.getValue()) {
                    array.put(obj);
                }
                jsonData.put(entry.getKey(), array);
            } else {
                jsonData.put(entry.getKey(), entry.getValue());
            }
        }
        return jsonData;
    }

}

