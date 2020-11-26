package com.gorvodokanalVer1.meters.activity;

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
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmailDialog extends DialogFragment {
    EditText emailDialog;

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_email_dialog, container, false);
        View changePasswordButton = view.findViewById(R.id.changeemailButton);
        emailDialog = view.findViewById(R.id.emailDialog);
        user_get_info();
        changePasswordButton.setOnClickListener(v -> processUserData());
        return view;

    }

    private void processUserData() {
        String email = ((EditText) getView().findViewById(R.id.emailDialog)).getText().toString();


        email = email.trim();

        ConfirmedDialogMessage confirmedDialogMessage = new ConfirmedDialogMessage(email);
        confirmedDialogMessage.setTargetFragment(ChangeEmailDialog.this, 1);
        confirmedDialogMessage.show(ChangeEmailDialog.this.getFragmentManager(), "MyCustomDialog");
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
                        Toast.makeText(getContext(), "Email не изменен", Toast.LENGTH_LONG).show();
                      //  Toast.makeText(getContext(), response.has("message") ? response.getString("message") : "Неизвестная ошибка", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Toast.makeText(getContext(), "Email  изменен", Toast.LENGTH_LONG).show();
                       // Toast.makeText(getContext(), response.has("message") ? response.getString("message") : "Email изменен", Toast.LENGTH_LONG).show();
                        getDialog().dismiss();
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

    private void user_get_info() {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest request = new GetRequest(mQueue);
        String requestUrl = UrlCollection.GET_USER_INFO_URL;
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
                        Toast.makeText(getContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                        return;
                    }

                    JSONObject emailObject = response.getJSONObject("data");
                    String email = emailObject.getString("EMAIL");
                    emailDialog.setText(email);
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
