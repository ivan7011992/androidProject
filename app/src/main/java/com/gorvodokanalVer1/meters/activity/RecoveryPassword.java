package com.gorvodokanalVer1.meters.activity;

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
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class RecoveryPassword extends AppCompatActivity {
Button recoveryPasswordButton;
    EditText kodRecovery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        recoveryPasswordButton = findViewById(R.id.recoveryPasswordButton);
        kodRecovery =   findViewById(R.id.kodRecovery);
        kodRecovery.setMaxLines(1);
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots("__-_______");
        FormatWatcher formatWatcher = new MaskFormatWatcher( // форматировать текст будет вот он
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(kodRecovery);

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
                createAlertDialog("Код абонента", "Код абонента вводится в формате **-*******(пр.10-7777777)");
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



        String kodRec = kodRecovery.getText().toString();
        if(kodRec.trim().length() == 0){
            Toast.makeText(RecoveryPassword.this, "Введите код абонента", Toast.LENGTH_LONG).show();
            return;
        }
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

                    if(!isSuccess){
                        JSONObject rows = response.getJSONObject("message");
                        StringBuilder errorBuilder = new StringBuilder();
                        Iterator<String> temp = rows.keys();
                        String error = "";
                        while (temp.hasNext()) {
                            String key = temp.next();
                               error = rows.getString(key);

                        }
                     // String error =   getErrors(response.getJSONArray("message"));
                        Toast.makeText(RecoveryPassword.this,  error, Toast.LENGTH_LONG).show();
                        return;
                    }



                    String email = response.getString("email");
                    RecoveryFinalDialog recovery = new RecoveryFinalDialog(email);
                    Toast.makeText(RecoveryPassword.this, "Сообщение отправлено на почту", Toast.LENGTH_LONG).show();
                    recovery.show(getSupportFragmentManager(), "NoticeDialogFragment");
                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {

                Toast.makeText(RecoveryPassword.this, "Произошла ошибка, попробуйте повторить попытку позже", Toast.LENGTH_LONG).show();

            }});

    }

    private String  getErrors(JSONArray array) {

        String error = "";

        for (int i = 0; i < array.length(); i++) {
            JSONObject objects = null;
            try {
                objects = array.getJSONObject(i);

                Iterator key = objects.keys();
                while (key.hasNext()) {
                    String k = key.next().toString();
                    error = objects.getString(k);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return error;
    }

}