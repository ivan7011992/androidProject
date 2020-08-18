package com.gorvodokanal.meters.activity;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.HistoryItem;
import com.gorvodokanal.meters.model.SummaryHistoryItem;

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

        for (HistoryItem Item : historyItem.getItems()) {

        }
        return view;
    }

}


