package com.gorvodokanalVer1.meters.activity;

import android.app.Activity;
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

public class ResendEmailRegistration extends DialogFragment {
    private int userID;
    private String emailReg;


    public ResendEmailRegistration(int userID,String email) {

        this.userID = userID;
        this.emailReg = email;

    }
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resend_email_registration, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        Button buttonResentMail  = (Button) view.findViewById(R.id.buttonResentMail);
        FloatingActionButton floatingcloseResentDialog  = (FloatingActionButton) view.findViewById(R.id.floatingcloseDialogResent);



        ((TextView) view.findViewById(R.id.textResentEmail)).setText("Если вам не пришло письмо на почту, то проверьте корректность введенных вами данных(" + emailReg +") \n Если почта указана неверно, у вас есть возможность изменить электронный адрес, указав новую почту в поле ниже");
        //EditText editemailConfirm =  view.findViewById(R.id.editemailResent);
       // String emailReg =editemailConfirm.getText().toString();








        floatingcloseResentDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();

            }
        });




        buttonResentMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                EditText emailCheck = ((EditText) getView().findViewById(R.id.editemailResent));
                if(emailCheck.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Введите почту", Toast.LENGTH_LONG).show();
                    return;
                }
                String emailReg = emailCheck.getText().toString();
                resendMail(emailReg);
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

public  void resendMail(String emailReg){
    final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
    GetRequest request = new GetRequest(mQueue);

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
                if (!isSuccess) {
                    Toast.makeText(getContext(), "Не удалось отправить сообщение", Toast.LENGTH_LONG).show();
                    }

                Toast.makeText(getContext(), "Сообщение отправлено на почту", Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(getContext(),RegistrationFinalDialog.class);
//                startActivity(intent);
            } catch (Exception e) {
                Log.e("valley", "error", e);
            }
        }
    });
}

}