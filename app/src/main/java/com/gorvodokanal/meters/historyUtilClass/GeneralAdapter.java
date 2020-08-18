package com.gorvodokanal.meters.historyUtilClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.GeneralItem;
import com.gorvodokanal.meters.model.PaymentItem;
import com.gorvodokanal.meters.model.SummaryGeneralItem;
import com.gorvodokanal.meters.model.SummaryHistoryItem;
import com.gorvodokanal.meters.model.SummaryPaymentData;

import java.util.ArrayList;

public class GeneralAdapter extends RecyclerView.Adapter<GeneralAdapter.RecycleViewViewHolder> {
    private SummaryGeneralItem genetalData;
    private ArrayList<SummaryGeneralItem> generalItems;

    public GeneralAdapter(SummaryGeneralItem generalData) {
        this.genetalData = generalData;
    }

    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {
        public TextView nomer_vodomer;
        public TextView vid_uslgi;
        public TextView date_ust;
        public TextView date_poverk;





        public RecycleViewViewHolder(@NonNull View itemView) {
            super(itemView);
           nomer_vodomer = itemView.findViewById(R.id.TitleValue);
            vid_uslgi = itemView.findViewById(R.id.viewPuValue);
            date_ust = itemView.findViewById(R.id.dateInstalValue);
            date_poverk = itemView.findViewById(R.id.datePoverkValue);

        }
    }

    @NonNull
    @Override
    public RecycleViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.general_item, viewGroup, false);
        RecycleViewViewHolder recycleViewViewHolder = new RecycleViewViewHolder(view);
        return recycleViewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewViewHolder recycleViewViewHolder, int i) {
        GeneralItem item;
        item = genetalData.getItem(i);

        //final SummaryGeneralItem generalItem = generalItems.get(i);// при помощт i свящвваем каждый элемт из ArrayList с элметом RecycleVIew

        recycleViewViewHolder.nomer_vodomer.setText(String.valueOf(item.getNomer_vodomer()));
        recycleViewViewHolder.vid_uslgi.setText(String.valueOf(item.getVid_uslgi()));
        recycleViewViewHolder.date_ust.setText(String.valueOf(item.getDate_ust()));
        recycleViewViewHolder.date_poverk.setText(String.valueOf(item.getDate_poverk()));




    }

    @Override
    public int getItemCount() {
        return genetalData.size() ;
    }

}
