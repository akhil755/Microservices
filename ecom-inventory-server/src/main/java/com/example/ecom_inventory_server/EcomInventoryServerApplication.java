package com.example.ecom_inventory_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EcomInventoryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomInventoryServerApplication.class, args);
	}

}
