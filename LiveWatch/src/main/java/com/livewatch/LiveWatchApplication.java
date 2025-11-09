package com.livewatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveWatchApplication {
	private static final Logger logger = LoggerFactory.getLogger(LiveWatchApplication.class);
	public static void main(String[] args) {
		logger.info("Project Start.....");
		SpringApplication.run(LiveWatchApplication.class, args);
	}

}
