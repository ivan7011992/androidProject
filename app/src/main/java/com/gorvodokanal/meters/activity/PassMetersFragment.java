package com.gorvodokanal.meters.activity;

import android.app.DatePickerDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.historyUtilClass.SummaryPassItemAdapter;
import com.gorvodokanal.meters.model.VodomerItem;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.PostRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;
import com.gorvodokanal.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PassMetersFragment extends Fragment {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy");
    private static String datePassMeters;
    Button datePassMetersValue;
    private final static Pattern metersDataPattern = Pattern.compile("/^[0-9]{1,20}[.]?[0-9]{0,3}$/");
    TextView date111;
    ArrayList<VodomerItem> data;
    String currentDateNew;
     TextView currentDate;
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


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchAndDisplayData();

        Calendar calendar = Calendar.getInstance();
        String[] monthNames = { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
        String month = monthNames[calendar.get(Calendar.MONTH)];
        int year = calendar.get(Calendar.YEAR);
        String currentDate =  month + " " + year;


        ((TextView) getView().findViewById(R.id.headerPassMeters)).setText(currentDate);
    }

    public void fetchAndDisplayData() {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest userInfoRequest = new GetRequest(mQueue);
        userInfoRequest.makeRequest(UrlCollection.PASS_METERS, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
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

                } catch (Exception e) {
                    Log.e("valley", "Error", e);
                }
            }
        });
    }

    public void dataType(ArrayList<VodomerItem> data){

        if(data.size()>0){
            LinearLayout parentPassMeters = getView().findViewById(R.id.parentPassMeters);
            TextView noMeters = getView().findViewById(R.id.noMeters);
            parentPassMeters.removeView(noMeters);
        }
        if(data.size()==0)
        {
            Button button = getView().findViewById(R.id.buttonPassMeters);
            button.setText("Передача показаний недоступна");
            TextView noMeters = getView().findViewById(R.id.noMeters);
            noMeters.setText("По вашему лицевому счёту не установлены приборы учёта");

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



        getView().findViewById(R.id.buttonPassMeters).setOnClickListener(new View.OnClickListener() {
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
        //   boolean validData = validateMeters();
        // if (!validData) {
        //   return;
        //}
        passMetrsView(data);
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        HashMap<String, Object> requestData = new HashMap<String, Object>();

        if (datePassMeters == null) {
            requestData.put("dateIndicators", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        } else {
            requestData.put("dateIndicators", datePassMeters);
        }

        requestData.put("meters", userData);

        PostRequest request = new PostRequest(mQueue);
        request.makeRequest(UrlCollection.SET_METERS, requestData, new VolleyJsonCallback() {
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
                    fetchAndDisplayData();


                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
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
}





