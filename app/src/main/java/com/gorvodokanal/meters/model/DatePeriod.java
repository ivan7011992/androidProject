package com.gorvodokanal.meters.model;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatePeriod {
    public static final LinkedHashMap<String, Integer> MONTH_NAMES = new LinkedHashMap<>();
    public static final LinkedHashMap<Integer, String> REVERSE_MONTH_NAMES = new LinkedHashMap<>();

    static {
        MONTH_NAMES.put("Январь", 1);
        MONTH_NAMES.put("Февраль", 2);
        MONTH_NAMES.put("Март", 3);
        MONTH_NAMES.put("Апрель", 4);
        MONTH_NAMES.put("Май", 5);
        MONTH_NAMES.put("Июнь", 6);
        MONTH_NAMES.put("Июль", 7);
        MONTH_NAMES.put("Август", 8);
        MONTH_NAMES.put("Сентябрь", 9);
        MONTH_NAMES.put("Октябрь", 10);
        MONTH_NAMES.put("Ноябрь", 11);
        MONTH_NAMES.put("Декабрь", 12);

        for(Map.Entry<String, Integer> entry : MONTH_NAMES.entrySet()) {
            REVERSE_MONTH_NAMES.put(entry.getValue(), entry.getKey());
        }
    }

    private int startMonth;
    private int startYear;
    private int endMonth;
    private int endYear;


    public DatePeriod(int startMonth, int startYear, int endMonth, int endYear) {
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.endMonth = endMonth;
        this.endYear = endYear;
    }


    public static DatePeriod fromCurrentDate() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int endYear = cal.get(Calendar.YEAR);
        int endMonth = cal.get(Calendar.MONTH) + 1;

        int startMonth, startYear;

        if (endMonth - 6 >= 1) {
            startMonth = endMonth - 6;
            startYear = endYear;
        } else {
            startMonth = 12 - (6 - endMonth);
            startYear = endYear - 1;
        }

        return new DatePeriod(startMonth, startYear, endMonth, endYear);
    }

    public boolean valid() {
        if (startYear > endYear) {
            return false;
        }

        if(startYear == endYear) {
            if (startMonth > endMonth) {
                return false;
            }
        }

        return true;
    }

    public String formatStartDate() {
        return String.format("%s.%02d.%d", "01", startMonth, startYear);
    }

    public String formatEndDate() {
        return String.format("%s.%02d.%d", "01", endMonth, endYear);
    }

    public String format() {
        return String.format("%s %d - %s %d", REVERSE_MONTH_NAMES.get(startMonth), startYear, REVERSE_MONTH_NAMES.get(endMonth), endYear);
    }

    public DatePeriod clone() {
        return new DatePeriod(startMonth, startYear, endMonth, endYear);
    }

    public void setStartMonth(String monthName) {
        this.startMonth = MONTH_NAMES.get(monthName);
    }

    public void setEndMonth(String monthName) {
        this.endMonth = MONTH_NAMES.get(monthName);
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndYear() {
        return endYear;
    }


}
