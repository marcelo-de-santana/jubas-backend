package com.jubasbackend.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private TimeUtils() {
    }

    public static LocalTime parseToLocalTime(String time) {
        return LocalTime.parse(time);
    }

    public static String formatTimeToString(LocalTime time) {
        return DateTimeFormatter.ofPattern("HH:mm").format(time);
    }
}
