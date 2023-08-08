package com.ddpzp.goodbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan(basePackages = "com.ddpzp.goodbook.filter")
public class GoodbookApplication {
	public static void main(String[] args) {
		SpringApplication.run(GoodbookApplication.class, args);
	}
}
