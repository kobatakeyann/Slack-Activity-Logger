package com.suu.hppa.slack_activity_logger.api;

import java.io.IOException;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

public class SlackApiClient {
    private final static String SLACK_API_TOKEN = TokenManager.getSlackApiToken();

    public static void sendMessage(String channelId, String message) {
        Slack slack = Slack.getInstance();
        try {
            ChatPostMessageResponse response = slack.methods(SLACK_API_TOKEN)
                    .chatPostMessage(req -> req.channel(channelId).text(message));
            if (!response.isOk()) {
                throw new RuntimeException(response.getError());
            }
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(
                    "Error while sending message to Slack channel: " + e.getMessage());
        }
    }
}
