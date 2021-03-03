package com.gorvodokanalVer1.meters.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.UserManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.meters.historyUtilClass.SummaryHistoryItemAdapter;
import com.gorvodokanalVer1.meters.model.SummaryHistoryItem;
import com.gorvodokanalVer1.meters.model.UserModel;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.PersistentCookieStore;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanalVer1.meters.settings.Setting;
import com.gorvodokanalVer1.meters.settings.SettingVariable;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    private static MainActivity instance;
    EditText login;
    private TextView email1;
    public Button button;
    ImageView imageView;
    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (true) {
            throw new RuntimeException("Error");
        }
        instance = this;
        imageView = this.findViewById(R.id.image2);
        passwordUser = findViewById(R.id.password);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setIntervalFromSharedPrefarences(sharedPreferences);
        button = findViewById(R.id.button);
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
                createAlertDialog("", "Поле 'Логин' соответствует коду лицевого счёта (Код абонента) и вводится в формате **-*******(пр.10-7777777)." + "\n" +
                        "Код абонента отражен в кассовом чеке, либо уточнить по тел: 204-99-19.");
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
        float height = this.getResources().getDisplayMetrics().heightPixels / this.getResources().getDisplayMetrics().density;
        if (height < 600) {
            RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams) button.getLayoutParams();
            layoutparams.topMargin = 15;
            button.setLayoutParams(layoutparams);
        }

        login = findViewById(R.id.login);

        login.setText(SettingsManager.getInstanse().getlogin(this));


//        String cookies = SettingsManager.getInstanse().getCookies(this);
//        if (cookies != null) {
//            RequestQueueSingleton.getInstance(this);
//            CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
//            CookieStore cookieStore = cookieManager.getCookieStore();
//
//            URI cookieUrl = URI.create("https://www.gorvodokanal.com");
//            String[] cookiesParth = cookies.split(";", 0);
//            for (String cookieParthValue : cookiesParth) {
//                String[] cookieParthValueArray = cookieParthValue.split("=");
//                HttpCookie httpCookie = new HttpCookie(cookieParthValueArray[0], cookieParthValueArray[1]);
//                httpCookie.setDomain("www.gorvodokanal.com");
//                 httpCookie.setPath("/mobile_app/");
//                cookieStore.add( cookieUrl,httpCookie);
//            }

//        }
        RequestQueueSingleton.getInstance(this);
        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
        CookieStore cookieStore = cookieManager.getCookieStore();
        if (cookieStore.getCookies().size() > 0) {
            getUserData();
        }
        ;
    }

    public void getUserData() {


        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);
        GetRequest request = new GetRequest(mQueue);
        String requestUrl = UrlCollection.GET_USER_DATA;
        request.makeRequest(requestUrl, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {

                try {
                    UserModel.createInstanceFromJson(response);
                    Intent appActivity = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(appActivity);
                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

                showErrorDialog(error);
            }
        });

    }

    private void createAlertDialog(String title, String content) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });
        builder.show();
    }

    private void showMessage(String textInMessage) {
        Toast.makeText(getApplicationContext(), textInMessage, Toast.LENGTH_LONG).show();
    }


    public void formSubmit(View view) {

        mDialog = new ProgressDialog(MainActivity.this,R.style.MyTheme);
        mDialog.setCancelable(false);
        mDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mDialog.show();


        button.setEnabled(false);
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
                button.setEnabled(true);
                mDialog.dismiss();
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(MainActivity.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        String errorMessage = response.getString("message");
                        if (response.has("status")) {
                            if (!response.getBoolean("status")) {

                                JSONArray rows = response.getJSONArray("data");
                                JSONObject userData = (JSONObject) rows.getJSONObject(0);
                                CheckConfirmEmailDialog checkConfirmEmailDialog = new CheckConfirmEmailDialog(loginValue, userData.getInt("ID"), userData.getString("EMAIL"), MainActivity.this);

                                FragmentManager fm = getSupportFragmentManager();
                                checkConfirmEmailDialog.show(fm, "NoticeDialogFragment");
                            }
                        }
                        Toast.makeText(MainActivity.this, String.valueOf(errorMessage), Toast.LENGTH_LONG).show();
                        return;
                    }

                    String loginValueShared = login.getText().toString();
                    SettingsManager.getInstanse().savelogin(MainActivity.this, loginValueShared);
//                    CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
//                    List<HttpCookie> coookies = cookieManager.getCookieStore().getCookies();

//                    StringBuilder cookieStr = new StringBuilder();
//                    for (HttpCookie cookie : coookies) {
//                        cookieStr.append(cookie.getName() + "=" + cookie.getValue());
//                        cookieStr.append(";");
//                    }


                    UserModel.createInstanceFromJson(response);
                    Intent intent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mDialog.dismiss();
                button.setEnabled(true);
                showErrorDialog(error);
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

    private void showErrorDialog(VolleyError error) {
        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, error.toString()));
        Toast.makeText(this, "Произошла ошибка, попробуйте повторить попытку позже", Toast.LENGTH_LONG).show();

    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void confirmCodeView(String login) {
        ConfirmCode confirmCode = new ConfirmCode(login, this);
        FragmentManager fm = getSupportFragmentManager();
        confirmCode.show(fm, "NoticeDialogFragment");
    }

}
//сделать страницу на сервере для полуение дданных
//активити