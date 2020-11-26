package com.gorvodokanalVer1.meters.activity;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SwithAccount {

    public interface SuccessReponseHandler {
        void process();
    }

    private String login;
    private AppCompatActivity view;
    private SuccessReponseHandler successReponseHandler;

    public SwithAccount(
            String login,
            AppCompatActivity view,
            SuccessReponseHandler successReponseHandler) {
        this.login = login;
        this.view = view;
        this.successReponseHandler = successReponseHandler;
    }


    public void sendDataAuth() {

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("login", login);

        final RequestQueue mQueue = RequestQueueSingleton.getInstance(view);
        PostRequest request = new PostRequest(mQueue);


        request.makeRequest(UrlCollection.SWITH_ACCOUNT, requestData, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(view, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");
                    if (!isSuccess) {
                        String errorMessage = response.getString("message");

                        Toast.makeText(view, String.valueOf(errorMessage), Toast.LENGTH_LONG).show();

                    }

                    SwithAccount.this.successReponseHandler.process();


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        },
                new VolleyJsonErrorCallback() {
                    @Override
                    public void onError(VolleyError error) {
                        Toast.makeText(view, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                    }

    });


    }
}
