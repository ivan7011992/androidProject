package com.gorvodokanalVer1.meters.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmCode extends DialogFragment {
private  String login;
Activity mActivity;

;

public ConfirmCode(String login, Activity activity ){
    this.login = login;
    this.mActivity = activity;
}


    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confim_code_dialog, container, false);
        Button confirmEmail  = (Button) view.findViewById(R.id.confirmEmail);


        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }


        confirmEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setCodeConfirm(getCode());


            }
        });

        enableSubmitButton(view);
        return view;
    }

   public void setCodeConfirm(String code){


       final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());

       Map<String, Object> requestData = new HashMap<>();

       requestData.put("code", code);
       requestData.put("login", login);

       PostRequest request = new PostRequest(mQueue);
       request.makeRequest(UrlCollection.CONFIRM_EMAIL, requestData, new VolleyJsonSuccessCallback() {
                   @Override
                   public void onSuccess(JSONObject response) {
                       try {
                           if (!response.has("success")) {
                               Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                               Toast.makeText(mActivity, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                               return;
                           }
                           final boolean isSuccess = response.getBoolean("success");
                          if(!isSuccess){
                              Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG).show();
                              return;
                          }
                           UserModel.createInstanceFromJson(response.getJSONObject("userData"));
                           Toast.makeText(mActivity, "Аккаунт активирован", Toast.LENGTH_LONG).show();
                           getDialog().dismiss();
                           Intent intent = new Intent(mActivity, AppActivity.class);
                           mActivity.startActivity(intent);

                       } catch (Exception e) {
                           Log.e("valley", "error", e);
                       }
                   }
               },
               new VolleyJsonErrorCallback() {
                   @Override
                   public void onError(VolleyError error) {

                   }

               });


   }
    private String getCode() {

        String confirmDigatal1 = ((EditText) getView().findViewById(R.id.digitOne)).getText().toString();
        String confirmDigatal2 = ((EditText) getView().findViewById(R.id.digitTwo)).getText().toString();
        String confirmDigatal3 = ((EditText) getView().findViewById(R.id.digitThree)).getText().toString();
        String confirmDigatal4 = ((EditText) getView().findViewById(R.id.digitFour)).getText().toString();

        return confirmDigatal1 + confirmDigatal2 + confirmDigatal3 + confirmDigatal4;

    }

    private void enableSubmitButton(View view) {
        ArrayList<EditText> listInput = new ArrayList<>();
        int[] inpitId = {R.id.digitOne, R.id.digitTwo, R.id.digitThree, R.id.digitFour};
        for (int inoutId : inpitId) {
            listInput.add((EditText) view.findViewById(inoutId));
        }


        for (int i = 0; i < listInput.size(); i++) {


            final int finalI = i;
            listInput.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (editable.toString().isEmpty()) {
//                        if (finalI > 0) {
//                            EditText nextInput = listInput.get(finalI - 1);
//                            nextInput.requestFocus();
//                        }
                    }else{
                        if (finalI < listInput.size() - 1) {
                            EditText nextInput = listInput.get(finalI + 1);
                            nextInput.requestFocus();
                        }
                    }
                }

            });


            listInput.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    String code = getCode();
                    if (code.length() != 4) {
                        Toast.makeText(getContext(), "Введие код", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }
}