package com.gorvodokanalVer1.meters.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SummaryPaymentData {
    private ArrayList<PaymentItem> paymentItems = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    String[] monthNames = { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
    String month = monthNames[calendar.get(Calendar.MONTH)];
  int year = calendar.get(Calendar.YEAR);


    public SummaryPaymentData(JSONArray data) throws JSONException {
        for (int i = 0; i < data.length(); i++) {
            JSONObject currentPaymentObj = (JSONObject) data.get(i);
            PaymentItem paymentItem = new PaymentItem(currentPaymentObj);
            paymentItems.add(paymentItem);
        }
    }

    public PaymentItem getSummaryItem() {
        PaymentItem summaryItem = new PaymentItem();
        summaryItem.setSALDO_BEGIN(saldoBegin());
        summaryItem.setNACHISLENO(nachisleno());
        summaryItem.setOPLATA(oplata());
        summaryItem.setNAME_USLUGI(month + " " + year);
        return summaryItem;
    }

    public PaymentItem getItem(int i) {
        return paymentItems.get(i);
    }

    public int size() {
        return paymentItems.size();
    }

    private double saldoBegin() {
        double sum = 0.0;
        for (PaymentItem item : paymentItems) {
            sum += item.getSALDO_BEGIN();
        }
        return sum;
    }

    private double nachisleno() {
        double sum = 0.0;
        for (PaymentItem item : paymentItems) {
            sum += item.getNACHISLENO();
        }
        return sum;
    }

    private double oplata() {
        double sum = 0.0;
        for (PaymentItem item : paymentItems) {
            sum += item.getOPLATA();
        }
        return sum;
    }


}
