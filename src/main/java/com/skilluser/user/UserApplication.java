package com.skilluser.user;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync

@SpringBootApplication
@EnableFeignClients(basePackages = "com.skilluser.user.fiegnclient")
public class UserApplication {

	public static void main(String[] args) {


		/*Dotenv dotenv = Dotenv.load(); // loads .env file automatically

		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));*/


		SpringApplication.run(UserApplication.class, args);

	}


}
