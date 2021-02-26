package com.gorvodokanalVer1.meters.settings;


import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.activity.ChangeEmailDialog;
import com.gorvodokanalVer1.meters.activity.ChangePasswordDialog;
import com.gorvodokanalVer1.meters.activity.ConfirmedDialogMessage;
import com.gorvodokanalVer1.meters.model.UserModel;



public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
        PreferenceScreen preferenceScreen = findPreference(getResources().getString(R.string.preferenceScreen));
        findPreference("change_password").setOnPreferenceClickListener(preference -> showChangePasswordFragment());
        findPreference("change_email").setOnPreferenceClickListener(preference -> showChangeEmailFragment());


        Preference showContact = findPreference("change_emailsConfirm");
        showContact.setTitle("Почта  подтверждена ✓");


        if(!UserModel.getInstance().isStatus()) {

            preferenceScreen.removePreference(showContact);
            ConfirmedDialogMessage confirmedDialogMessage = new ConfirmedDialogMessage(UserModel.getInstance().getEmail());
            confirmedDialogMessage.setTargetFragment(SettingsFragment.this, 1);
            confirmedDialogMessage.show(SettingsFragment.this.getFragmentManager(), "MyCustomDialog");
        }
    }



    private void showErrorDialog() {

        Toast.makeText(getContext(), "Произошла ошибка, попробуйте повторить попытку позже", Toast.LENGTH_LONG).show();

    }

    private boolean showChangePasswordFragment() {

        ChangePasswordDialog dialog = new ChangePasswordDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");
        return true;

    }

    private boolean showChangeEmailFragment() {

        ChangeEmailDialog dialog = new ChangeEmailDialog();
        dialog.setTargetFragment(this, 1);

        dialog.show(this.getFragmentManager(), "MyCustomDialog");
        return true;

    }


}
