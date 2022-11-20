package com.pl.df.service;

import com.pl.df.lib.HostPortLogWebFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@RequiredArgsConstructor
public class ProductDetailsRouteConfiguration {

    private final ProductDetailsHandler productDetailsHandler;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(GET("/product-details"), productDetailsHandler::getAll)
                .andRoute(GET("/product-details/{id}"), productDetailsHandler::getById)
                .filter(new HostPortLogWebFilter());
    }

}
