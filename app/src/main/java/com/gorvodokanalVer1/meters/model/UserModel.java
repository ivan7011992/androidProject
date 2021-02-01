package com.gorvodokanalVer1.meters.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserModel {
    private static UserModel instance;

    private String login;
    private HashMap<Integer,String> ls;
    private  int countSupportItems;

    private UserModel(String login, HashMap<Integer, String> ls, int countSupportItems) {
        this.login = login;
        this.ls = ls;
        this.countSupportItems = countSupportItems;
    }

    public void removeLs(Integer lsUser){
        ls.remove(lsUser);
            }
    public void addLs(Integer lsUser,String login) {
        ls.put(lsUser,login);
    }
    public String getLogin() {
        return login;
    }

    public int getCountSupportItems() {
        return countSupportItems;
    }

    public HashMap<Integer, String> getLs() {
        return ls;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public static UserModel getInstance() {
        return instance;
    }

    public static void createInstance(String login, HashMap<Integer, String> ls,int countSupportItems) {
        instance = new UserModel(login, ls,countSupportItems);
    }
    public  static void createInstanceFromJson(JSONObject json) throws JSONException {
        HashMap<Integer, String> ls = new HashMap<>();
        JSONArray lsList = json.getJSONArray("ls");
        for(int i = 0; i < lsList.length(); i++) {
            JSONObject currentLs = (JSONObject) lsList.get(i);
            ls.put(Integer.parseInt(currentLs.getString("ID")), currentLs.getString("LOGIN"));
        }
        Integer countSupportItems = Integer.parseInt(json.getString("SupportItems"));
        String login = json.getString("login");
        UserModel.createInstance(login,ls,countSupportItems);
    }
    public void clearLs(){
        this.ls =  new HashMap<Integer, String>();
    }


}
