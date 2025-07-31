//package com.security.spring.utils;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//
//public class DateTimeGeneratorGameSoft {
////    Get Current Data Time With UTC
//    public static String getCurrentDateTimeInUTC() {
//
//        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//
//        return nowUtc.format(formatter);
//    }
//
////    Convert String To LocalDateTime Format Method
//    public static LocalDateTime convertStringToLocalDateTime(String dateTimeStr) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return LocalDateTime.parse(dateTimeStr, formatter);
//    }
//
//    //    Convert LocalDateTime Format To UTC Date Time Method
//    public static ZonedDateTime convertToUTC(LocalDateTime localDateTime) {
//        return localDateTime.atZone(ZoneId.systemDefault())
//                .withZoneSameInstant(ZoneId.of("UTC"));
//    }
//
//    //    Convert String Format To UTC Date Time Method
//    public static ZonedDateTime convertStringToUTC(String dateTimeStr) {
//        LocalDateTime localDateTime = convertStringToLocalDateTime(dateTimeStr);
//        return convertToUTC(localDateTime);
//    }
//
//    //Date Value + Hours method
//    public static ZonedDateTime addHours(ZonedDateTime dateTime, long hours) {
//        return dateTime.plusHours(hours);
//    }
//
//    //Date Value + Minute method
//    public static ZonedDateTime addMinute(ZonedDateTime dateTime, long minute) {
//        return dateTime.plusMinutes(minute);
//    }
//}
