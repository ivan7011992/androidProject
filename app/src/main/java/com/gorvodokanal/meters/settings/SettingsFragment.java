package com.gorvodokanal.meters.settings;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.activity.ChangeEmailDialog;
import com.gorvodokanal.meters.activity.ChangePasswordDialog;
import com.gorvodokanal.meters.activity.ConfirmedDialogMessage;
import com.gorvodokanal.meters.activity.HistoryMetersFragment;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

      findPreference("change_password").setOnPreferenceClickListener(preference -> showChangePasswordFragment());
        findPreference("change_email").setOnPreferenceClickListener(preference -> showChangeEmailFragment());
        getStatusConfirmEmail();




    }
   public void getStatusConfirmEmail() {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest request = new GetRequest(mQueue);
        String requestUrl = UrlCollection.GET_STATUS_CONFIRM_EMAIL;
        request.makeRequest(requestUrl, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {

                try {
                    if (!response.has("success")) {

                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                       JSONObject emailObject = response.getJSONObject("data");
                       String email = emailObject.getString("EMAIL");
                        ConfirmedDialogMessage confirmedDialogMessage = new ConfirmedDialogMessage(email);
                        confirmedDialogMessage.setTargetFragment(SettingsFragment.this, 1);
                        confirmedDialogMessage.show(SettingsFragment.this.getFragmentManager(), "MyCustomDialog");
                        Toast.makeText(getContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                        return;
                    }





                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }

            }
        });
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
