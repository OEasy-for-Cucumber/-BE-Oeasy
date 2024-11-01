package com.OEzoa.OEasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableScheduling //스케줄러 활성화
public class OEasyApplication {

	public static void main(String[] args) {
		SpringApplication.run(OEasyApplication.class, args);
	}

}
