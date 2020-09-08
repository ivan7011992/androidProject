package com.gorvodokanal.meters.settings;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.activity.ChangePasswordDialog;
import com.gorvodokanal.meters.activity.HistoryMetersFragment;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

      findPreference("change_password").setOnPreferenceClickListener(preference -> showChangePasswordFragment());
    }

    private boolean showChangePasswordFragment() {
        Toast.makeText(getContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
        ChangePasswordDialog dialog = new ChangePasswordDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");
        return true;

    }


}
