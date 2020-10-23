package com.gorvodokanal.meters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
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

        final CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);


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
                        Toast.makeText(view, "Не удалось перейти в личный кабинет", Toast.LENGTH_LONG).show();
                        ;
                    }
                    SwithAccount.this.successReponseHandler.process();


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });


    }
}
