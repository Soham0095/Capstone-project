package com.example;

import com.example.controller.TransactionLogController;
import com.example.service.AccountServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;

@SpringBootApplication
public class CapstoneProjectApplication {

	public static void main(String[] args) {

//		ConfigurableApplicationContext applicationContext = null;
//		SpringApplication app = new SpringApplication(CapstoneProjectApplication.class);
//		app.run(args);
//
//		TransactionLogController transactionLogController = new TransactionLogController();
//		transactionLogController.createTransactionLog();
		ApplicationContext context = SpringApplication.run(CapstoneProjectApplication.class, args);
//		TransactionLogController transactionLogController = context.getBean(TransactionLogController.class);
//		transactionLogController.createTransactionLog();
	}

}
