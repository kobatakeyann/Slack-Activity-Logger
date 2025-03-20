package com.suu.hppa.slack_activity_logger.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyGetter {
    private static final String APP_PROPERTIES_PATH =
            PropertyGetter.class.getResource("/application.properties").getPath();

    public static String getProperty(String key) {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(APP_PROPERTIES_PATH)) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file: " + e.getMessage(), e);
        }
        String value = prop.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Property not found or is missing.");
        }
        return value;
    }
}
