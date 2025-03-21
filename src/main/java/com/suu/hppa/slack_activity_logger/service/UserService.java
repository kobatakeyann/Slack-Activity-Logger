package com.suu.hppa.slack_activity_logger.service;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.suu.hppa.slack_activity_logger.api.ApiClient;
import com.suu.hppa.slack_activity_logger.api.TokenManager;
import com.suu.hppa.slack_activity_logger.model.User;
import com.suu.hppa.slack_activity_logger.util.PropertyGetter;

@Service
public class UserService {
    private final static String SLACK_API_TOKEN = TokenManager.getSlackApiToken();
    private final static List<JSONObject> ALL_USERS = fetchAllUsers();

    private static List<JSONObject> fetchAllUsers() {
        final String ENDPOINT = "https://slack.com/api/users.list";
        HttpResponse<String> response =
                ApiClient.fetchResponse(ENDPOINT, Optional.of(SLACK_API_TOKEN));
        JSONArray allUsersJsonArray = new JSONObject(response.body()).getJSONArray("members");
        List<JSONObject> allUsersList = IntStream.range(0, allUsersJsonArray.length())
                .mapToObj(allUsersJsonArray::getJSONObject).toList();
        return allUsersList;
    }

    private List<JSONObject> getActiveUsersJsonList() {
        return ALL_USERS.stream().filter(this::isActiveUser).toList();
    }

    private boolean isActiveUser(JSONObject user) {
        final long THRESHOLD_IN_UNIX_TIME =
                Long.parseLong(PropertyGetter.getProperty("active.user.threshold.in.unix.time"));
        return user.has("real_name") && user.getLong("updated") > THRESHOLD_IN_UNIX_TIME
                && !user.getBoolean("is_bot");
    }

    public List<User> getActiveUsersList() {
        List<JSONObject> activeUsersJsonList = this.getActiveUsersJsonList();
        return activeUsersJsonList.stream().map(
                userJson -> new User(userJson.getString("id"), userJson.getString("real_name")))
                .toList();
    }

    public void setPresenceForUsers(List<User> users) {
        for (User user : users) {
            HttpResponse<String> response = ApiClient.fetchResponse(
                    "https://slack.com/api/users.getPresence?user=" + user.getId(),
                    Optional.of(SLACK_API_TOKEN));
            String status = new JSONObject(response.body()).getString("presence");
            if (status.equals("active")) {
                user.setIsPresent(true);
            } else {
                user.setIsPresent(false);
            }
        }
    }
}
