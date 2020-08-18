package com.gorvodokanal.meters.model;

import java.util.HashMap;

public class DateConverter {
    private static HashMap<String, String> monthNames = new HashMap<>();

    static {
        monthNames.put("JAN", "Январь");
        monthNames.put("FEB", "Февраль");
        monthNames.put("MAR", "Март");
        monthNames.put("APR", "Апрель");
        monthNames.put("MAY", "Май");
        monthNames.put("JUN", "Июнь");
        monthNames.put("JUL", "Июль");
        monthNames.put("AUG", "Август");
        monthNames.put("SEP", "Сентябрь");
        monthNames.put("OCT", "Октябрь");
        monthNames.put("NOV", "Ноябрь");
        monthNames.put("DEC", "Декабрь");

    }

    public static String convert(String date) {
        String[] dateParth = date.split("-");
        String monthName = monthNames.get(dateParth[1]);
        String year = "20" + dateParth[2];
        return monthName + " " + year;
    }
}
