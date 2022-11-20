package com.pl.df.service.product;

import com.pl.df.lib.HostPortLogWebFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ProductRouteConfiguration {

    private final ProductHandler productHandler;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(GET("/public"), productHandler::getPublic)
                .andRoute(GET("/product"), productHandler::getAll)
                .andRoute(GET("/product/{id}"), productHandler::getById)
                .filter(new HostPortLogWebFilter());
    }

}