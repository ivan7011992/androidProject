package com.gorvodokanal.meters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

public class SwithAccount extends AppActivity {
   private String login;
    public  SwithAccount(String login){
        this.login = login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendDataAuth();
    }

    public void  sendDataAuth(){
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);
        GetRequest request = new GetRequest(mQueue);
        final CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);


        String requestUrl = UrlCollection.RECOVERY_URL + "?login=" + login;

        request.makeRequest(requestUrl, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(SwithAccount.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");
                    if (!isSuccess) {
                        Toast.makeText(SwithAccount.this, "Не удалось перейти в личный кабинет", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent( SwithAccount.this, AppActivity.class);
                    startActivity(intent);
                    Toast.makeText(SwithAccount.this, "Успех", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });


    }
}
