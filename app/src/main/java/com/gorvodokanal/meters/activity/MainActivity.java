package com.gorvodokanal.meters.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.RemoteActionCompatParcelizer;
import android.text.Html;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanal.meters.model.UserModel;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanal.meters.settings.Setting;
import com.gorvodokanal.meters.settings.SettingVariable;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String email;
    private String password;
    SharedPreferences sharedPreferences;
    public EditText passwordUser;
    public boolean showPassword = false;
    public Button button2;
    public Button button3;

    EditText login;
    private TextView email1;
    public Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = this.findViewById(R.id.image2);
        passwordUser = findViewById(R.id.password);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setIntervalFromSharedPrefarences(sharedPreferences);
        button = findViewById(R.id.button);
        //  buttom.getBackground().setAlpha(64);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        RegistrationData data = new RegistrationData();

        login = findViewById(R.id.login);
        login.setMaxLines(1);
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("__-_______");
        FormatWatcher formatWatcher = new MaskFormatWatcher( // форматировать текст будет вот он
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(login);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });
        button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecoveryPassword.class);
                startActivity(intent);
            }

        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog("Код абонента", "Код абонента вводится в формате **-*******(пр.10-7777777)");
            }
        });

        final ImageView imageView = findViewById(R.id.image3);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showPassword = !showPassword;
                if (showPassword) {
                    passwordUser.setTransformationMethod(null);
                } else {
                    passwordUser.setTransformationMethod(new PasswordTransformationMethod());
                }
                passwordUser.setSelection(passwordUser.length());

            }

        });


    }

    private void createAlertDialog(String title, String content) {
        // объект Builder для создания диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // добавляем различные компоненты в диалоговое окно
        builder.setTitle(title);
        builder.setMessage(content);
        // устанавливаем кнопку, которая отвечает за позитивный ответ
        builder.setPositiveButton("OK",
                // устанавливаем слушатель
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // по нажатию создаем всплывающее окно с типом нажатой конпки

                    }
                });
        // объект Builder создал диалоговое окно и оно готово появиться на экране
        // вызываем этот метод, чтобы показать AlertDialog на экране пользователя
        builder.show();
    }

    private void showMessage(String textInMessage) {
        Toast.makeText(getApplicationContext(), textInMessage, Toast.LENGTH_LONG).show();
    }

    public void formSubmit(View view) {
        login = findViewById(R.id.login);
        final EditText password = findViewById(R.id.password);


        final String loginValue = login.getText().toString();
        String passwordValue = password.getText().toString();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("login", loginValue);
        requestData.put("password", passwordValue);


        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);

        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.AUTH_URL, requestData, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(MainActivity.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {


                        String errorMessage = response.getString("message");


                        Toast.makeText(MainActivity.this, String.valueOf(errorMessage), Toast.LENGTH_LONG).show();
                        return;
                    }

                    HashMap<Integer, String> ls = new HashMap<>();
                    JSONArray lsList = response.getJSONArray("ls");
                    for(int i = 0; i < lsList.length(); i++) {
                        JSONObject currentLs = (JSONObject) lsList.get(i);
                        ls.put(Integer.parseInt(currentLs.getString("ID")), currentLs.getString("LOGIN"));
                    }

                    UserModel.createInstance(loginValue, ls);
                    Intent intent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

                showErrorDialog();
            }


        });


    }

    public String convert(String inString, String inCharset, String outCharset) throws UnsupportedEncodingException {
        byte[] bytes = inString.getBytes(inCharset);
        return new String(bytes, outCharset);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//этот метод вызввает когда происходит нажатие на каком-то из эдлементов меню
        int id = item.getItemId();// получаем элемнт которые был выбран в меню
        if (id == R.id.action_setting) {
            Intent openSetting = new Intent(this, Setting.class);
            startActivity(openSetting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setIntervalFromSharedPrefarences(SharedPreferences sharedPrefarences) {

        try {
            email = sharedPrefarences.getString("Change email", "null");

        } catch (NumberFormatException nef) {//ксли exception то влю бом слкчае м выведится торока ниже
            Toast.makeText(this, "Number format exceprion", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {//ксли exception то влю бом слкчае м выведится торока ниже
            Toast.makeText(this, "Some error happanns", Toast.LENGTH_LONG).show();
        }
        email = sharedPrefarences.getString("Change email", "30");
//        email1 = findViewById(R.id.log);
//        email1.setText("kkkk" + email);
        SettingVariable.email = email;
        password = sharedPrefarences.getString("Change password", "30");
        Log.i("email", "" + email);
        SettingVariable.password = password;


    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("Change email")) {
            setIntervalFromSharedPrefarences(sharedPreferences);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void  showErrorDialog(){

        Toast.makeText(this, "Some error happanns", Toast.LENGTH_LONG).show();

    }
}
//сделать страницу на сервере для полуение дданных
//активити