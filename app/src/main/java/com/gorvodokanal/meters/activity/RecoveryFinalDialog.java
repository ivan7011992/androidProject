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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class RecoveryFinalDialog extends DialogFragment {
    private String email;

    public RecoveryFinalDialog(String email) {

        this.email = email;
    }
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recovery_final, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        TextView closeDialog  = (TextView) view.findViewById(R.id.buttonCloseRecovery);

        ((TextView) view.findViewById(R.id.RecoveryFinalText)).setText("На почтовый ящик" + " " + email +" "
                + "выслано письмо с инструкцией по восстановлению пароля.");

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