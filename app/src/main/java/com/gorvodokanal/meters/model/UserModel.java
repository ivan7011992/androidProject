package com.gorvodokanal.meters.model;

import java.util.HashMap;

public class UserModel {
    private static UserModel instance;

    private String login;
    private HashMap<Integer,String> ls;

    private UserModel(String login, HashMap<Integer, String> ls) {
        this.login = login;
        this.ls = ls;
    }

    public String getLogin() {
        return login;
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

    public static void createInstance(String login, HashMap<Integer, String> ls) {
        instance = new UserModel(login, ls);
    }
}
