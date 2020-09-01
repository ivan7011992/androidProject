package com.gorvodokanal.meters.historyUtilClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.activity.DetailHistoryDialog;
import com.gorvodokanal.meters.model.SummaryHistoryItem;

import java.util.ArrayList;


public class SummaryHistoryItemAdapter extends RecyclerView.Adapter<SummaryHistoryItemAdapter.RecycleViewViewHolder> {
    private ArrayList<SummaryHistoryItem> historyItems;
    private Fragment historyMetersFragment;

    public SummaryHistoryItemAdapter(ArrayList<SummaryHistoryItem> historyItems, Fragment historyMetersFragment) {
        this.historyItems = historyItems;
        this.historyMetersFragment = historyMetersFragment;
    }

    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {//ViewViewHolder, держатель вида, содержит желемнты котрые нахотся в кадом элементе recycle_view

        public TextView date;
        public TextView saldoBeginValue;
        public TextView nachislenoValue;
        public TextView oplataValue;
        public TextView dept;
        public TextView informationButton;



        public RecycleViewViewHolder(@NonNull View itemView) {//коструктор,  View itemView, этот параметор это отльный элемент RecelceView
            super(itemView);
            date = itemView.findViewById(R.id.dateHistory);
            saldoBeginValue = itemView.findViewById(R.id.saldoBeginValue);
            nachislenoValue = itemView.findViewById(R.id.nachisPeriodValue);
            oplataValue = itemView.findViewById(R.id.oplataPeriodValue);
            dept = itemView.findViewById(R.id.deptValue);

            informationButton = itemView.findViewById(R.id.information);


        }
    }


    @NonNull
    @Override
    public RecycleViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {// нужно передать разметку в наш адаптер
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_items, viewGroup, false);
        RecycleViewViewHolder recycleViewViewHolder = new RecycleViewViewHolder(view);
        return recycleViewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleViewViewHolder recycleViewViewHolder, int i) {
        final SummaryHistoryItem historyItem = historyItems.get(i);
        recycleViewViewHolder.date.setText(historyItem.getReadableDate());
        recycleViewViewHolder.saldoBeginValue.setText(String.valueOf(historyItem.saldoBegin()));
        recycleViewViewHolder.nachislenoValue.setText(String.valueOf(historyItem.nachisleno()));
        recycleViewViewHolder.oplataValue.setText(String.valueOf(historyItem.oplata()));
        recycleViewViewHolder.dept.setText(String.valueOf(historyItem.debt()));

        recycleViewViewHolder.informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailHistoryDialog dialog = new DetailHistoryDialog(historyItem);
                dialog.setTargetFragment(historyMetersFragment, 1);
                dialog.show(historyMetersFragment.getFragmentManager(), "MyCustomDialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }


}