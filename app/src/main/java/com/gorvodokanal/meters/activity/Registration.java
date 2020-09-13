package com.gorvodokanal.meters.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {


    private static HashMap<String, EditText> dataRegistr = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        String kod = ((EditText) findViewById(R.id.kod)).getText().toString();
        String street= ((EditText) findViewById(R.id.street)).getText().toString();
        String house = ((EditText) findViewById(R.id.house)).getText().toString();
        String flat = ((EditText) findViewById(R.id.flat)).getText().toString();
        String fio = ((EditText) findViewById(R.id.fio)).getText().toString();
        String passwordReg = ((EditText) findViewById(R.id.passwordReg)).getText().toString();

       EditText passwordReg1 = (EditText) findViewById(R.id.passwordReg);
    //    passwordReg1.addTextChangedListener(new PatternedTextWatcher("###-###-####"));
        String ConfirmPassword = ((EditText) findViewById(R.id.ConfirmPassword)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        kod= kod.trim();
        street = street.trim();
        house =  house.trim();
        flat = flat.trim();
        fio= fio.trim();
        passwordReg = passwordReg.trim();
        ConfirmPassword= ConfirmPassword.trim();
        phone = phone.trim();
        email = email.trim();


        if(kod.isEmpty()) {
            displayError("Введите код");
            return;
        }
        if(street .isEmpty()) {
            displayError("Введите улицу");
            return;
        }
        if(house.isEmpty()) {
            displayError("Введите дом");
            return;
        }
        if(flat.isEmpty()) {
            displayError("Введите квартиру");
            return;
        }
        if(fio.isEmpty()) {
            displayError("Введите фамилию");
            return;
        }
        if(passwordReg.isEmpty()) {
            displayError("Введите пароль");
            return;
        }
        if(ConfirmPassword.isEmpty()) {
            displayError("Подтвердите пароль");
            return;
        }
        if(phone.isEmpty()) {
            displayError("Введите телефон");
            return;
        }
        if(email.isEmpty()) {
            displayError("Введете email");
            return;
        }


    }

    private void changePasswordOnServer(String kod, String  street, String house,String flat,String fio,String password,String ConfirmPassword ,String phone,String email) {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);


        Map<String, Object> requestData = new HashMap<>();
        requestData.put("kod", kod);
        requestData.put("street", street);
        requestData.put("house", house);
        requestData.put("kflat", flat);
        requestData.put("fio", fio);
        requestData.put("password", password);
        requestData.put("ConfirmPassword", ConfirmPassword);
        requestData.put("phone", phone);
        requestData.put("email", email);


        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.CHANGE_PASSWORD_URL, requestData, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(Registration.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        Toast.makeText(Registration.this, "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });
    }
    private void displayError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}