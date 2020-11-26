package com.gorvodokanalVer1.meters.model;

import org.json.JSONException;
import org.json.JSONObject;


public class VodomerItem {
        private int node;
        private int nomerVodomer;
        private String enterDate;
        private  double pokaz;
        private String date_prom;
        private String pr_vod;





        public VodomerItem(JSONObject row) throws JSONException {
            if (row.has("NODE")) {
                node = row.getInt("NODE");
            }
            nomerVodomer =  row.getInt("N_VODOMER");
            enterDate =  row.getString("ENTER_DATE");
            pokaz = row.getDouble("POKAZ2");
            date_prom = row.getString("DATE_PROM");
            if(date_prom.equals("null")){
                date_prom = null;
            }
            pr_vod = row.getString("PR_VOD");
                if(pr_vod.equals("1")){
                    pr_vod = "- Холодная";
                }else if(pr_vod.equals("2")){
                    pr_vod = "- Горячая";
                }



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

        public String getPr_vod() {
            return pr_vod;
        }

        public String getDate_prom() {
            return date_prom;
        }
    }





