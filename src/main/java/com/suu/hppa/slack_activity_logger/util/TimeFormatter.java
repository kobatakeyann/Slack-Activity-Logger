package com.suu.hppa.slack_activity_logger.util;

public class TimeFormatter {
    public static String MinToStringTime(int minutes) {
        int hour = minutes / 60;
        int minute = minutes % 60;
        if (hour == 0) {
            return String.format("%02d分", minute);
        }
        return String.format("%d時間%02d分", hour, minute);
    }
}
