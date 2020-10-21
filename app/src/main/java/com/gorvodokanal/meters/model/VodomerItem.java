package com.gorvodokanal.meters.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;


    public class VodomerItem {
        private int node;
        private int nomerVodomer;
        private String enterDate;
        private  double pokaz;


        public VodomerItem(JSONObject row) throws JSONException {
            node =  row.getInt("NODE");
            nomerVodomer =  row.getInt("N_VODOMER");
            enterDate =  row.getString("ENTER_DATE");
            pokaz = row.getDouble("POKAZ2");
        }

        public int getNode() {
            return  node;
        }

        public int getNomerVodomer() {
            return nomerVodomer;
        }

        public String  getEnterDate() {
            return enterDate;
        }

        public double getPokaz() {
            return pokaz;
        }
    }





