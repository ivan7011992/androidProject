package com.gorvodokanal.meters.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.gorvodokanal.R;

public class AppActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        NavHostFragment host = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.navFragment);
        NavController navController = host.getNavController();
        // включаем боковое меню
        NavigationView sideBar = (NavigationView) findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(sideBar, navController);
    }
}
