package com.gorvodokanal.meters.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.gorvodokanal.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener  {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();//получаем количество настроек и далее мы можем получать конкретную настройку при помоши итерации
        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);

            //исключаем настроки с preference настрйокой звука.если настойка не checkboxpreference
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");


            }
        }
        Preference preference = findPreference("Change email");
        preference.setOnPreferenceChangeListener(this);
    }
    private void setPreferenceLabel(Preference preference, String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0) {
                listPreference.setSummary(listPreference.getEntries()[index]);//getEntries()[index] этот метод возращает массив
            }
        }else if (preference instanceof EditTextPreference){
            preference.setSummary(value);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(!(preference instanceof CheckBoxPreference)){
            String value = sharedPreferences.getString(preference.getKey(),"");
            setPreferenceLabel(preference,value);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast toast = Toast.makeText(getContext(),"Please enter an integer number",Toast.LENGTH_LONG);
        if(preference.getKey().equals("default_interval")) {
            String defaultIntervalString = (String) newValue;

            try {
                int defaultIntervar = Integer.parseInt(defaultIntervalString);//пробуем распознать сраку как целое число
            } catch (NumberFormatException nef) {
                toast.show();
                return false;

            }
        }
        return true;
    }
}
