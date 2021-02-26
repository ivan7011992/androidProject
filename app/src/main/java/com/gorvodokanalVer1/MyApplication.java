package com.gorvodokanalVer1;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.AcraCore;

import org.acra.annotation.AcraHttpSender;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;
import org.acra.sender.HttpSender;


@AcraCore(buildConfigClass = BuildConfig.class)
@AcraHttpSender(uri = "https://www.gorvodokanal.com/mobile_app/log_receiver.php",
        httpMethod = HttpSender.Method.POST)

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

}
