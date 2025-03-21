package com.suu.hppa.slack_activity_logger.service.log;

import java.util.List;
import org.springframework.stereotype.Service;
import com.suu.hppa.slack_activity_logger.model.User;
import com.suu.hppa.slack_activity_logger.service.UserService;

@Service
public class LoggingService {
    private final UserService userService;

    public LoggingService(UserService userService) {
        this.userService = userService;
    }

    public void logUserStatus() {
        List<User> activeUsers = userService.getActiveUsersList();
        userService.setPresenceForUsers(activeUsers);
        ActivityStatusLogger.writeUserStatus(activeUsers);
    }
}
