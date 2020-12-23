package com.gorvodokanalVer1.meters.historyUtilClass;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gorvodokanalVer1.R;
import com.gorvodokanalVer1.meters.activity.PaymentFragment;
import com.gorvodokanalVer1.meters.model.PaymentItem;
import com.gorvodokanalVer1.meters.model.SummaryPaymentData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.RecycleViewViewHolder> {
    private HashMap<Integer, String> userInputData = new HashMap<>();
    TextView paymentSumTotalPay;
    private SummaryPaymentData paymentData;
    private  PaymentFragment paymentFragment;


    public PaymentAdapter(SummaryPaymentData paymentData,PaymentFragment paymentFragment) {
        this.paymentData = paymentData;
        this.paymentFragment = paymentFragment;
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
            paymentValue.setMaxLines(1);
            paymentValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            paymentValue.setKeyListener(DigitsKeyListener.getInstance(true, true));
            title = itemView.findViewById(R.id.TitlePayment);
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
            paymentSumTotalPay = recycleViewViewHolder.paymentSum;
            paymentSumTotalPay.setText(String.format("%.2f",item.dept()));
            ((ViewManager) entry.getParent()).removeView(entry);
            double sum =0;
            double totalSum = item.dept();
            paymentFragment.setTextSum(sum,totalSum);
            TextView title = recycleViewViewHolder.title;
            title.setBackgroundResource(0);
            title.setTextSize(18);


        } else {
            item = paymentData.getItem(i - 1);
            recycleViewViewHolder.paymentValue.setHint(String.format("%.2f", item.dept()));
            userInputData.put(item.getVID_USLUGI(), String.valueOf(item.dept()));

            TextView paymentSum = recycleViewViewHolder.paymentSum;
            ((ViewManager) paymentSum.getParent()).removeView(paymentSum);
            recycleViewViewHolder.paymentValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    recycleViewViewHolder.paymentValue.setHint(String.format("0.0"));
                    PaymentAdapter.this.userInputData.put(item.getVID_USLUGI(), s.toString());
                  double sum =  changePayment(PaymentAdapter.this.userInputData);
                    paymentSumTotalPay.setText(String.format("%.2f",sum));
                     double totalSum = item.dept();

                   paymentFragment.setTextSum(sum,totalSum);



                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        recycleViewViewHolder.deptBeginPeriodValue.setText(String.format("%.2f", item.getSALDO_BEGIN()));
        recycleViewViewHolder.nachisPeriodValue.setText(String.format("%.2f", item.getNACHISLENO()));
        recycleViewViewHolder.oplataPeriodPaymentValue.setText(String.format("%.2f", item.getOPLATA()));
        recycleViewViewHolder.deptPeriodValue.setText(String.format("%.2f", item.dept()));
        recycleViewViewHolder.title.setText(String.valueOf(item.getNAME_USLUGI()));


    }

    private double changePayment(HashMap dataPayment) {
        Set<Map.Entry<Integer, String>> set = dataPayment.entrySet();
        double sum = 0.0;
        for (Map.Entry<Integer, String> entry : set) {
            String value = entry.getValue();
            try {
                sum += Double.parseDouble(value);
            }  catch (NumberFormatException e) {

            }

        }
        return sum;
    }


    @Override
    public int getItemCount() {
        return paymentData.size() + 1;
    }

    public HashMap<Integer, String> getUserInputData() {
        return userInputData;
    }
}






