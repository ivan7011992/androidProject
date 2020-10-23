package com.gorvodokanal.meters.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;


public class AppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
 int login;
    String currentLogin;
    Spinner listUser;
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

       // sideBar.setNavigationItemSelectedListener(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout)
                .build();
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar); // для верхнего меню
        NavigationUI.setupWithNavController(toolBar, navController, appBarConfiguration);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_humburg);
       toolBar.setTitleTextColor(Color.WHITE);





        // toolBar.setTitleTextColor(Color.WHITE);
        //  toolBar.setSubtitleTextColor(Color.WHITE);

        View header = sideBar.getHeaderView(0);
        ((TextView) header.findViewById(R.id.text)).setText(UserModel.getInstance().getLogin());
        View exit = sideBar.findViewById(R.id.exit);
        listUser = header.findViewById(R.id.spinnerUserListSwith);
        RelativeLayout swith = header.findViewById(R.id.swithParrent);
        TextView text = header.findViewById(R.id.text);
        listUser.setOnItemSelectedListener(this);
        if(UserModel.getInstance().getLs().size() ==1){
           swith.removeView(listUser);
        } else{
            ArrayList<String> loginList = new ArrayList<>(UserModel.getInstance().getLs().values());
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, loginList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// устанавливаем выпадающий список для спиннера


             listUser.setAdapter(adapter);
            swith.removeView(text);
        }




//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AppActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.exit) {
            Toast.makeText(this, "5435", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AppActivity.this, MainActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//        currentLogin = listUser.getSelectedItem().toString();
//       SwithAccount swithAccount = new SwithAccount(currentLogin, this, new SwithAccount.SuccessReponseHandler() {
//           @Override
//           public void process() {
//               Intent intent = new Intent( AppActivity.this,AppActivity.class);
//               startActivity(intent);
//           }
//       });
//       swithAccount.sendDataAuth();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}