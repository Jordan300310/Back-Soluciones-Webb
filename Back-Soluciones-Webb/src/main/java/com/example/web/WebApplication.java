package com.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class WebApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(WebApplication.class, args);
		var encoder = new BCryptPasswordEncoder();
    String hash = encoder.encode("admin123");
    System.out.println(hash);
  }
	}
