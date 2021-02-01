package com.gorvodokanalVer1.meters.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentItem {
    private String DATE_OT;
    private double SALDO_BEGIN;
    private double OPLATA;
    private int VID_USLUGI;
    private String NAME_USLUGI;
    private double NACHISLENO;
    private String TITLE;


    public PaymentItem() {

    }

    public PaymentItem(JSONObject row) throws JSONException {
        DATE_OT = row.getString("DATE_OT");
        SALDO_BEGIN = row.getDouble("SALDO_BEGIN");
        NACHISLENO = row.getDouble("NACHISLENO");
        OPLATA = row.getDouble("OPLATA");
        VID_USLUGI = row.getInt("VID_USLUGI");
        NAME_USLUGI = row.getString("NAME_USLUGI");



    }

    public double dept() {
        double dept;
        dept = getSALDO_BEGIN() + getNACHISLENO() - getOPLATA();
        if(dept<0){
            dept = 0;
        }
        return dept;
    }

    public double deptEnd() {
        double dept;
        dept = getSALDO_BEGIN() + getNACHISLENO() - getOPLATA();
        return dept;
    }
    public String getDATE_OT() {
        return DATE_OT;
    }

    public double getSALDO_BEGIN() {
        return SALDO_BEGIN;
    }

    public double getNACHISLENO() {
        return NACHISLENO;
    }

    public double getOPLATA() {
        return OPLATA;
    }

    public int getVID_USLUGI() {
        return VID_USLUGI;
    }

    public String getNAME_USLUGI() {
        return NAME_USLUGI;
    }

    public void setDATE_OT(String DATE_OT) {
        this.DATE_OT = DATE_OT;
    }

    public void setSALDO_BEGIN(double SALDO_BEGIN) {
        this.SALDO_BEGIN = SALDO_BEGIN;
    }

    public void setOPLATA(double OPLATA) {
        this.OPLATA = OPLATA;
    }

    public void setVID_USLUGI(int VID_USLUGI) {
        this.VID_USLUGI = VID_USLUGI;
    }

    public void setNAME_USLUGI(String NAME_USLUGI) {
        this.NAME_USLUGI = NAME_USLUGI;
    }

    public void setNACHISLENO(double NACHISLENO) {
        this.NACHISLENO = NACHISLENO;
    }
}

