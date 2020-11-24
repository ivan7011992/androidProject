package com.gorvodokanal.meters.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.DatePeriod;

import java.util.ArrayList;
import java.util.Calendar;

public class DateDialog extends DialogFragment {

    interface SpinnerSelectedItemHandler {
        public void onSelect(String value);
    }

    public interface PeriodProcessor {
        public void process(DatePeriod period);
    }

    private static final String TAG = "MyCustomDialog";
    private DatePeriod currentPeriod;
    private PeriodProcessor periodProcessor;

    public DateDialog(DatePeriod datePeriod, PeriodProcessor periodProcessor) {
        this.periodProcessor = periodProcessor;
        this.currentPeriod = datePeriod.clone();
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
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPeriod.valid()) {
                    Log.d(TAG, "onClick: closing dialog");
                    getDialog().dismiss();
                    periodProcessor.process(currentPeriod);
                } else {
                    Toast.makeText(getContext(), "Дата начала периода не может быть больше даты конца периода", Toast.LENGTH_LONG).show();
                }
            }
        });


        Spinner spinnerDateBeginMonth = (Spinner) view.findViewById(R.id.spinnerMonthBegin);
        Spinner spinnerDateBeginYear = (Spinner) view.findViewById(R.id.spinnerYearBegin);
        Spinner spinnerDateEndMonth = (Spinner) view.findViewById(R.id.spinnerMonthEnd);
        Spinner spinnerDateEndYear = (Spinner) view.findViewById(R.id.spinnerYearEnd);

        setSpinnerHandler(spinnerDateBeginMonth, (String value) -> DateDialog.this.currentPeriod.setStartMonth(value));
        setSpinnerHandler(spinnerDateBeginYear, (String value) -> DateDialog.this.currentPeriod.setStartYear(Integer.parseInt(value)));
        setSpinnerHandler(spinnerDateEndMonth, (String value) -> DateDialog.this.currentPeriod.setEndMonth(value));
        setSpinnerHandler(spinnerDateEndYear, (String value) -> DateDialog.this.currentPeriod.setEndYear(Integer.parseInt(value)));

        initMonthSpinner(spinnerDateBeginMonth,  DateDialog.this.currentPeriod.getStartMonth() - 1);
        initMonthSpinner(spinnerDateEndMonth, DateDialog.this.currentPeriod.getEndMonth() - 1);
        initYearSpinner(spinnerDateBeginYear, DateDialog.this.currentPeriod.getStartYear());
        initYearSpinner(spinnerDateEndYear, DateDialog.this.currentPeriod.getEndYear());


        return view;
    }

    private void initMonthSpinner(Spinner spinner, int initialPosition) {
        ArrayList<String> data = new ArrayList(DatePeriod.MONTH_NAMES.keySet());

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// устанавливаем выпадающий список для спиннера
        spinner.setAdapter(adapter);

        spinner.setSelection(initialPosition);
    }

    private void initYearSpinner(Spinner spinner, int initialYear) {
        ArrayList<String> data = new ArrayList();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 5; i--) {
            data.add(String.valueOf(i));
        }


        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, data);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// устанавливаем выпадающий список для спиннера
        spinner.setAdapter(adapter2);

        spinner.setSelection(currentYear - initialYear);

    }


}
