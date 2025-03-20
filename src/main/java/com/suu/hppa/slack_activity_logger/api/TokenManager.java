package com.suu.hppa.slack_activity_logger.api;

import com.suu.hppa.slack_activity_logger.util.PropertyGetter;

public class TokenManager {
    public static String getSlackApiToken() {
        return PropertyGetter.getProperty("slack.api.token");
    }
}
