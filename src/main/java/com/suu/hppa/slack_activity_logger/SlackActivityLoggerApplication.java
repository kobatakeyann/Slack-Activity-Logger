package com.suu.hppa.slack_activity_logger;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SlackActivityLoggerApplication implements ApplicationRunner {
	public static void main(String[] args) {
		SpringApplication.run(SlackActivityLoggerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		System.out.println("Application start!");
	}
}
