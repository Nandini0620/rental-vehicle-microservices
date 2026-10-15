package com.rental_vehicle.driver_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DriverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverServiceApplication.class, args);
	}

}
