package com.company.newseat.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter[] INPUT_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd")
    };

    private static final DateTimeFormatter OUTPUT_FORMATTERS =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private DateUtil() {
    }

    public static String formatDate(String rawDate) {

        if (rawDate == null || rawDate.isBlank()) {
            return "";
        }

        for (DateTimeFormatter inputFormatter : INPUT_FORMATTERS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(rawDate, inputFormatter);
                return dateTime.format(OUTPUT_FORMATTERS);
            } catch (Exception e1) {
                try {
                    LocalDate date = LocalDate.parse(rawDate, inputFormatter);
                    return date.format(OUTPUT_FORMATTERS);
                } catch (Exception e2) {
                }
            }
        }
        return rawDate;
    }
}
