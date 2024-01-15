package com.anik.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
@Slf4j
public class SpringWebTemplateApplication {

	public static void main(String[] args) {
		log.info("Welcome {}", LocalDate.now());
		SpringApplication.run(SpringWebTemplateApplication.class, args);
	}

}
