package com.gorvodokanalVer1.meters.model;

import com.gorvodokanalVer1.meters.Tuple;

import java.util.HashMap;

public class DateConverter {
    private static HashMap<String, String> monthNames = new HashMap<>();
     private static  HashMap<String,Integer> mounthNumbers = new HashMap<>();

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

        mounthNumbers.put("JAN", 1);
        mounthNumbers.put("FEB", 2);
        mounthNumbers.put("MAR", 3);
        mounthNumbers.put("APR", 4);
        mounthNumbers.put("MAY", 5);
        mounthNumbers.put("JUN", 6);
        mounthNumbers.put("JUL", 7);
        mounthNumbers.put("AUG", 8);
        mounthNumbers.put("SEP", 9);
        mounthNumbers.put("OCT", 10);
        mounthNumbers.put("NOV", 11);
        mounthNumbers.put("DEC", 12);

    }



    public static String convert(String date) {
        String[] dateParth = date.split("-");
        String monthName = monthNames.get(dateParth[1]);
        String year = "20" + dateParth[2];
        return monthName + " " + year;
    }

    public static Tuple<Integer,Integer> dateToMonthYear(String date){
        String[] dateParth = date.split("-");
        int month =   mounthNumbers.get(dateParth[1]);
        int year  = Integer.parseInt("20" + dateParth[2]);
        return  new Tuple<>(month, year);
    }

}
