package com.pl.df.service.product;

import com.pl.df.lib.Product;
import com.pl.df.lib.ProductDetails;
import com.pl.df.service.product.details.ProductDetailsComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailsComponent productDetailsComponent;

    public Mono<List<Product>> getAll() {
        var productDetailsFlux = productDetailsComponent.requestProductDetails();
        var productsFlux = productRepository.getAll();

        return Mono.zip(productsFlux.collectList(), productDetailsFlux.collectList())
                .map(tuple2 -> {
                    List<Product> products = tuple2.getT1();
                    Map<Long, ProductDetails> productDetails = tuple2.getT2()
                            .stream()
                            .collect(Collectors.toConcurrentMap(key -> key.getProductId(), value -> value));

                    return products.stream()
                            .map(product -> {
                                ProductDetails productDetail = productDetails.getOrDefault(product.getId(), null);
                                product.setProductDetails(productDetail);
                                return product;
                            })
                            .collect(Collectors.toList());
                });
    }

    public Mono<Product> getById(Long id) {
        var productDetailsMono = productDetailsComponent.requestProductDetails(id);
        var productMono = productRepository.getById(id);

        Mono<Product> product = Mono.zip(productMono, productDetailsMono)
                .filter(tuple2 -> Objects.nonNull(tuple2.getT1()) && Objects.nonNull(tuple2.getT2()))
                .map(tuple2 -> {
                    Product currentProduct = tuple2.getT1();
                    ProductDetails productDetails = tuple2.getT2();
                    currentProduct.setProductDetails(productDetails);
                    return currentProduct;
                });

        return Mono.just(id)
                .map(theId -> {
                    var productSize = this.productRepository.getProductSize();
                    if (theId > productSize)
                        throw new IllegalArgumentException("Product with " + id + " id must be in range of: productId=<1, " + productSize + ">!");
                    return theId;
                })
                .then(product);
    }

}
