package com.gorvodokanalVer1.meters.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanalVer1.R;


public class NoConnection extends DialogFragment {

    Button confClose;
    FloatingActionButton floatingcloseNoConnection;
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_connection, container, false);
         confClose = view.findViewById(R.id.buttonNoConnect);
        floatingcloseNoConnection = view.findViewById(R.id.floatingcloseNoConnection);
        confClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                final NavController navController = NavHostFragment.findNavController(NoConnection.this);
                navController.popBackStack(R.id.generalInfoFragment,false);


            }
        });
        floatingcloseNoConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();


            }
        });

        return view;
    }
}