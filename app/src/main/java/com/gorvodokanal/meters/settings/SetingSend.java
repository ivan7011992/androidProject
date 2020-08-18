package com.gorvodokanal.meters.settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SetingSend extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);
        String email = SettingVariable.email;
        String password = SettingVariable.password;


        Map<String, Object> requestData = new HashMap<>();
        requestData.put("login", email);
        requestData.put("password", password);
        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.SETTING_URL, requestData, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(SetingSend.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        Toast.makeText(SetingSend.this, "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
            });

        }
    }

