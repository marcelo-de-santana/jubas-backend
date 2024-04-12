package com.jubasbackend.utils;

import java.time.*;

public class DateTimeUtils {

    public static final ZoneOffset BRAZILIAN_ZONE = ZoneOffset.of("-3");

    public static final Instant BRAZILIAN_INSTANT = Instant.now()
            .atOffset(BRAZILIAN_ZONE).toInstant();

    public static final LocalDateTime BRAZILIAN_DATETIME = LocalDateTime.now()
            .atOffset(BRAZILIAN_ZONE).toLocalDateTime();

    public static final LocalTime BRAZILIAN_TIME = LocalTime.now()
            .atOffset(BRAZILIAN_ZONE).toLocalTime();

    public static LocalDateTime obtainDateTimeFromOptionalDate(LocalDate optionalDate) {
        var date = optionalDate != null ? optionalDate : LocalDate.now();

        if (date.equals(LocalDate.now()))
            return LocalDateTime.now();
        return date.atStartOfDay();
    }

    public static LocalDateTime parseEndOfDay(LocalDate dateTime) {
        return dateTime.atTime(23, 59, 59);
    }

    public static LocalDateTime parseEndOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(23, 59, 59);
    }

    public static LocalDateTime parseStatOfDay(LocalDate dateTime) {
        return dateTime.atStartOfDay();
    }

    public static LocalDateTime parseStatOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

}
