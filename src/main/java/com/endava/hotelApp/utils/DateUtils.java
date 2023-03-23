package com.endava.hotelApp.utils;

import java.time.LocalDate;

public class DateUtils {
    
    /**
     * Checks if this date is after or equal the specified date.
     * */
    public static boolean isAfterOrEqual(LocalDate date, LocalDate other){
        return date.isAfter(other) || date.isEqual(other);
    }

    /**
     * Checks if this date is before or equal the specified date.
     * */
    public static boolean isBeforeOrEqual(LocalDate date, LocalDate other){
        return date.isBefore(other) || date.isEqual(other);
    }
}
