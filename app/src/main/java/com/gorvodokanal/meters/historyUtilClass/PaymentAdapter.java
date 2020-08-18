package com.gorvodokanal.meters.historyUtilClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.PaymentItem;
import com.gorvodokanal.meters.model.SummaryHistoryItem;
import com.gorvodokanal.meters.model.SummaryPaymentData;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.RecycleViewViewHolder> {


    private SummaryPaymentData paymentData;

    public PaymentAdapter(SummaryPaymentData paymentData) {
        this.paymentData = paymentData;
    }

    public static class RecycleViewViewHolder extends RecyclerView.ViewHolder {
        public TextView deptBeginPeriodValue;
        public TextView nachisPeriodValue;
        public TextView oplataPeriodPaymentValue;
        public TextView deptPeriodValue;
        public EditText paymentValue;

        public TextView title;


        public RecycleViewViewHolder(@NonNull View itemView) {
            super(itemView);
            deptBeginPeriodValue = itemView.findViewById(R.id.deptBeginPeriodValue);
            nachisPeriodValue = itemView.findViewById(R.id.nachisPeriodValue);
            oplataPeriodPaymentValue = itemView.findViewById(R.id.oplataPeriodPaymentValue);
            deptPeriodValue = itemView.findViewById(R.id.deptPeriodValue);
            paymentValue = itemView.findViewById(R.id.paymentValue);
            title = itemView.findViewById(R.id.Title);
        }
    }

    @NonNull
    @Override
    public RecycleViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_meters_items, viewGroup, false);
        RecycleViewViewHolder recycleViewViewHolder = new RecycleViewViewHolder(view);
        return recycleViewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewViewHolder recycleViewViewHolder, int i) {
        PaymentItem item;
        if(i == 0) {
            item = paymentData.getSummaryItem();
        } else {
            item = paymentData.getItem(i - 1);
        }

        recycleViewViewHolder.deptBeginPeriodValue.setText(String.valueOf(item.getSALDO_BEGIN()));
       recycleViewViewHolder.nachisPeriodValue.setText(String.valueOf(item.getNACHISLENO()));
       recycleViewViewHolder.oplataPeriodPaymentValue.setText(String.valueOf(item.getOPLATA()));
       recycleViewViewHolder.deptPeriodValue.setText(String.valueOf(item.dept()));
       recycleViewViewHolder.title.setText(String.valueOf(item.getNAME_USLUGI()));
    }

    @Override
    public int getItemCount() {
        return paymentData.size() + 1;
    }


}






