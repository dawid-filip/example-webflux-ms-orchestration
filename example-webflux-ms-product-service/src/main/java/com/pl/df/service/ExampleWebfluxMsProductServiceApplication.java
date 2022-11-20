package com.pl.df.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@Log4j2
public class ExampleWebfluxMsProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleWebfluxMsProductServiceApplication.class, args);
	}

}
