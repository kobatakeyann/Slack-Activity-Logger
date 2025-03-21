package com.suu.hppa.slack_activity_logger;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.suu.hppa.slack_activity_logger.service.ReportingService;
import com.suu.hppa.slack_activity_logger.service.log.LoggingService;
import com.suu.hppa.slack_activity_logger.util.TimeChecker;

@SpringBootApplication
public class SlackActivityLoggerApplication implements ApplicationRunner {
	private final LoggingService logger;
	private final ReportingService reporter;

	public SlackActivityLoggerApplication(LoggingService logger, ReportingService reporter) {
		this.logger = logger;
		this.reporter = reporter;
	}

	public static void main(String[] args) {
		SpringApplication.run(SlackActivityLoggerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		if (TimeChecker.hasDayChenged()) {
			reporter.sendDailyReport();
		}
		logger.logUserStatus();
	}
}
