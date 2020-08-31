package com.gorvodokanal.meters.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.UrlCollection;

public class header extends AppActivity{
 TextView kod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
        kod = findViewById(R.id.text);


    }

    public void showKod(){
        kod.setText(UrlCollection.KOD);
    }
}
