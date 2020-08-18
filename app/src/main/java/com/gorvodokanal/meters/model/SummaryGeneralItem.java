package com.gorvodokanal.meters.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SummaryGeneralItem {


    private ArrayList<GeneralItem> generaltItems = new ArrayList<>();


    public SummaryGeneralItem(JSONArray data) throws JSONException {
        for (int i = 0; i < data.length(); i++) {
            JSONObject currentPaymentObj = (JSONObject) data.get(i);
            GeneralItem generalItem = new GeneralItem(currentPaymentObj);
            generaltItems.add(generalItem);
        }
    }

    public GeneralItem getSummaryItem() {
        GeneralItem summaryItem = new GeneralItem();
        summaryItem.setNomer_vodomer(nomerVodomer());
        summaryItem.setVid_uslgi(vidUslugi());
        summaryItem.setDate_ust(dataUst());
        summaryItem.setDate_poverk(datePoverk());
        return summaryItem;
    }

    public GeneralItem getItem(int i) {
        return generaltItems.get(i);
    }

    public int size() {
        return generaltItems.size();
    }

    private int nomerVodomer() {
        int sum = 0;
        for (GeneralItem item : generaltItems) {
            sum = item.getNomer_vodomer();
        }
        return sum;
    }

    private String vidUslugi() {
         String sum = null;
        for (GeneralItem item :  generaltItems) {
           sum = item.getVid_uslgi();
        }
        return sum;
    }

    private String  dataUst() {
        String sum = null;
        for (GeneralItem item : generaltItems) {
            sum = item.getDate_ust();
        }
        return sum;
    }

    private String datePoverk() {
        String sum = null;
        for (GeneralItem item : generaltItems) {
            sum = item.getDate_poverk();
        }
        return sum;
    }


}
