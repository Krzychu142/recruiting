package com.krzysiek.recruiting;

import com.krzysiek.recruiting.dto.FileDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Autowired
	private EmailService emailService;
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.info("Application started successfully!");
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			emailService.sendSimpleMessage("kustosz142@gmail.com", "Test message", "text");
//			FileDTO  fileDto = new FileDTO(1L, 2L, FileType.CV, "link");
//			System.out.println(fileDto);
		};
	}
}