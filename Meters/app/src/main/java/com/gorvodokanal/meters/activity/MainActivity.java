package com.gorvodokanal.meters.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;


import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
 private String email;
 private String password;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void formSubmit(View view) {
        EditText login = findViewById(R.id.login);
        EditText password = findViewById(R.id.password);

        String loginValue = login.getText().toString();
        String passwordValue = password.getText().toString();

        Map<String, String> requestData = new HashMap<>();
        requestData.put("login", loginValue);
        requestData.put("password", passwordValue);

        final CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);

        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.AUTH_URL, requestData, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(MainActivity.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        Toast.makeText(MainActivity.this, "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(MainActivity.this, GeneralInfoActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });


    }


  private void setIntervalFromSharedPrefarences(SharedPreferences sharedPrefarences){
      email = String.valueOf(sharedPrefarences.getString("Change email","30"));
      password =  String.valueOf(sharedPrefarences.getString("Change password","30"));


    }
}
//сделать страницу на сервере для полуение дданных
//активити