package com.gorvodokanal.meters.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.gorvodokanal.R;


public class AppActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navFragment);
        NavController navController = host.getNavController();
        // включаем боковое меню
        NavigationView sideBar = (NavigationView) findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(sideBar, navController);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout)
                .build();
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar); // для верхнего меню
        NavigationUI.setupWithNavController(toolBar, navController, appBarConfiguration);


    }
}
