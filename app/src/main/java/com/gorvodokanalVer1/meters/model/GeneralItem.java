package com.gorvodokanalVer1.meters.model;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneralItem {
    private int nomer_vodomer;
    private String vid_uslgi;
    private String date_ust;
    private  String date_poverk;
    private int IPU;



    public GeneralItem() {

    }


    public GeneralItem (JSONObject row) throws JSONException {
        nomer_vodomer =  row.getInt("N_VODOMER");
        vid_uslgi =  row.getString("VID");
        date_ust=  row.getString("DAT_UST");
        date_poverk = row.getString("DAT_POVER");
        IPU = row.getInt("IPU");
    }



    public int getNomer_vodomer() {
        return nomer_vodomer;
    }
    public String getVid_uslgi() {  return vid_uslgi;  }
    public String getDate_ust() {
        return  date_ust;
    }
    public String getDate_poverk() {return date_poverk;}


    public int getIPU() {
        return IPU;
    }

    public void setIPU(int IPU) {
        this.IPU = IPU;
    }

    public void setNomer_vodomer(int nomer_vodomer) {
        this.nomer_vodomer = nomer_vodomer;
    }

    public void setVid_uslgi(String vid_uslgi) {
        this.vid_uslgi = vid_uslgi;
    }

    public void setDate_ust(String date_ust) {
        this.date_ust = date_ust;
    }

    public void setDate_poverk(String date_poverk) {
        this.date_poverk = date_poverk;
    }





}
