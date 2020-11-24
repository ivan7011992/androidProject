package com.gorvodokanal.meters.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.gorvodokanal.R;
import com.gorvodokanal.meters.historyUtilClass.SummaryHistoryItemAdapter;
import com.gorvodokanal.meters.model.DatePeriod;
import com.gorvodokanal.meters.model.HistoryItem;
import com.gorvodokanal.meters.model.SummaryHistoryItem;
import com.gorvodokanal.meters.net.GetRequest;
import com.gorvodokanal.meters.net.RequestQueueSingleton;
import com.gorvodokanal.meters.net.UrlCollection;
import com.gorvodokanal.meters.net.VolleyJsonErrorCallback;
import com.gorvodokanal.meters.net.VolleyJsonSuccessCallback;
import com.gorvodokanal.meters.settings.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public class HistoryMetersFragment extends Fragment {

    Button startDateButton;
    ProgressDialog mDialog;
    private DatePeriod datePeriod = DatePeriod.fromCurrentDate();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_meters, container, false);
        setHasOptionsMenu(true);

        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Загрузка...");
        mDialog.setCancelable(false);
        mDialog.show();


        startDateButton = view.findViewById(R.id.dateButton);
        startDateButton.setText(datePeriod.format());

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(datePeriod, new DateDialog.PeriodProcessor() {
                    @Override
                    public void process(DatePeriod userDatePeriod) {
                        datePeriod = userDatePeriod;
                        showData(datePeriod.formatStartDate(), datePeriod.formatEndDate());
                        startDateButton.setText(datePeriod.format());
                        Toast.makeText(getContext(), "Отчёт построен", Toast.LENGTH_LONG).show();

                    }
                });
                dialog.setTargetFragment(HistoryMetersFragment.this, 1);
                dialog.show(HistoryMetersFragment.this.getFragmentManager(), "MyCustomDialog");
            }
        });
        Button applyPeriodButton = view.findViewById(R.id.applyPeriodButton);
        applyPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(datePeriod, new DateDialog.PeriodProcessor() {
                    @Override
                    public void process(DatePeriod userDatePeriod) {
                        datePeriod = userDatePeriod;
                        showData(datePeriod.formatStartDate(), datePeriod.formatEndDate());
                        startDateButton.setText(datePeriod.format());
                        Toast.makeText(getContext(), "Отчёт построен", Toast.LENGTH_LONG).show();

                    }
                });
                dialog.setTargetFragment(HistoryMetersFragment.this, 1);
                dialog.show(HistoryMetersFragment.this.getFragmentManager(), "MyCustomDialog");
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        showData(datePeriod.formatStartDate(), datePeriod.formatEndDate());
    }


    public void showData(String beginDate, String endDate) {
        final RequestQueue mQueue = RequestQueueSingleton.getInstance(getContext());
        GetRequest request = new GetRequest(mQueue);
        String requestUrl = UrlCollection.HISTORY_METERS + "?beginDate=" + beginDate + "&endDate=" + endDate;
        request.makeRequest(requestUrl, new VolleyJsonSuccessCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                mDialog.dismiss();
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
        }, new VolleyJsonErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mDialog.dismiss();
                showErrorDialog();
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
        inflater.inflate(R.menu.settings_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//этот метод вызввает когда происходит нажатие на каком-то из эдлементов меню
        int id = item.getItemId();// получаем элемнт которые был выбран в меню
        switch (id) {
            case R.id.action_setting:
                Intent openSetting = new Intent(HistoryMetersFragment.this.getActivity(), Setting.class);
                startActivity(openSetting);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void  showErrorDialog(){
        NoConnection dialog = new NoConnection();
        dialog.setTargetFragment(this, 1);
        dialog.show(this.getFragmentManager(), "MyCustomDialog");

    }
}