package com.gorvodokanal.meters.model;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryItem {
    private double saldoBegin;
    private double nachisleno;
    private double oplata;
    private  double val;




    public HistoryItem(JSONObject row) throws JSONException {
       saldoBegin =  row.getDouble("SALDO_BEGIN");
        nachisleno =  row.getDouble("NACHISLENO");
        oplata =  row.getDouble("OPLATA");


    }

    public double getSaldoBegin() {
        return saldoBegin;
    }

    public double getNachisleno() {
        return nachisleno;
    }

    public double getOplata() {
        return oplata;
    }


}
