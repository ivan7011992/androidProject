package com.gorvodokanal.meters.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.navigation.NavigationView;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;
import com.gorvodokanal.meters.settings.Setting;
import com.gorvodokanal.meters.settings.SettingVariable;


import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String email;
    private String password;
    SharedPreferences sharedPreferences;

    private TextView email1;
    public Button buttom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setIntervalFromSharedPrefarences(sharedPreferences);
        buttom = findViewById(R.id.button);
      //  buttom.getBackground().setAlpha(64);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }


    public void formSubmit(View view) {
        EditText login = findViewById(R.id.login);
        EditText password = findViewById(R.id.password);

        String loginValue = login.getText().toString();
        String passwordValue = password.getText().toString();

        Map<String, Object> requestData = new HashMap<>();
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

                    Intent intent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//этот метод вызввает когда происходит нажатие на каком-то из эдлементов меню
        int id = item.getItemId();// получаем элемнт которые был выбран в меню
        if (id == R.id.action_setting) {
            Intent openSetting = new Intent(this, Setting.class);
            startActivity(openSetting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setIntervalFromSharedPrefarences(SharedPreferences sharedPrefarences) {

        try {
            email = sharedPrefarences.getString("Change email", "30");

        } catch (NumberFormatException nef) {//ксли exception то влю бом слкчае м выведится торока ниже
            Toast.makeText(this, "Number format exceprion", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {//ксли exception то влю бом слкчае м выведится торока ниже
            Toast.makeText(this, "Some error happanns", Toast.LENGTH_LONG).show();
        }
        email = sharedPrefarences.getString("Change email", "30");
//        email1 = findViewById(R.id.log);
//        email1.setText("kkkk" + email);
        SettingVariable.email = email;
        password = sharedPrefarences.getString("Change password", "30");
        Log.i("email", "" + email);
        SettingVariable.password = password;


    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("Change email")) {
            setIntervalFromSharedPrefarences(sharedPreferences);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
//сделать страницу на сервере для полуение дданных
//активити