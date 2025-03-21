package com.suu.hppa.slack_activity_logger.service;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.suu.hppa.slack_activity_logger.api.SlackApiClient;
import com.suu.hppa.slack_activity_logger.service.log.ActivityStatusLogger;
import com.suu.hppa.slack_activity_logger.service.log.Aggregator;
import com.suu.hppa.slack_activity_logger.util.PropertyGetter;

@Service
public class ReportingService {
    private final Aggregator aggregator;
    private final static String CHANNEL_ID = PropertyGetter.getProperty("slack.channel.id");

    public ReportingService(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    public void sendDailyReport() {
        Map<String, Integer> activeMinutes = aggregator.aggregateDailyActiveMinutes();
        LinkedHashMap<String, String> formattedActiveMinutes =
                aggregator.formatActiveMinutes(activeMinutes);
        String reportMessage = aggregator.buildReportMessage(formattedActiveMinutes);
        SlackApiClient.sendMessage(CHANNEL_ID, reportMessage);
        ActivityStatusLogger.clearLog();
    }
}
