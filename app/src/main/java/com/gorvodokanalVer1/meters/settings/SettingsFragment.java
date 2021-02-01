package com.gorvodokanalVer1.meters.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
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
        getStatusConfirmEmail();


    }

    public void getStatusConfirmEmail() {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        PostRequest request = new PostRequest(mQueue);
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("login", "10-6666666");

        request.makeRequest(UrlCollection.GET_STATUS_CONFIRM_EMAIL, requestData, new VolleyJsonSuccessCallback() {
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


                        String errorMessage = response.getString("message");


                       // JSONArray rows = response.getJSONArray("data");
                       // JSONObject userData = (JSONObject) rows.getJSONObject(0);
                        JSONObject emailObject = response.getJSONObject("data");
                        String email = emailObject.getString("EMAIL");
                        ConfirmedDialogMessage confirmedDialogMessage = new ConfirmedDialogMessage(email);
                        confirmedDialogMessage.setTargetFragment(SettingsFragment.this, 1);
                        confirmedDialogMessage.show(SettingsFragment.this.getFragmentManager(), "MyCustomDialog");

                       Toast.makeText(getContext(), String.valueOf(errorMessage), Toast.LENGTH_LONG).show();
                        return;
                    }



                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

                showErrorDialog();
            }


        });


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
