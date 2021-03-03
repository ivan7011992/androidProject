package com.gorvodokanalVer1.meters.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanalVer1.meters.settings.SettingsFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmailDialog extends DialogFragment {
    EditText emailDialog;

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_email_dialog, container, false);
        View changeEmailButton = view.findViewById(R.id.changeemailButton);
        emailDialog = view.findViewById(R.id.emailDialog);
        emailDialog.setText(UserModel.getInstance().getEmail());
        changeEmailButton.setOnClickListener(v -> processUserData());
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

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
        request.makeRequest(UrlCollection.CHANGE_EMAIL_URL, requestData, new VolleyJsonSuccessCallback() {
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
                        Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Toast.makeText(getContext(), "Email  изменен", Toast.LENGTH_LONG).show();

                        UserModel.getInstance().setEmail(email);
                        UserModel.getInstance().setStatus(false);
                        getDialog().dismiss();


                        ConfirmedDialogMessage confirmedDialogMessage = new ConfirmedDialogMessage(UserModel.getInstance().getEmail());
                        confirmedDialogMessage.setTargetFragment(ChangeEmailDialog.this, 1);
                        confirmedDialogMessage.show(ChangeEmailDialog.this.getFragmentManager(), "MyCustomDialog");

                    }

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        },
                new VolleyJsonErrorCallback() {
                    @Override
                    public void onError(VolleyError error) {
                        showErrorDialog();
                    }

                });
    }



    private void displayError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    private void  showErrorDialog(){
        NoConnection dialog = new NoConnection();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");

    }
}
