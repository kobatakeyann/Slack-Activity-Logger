package com.suu.hppa.slack_activity_logger.service.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.suu.hppa.slack_activity_logger.util.TimeFormatter;

@Component
public class Aggregator {
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

    public LinkedHashMap<String, String> formatActiveMinutes(Map<String, Integer> activeMinutes) {
        return activeMinutes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> TimeFormatter.MinToStringTime(entry.getValue()),
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public String buildReportMessage(LinkedHashMap<String, String> activeMinutes) {
        var builder = new StringBuilder();
        var dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.now().minusDays(1);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.JAPANESE);
        String dateHeaderLine = String.format("%s (%s) \n", date.format(dateFormat), dayOfWeek);
        builder.append(dateHeaderLine);
        int rank = 1;
        for (String name : activeMinutes.keySet()) {
            if (rank == 1) {
                builder.append(
                        String.format("\n👑   %-15s  :  %10s\n", name, activeMinutes.get(name)));
            } else {
                builder.append(String.format("%3s.   %-13s  :  %10s\n", rank, name,
                        activeMinutes.get(name)));
            }
            rank++;
        }
        builder.append("\n");
        return builder.toString();
    }
}
