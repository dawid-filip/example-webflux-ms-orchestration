package com.pl.df.service;

import com.pl.df.lib.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ProductDetailsHandler {

    private final ProductDetailsRepository productDetailsRepository;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("In [/product] endpoint...");

        return ServerResponse.status(HttpStatus.OK)
                .body(this.productDetailsRepository.getAll(), List.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        log.info("In [/product/" + id + "] endpoint...");

        return ServerResponse.status(HttpStatus.OK).body(
                this.productDetailsRepository.getById(id)
                        .doOnError(log::error)
                        .switchIfEmpty(Mono.error(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product " + id + " not found!")))
                , Product.class);
    }


}
