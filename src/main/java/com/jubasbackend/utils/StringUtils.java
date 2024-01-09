package com.jubasbackend.utils;

public class StringUtils {

    private StringUtils() {
    }

    public static boolean isNonBlank(String input) {
        return (input != null && !input.isBlank());
    }

    public static boolean isNonBlank(Short input) {
        return (input != null);
    }
}
