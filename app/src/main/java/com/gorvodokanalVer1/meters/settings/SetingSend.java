package com.gorvodokanalVer1.meters.settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

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
        request.makeRequest(UrlCollection.CHANGE_PASSWORD_URL, requestData, new VolleyJsonSuccessCallback() {
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
            }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

                Toast.makeText(SetingSend.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
            }


        });

        }
    }

