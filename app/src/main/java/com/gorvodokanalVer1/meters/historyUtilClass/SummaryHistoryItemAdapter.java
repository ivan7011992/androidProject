package com.gorvodokanalVer1.meters.historyUtilClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.Tuple;
import com.gorvodokanalVer1.meters.Utils;
import com.gorvodokanalVer1.meters.activity.DetailHistoryDialog;
import com.gorvodokanalVer1.meters.model.DateConverter;
import com.gorvodokanalVer1.meters.model.SummaryHistoryItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


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

        Tuple<Integer, Integer> startDate = DateConverter.dateToMonthYear(historyItem.getStartDate());
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if(startDate.x == month && startDate.y == year){

            Utils.removeElement( recycleViewViewHolder.informationButton);
        }

            recycleViewViewHolder.date.setText(historyItem.getReadableDate());
            recycleViewViewHolder.saldoBeginValue.setText(String.format("%.2f", historyItem.saldoBegin()));
            recycleViewViewHolder.nachislenoValue.setText(String.format("%.2f", historyItem.nachisleno()));
            recycleViewViewHolder.oplataValue.setText(String.format("%.2f", historyItem.oplata()));
            recycleViewViewHolder.dept.setText(String.format("%.2f", historyItem.debt()));


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