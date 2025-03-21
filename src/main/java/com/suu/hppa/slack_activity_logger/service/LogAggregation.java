package com.suu.hppa.slack_activity_logger.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.suu.hppa.slack_activity_logger.util.TimeFormatter;

public class LogAggregation {
    private static final Path ACTIVITY_LOG_PATH =
            Path.of("src/main/resources/data/daily_activity_log.csv");

    public Map<String, Integer> aggregateDailyActiveMinutes() {
        Map<String, Integer> activeMinutes = new HashMap<>();
        try (var reader = Files.newBufferedReader(ACTIVITY_LOG_PATH)) {
            String line;
            List<String> header = List.of(reader.readLine().split(","));
            int nameIndex = header.indexOf("name");
            int presenceIndex = header.indexOf("present");
            reader.readLine(); // skip header line
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String name = values[nameIndex];
                boolean isPresent = Boolean.parseBoolean(values[presenceIndex]);
                if (isPresent) {
                    activeMinutes.put(name, activeMinutes.getOrDefault(name, 0) + 10);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading csv log file: " + e.getMessage());
        }
        return activeMinutes;
    }

    public Map<String, String> formatActiveMinutes(Map<String, Integer> activeMinutes) {
        return activeMinutes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> TimeFormatter.MinToStringTime(entry.getValue()),
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
