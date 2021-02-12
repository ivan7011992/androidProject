package com.gorvodokanalVer1.meters.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.UserModel;


public class Exit extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = new Bundle();
        UserModel.getInstance(). setStatusAuth(1);
        getActivity().finish();

        //System.exit (0);

        return inflater.inflate(R.layout.fragment_exit, container, false);

    }
}