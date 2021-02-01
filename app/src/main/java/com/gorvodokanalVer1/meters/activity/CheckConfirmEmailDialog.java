package com.gorvodokanalVer1.meters.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;


import org.json.JSONObject;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class CheckConfirmEmailDialog extends DialogFragment {
    private int userId;
    private String email;
    private String login;
    Activity mainActivity;
    public CheckConfirmEmailDialog(String login,int userId,String email, Activity activity){
        this.userId = userId;
        this.email = email;
        this.mainActivity = activity;
        this.login = login;
    }

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_confirm_email_dialog, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        Button buttonSentMail  = (Button) view.findViewById(R.id.buttonSendMail);
        FloatingActionButton floatingcloseDialogSendMail  = (FloatingActionButton) view.findViewById(R.id.floatingcloseDialogSendMail);

        ((TextView) view.findViewById(R.id.texSentEmail)).setText("Аккаунт не активирован. Подтвердите почту " + email);






        floatingcloseDialogSendMail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();

            }
        });




        buttonSentMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");


                sendMail(email);
                getDialog().dismiss();

                // ConfirmCode confirmCode = new ConfirmCode(login,activityReg);
                // FragmentManager fm = getActivity().getSupportFragmentManager();
                // confirmCode.show(fm, "NoticeDialogFragment");
                // Intent intent = new Intent(getContext(),MainActivity.class);
                //  startActivity(intent);

            }
        });


        return view;
    }

    public  void sendMail(String email){
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest request = new GetRequest(mQueue);

        String requestUrl = UrlCollection.RESENDING_URL + "?userId=" + userId + "&email=" +  email;


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

                    Toast.makeText(mainActivity, "Сообщение отправлено на почту", Toast.LENGTH_LONG).show();
                   // ConfirmCode confirmCode = new ConfirmCode(login,mainActivity);
                  //  FragmentManager fm = getActivity().getSupportFragmentManager();
                  //  confirmCode.show(fm, "NoticeDialogFragment");
              //  ((MainActivity)getActivity()).confirmCodeView(login);
                    MainActivity.getInstance().confirmCodeView(login);
//               Intent intent = new Intent(getContext(),RegistrationFinalDialog.class);
//                startActivity(intent);
                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });
    }

}