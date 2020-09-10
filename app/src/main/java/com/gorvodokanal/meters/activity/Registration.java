package com.gorvodokanal.meters.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.gorvodokanal.R;

import java.util.ArrayList;

public class Registration extends AppCompatActivity {
    EditText kod;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ((EditText) findViewById(R.id.kod)).getText();



    }

}