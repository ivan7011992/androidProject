package com.gorvodokanal.meters.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gorvodokanal.R;

import java.util.ArrayList;
import java.util.HashMap;

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
    private void displayError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}