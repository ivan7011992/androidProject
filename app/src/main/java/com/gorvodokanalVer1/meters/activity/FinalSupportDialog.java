package com.gorvodokanalVer1.meters.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanalVer1.R;

public class FinalSupportDialog extends DialogFragment {
   Button buttonConfirm;
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.final_support_dialog, container, false);



        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        buttonConfirm =  view.findViewById(R.id.buttonConfirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
                final NavController navController = NavHostFragment.findNavController(FinalSupportDialog .this);

                navController.popBackStack(R.id.generalInfoFragment,false);
            }
        });

        return view;
    }
}