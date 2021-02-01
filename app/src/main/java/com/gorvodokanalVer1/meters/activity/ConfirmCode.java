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
                setCodeConfirm();
                getDialog().dismiss();

            }
        });


        return view;
    }

   public void setCodeConfirm(){

       String confirmDigatal1 = ((EditText) getView().findViewById(R.id.digitOne)).getText().toString();
       String confirmDigatal2 = ((EditText) getView().findViewById(R.id.digitTwo)).getText().toString();
       String confirmDigatal3 = ((EditText) getView().findViewById(R.id.digitThree)).getText().toString();
       String confirmDigatal4 = ((EditText) getView().findViewById(R.id.digitFour)).getText().toString();


       String code = confirmDigatal1 + confirmDigatal2 + confirmDigatal3 + confirmDigatal4;
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
                               Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                               return;
                           }
                           final boolean isSuccess = response.getBoolean("success");
                          if(!isSuccess){
                              Toast.makeText(getContext(), "Неудалось активировать аккаунт, попробуйте повторить попытку позже", Toast.LENGTH_LONG).show();
                              return;
                          }
                           UserModel.createInstanceFromJson(response.getJSONObject("userData"));
                           Intent intent = new Intent(mActivity, AppActivity.class);
                           mActivity.startActivity(intent);
                           Toast.makeText(getContext(), "Аккаунт акктивирован", Toast.LENGTH_LONG).show();
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

}