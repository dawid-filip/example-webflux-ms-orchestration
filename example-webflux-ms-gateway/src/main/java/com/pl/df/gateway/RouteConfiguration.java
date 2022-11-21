package com.pl.df.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.PrincipalNameKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 100);  // 1-100 requests per second
    }

    @Bean
    public PrincipalNameKeyResolver principalNameKeyResolver() {
        return new PrincipalNameKeyResolver();
    }

    @Bean
    public RouteLocator gateway(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route(routeSpec -> routeSpec
                                .path("/logout", "/login")                           // http://localhost:8080/public
//                        .filters(fs ->
//                                fs.requestRateLimiter(rl -> rl.setRateLimiter(redisRateLimiter())
//                                        .setKeyResolver(principalNameKeyResolver()))
//                        )
                                .uri("lb://gateway/")
                )
                .route(routeSpec -> routeSpec
                                .path("/public")                           // http://localhost:8080/public
//                        .filters(fs ->
//                                fs.requestRateLimiter(rl -> rl.setRateLimiter(redisRateLimiter())
//                                        .setKeyResolver(principalNameKeyResolver()))
//                        )
                                .uri("lb://product-service/")
                )
                .route(routeSpec -> routeSpec
                        .path("/product", "/product/*")                           // http://localhost:8080/product
//                        .filters(fs ->
//                                fs.requestRateLimiter(rl -> rl.setRateLimiter(redisRateLimiter())
//                                        .setKeyResolver(principalNameKeyResolver()))
//                        )
                        .uri("lb://product-service/")
                )
                .route(routeSpec -> routeSpec
                        .path("/product-details", "/product-details/*")           // http://localhost:8080/product-details
//                        .filters(fs ->
//                                fs.requestRateLimiter(rl -> rl.setRateLimiter(redisRateLimiter())
//                                        .setKeyResolver(principalNameKeyResolver()))
//                        )
                        .uri("lb://product-details-service/")
                )
                .build();
    }

//    OR -> following application.properties can be set instead of RouteConfiguration beans definition:
//      spring.cloud.gateway.discovery.locator.enabled=true
//      spring.cloud.gateway.discovery.locator.lower-case-service-id=true

}
