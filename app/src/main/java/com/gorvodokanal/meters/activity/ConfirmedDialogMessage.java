package com.gorvodokanal.meters.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.Iterator;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


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
        myFab = (FloatingActionButton) view.findViewById(R.id.floatingcloseConfirmDialog);

       myFab.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
              getDialog().dismiss();
           }
       });


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processSendEmail();
            }
        });
        return view;
    }
   public void processSendEmail(){

       final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
       GetRequest request = new GetRequest(mQueue);


       String requestUrl = UrlCollection.EMAIL_SEND_CONFIRM ;


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
                       Toast.makeText(getContext(), "Сообщение не  отправлено на почту", Toast.LENGTH_LONG).show();
                       return;
                   }

                   Toast.makeText(getContext(), "Сообщение отправлено на почту", Toast.LENGTH_LONG).show();

               } catch (Exception e) {
                   Log.e("valley", "error", e);
               }
           }
       });

   }

   }
