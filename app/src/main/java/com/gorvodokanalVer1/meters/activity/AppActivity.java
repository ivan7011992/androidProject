package com.gorvodokanalVer1.meters.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.Calendar;


public class AppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    private long pausedMillis;
    Spinner listUser;
    private boolean wasSelected = false;
    Button buttonBindingLs;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        onResume();

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
        buttonBindingLs = header.findViewById(R.id.buttonBindingLs);
       buttonBindingLs.setOnClickListener(v -> processBindingData());


        if (UserModel.getInstance().getLs().size() <=1) {
            swith.removeView(listUser);
            text.setText(UserModel.getInstance().getLogin());

        } else {
            ArrayList<String> loginList = new ArrayList<>(UserModel.getInstance().getLs().values());

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, loginList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// устанавливаем выпадающий список для спиннера

            listUser.setPadding(0,0,-10,0 );
            listUser.setOnItemSelectedListener(this);
            listUser.setAdapter(adapter);
            listUser.setSelection(loginList.indexOf(UserModel.getInstance().getLogin()));
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

    public  void processBindingData(){
        BindingLsDialog bindingLsDialog = new BindingLsDialog();
        bindingLsDialog .show(getSupportFragmentManager(), "NoticeDialogFragment");


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
        if (!wasSelected) {
            wasSelected = true;
            return;
        }

        String currentLogin = listUser.getSelectedItem().toString();

        SwithAccount swithAccount = new SwithAccount(currentLogin, this, new SwithAccount.SuccessReponseHandler() {
            @Override
            public void process() {

            UserModel.getInstance().setLogin(currentLogin);
                Intent intent = new Intent(AppActivity.this, AppActivity.class);
               // intent.putExtra("login",currentLogin );
                startActivity(intent);
            }
        });
        swithAccount.sendDataAuth();
        final NavController navController = new NavController(this);
        navController.popBackStack(R.id.generalInfoFragment,false);

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    @Override
    protected void onStop() {
        super.onStop();
        pausedMillis = Calendar.getInstance().getTimeInMillis();
    }
    @Override

    public void onResume(){
        super.onResume();

        try {
            long currentMillis = Calendar.getInstance().getTimeInMillis();
            if ( !(this instanceof AppActivity) && currentMillis - pausedMillis  > 1000 * 60 * 3 ) {
                Intent intent = new Intent(this, AppActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}