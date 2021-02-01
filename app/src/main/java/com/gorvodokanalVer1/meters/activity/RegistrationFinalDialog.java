package com.gorvodokanalVer1.meters.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class RegistrationFinalDialog extends DialogFragment {

    private String email;
    private String login;
    private int userID;
    Activity activityReg;
    Button finishRegistration;

    public int varible = 0;

    public RegistrationFinalDialog() {


    }


    public RegistrationFinalDialog(int userID, String email, String login, Activity activity) {

        this.email = email;
        this.login = login;
        this.activityReg = activity;
        this.userID = userID;
    }

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration_final_dialog, container, false);
        FloatingActionButton floatingcloseConfirmDialog = (FloatingActionButton) view.findViewById(R.id.floatingcloseFinealRegDialog);
        finishRegistration = (Button) view.findViewById(R.id.finishRegistration);
        TextView noSendEmail = (TextView) view.findViewById(R.id.noSendEmail);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        ((TextView) view.findViewById(R.id.confirmText)).setText("Абонент успешно зарегистрирован. На почтовый ящик " + email + "выслано письмо с кодом активации.Введите код из письма в форме ниже: ");


        enableSubmitButton(view);


        finishRegistration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCode().length() == 4) {
                    setCodeConfirm(getCode());
                    getDialog().dismiss();
                }
            }
        });


        floatingcloseConfirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                Intent intent = new Intent(activityReg, MainActivity.class);
                activityReg.startActivity(intent);
                getDialog().dismiss();

                // ConfirmCode confirmCode = new ConfirmCode(login,activityReg);
                // FragmentManager fm = getActivity().getSupportFragmentManager();
                // confirmCode.show(fm, "NoticeDialogFragment");
                // Intent intent = new Intent(getContext(),MainActivity.class);
                //  startActivity(intent);

            }

        });

        noSendEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ResendEmailRegistration resendEmailRegistration = new ResendEmailRegistration(userID, email);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                resendEmailRegistration.show(fm, "NoticeDialogFragment");
            }
        });


        return view;
    }

    public void setCodeConfirm(String code) {


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
                            if (!isSuccess) {
                                Toast.makeText(getContext(), "Неудалось активировать аккаунт, попробуйте повторить попытку позже", Toast.LENGTH_LONG).show();
                                return;
                            }
                            UserModel.createInstanceFromJson(response.getJSONObject("userData"));
                            Intent intent = new Intent(activityReg, AppActivity.class);
                            activityReg.startActivity(intent);
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
                        if (finalI > 0) {
                            EditText nextInput = listInput.get(finalI - 1);
                            nextInput.requestFocus();
                        }
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
                        finishRegistration.setBackgroundResource(R.drawable.dra20);
                    } else {
                        finishRegistration.setBackgroundResource(R.drawable.dra9);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }
}