package com.appsdeveloperblog.ws.emailnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableKafka
public class EmailNotificationMicroserviceApplication {

	public static void main(String[] args) {

		SpringApplication.run(EmailNotificationMicroserviceApplication.class, args);
	}

	// To send the Http request to external microservice
	@Bean
	RestTemplate getRestTemplate(){

		return new RestTemplate();
	}

}
