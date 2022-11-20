package com.pl.df.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ExampleWebfluxMsProductDetailsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleWebfluxMsProductDetailsServiceApplication.class, args);
	}

}
