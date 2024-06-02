package com.inkronsane.ReadArticlesServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ReadArticlesServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadArticlesServerApplication.class, args);
	}

}
