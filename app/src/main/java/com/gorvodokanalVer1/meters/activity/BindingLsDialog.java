package com.gorvodokanalVer1.meters.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BindingLsDialog extends DialogFragment {
   Button bindingButton;


    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.binding_ls_dialog, container, false);


        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }






        bindingButton = view.findViewById(R.id. bindingButton);
        bindingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    passDataBindingUser(view);
            }
        });

        return view;
    }


    public void passDataBindingUser() {
        View view = getView();
        String numberLs = ((EditText) view.findViewById(R.id.numberLs)).getText().toString();
        String bindingStreet = ((EditText) view.findViewById(R.id.bindingStreet)).getText().toString();
        String bindingHouse = ((EditText) view.findViewById(R.id.bindingHouse)).getText().toString();
        String bindingFlat = ((EditText) view.findViewById(R.id.bindingFlat)).getText().toString();
        String binfingFio = ((EditText) view.findViewById(R.id.binfingFio)).getText().toString();
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("numberLs", numberLs);
        requestData.put("bindingStreet", bindingStreet);
        requestData.put("bindingHouse", bindingHouse);
        requestData.put("bindingFlat", bindingFlat);
        requestData.put("binfingFio", binfingFio);



        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());

        HashMap<String, String> emptyErrorMessages = new HashMap<>();
        emptyErrorMessages.put("numberLs", "Введите код");
        emptyErrorMessages.put("bindingStreet", "Введите улицу");
        emptyErrorMessages.put("bindingHouse", "Введите дом");
        emptyErrorMessages.put("bindingFlat", "Введите квартиру");
        emptyErrorMessages.put("binfingFio", "Введите Введите фамилию");
     

//        ArrayList<String> errors = new ArrayList<>();
//        for (Map.Entry<String, String> errorEntry : emptyErrorMessages.entrySet()) {
//            String value = data.get(errorEntry.getKey());
//            if (value.isEmpty()) {
//                errors.add(errorEntry.getValue());
//            }

    //    }
        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.BILDING_LS, requestData, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                      //  Toast.makeText(Registration.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        JSONObject rows = response.getJSONObject("errors");

                        StringBuilder errorBuilder = new StringBuilder();
                        Iterator<String> temp = rows.keys();
                        while (temp.hasNext()) {
                            String key = temp.next();
                            String error = rows.getString(key);
                            errorBuilder.append(error).append("\n");
                        }
                    //    displayError("Обраружены ошибки: " + errorBuilder.toString());

                        return;
                    }
                //    Toast.makeText(Registration.this, "Регистрация  удалась", Toast.LENGTH_LONG).show();
                    int userId = response.getInt("userId");

                   // RegistrationFinalDialog dialog = new RegistrationFinalDialog(userId,((EditText) findViewById(R.id.emailReg)).getText().toString());

                   // dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }


            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

              //  showErrorDialog();
            }
        });
    }


    }
