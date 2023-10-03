package com.example.FAMS;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class FamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FamsApplication.class, args);
	}

}
