package com.gorvodokanal.meters.model;

import org.json.JSONException;
import org.json.JSONObject;

public class HistoryItem {
    public static final int VID_USLUGI_VODA = 1;
    public static final int VID_USLUGI_STOKI = 3;
    public static final int VID_USLUGI_ODN = 4;

    private double saldoBegin;
    private double nachisleno;
    private double oplata;
    private int vidUslugi;
    private double dept;


    public HistoryItem(JSONObject row) throws JSONException {
       saldoBegin =  row.getDouble("SALDO_BEGIN");
        nachisleno =  row.getDouble("NACHISLENO");
        oplata =  row.getDouble("OPLATA");
        vidUslugi = row.getInt("VID_USLUGI");
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

    public int getVidUslugi() {
        return vidUslugi;
    }

    public  double getDept(){

        return getSaldoBegin() + getNachisleno() + getOplata();
    }


}
