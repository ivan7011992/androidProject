package com.gorvodokanalVer1.meters.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;


public class ConfirmedDialogMessage extends DialogFragment {


    String email;
    FloatingActionButton myFab;
    public  ConfirmedDialogMessage(String email){
        this.email = email;
    }
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_dialog, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        Button buttonConfirm = (Button) view.findViewById(R.id.buttonConfirm);
        ((TextView) view.findViewById(R.id.emailConfirm)).setText(email);
        myFab =  view.findViewById(R.id.floatingcloseConfirmDialog);

        RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams) myFab.getLayoutParams();
        layoutparams.setMargins(0, 0, 0, 0);
        myFab.setLayoutParams(layoutparams);
       myFab.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
              getDialog().dismiss();
           }
       });


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();

            }
        });

        return view;
    }
    public  void sendMail(){
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest request = new GetRequest(mQueue);

        String requestUrl = UrlCollection.RESENDING_URL + "?userId=" + UserModel.getInstance().getUserId() + "&email=" +  UserModel.getInstance().getEmail();


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
                        Toast.makeText(getContext(), "Не удалось отправить сообщение", Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(getContext(), "Сообщение отправлено на почту", Toast.LENGTH_LONG).show();

                    ConfirmCode confirmCode = new ConfirmCode(UserModel.getInstance().getLogin(), getActivity());
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    confirmCode.show(fm, "NoticeDialogFragment");


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });
    }

   }
