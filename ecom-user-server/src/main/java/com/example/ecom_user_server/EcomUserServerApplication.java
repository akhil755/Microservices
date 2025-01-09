package com.example.ecom_user_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableJpaRepositories
public class EcomUserServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EcomUserServerApplication.class, args);
	}

}
