package com.gorvodokanal.meters.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentTransaction;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

public class HistoryMetersFragment extends Fragment {

    private static String beginDate =  "1.1.2020";
    private static String endDate = "1.7.2020";
    Button startDateButton;

    int Date;





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_meters, container, false);
        setHasOptionsMenu(true);

        Calendar calendar = Calendar.getInstance();



        YearMonth month = YearMonth.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.y");
        String firstDay = String.format(month.atDay(1).format(formatter).toString());
                String   endDay = month.atEndOfMonth().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.y");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(firstDay));
        } catch (ParseException e) {
            e.printStackTrace();
        }
       endDate = sdf.format(c.getTime());


        c.add(Calendar.DAY_OF_WEEK,0);
        c.add(Calendar.MONTH, -6);  // number of days to add
        beginDate = sdf.format(c.getTime());  // dt is now the new date

        startDateButton = view.findViewById(R.id.dateButton);
        startDateButton.setText(beginDate + "-" + endDate );

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DateDialog dialog = new DateDialog(new DateDialog.PeriodProcessor() {
                   @Override
                   public void process(int startMonth, int startYear, int endMonth, int endYear) {
                       beginDate = String.format("%s.%d.%d", "01", startMonth, startYear);
                       endDate = String.format("%s.%d.%d", "01", endMonth, endYear);

                       SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.y");
                       SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.y");
                       Calendar cal = Calendar.getInstance();
                       try {
                           cal.setTime(sdf.parse(beginDate));
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                       beginDate= dateFormat.format(cal.getTime());
                       Calendar cal1 = Calendar.getInstance();
                       try {
                           cal1.setTime(sdf.parse(endDate));
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                       endDate= dateFormat1.format(cal1.getTime());
                       try {
                           Date dateBegin = dateFormat.parse(beginDate);
                           Date dateEnd  = dateFormat1.parse(endDate);


                           if (dateBegin.compareTo(dateEnd) > 0) {

                               Toast.makeText(getContext(), "Дата начала периода не может быть больше даты конца периода", Toast.LENGTH_LONG).show();


                              }
                           if (dateBegin.compareTo(dateEnd) < 0) {

                               showData(beginDate, endDate);

                               startDateButton.setText(beginDate + "-" + endDate);
                               Toast.makeText(getContext(), "Отчёт построен", Toast.LENGTH_LONG).show();

                           }


                       } catch (ParseException e) {
                           e.printStackTrace();
                       }


                   }
               });
                dialog.setTargetFragment(HistoryMetersFragment.this, 1);
                dialog.show(HistoryMetersFragment.this.getFragmentManager(), "MyCustomDialog");
            }
        });
//        endDateButon = view.findViewById(R.id.endDateButton);
//
//        Button endDateButton = view.findViewById(R.id.endDateButton);
//        endDateButton.setText("" + endDate);
//        endDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                endDate();
//            }
//        });

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
                      //  endDateButon.setText("" + endDate);


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
}