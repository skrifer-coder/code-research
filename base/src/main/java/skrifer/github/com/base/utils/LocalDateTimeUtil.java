package skrifer.github.com.base.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    public static String format(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDateTime max(LocalDateTime localDateTime1, LocalDateTime localDateTime2){
        return localDateTime1.isBefore(localDateTime2) ? localDateTime2 : localDateTime1;
    }

    public static LocalDateTime min(LocalDateTime localDateTime1, LocalDateTime localDateTime2){
        return localDateTime1.isAfter(localDateTime2) ? localDateTime2 : localDateTime1;
    }
}
