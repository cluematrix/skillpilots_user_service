package com.skilluser.user;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {


//		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load(); // loads .env into System env
//
//		System.out.println("===== Loaded .env entries =====");
//		dotenv.entries().forEach(e -> {
//			System.out.println(e.getKey() + " = " + e.getValue());
//		});
//		System.out.println("================================");
//
//		// Inject into JVM system properties
//		dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));


		SpringApplication.run(UserApplication.class, args);

	}


}
