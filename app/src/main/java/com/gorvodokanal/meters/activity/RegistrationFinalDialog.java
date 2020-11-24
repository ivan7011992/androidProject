package com.gorvodokanal.meters.activity;


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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class RegistrationFinalDialog extends DialogFragment {
    private int userID;
    private String email;

    public RegistrationFinalDialog() {


    }


    public RegistrationFinalDialog(int userID,String email) {

        this.userID = userID;
        this.email = email;
    }

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration_final_dialog, container, false);
        TextView sendMail = (TextView) view.findViewById(R.id.buttonResentMail);
        TextView closeDialog  = (TextView) view.findViewById(R.id.buttonCloseRegistration);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        ((TextView) view.findViewById(R.id.confirmText)).setText("Абонент успешно зарегистрирован. На почтовый ящик " + email + "выслано письмо с инструкцией по активации аккаунта. Если письмо отсутствует, проверьте папку 'Спам'" +
                "Если вам не пришло письмо на почту, то проверьте корректность введенных вами данных. Если почта указана неверно, у вас есть возможность изменить электронный адрес, указав новую почту в поле ниже");
        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
                GetRequest request = new GetRequest(mQueue);
                EditText editemailConfirm =  view.findViewById(R.id.editemailConfirm);
              String emailReg =editemailConfirm.getText().toString();
                String requestUrl = UrlCollection.RESENDING_URL + "?user_id=" + userID + "&email=" +  emailReg;


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

                            Toast.makeText(getContext(), "Сообщение отправлено на почту", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(),MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e("valley", "error", e);
                        }
                    }
                });



            }
        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();

                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);

            }
        });


        return view;
    }

}