package com.gorvodokanal.meters.net;

public final class UrlCollection {
    public static final String BASE_URL = "https://www.gorvodokanal.com/mobile_app/";
    public static String KOD = "10-6666666";

    public static final String AUTH_URL = BASE_URL + "auth.php"; //url, из которого мы будем брать JSON-объект
    public static final String GENERAL_INFO_URL = BASE_URL + "general_info.php";
    public static final String SETTING_URL = BASE_URL + "setting.php";
    public static final String HISTORY_METERS = BASE_URL + "history_meters.php";
    public static final String PASS_METERS = BASE_URL + "pass_meters.php";
    public static final String SET_METERS =  BASE_URL + "set_meters.php";
    public static final String PAYMENT_METERS =  BASE_URL + "payment_meters.php";


}
