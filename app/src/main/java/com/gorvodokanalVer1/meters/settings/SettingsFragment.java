package com.gorvodokanalVer1.meters.settings;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.RequestQueue;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.activity.ChangeEmailDialog;
import com.gorvodokanalVer1.meters.activity.ChangePasswordDialog;
import com.gorvodokanalVer1.meters.activity.ConfirmedDialogMessage;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

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
