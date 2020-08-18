package com.gorvodokanal.meters.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gorvodokanal.R;

public class Setting extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener  {
         private String email;
         private String password;
         private TextView email1;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        }
    }
    //переопределяем метод

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){//предуустановленная коснтанта в android шв кнопки home
            NavUtils.navigateUpFromSameTask(this);//при неажатии на кнопку home возращаетимся в родительскую активити
        }
        return super.onOptionsItemSelected(item);
    }

    private void setIntervalFromSharedPrefarences(SharedPreferences sharedPrefarences){

        try{
            email = sharedPrefarences.getString("Change email","30");

        }catch (NumberFormatException nef){//ксли exception то влю бом слкчае м выведится торока ниже
            Toast.makeText(this, "Number format exceprion", Toast.LENGTH_LONG).show();
        }
        catch (Exception  ex){//ксли exception то влю бом слкчае м выведится торока ниже
            Toast.makeText(this, "Some error happanns", Toast.LENGTH_LONG).show();
        }
        email = sharedPrefarences.getString("Change email","30");
        email1= findViewById(R.id.log);
        email1.setText("kkkk" + email);
        password = sharedPrefarences.getString("Change password","30");
        Log.i("email", "" +  email);




    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals("default_interval")){
            setIntervalFromSharedPrefarences(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}