package com.gorvodokanal.meters.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanal.R;


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