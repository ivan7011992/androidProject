package com.gorvodokanal.meters.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.HistoryItem;
import com.gorvodokanal.meters.model.SummaryHistoryItem;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailHistoryDialog extends DialogFragment {
    private SummaryHistoryItem historyItem;


    private static final String TAG = "MyCustomDialog";

    public DetailHistoryDialog(SummaryHistoryItem historyItem) {
        this.historyItem = historyItem;

    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_history_dialog, container, false);
        TextView heading = view.findViewById(R.id.headingMonth);
        TextView saldoBeginValueVodosnab = view.findViewById(R.id.saldoBeginValueVodosnab);
        TextView nachisPeriodValueVodosnab = view.findViewById(R.id.nachisPeriodValueVodosnab);
        TextView oplataPeriodValueVodosnab = view.findViewById(R.id.oplataPeriodValueVodosnab);
        TextView deptValueVodosnab = view.findViewById(R.id.deptValueVodosnab);


        HistoryItem voda = historyItem.getByVidUslugi(HistoryItem.VID_USLUGI_VODA);
        HistoryItem stoki = historyItem.getByVidUslugi(HistoryItem.VID_USLUGI_STOKI);
        HistoryItem odn = historyItem.getByVidUslugi(HistoryItem.VID_USLUGI_ODN);


        saldoBeginValueVodosnab.setText(String.valueOf((int) voda.getSaldoBegin()));

        //nachisPeriodValueVodosnab.setText(String.valueOf(stoki.getSaldoBegin()));
        //oplataPeriodValueVodosnab.setText(String.valueOf());

        return view;
    }

}


