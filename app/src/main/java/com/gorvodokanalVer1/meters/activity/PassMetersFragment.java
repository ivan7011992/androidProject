package com.gorvodokanalVer1.meters.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.Utils;
import com.gorvodokanalVer1.meters.historyUtilClass.SummaryPassItemAdapter;
import com.gorvodokanalVer1.meters.model.VodomerItem;
import com.gorvodokanalVer1.meters.net.GetRequest;
import com.gorvodokanalVer1.meters.net.PostRequest;
import com.gorvodokanalVer1.meters.net.RequestQueueSingleton;
import com.gorvodokanalVer1.meters.net.UrlCollection;
import com.gorvodokanalVer1.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanalVer1.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanalVer1.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PassMetersFragment extends Fragment {
    private static String datePassMeters;
    Button datePassMetersValue;
    private final static Pattern metersDataPattern = Pattern.compile("/^[0-9]{1,20}[.]?[0-9]{0,3}$/");
    TextView date111;
    ArrayList<VodomerItem> data;
    String currentDateNew;
    TextView currentDate;
    ProgressDialog mDialog;
    VodomerItem vodomerItem;
    TextView noMeters;
   static int  i=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pass_meters, container, false);
        // datePassMetersValue = view.findViewById(R.id.passDate);
//        datePassMetersValue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePassMeters();
//            }
//        });
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Загрузка...");
        mDialog.setCancelable(false);
        mDialog.show();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchAndDisplayData();

        noMeters = getView().findViewById(R.id.noMeters);
        Calendar calendar = Calendar.getInstance();
        String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        String month = monthNames[calendar.get(Calendar.MONTH)];
        int year = calendar.get(Calendar.YEAR);
        String currentDate = month + " " + year;


        ((TextView) getView().findViewById(R.id.headerPassMeters)).setText(currentDate);
    }

    public void fetchAndDisplayData() {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.PASS_METERS, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                mDialog.dismiss();
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONArray rows = response.getJSONArray("data");
                    data = buildData(rows);
                    dataType(data);
                    passMetrsView(data);
//               TextView text = (TextView) getView().findViewById(R.id.
//                           buttonPassMeters);
//
//                i++;
//                   text.setText("ОК" + i);
//


                } catch (Exception e) {
                    Log.e("valley", "Error", e);
                }
            }
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mDialog.dismiss();
                showErrorDialog();

            }
        });
    }

    private boolean needShowPassButton(ArrayList<VodomerItem> data) {

        for (VodomerItem item : data) {

            if (item.getDate_prom() == null) {
                return true;
            }
        }
        return false;
    }


    public void dataType(ArrayList<VodomerItem> data) {

        if (!this.needShowPassButton(data)) {
            Button button = getView().findViewById(R.id.buttonPassMeters);
            Utils.removeElement(button);
            noMeters.setText("Расчет платы за холодное водоснабжение и водоотведение по вашему адресу производит обслуживающая управляющая организация. По этой причине ввод показаний приборов учёта в приложении к сожалению недоступен. Вам следует передавать показания в вашу управляющую организацию.");

        }
        if (data.size() == 0) {
            noMeters.setText("По вашему лицевому счёту не установлены приборы учёта");

        }

        TextView noMeters = getView().findViewById(R.id.noMeters);

        if(noMeters != null) {
            if (noMeters.getText().length() == 0) {
                Utils.removeElement(noMeters);
            }
        }
    }

    private void passMetrsView(ArrayList<VodomerItem> data) {

        RecyclerView passMetersView = (RecyclerView) getView().findViewById(R.id.passMeters);
        passMetersView.setAdapter(null);
        final SummaryPassItemAdapter adapter = new SummaryPassItemAdapter(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        passMetersView.setAdapter(adapter);
        passMetersView.setNestedScrollingEnabled(false);
        passMetersView.setLayoutManager(layoutManager);


        getView().findViewById(R.id.
                buttonPassMeters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   passMetersData(adapter.getUserData());

            }
        });


    }


    private ArrayList<VodomerItem> buildData(JSONArray rows) throws JSONException {
        ArrayList<VodomerItem> data = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) {
            VodomerItem item = new VodomerItem((JSONObject) rows.get(i));
            data.add(item);
        }
        return data;

    }



    public void passMetersData(ArrayList<String> userData) {

        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        HashMap<String, Object> requestData = new HashMap<String, Object>();

        if (datePassMeters == null) {
            requestData.put("dateIndicators", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        } else {
            requestData.put("dateIndicators", datePassMeters);
        }

       requestData.put("meters", userData);

          for(int i= 0; i< userData.size(); i++) {
                String item = userData.get(i);



              if (item.isEmpty()) {
                  Toast.makeText(getContext(), "Введите показания", Toast.LENGTH_LONG).show();
                  return;
              }
              double itemValue = Double.parseDouble(item);
              if(itemValue < data.get(i).getPokaz()){
                  Toast.makeText(getContext(), "Показания не могут быть меньше текущих", Toast.LENGTH_LONG).show();
                  return;
              }

          }




        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.SET_METERS, requestData, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");


                    Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    createAlertDialog("",  response.getString("message"));
                    fetchAndDisplayData();


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        },
                new VolleyJsonErrorCallback() {
                    @Override
                    public void onError(VolleyError error) {
                        mDialog.dismiss();
                        showErrorDialog();
                    }

                });
    }

    //    private boolean validateMeters() {
//        for (EditText input : inputs) {
//            String userInput = input.getText().toString();
//            Matcher matcher = metersDataPattern.matcher(userInput);
//            if (!matcher.find()) {
//                // выделить INPUT в красную рамку
//                return false;
//            }
//
//        }
//        return true;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        //MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.drawer_menu, menu);
        inflater.inflate(R.menu.settings_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//этот метод вызввает когда происходит нажатие на каком-то из эдлементов меню
        int id = item.getItemId();// получаем элемнт которые был выбран в меню
        if (id == R.id.action_setting) {

            Intent openSetting = new Intent(PassMetersFragment.this.getActivity(), Setting.class);
            startActivity(openSetting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void datePassMeters() {
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String editTextDateParam = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                        datePassMeters = editTextDateParam;
                        datePassMetersValue.setText("" + datePassMeters);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void  showErrorDialog(){
        NoConnection dialog = new NoConnection();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");

    }

    private void createAlertDialog(String title, String content) {
        // объект Builder для создания диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

}





