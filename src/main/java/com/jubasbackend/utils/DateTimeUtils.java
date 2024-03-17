package com.jubasbackend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeUtils {

    public static LocalDateTime obtainDateTimeFromOptionalDate(LocalDate optionalDate) {
        var date = optionalDate != null ? optionalDate : LocalDate.now();

        if (date.equals(LocalDate.now()))
            return LocalDateTime.now();

        return date.atStartOfDay();
    }

    public static LocalDateTime parseEndOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(23, 59, 59);
    }

    public static LocalDateTime parseStatOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

}
