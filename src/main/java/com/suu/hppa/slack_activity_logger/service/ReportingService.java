package com.suu.hppa.slack_activity_logger.service;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.suu.hppa.slack_activity_logger.api.SlackApiClient;
import com.suu.hppa.slack_activity_logger.service.log.ActivityStatusLogger;
import com.suu.hppa.slack_activity_logger.service.log.Aggregator;

@Service
public class ReportingService {
    @Value("${slack.channel.id}")
    private static String channelId;
    private final Aggregator aggregator;

    public ReportingService(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    public void sendDailyReport() {
        Map<String, Integer> activeMinutes = aggregator.aggregateDailyActiveMinutes();
        LinkedHashMap<String, String> formattedActiveMinutes =
                aggregator.formatActiveMinutes(activeMinutes);
        String reportMessage = aggregator.buildReportMessage(formattedActiveMinutes);
        SlackApiClient.sendMessage(channelId, reportMessage);
        ActivityStatusLogger.clearLog();
    }
}
