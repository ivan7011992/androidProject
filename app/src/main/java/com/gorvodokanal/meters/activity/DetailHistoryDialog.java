package com.gorvodokanal.meters.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    private TextView mActionOk;

    private static final String TAG = "MyCustomDialog";

    public DetailHistoryDialog(SummaryHistoryItem historyItem) {
        this.historyItem = historyItem;

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_history_dialog, container, false);
        mActionOk = view.findViewById(R.id.action_ok);
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        TextView heading = view.findViewById(R.id.headingMonth);
        heading.setText(String.valueOf(historyItem.getReadableDate()));
        TextView saldoBeginValueVodosnab = view.findViewById(R.id.saldoBeginValueVodosnab);
        TextView nachisPeriodValueVodosnab = view.findViewById(R.id.nachisPeriodValueVodosnab);
        TextView oplataPeriodValueVodosnab = view.findViewById(R.id.oplataPeriodValueVodosnab);
        TextView deptValueVodosnab = view.findViewById(R.id.deptValueVodosnab);

        TextView saldoBeginValueVodootv = view.findViewById(R.id.saldoBeginValueVodootv);
        TextView nachisPeriodValueVodootv = view.findViewById(R.id.nachisPeriodValueVodootv);
        TextView oplataPeriodValueVodootv = view.findViewById(R.id.oplataPeriodValueVodootv);
        TextView deptValueVodootv = view.findViewById(R.id.deptValueVodootv);

        TextView saldoBeginValueOdn = view.findViewById(R.id.saldoBeginValueOdn);
        TextView nachisPeriodValueOdn = view.findViewById(R.id.nachisPeriodValueOdn);
        TextView oplataPeriodValueOdn = view.findViewById(R.id.oplataPeriodValueOdn);
        TextView deptValueVodoOdn = view.findViewById(R.id.deptValueOdn);


        HistoryItem voda = historyItem.getByVidUslugi(HistoryItem.VID_USLUGI_VODA);
        HistoryItem stoki = historyItem.getByVidUslugi(HistoryItem.VID_USLUGI_STOKI);
        HistoryItem odn = historyItem.getByVidUslugi(HistoryItem.VID_USLUGI_ODN);



        saldoBeginValueVodosnab.setText(String.format("%.2f",voda.getSaldoBegin()));
        nachisPeriodValueVodosnab.setText(String.format("%.2f",voda.getNachisleno()));
        oplataPeriodValueVodosnab.setText(String.format("%.2f",voda.getOplata()));
        double deptVodosnab = voda.getDept();
        deptValueVodosnab.setText(String.format("%.2f", deptVodosnab));


        saldoBeginValueVodootv.setText(String.format("%.2f",stoki.getSaldoBegin()));
        nachisPeriodValueVodootv.setText(String.format("%.2f",stoki.getNachisleno()));
        oplataPeriodValueVodootv.setText(String.format("%.2f",stoki.getOplata()));
        double deptVootv = stoki.getDept();
        deptValueVodootv.setText(String.format("%.2f",deptVootv));


        saldoBeginValueOdn.setText(String.format("%.2f", odn.getSaldoBegin()));
        nachisPeriodValueOdn.setText(String.format("%.2f",odn.getNachisleno()));
        oplataPeriodValueOdn.setText(String.format("%.2f",odn.getOplata()));
        double deptOdn = odn.getDept();
        deptValueVodoOdn.setText(String.format("%.2f",deptOdn));

        //nachisPeriodValueVodosnab.setText(String.valueOf(stoki.getSaldoBegin()));
        //oplataPeriodValueVodosnab.setText(String.valueOf());

        return view;
    }

}


