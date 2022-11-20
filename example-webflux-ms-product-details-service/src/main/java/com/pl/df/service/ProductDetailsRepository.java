package com.pl.df.service;

import com.pl.df.lib.ProductDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Repository
public class ProductDetailsRepository {

    private static Set<ProductDetails> productDetails = new HashSet<>();

    static {
        productDetails.add(new ProductDetails(1L, 1L, 2, 6000.0, "description 1"));
        productDetails.add(new ProductDetails(2L, 2L, 2, 5000.0, "description 2"));
        productDetails.add(new ProductDetails(3L, 3L, 2, 4000.0, "description 3"));
        productDetails.add(new ProductDetails(4L, 4L, 2, 3000.0,"description 4"));
        productDetails.add(new ProductDetails(5L, 5L, 3, 3500.0,"description 5"));
        productDetails.add(new ProductDetails(6L, 6L, 3, 5600.0,"description 6"));
        productDetails.add(new ProductDetails(7L, 7L, 3, 7700.0,"description 7"));
        productDetails.add(new ProductDetails(8L, 8L, 4, 2990.0,"description 8"));
        productDetails.add(new ProductDetails(9L, 9L, 4, 11500.0,"description 9"));
        productDetails.add(new ProductDetails(10L, 10L, 6, 3500.0,"description 10"));
    }

    public final int getProductDetailsSize() {
        return productDetails.size();
    }

    public Flux<ProductDetails> getAll() {
        return Flux.fromIterable(productDetails);
    }

    public Mono<ProductDetails> getById(Long id) {
        return getAll()
                .filter(product -> product.getId()==id)
                .next();
    }

}
