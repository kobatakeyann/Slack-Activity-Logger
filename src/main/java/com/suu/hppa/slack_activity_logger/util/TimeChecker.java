package com.suu.hppa.slack_activity_logger.util;

import java.time.LocalTime;

public class TimeChecker {
    public static boolean hasDayChenged() {
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(0, 10);
        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }
}
