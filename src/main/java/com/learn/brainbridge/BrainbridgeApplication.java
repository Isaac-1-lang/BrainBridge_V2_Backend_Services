package com.learn.brainbridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BrainbridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrainbridgeApplication.class, args);
	}

}
