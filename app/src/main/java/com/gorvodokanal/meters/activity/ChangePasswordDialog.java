package com.gorvodokanal.meters.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanal.meters.settings.SettingsFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordDialog extends DialogFragment {


    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_password_dialog, container, false);
        View changePasswordButton = view.findViewById(R.id.changePawwordButton);
        changePasswordButton.setOnClickListener(v -> processUserData());
        return view;

    }

    private void processUserData() {

        SettingsFragment confirm = new SettingsFragment();
        confirm.getStatusConfirmEmail();
        String oldPassword = ((EditText) getView().findViewById(R.id.oldPassword)).getText().toString();
        String newPassword = ((EditText) getView().findViewById(R.id.newPassword)).getText().toString();
        String confirmationPassword = ((EditText) getView().findViewById(R.id.confirmationPassword)).getText().toString();
        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();
        confirmationPassword = confirmationPassword.trim();


        if (oldPassword.isEmpty()) {
            displayError("Нужно ввести старый пароль");
            return;
        }
        if (!newPassword.equals(confirmationPassword)) {
            displayError("Пароли не совпадают");
            return;
        }

        changePasswordOnServer(oldPassword, newPassword, confirmationPassword);


    }

    private void changePasswordOnServer(String oldPassword, String newPassword, String confirmationPassword) {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getView().getContext());


        Map<String, Object> requestData = new HashMap<>();
        requestData.put("oldPassword", oldPassword);
        requestData.put("newPassword", newPassword);

        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.CHANGE_PASSWORD_URL, requestData, new VolleyJsonSuccessCallback() {
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

                        Toast.makeText(getContext(), response.has("message") ? response.getString("message") : "Неизвестная ошибка", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        getDialog().dismiss();
                        Toast.makeText(getContext(), response.has("message") ? response.getString("message") : "Пароль изменен", Toast.LENGTH_LONG).show();

                    }


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });
    }

    private void displayError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
}