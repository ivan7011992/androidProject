package com.gorvodokanal.meters.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentTransaction;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.historyUtilClass.SummaryHistoryItemAdapter;
import com.gorvodokanal.meters.model.HistoryItem;
import com.gorvodokanal.meters.model.SummaryHistoryItem;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonCallback;
import com.gorvodokanal.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class HistoryMetersFragment extends Fragment {

    private static String beginDate = "1.1.2020";
    private static String endDate = "1.7.2020";
    Button startDateButton;
    Button  endDateButon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_meters, container, false);
       startDateButton = view.findViewById(R.id.dateButton);
        startDateButton.setText("" + beginDate );

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginDate();
            }
        });
        endDateButon = view.findViewById(R.id.endDateButton);

        Button endDateButton = view.findViewById(R.id.endDateButton);
        endDateButton.setText("" +  endDate);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate();
            }
        });

        Button applyPeriodButton = view.findViewById(R.id.applyPeriodButton);
        applyPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData(beginDate, endDate);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        showData(beginDate, endDate);
    }


    public void beginDate() {
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
                        beginDate = editTextDateParam;
                        startDateButton.setText("" + beginDate);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void endDate() {
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
                        endDate = editTextDateParam;
                        endDateButon.setText("" + endDate);




                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void showData(String beginDate, String endDate) {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest request = new GetRequest(mQueue);
        String requestUrl = UrlCollection.HISTORY_METERS + "?beginDate=" + beginDate + "&endDate=" + endDate;
        request.makeRequest(requestUrl, new VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (!response.has("success")) {
                        Log.e("server", String.format("Error response from url %s: %s", UrlCollection.AUTH_URL, response.toString()));
                        Toast.makeText(getContext(), "Неизвестная ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
                        return;
                    }
                    final boolean isSuccess = response.getBoolean("success");

                    if (!isSuccess) {
                        Toast.makeText(getContext(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                        return;
                    }

                    LinkedHashMap<String, SummaryHistoryItem> data = buildData(response.getJSONArray("data"));


                    ArrayList<SummaryHistoryItem> historyItems = new ArrayList<SummaryHistoryItem>(data.values());
                    SummaryHistoryItemAdapter adapter = new SummaryHistoryItemAdapter(historyItems, HistoryMetersFragment.this);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    };
                    RecyclerView historyItemsView = (RecyclerView) getView().findViewById(R.id.historyItems);
                    historyItemsView.setNestedScrollingEnabled(false);
                    historyItemsView.setAdapter(adapter);
                    historyItemsView.setLayoutManager(layoutManager);
                } catch (Exception e) {
                    Log.e("valley", "error", e);
                }
            }
        });

    }

    private LinkedHashMap<String, SummaryHistoryItem> buildData(JSONArray rows) throws JSONException {
        LinkedHashMap<String, SummaryHistoryItem> data = new LinkedHashMap<>();

        for (int i = 0; i < rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            String date = row.getString("DATE_OT");
            if (data.containsKey(date)) {
                data.get(date).addItem(new HistoryItem(row));
            } else {
                SummaryHistoryItem historyItem = new SummaryHistoryItem(date);
                historyItem.addItem(new HistoryItem(row));
                data.put(date, historyItem);
            }
        }

        return data;
    }



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
        inflater.inflate(R.menu.settings_menu, menu) ;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//этот метод вызввает когда происходит нажатие на каком-то из эдлементов меню
        int id = item.getItemId();// получаем элемнт которые был выбран в меню
        if (id == R.id.action_setting) {

            Intent openSetting = new Intent(HistoryMetersFragment.this.getActivity(), Setting.class);
            startActivity(openSetting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}