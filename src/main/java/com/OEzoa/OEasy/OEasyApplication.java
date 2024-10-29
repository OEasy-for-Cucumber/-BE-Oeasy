package com.OEzoa.OEasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class OEasyApplication {

	public static void main(String[] args) {
		SpringApplication.run(OEasyApplication.class, args);
	}

}
