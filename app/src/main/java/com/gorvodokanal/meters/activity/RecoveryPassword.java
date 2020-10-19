package com.gorvodokanal.meters.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONObject;

public class RecoveryPassword extends AppCompatActivity {
Button recoveryPasswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        recoveryPasswordButton = findViewById(R.id.recoveryPasswordButton);

        recoveryPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sentMail();
            }
        });
        View imageKodRecovery = findViewById(R.id.imageKodRecovery);
        imageKodRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog("Код абоента", "Код абонента вводится в формате **-*******(пр.10-7777777)");
            }
        });
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.floatingActionRecoveryButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RecoveryPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void createAlertDialog(String title, String content) {
        // объект Builder для создания диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(RecoveryPassword.this);
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
                        // showMessage("Нажали ОК");
                    }
                });
        // объект Builder создал диалоговое окно и оно готово появиться на экране
        // вызываем этот метод, чтобы показать AlertDialog на экране пользователя
        builder.show();
    }

    public void sentMail(){

        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);
        GetRequest request = new GetRequest(mQueue);
        EditText kodRecovery = (EditText) findViewById(R.id.kodRecovery);
        String kodRec = kodRecovery.getText().toString();
        String requestUrl = UrlCollection.RECOVERY_URL + "?kodRec=" + kodRec;


        request.makeRequest(requestUrl, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(RecoveryPassword.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");


                    String email = response.getString("email");
                    RecoveryFinalDialog recovery = new RecoveryFinalDialog(email);
                    Toast.makeText(RecoveryPassword.this, "Сообщение отправлено на почту", Toast.LENGTH_LONG).show();
                    recovery.show(getSupportFragmentManager(), "NoticeDialogFragment");
                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });

    }


}