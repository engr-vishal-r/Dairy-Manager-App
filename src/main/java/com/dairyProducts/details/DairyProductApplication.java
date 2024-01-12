package com.dairyProducts.details;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.dairyProducts.details")
public class DairyProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(DairyProductApplication.class, args);
	}

}
