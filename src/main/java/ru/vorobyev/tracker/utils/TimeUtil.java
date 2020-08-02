package ru.vorobyev.tracker.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
