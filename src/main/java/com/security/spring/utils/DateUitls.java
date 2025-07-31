package com.security.spring.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateUitls {
    private DateUitls(){

    }
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr, formatter) : null;
    }

}
