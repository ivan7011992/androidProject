package com.gorvodokanalVer1.meters.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.activity.AppActivity;
import com.gorvodokanalVer1.meters.activity.ChangeEmailDialog;
import com.gorvodokanalVer1.meters.activity.ChangePasswordDialog;
import com.gorvodokanalVer1.meters.activity.CheckConfirmEmailDialog;
import com.gorvodokanalVer1.meters.activity.ConfirmedDialogMessage;
import com.gorvodokanalVer1.meters.activity.MainActivity;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

        findPreference("change_password").setOnPreferenceClickListener(preference -> showChangePasswordFragment());
        findPreference("change_email").setOnPreferenceClickListener(preference -> showChangeEmailFragment());


        CheckBoxPreference showContact = (CheckBoxPreference)findPreference("change_email1");
        showContact.setChecked(true);
        showContact.setTitle("Почта  подтверждена ✓");
      

 if(!UserModel.getInstance().isStatus()) {

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
