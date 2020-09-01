package com.gorvodokanal.meters.model;

public class UserModel {
    private static UserModel instance;

    private String login;

    private UserModel(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public static UserModel getInstance() {
        return instance;
    }

    public static void createInstance(String login) {
        instance = new UserModel(login);
    }
}
