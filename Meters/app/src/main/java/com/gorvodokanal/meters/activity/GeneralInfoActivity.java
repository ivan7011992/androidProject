package com.gorvodokanal.meters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;
import com.gorvodokanal.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeneralInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generation_info);


        final RequestQueue mQueue = RequestQueueSingleton.getInstance(this);

        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.GENERAL_INFO_URL, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(GeneralInfoActivity.this, "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray rows = response.getJSONArray("data");
                    JSONObject firstRow = (JSONObject) rows.get(0);
                    ((TextView) findViewById(R.id.kod)).setText(firstRow.getString("KOD"));
                    ((TextView) findViewById(R.id.fio)).setText(firstRow.getString("FIO"));

                    TableLayout tableLayout = findViewById(R.id.table1);

                    final String[] fields = {"N_VODOMER", "VID" ,"DAT_UST","DAT_POVER"};
                    for (int i =0 ; i< rows.length(); i++) {
                        JSONObject row = rows.getJSONObject(i);

                        TableRow tableRow = new TableRow( GeneralInfoActivity.this );
                         tableRow = (TableRow) getLayoutInflater().inflate(R.layout.partial_table_row, null);
                        for(String field : fields) {
                            TextView cell = new TextView(GeneralInfoActivity.this);
                            String fieldValue = row.getString(field);
                            cell.setText(fieldValue);
                            tableRow.addView(cell);
                        }

                        tableLayout.addView(tableRow, i + 1);

                    }



                } catch (Exception e) {
                    Log.e("valley", "Error", e);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
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
}
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // при создании объекта View всегда передаем контекст в которм мы его создаем
//        TableLayout containerTableLayout = new TableLayout(this);
//        // создаем три растянутые на всю ширину строки
//        TableRow tableRow1 = new TableRow(this);
//        TableRow tableRow2 = new TableRow(this);
//        TableRow tableRow3 = new TableRow(this);
//
//        // TableLayout - наследник LinearLayout, поэтому используем настройку
//        // родительских параметров для ширины и высоты контейнера
//        containerTableLayout.setLayoutParams(new TableLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        ));
//        // растягиваем все столбцы с помощью
//        // программного аналога android:stretchColumns="*"
//        containerTableLayout.setStretchAllColumns(true);
//
//        // создаем поле для логина
//        EditText editTextLogin = new EditText(this);
//        editTextLogin.setHint("Введите свой логин");
//
//        // создаем поле для пароля
//        EditText editTextPassword = new EditText(this);
//        editTextPassword.setHint("Введите свой пароль");
//
//        // создаем кнопку подтверждения
//        Button buttonSend = new Button(this);
//        buttonSend.setText("Отправить");
//
//        // теперь можем добавить нашпи поля и кнопку
//        // на свои строки в таблице
//        tableRow1.addView(editTextLogin);
//        tableRow2.addView(editTextPassword);
//        tableRow3.addView(buttonSend);
//
//        // добавляем строки в таблицу
//        containerTableLayout.addView(tableRow1);
//        containerTableLayout.addView(tableRow2);
//        containerTableLayout.addView(tableRow3);
//
//        setContentView(containerTableLayout);
//    }