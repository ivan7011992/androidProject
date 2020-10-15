package com.gorvodokanal.meters.net;

public final class UrlCollection {
    public static final String BASE_URL = "https://www.gorvodokanal.com/mobile_app/";
    public static String KOD = "10-6666666";

    public static final String AUTH_URL = BASE_URL + "auth.php"; //url, из которого мы будем брать JSON-объект
    public static final String GENERAL_INFO_URL = BASE_URL + "general_info.php";
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "change_password.php";
    public static final String CHANGE_EMAIL_URL = BASE_URL + "change_email.php";
    public static final String HISTORY_METERS = BASE_URL + "history_meters.php";
    public static final String PASS_METERS = BASE_URL + "pass_meters.php";
    public static final String SET_METERS =  BASE_URL + "set_meters.php";
    public static final String PAYMENT_METERS =  BASE_URL + "payment_meters.php";
    public static final String PAYMENT_GENERATE_URL =  BASE_URL + "payment_url_generation.php";
    public static final String REGISTRATION_URL =  BASE_URL + "registration.php";
    public static final String RESENDING_URL =  BASE_URL + "resendingMail.php";
    public static final String RECOVERY_URL =  BASE_URL + "recovery.php";

}

