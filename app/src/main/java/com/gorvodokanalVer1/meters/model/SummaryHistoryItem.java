package com.gorvodokanalVer1.meters.model;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class SummaryHistoryItem {

    private String startDate;
    private HashMap<Integer, HistoryItem> items = new HashMap<>();


    public SummaryHistoryItem(String startDate) throws JSONException {
        this.startDate = startDate;
    }

    public HistoryItem getByIndex(int index) {
        Object[] keys = items.keySet().toArray();
        return items.get(keys[index]);

    }

    public void addItem(HistoryItem item) {
        items.put(item.getVidUslugi(), item);
    }

    public double saldoBegin() {
        double sum = 0.0;
        for (Map.Entry<Integer, HistoryItem> item : items.entrySet()) {
            sum += item.getValue().getSaldoBegin();
        }
        return sum;
    }

    public double nachisleno() {
        double sum = 0.0;
        for (Map.Entry<Integer, HistoryItem> item : items.entrySet()) {
            sum += item.getValue().getNachisleno();
        }
        return sum;
    }

    public double oplata() {
        double sum = 0.0;
        for (Map.Entry<Integer, HistoryItem> item : items.entrySet()) {
            sum += item.getValue().getOplata();
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

    public String getByVidUslugi() {
        String uslug = "";
        for (Map.Entry<Integer, HistoryItem> item : items.entrySet()) {
            uslug = item.getValue().getNameUslugi();

        }
        return uslug;
    }

    public int size() {
        return items.size();
    }

}
