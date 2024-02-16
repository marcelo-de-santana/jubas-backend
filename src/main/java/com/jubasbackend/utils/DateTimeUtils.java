package com.jubasbackend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class DateTimeUtils {

    public static LocalDateTime getSelectedDate(Optional<LocalDate> date) {
        if (date.isEmpty() || date.get().equals(LocalDate.now()))
            return LocalDateTime.now();

        return date.get().atStartOfDay();
    }

    public static LocalDateTime getCurrentOrFutureDate(LocalDate date) {
        if (date.equals(LocalDate.now()))
            return LocalDateTime.now();

        return date.atStartOfDay();
    }


    public static LocalDateTime getEndDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(23, 59, 59);
    }
}
