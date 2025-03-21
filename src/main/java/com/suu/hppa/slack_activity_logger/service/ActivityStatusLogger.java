package com.suu.hppa.slack_activity_logger.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.suu.hppa.slack_activity_logger.model.User;

public class ActivityStatusLogger {
    private static final Path ACTIVITY_LOG_PATH =
            Path.of("src/main/resources/data/daily_activity_log.csv");

    public void writeUserStatus(List<User> users) {
        boolean isExist = Files.exists(ACTIVITY_LOG_PATH);
        try (BufferedWriter writer = Files.newBufferedWriter(ACTIVITY_LOG_PATH,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (!isExist) {
                String header = "timestamp,id,name,present\n";
                writer.append(header);
            }
            var timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String timestamp = LocalDateTime.now().format(timeFormat);
            for (User user : users) {
                String line = String.format("%s,%s,%s,%b\n", timestamp, user.getId(),
                        user.getName(), user.isPresent());
                writer.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "IOException occured while writing online status: " + e.getMessage());
        }
    }

    public void clearLog() {
        try {
            Files.deleteIfExists(ACTIVITY_LOG_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting log file: " + e.getMessage());
        }
    }
}
