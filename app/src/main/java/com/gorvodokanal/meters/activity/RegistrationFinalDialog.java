package com.gorvodokanal.meters.activity;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.historyUtilClass.SummaryHistoryItemAdapter;
import com.gorvodokanal.meters.model.SummaryHistoryItem;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class RegistrationFinalDialog extends DialogFragment {
    private int userID;
    private String email;


    public RegistrationFinalDialog(int userID,String email) {

        this.userID = userID;
        this.email = email;
    }

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration_final_dialog, container, false);
        TextView mActionOk = (TextView) view.findViewById(R.id.registrationFinealText);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        ((TextView) view.findViewById(R.id.confirmText)).setText("Абонент успешно зарегистрирован. На почтовый ящик " + email + "выслано письмо с инструкцией по активации аккаунта. Если письмо отсутсвует, проверьте папку 'Спам'   " +
                "                                       Если вам не пришло письмо на почту, то проверьте корректность введенных вами данных. Если почта указана неверно, у вас есть возможность изменить электронный адрес, указав новуб посту в поле ниже");
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
                GetRequest request = new GetRequest(mQueue);
                String requestUrl = UrlCollection.RESENDING_URL + "?user_id" + userID ;


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


                        } catch (Exception e) {
                            Log.e("valley", "error", e);
                        }
                    }
                });



            }
        });


        return view;
    }

}