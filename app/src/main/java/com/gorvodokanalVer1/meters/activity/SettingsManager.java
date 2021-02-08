package com.gorvodokanalVer1.meters.activity;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public  class SettingsManager {
    public static SettingsManager instanse;

    public void saveCookies(Context context, String cookies) {
        if (cookies == null) {
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cookies", cookies);
        editor.commit();
    }

    public String getCookies(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String cookies = sharedPreferences.getString("cookies", "");
        if (cookies.contains("cookies")) {

            return "";
        }
        return cookies;
    }


    public void savelogin(Context context, String login) {
        if (login == null) {
            return;
        }

        // Save in the preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (null == sharedPreferences) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login", login);
        editor.commit();
    }

    public static String getlogin(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String login = sharedPreferences.getString("login", "");
        if (login.contains("login")) {

            return "";
        }
        return login;
    }

    public static SettingsManager getInstanse() {

        if (instanse == null) {
            instanse = new SettingsManager();
        }
       return instanse;
    }
}
