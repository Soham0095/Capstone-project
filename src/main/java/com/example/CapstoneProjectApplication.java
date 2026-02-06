package com.example;

import com.example.service.AccountServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;

@SpringBootApplication
public class CapstoneProjectApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext applicationContext = null;
		SpringApplication app = new SpringApplication(CapstoneProjectApplication.class);
		//app.setDefaultProperties(Collections.singletonMap("server.port", "8090"));
		app.run(args);
	}

}
