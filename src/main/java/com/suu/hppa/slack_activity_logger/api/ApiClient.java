package com.suu.hppa.slack_activity_logger.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class ApiClient {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static HttpResponse<String> fetchResponse(String url, Optional<String> apiToken) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL syntax", e);
        }
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).GET();
        apiToken.ifPresent(token -> requestBuilder.header("Authorization", "Bearer " + token));
        HttpRequest request = requestBuilder.build();
        try {
            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("IOException occured while GET request: %s ", url)
                            + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException("Request was interrupted: " + e.getMessage());
        }
    }
}
