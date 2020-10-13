package com.gorvodokanal.meters.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.VodomerItem;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Registration extends AppCompatActivity {


    private static HashMap<String, EditText> dataRegistr = new HashMap<>();

    private static final HashMap<String, Integer> inputNames = new HashMap<>();
    static {
        inputNames.put("kod", R.id.kod);
        inputNames.put("phone", R.id.phone);
        inputNames.put("street", R.id.street);
        inputNames.put("fio", R.id.fio);
        inputNames.put("house", R.id.house);
        inputNames.put("flat", R.id.flat);
        inputNames.put("passwordReg", R.id.passwordReg);
        inputNames.put("confirmPassword", R.id.ConfirmPassword);
        inputNames.put("emailReg", R.id.emailReg);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RegistrationData data = new RegistrationData();
        for (Map.Entry<String, Integer> inputNameEntry : inputNames.entrySet()) {
            data.add(inputNameEntry.getKey(), findViewById(inputNameEntry.getValue()));
        }

        data.setViewMask("phone", "+7(___) ___-__-__");
        data.setViewMask("kod", "__-_______");

        View image2 = findViewById(R.id.image2);
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog("Код абоента", "Код абонента вводится в формате **-*******(пр.10-7777777)");
            }
        });


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationSubmitData(data.getData());
            }
        });


    }

    private void createAlertDialog(String title, String content) {
        // объект Builder для создания диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
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
                        showMessage("Нажали ОК");
                    }
                });
        // объект Builder создал диалоговое окно и оно готово появиться на экране
        // вызываем этот метод, чтобы показать AlertDialog на экране пользователя
        builder.show();
    }

    private void showMessage(String textInMessage) {
        Toast.makeText(getApplicationContext(), textInMessage, Toast.LENGTH_LONG).show();
    }

    private void registrationSubmitData(HashMap<String, String> data) {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);

        HashMap<String, String> emptyErrorMessages = new HashMap<>();
        emptyErrorMessages.put("kod", "Введите код");
        emptyErrorMessages.put("phone", "Введите телефон");
        emptyErrorMessages.put("street", "Введите улицу");
        emptyErrorMessages.put("house", "Введите дом");
        emptyErrorMessages.put("flat", "Введите Введите квартиру");
        emptyErrorMessages.put("passwordReg", "Введите пароль");
        emptyErrorMessages.put("confirmPassword", "Подтвердите пароль");
        emptyErrorMessages.put("emailReg", "Введете почту");
        emptyErrorMessages.put("fio", "Введите инициалы");

        ArrayList<String> errors = new ArrayList<>();
        for (Map.Entry<String, String> errorEntry : emptyErrorMessages.entrySet()) {
            String value = data.get(errorEntry.getKey());
            if (value.isEmpty()) {
                errors.add(errorEntry.getValue());
            }

        }

        if (errors.size() > 0) {
            StringBuilder errorBuilder = new StringBuilder();
            for (String error : errors) {
                errorBuilder.append(error).append("\n");
            }
            displayError("Обраружены ошибки: " + errorBuilder.toString());
            return;
        }


        Map<String, Object> requestData = new HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            requestData.put(entry.getKey(), entry.getValue());
        }

        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.REGISTRATION_URL, requestData, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(Registration.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        JSONObject rows = response.getJSONObject("errors");

                        StringBuilder errorBuilder = new StringBuilder();
                        Iterator<String> temp = rows.keys();
                        while (temp.hasNext()) {
                            String key = temp.next();
                            String error = rows.getString(key);
                            errorBuilder.append(error).append("\n");
                        }
                        displayError("Обраружены ошибки: " + errorBuilder.toString());

                        return;
                    }
                    Toast.makeText(Registration.this, "Регистрация  удалась", Toast.LENGTH_LONG).show();
                    int userId = response.getInt("userId");

                    RegistrationFinalDialog dialog = new RegistrationFinalDialog(userId,((EditText) findViewById(R.id.emailReg)).getText().toString());

                    dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }


            }
        });
    }

    private ArrayList<String> buildData(JSONArray rows) throws JSONException {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) {
            String item = (String) rows.get(i);
            data.add(item);
        }
        return data;

    }

    private void displayError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}