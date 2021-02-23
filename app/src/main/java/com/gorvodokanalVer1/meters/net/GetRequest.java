package com.gorvodokanalVer1.meters.net;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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

    public void makeRequest(String url, final VolleyJsonSuccessCallback successCallback) {
        makeRequest(url, successCallback, null);
    }

    public void makeRequest(String url, final VolleyJsonSuccessCallback successCallback, VolleyJsonErrorCallback errorCallback) {

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {//здесь будем распозновать наш json объект
                    successCallback.onSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (errorCallback != null) {
                        errorCallback.onError(error);
                    }
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(request);
        } catch (Exception e) {
            Log.d("VolleyError", e.getMessage());
        }

    }


}
