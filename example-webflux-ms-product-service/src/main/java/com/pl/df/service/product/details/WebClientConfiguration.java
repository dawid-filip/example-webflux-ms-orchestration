package com.pl.df.service.product.details;

import com.pl.df.service.product.details.HedgingPattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.*;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class WebClientConfiguration {

    @Value("${application.service.name.product-details}")
    private String productDetailsServiceName;

    @Bean
    @Profile("heading-pattern")
    public HedgingPattern hedgingPattern(ReactiveDiscoveryClient reactiveDiscoveryClient,
                                         @Value("${application.service.name.product-details.max-instances}") Long maxInstances) {
        return new HedgingPattern(reactiveDiscoveryClient, maxInstances);
    }

    @Bean
    @Profile("heading-pattern")
    public WebClient webClientHeading(WebClient.Builder builder, HedgingPattern hedgingPattern) {
        return builder
                .filter(hedgingPattern)
                .baseUrl(productDetailsServiceName)
                .defaultHeader("X-internal-product-service-call", "true")
                .build();
    }

    @Bean
    @Profile("default")
    public WebClient webClientDefault(WebClient.Builder builder, ReactorLoadBalancerExchangeFilterFunction loadBalancer) {
        return builder
                .filter(loadBalancer)                       // it will look into Eureka registry server for available instances of:
                .baseUrl(productDetailsServiceName)         // the product-details-service (alternatively can use: "http://localhost:8092"
                .defaultHeader("X-internal-product-service-call", "true")
                .build();
    }

}
