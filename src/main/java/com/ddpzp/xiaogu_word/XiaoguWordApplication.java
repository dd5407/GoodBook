package com.ddpzp.xiaogu_word;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan(basePackages = "com.ddpzp.xiaogu_word.filter")
public class XiaoguWordApplication {
	public static void main(String[] args) {
		SpringApplication.run(XiaoguWordApplication.class, args);
	}
}
