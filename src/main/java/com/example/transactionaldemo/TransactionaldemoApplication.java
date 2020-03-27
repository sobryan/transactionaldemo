package com.example.transactionaldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TransactionaldemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionaldemoApplication.class, args);
	}

}
