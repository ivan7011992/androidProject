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
    private boolean status;
    private  String email;
    private int userId;
    private int statusAuth = 0;

    public void setStatusAuth(int statusAuth) {
        this.statusAuth = statusAuth;
    }

    public int getStatusAuth() {
        return statusAuth;
    }

    private UserModel(String login, HashMap<Integer, String> ls, int countSupportItems, boolean status, String email, int userId) {
        this.login = login;
        this.ls = ls;
        this.countSupportItems = countSupportItems;
        this.status = status;
        this.email = email;
        this.userId = userId;
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

    public boolean isStatus() {
        return status;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static UserModel getInstance() {
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public static void createInstance(String login, HashMap<Integer, String> ls, int countSupportItems, boolean status, String email,int userId) {
        instance = new UserModel(login, ls,countSupportItems,status,email,userId);
    }

    public void setStatus(boolean status) {
        this.status = status;
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
        boolean status = json.getBoolean("statusConfirmMail");
        String email = json.getString("email");
        int userId = json.getInt("id");
        UserModel.createInstance(login,ls,countSupportItems,status,email,userId);
    }
    public void clearLs(){
        this.ls =  new HashMap<Integer, String>();
    }


}
