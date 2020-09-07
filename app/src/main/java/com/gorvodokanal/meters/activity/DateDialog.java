package com.gorvodokanal.meters.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.HistoryItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DateDialog extends DialogFragment {
    interface SpinnerSelectedItemHandler {
        public void onSelect(String value);
    }

    public interface PeriodProcessor {
        public void process(int startMonth,
                            int startYear,
                            int endMonth,
                            int endYear);
    }

    private static final HashMap<String, Integer> month = new HashMap<>();

    static {
        month.put("Январь", 1);
        month.put("Февраль", 2);
        month.put("Март", 3);
        month.put("Апрель", 4);
        month.put("Май", 5);
        month.put("Июнь", 6);
        month.put("Июль", 7);
        month.put("Агуст", 8);
        month.put("Сентябрь", 9);
        month.put("Октябрь", 10);
        month.put("Ноябрь", 11);
        month.put("Декабрь", 12);
    }

    private static final String TAG = "MyCustomDialog";
    private int startMonth;
    private int startYear;
    private int endMonth;
    private int endYear;

    private PeriodProcessor periodProcessor;

    public DateDialog(PeriodProcessor periodProcessor) {
        this.periodProcessor = periodProcessor;
    }


    private void setSpinnerHandler(final Spinner spinner, final SpinnerSelectedItemHandler handler) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinner.getSelectedItem().toString();
                handler.onSelect(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_dialog, container, false);
        TextView mActionOk = (TextView) view.findViewById(R.id.action_ok);
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
                periodProcessor.process(
                        DateDialog.this.startMonth,
                        DateDialog.this.startYear,
                        DateDialog.this.endMonth,
                        DateDialog.this.endYear
                );

            }
        });


        Spinner spinnerDateBeginMonth = (Spinner) view.findViewById(R.id.spinnerMonthBegin);
        Spinner spinnerDateBeginYear = (Spinner) view.findViewById(R.id.spinnerYearBegin);
        Spinner spinnerDateEndMonth = (Spinner) view.findViewById(R.id.spinnerMonthEnd);
        Spinner spinnerDateEndYear = (Spinner) view.findViewById(R.id.spinnerYearEnd);

        setSpinnerHandler(spinnerDateBeginMonth, (String value) -> DateDialog.this.startMonth = month.get(value));
        setSpinnerHandler(spinnerDateBeginYear, (String value) -> DateDialog.this.startYear = Integer.parseInt(value));
        setSpinnerHandler(spinnerDateEndMonth, (String value) -> DateDialog.this.endMonth = month.get(value));
        setSpinnerHandler(spinnerDateEndYear, (String value) -> DateDialog.this.endYear = Integer.parseInt(value));

        initMonthSpinner(spinnerDateBeginMonth);
        initMonthSpinner(spinnerDateEndMonth);
        initYearSpinner(spinnerDateBeginYear);
        initYearSpinner(spinnerDateEndYear);

        return view;
    }

    private void initMonthSpinner(Spinner spinner) {
        ArrayList<String> data = new ArrayList(month.keySet());


        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// устанавливаем выпадающий список для спиннера
        spinner.setAdapter(adapter);
    }

    private void initYearSpinner(Spinner spinner) {
        ArrayList<String> data = new ArrayList();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = year; i >= year-5; i--) {
            data.add(String.valueOf(i));
        }


        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// устанавливаем выпадающий список для спиннера
        spinner.setAdapter(adapter);
    }
}
