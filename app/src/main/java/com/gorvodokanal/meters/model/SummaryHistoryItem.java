package com.gorvodokanal.meters.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SummaryHistoryItem {

    private String startDate;
    private ArrayList<HistoryItem> items = new ArrayList<>();

    public SummaryHistoryItem(String startDate) throws JSONException {
        this.startDate = startDate;
    }

    public void addItem(HistoryItem item) {
        items.add(item);
    }

    public double saldoBegin() {
        double sum = 0.0;
        for(HistoryItem item : items) {
            sum += item.getSaldoBegin();
        }
        return sum;
    }

    public double nachisleno() {
        double sum = 0.0;
        for(HistoryItem item : items) {
            sum += item.getNachisleno();
        }
        return sum;
    }

    public double oplata() {
        double sum = 0.0;
        for(HistoryItem item : items) {
            sum += item.getOplata();
        }
        return sum;
    }

    public double debt() {
       return saldoBegin() + nachisleno() - oplata();
    }

    public String getStartDate() {
        return startDate;
    }

    public String getReadableDate() {
        return DateConverter.convert(startDate);
    }

    public ArrayList<HistoryItem> getItems() {
        return items;
    }
}
