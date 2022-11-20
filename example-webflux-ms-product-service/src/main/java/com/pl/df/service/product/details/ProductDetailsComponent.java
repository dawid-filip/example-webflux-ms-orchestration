package com.pl.df.service.product.details;
import com.pl.df.lib.ProductDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Log4j2
public class ProductDetailsComponent {

    private static final String PRODUCT_DETAILS_ROOT_PATH = "/product-details";
    private final WebClient webClient;

    public Flux<ProductDetails> requestProductDetails() {
        var productDetails = webClient.get()
                .uri(PRODUCT_DETAILS_ROOT_PATH)
                .retrieve()
                .bodyToFlux(ProductDetails.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)));
        return productDetails;
    }

    public Mono<ProductDetails> requestProductDetails(Long id) {
        var productDetails = webClient.get()
                .uri(PRODUCT_DETAILS_ROOT_PATH + "/{id}", id)
                .retrieve()
                .bodyToMono(ProductDetails.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)));
        return productDetails;
    }

}
