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
import com.gorvodokanal.meters.net.VolleyJsonCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmailDialog extends DialogFragment {


    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_email_dialog, container, false);
        View changePasswordButton = view.findViewById(R.id.changeemailButton);
        changePasswordButton.setOnClickListener(v -> processUserData());
        return view;

    }

    private void processUserData() {
        String email = ((EditText) getView().findViewById(R.id.emailDialog)).getText().toString();


        email = email.trim();


        if (email.isEmpty()) {
            displayError("Введите почту");
            return;
        }


        changeEmailOnServer(email);


    }

    private void changeEmailOnServer(String email) {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getView().getContext());


        Map<String, Object> requestData = new HashMap<>();
        requestData.put("email", email);


        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.CHANGE_EMAIL_URL, requestData, new VolleyJsonCallback() {
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
                        Toast.makeText(getContext(),  response.has("message") ? response.getString("message") : "Неизвестная ошибка", Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        getDialog().dismiss();
                        Toast.makeText(getContext(), response.has("message") ? response.getString("message") : "Email изменен", Toast.LENGTH_LONG).show();
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