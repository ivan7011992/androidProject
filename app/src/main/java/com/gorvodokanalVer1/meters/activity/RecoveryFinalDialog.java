package com.gorvodokanalVer1.meters.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gorvodokanalVer1.R;

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

        ((TextView) view.findViewById(R.id.RecoveryFinalText)).setText("Для восстановления пароля на почтовый ящик" + " " + email +" "
                + "выслан временный код. Используйте его при авторизации ");

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