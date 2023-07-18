package com.point.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.point"})
public class PointApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PointApiApplication.class, args);
	}

}
