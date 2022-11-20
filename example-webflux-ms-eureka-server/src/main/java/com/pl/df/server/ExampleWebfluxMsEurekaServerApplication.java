package com.pl.df.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ExampleWebfluxMsEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExampleWebfluxMsEurekaServerApplication.class, args);
	}

}
