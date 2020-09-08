package com.gorvodokanal.meters.historyUtilClass;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanal.R;
import com.gorvodokanal.meters.model.PaymentItem;
import com.gorvodokanal.meters.model.SummaryHistoryItem;
import com.gorvodokanal.meters.model.SummaryPaymentData;

import java.util.HashMap;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.RecycleViewViewHolder> {
    private HashMap<Integer, String> userInputData = new HashMap<>();

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
        public TextView paymentSum;
        public TextView title;


        public RecycleViewViewHolder(@NonNull View itemView) {
            super(itemView);
            deptBeginPeriodValue = itemView.findViewById(R.id.deptBeginPeriodValue);
            nachisPeriodValue = itemView.findViewById(R.id.nachisPeriodValue);
            oplataPeriodPaymentValue = itemView.findViewById(R.id.oplataPeriodPaymentValue);
            deptPeriodValue = itemView.findViewById(R.id.deptPeriodValue);
            paymentValue = itemView.findViewById(R.id.payment);
            title = itemView.findViewById(R.id.Title);
            paymentSum = itemView.findViewById(R.id.paymentSum);
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
        final PaymentItem item;
        if (i == 0) {
            item = paymentData.getSummaryItem();
            EditText entry = recycleViewViewHolder.paymentValue;
            TextView paymentSum = recycleViewViewHolder.paymentSum;
            paymentSum.setText(String.valueOf(item.getOPLATA()));
            ((ViewManager) entry.getParent()).removeView(entry);
            TextView title = recycleViewViewHolder.title;
            title.setBackgroundResource(0);
            title.setTextSize(18);
        } else {
            item = paymentData.getItem(i - 1);
            recycleViewViewHolder.paymentValue.setText(String.format("%.2f",item.dept()));
            userInputData.put(item.getVID_USLUGI(),String.valueOf(item.dept()));

            TextView paymentSum = recycleViewViewHolder.paymentSum;
            ((ViewManager) paymentSum.getParent()).removeView(paymentSum);
            recycleViewViewHolder.paymentValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    PaymentAdapter.this.userInputData.put(item.getVID_USLUGI(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        recycleViewViewHolder.deptBeginPeriodValue.setText(String.format("%.2f",item.getSALDO_BEGIN()));
        recycleViewViewHolder.nachisPeriodValue.setText(String.format("%.2f",item.getNACHISLENO()));
        recycleViewViewHolder.oplataPeriodPaymentValue.setText(String.format("%.2f",item.getOPLATA()));
        recycleViewViewHolder.deptPeriodValue.setText(String.format("%.2f",item.dept()));
        recycleViewViewHolder.title.setText(String.valueOf(item.getNAME_USLUGI()));


    }

    @Override
    public int getItemCount() {
        return paymentData.size() + 1;
    }

    public HashMap<Integer, String> getUserInputData() {
        return userInputData;
    }
}






