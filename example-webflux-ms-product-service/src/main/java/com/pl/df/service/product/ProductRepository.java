package com.pl.df.service.product;

import com.pl.df.lib.Product;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Repository
public class ProductRepository {

    private static Set<Product> products = new HashSet<>();

    static {
        products.add(new Product(1L, "Electronics", "Laptop", "ASUS TUF Gaming F15", null));
        products.add(new Product(2L, "Electronics", "Laptop", "ASUS TUF Gaming A15", null));
        products.add(new Product(3L, "Electronics", "Laptop", "ASUS ROG Strix G15", null));
        products.add(new Product(4L, "Electronics", "Laptop", "ASUS TUF Dash F15", null));
        products.add(new Product(5L, "Electronics", "Laptop", "MSI GF66", null));
        products.add(new Product(6L, "Electronics", "Laptop", "MSI GF63", null));
        products.add(new Product(7L, "Electronics", "Laptop", "MSI GF76", null));
        products.add(new Product(8L, "Electronics", "GPU", "Gigabyte GeForce RTX 3060 EAGLE OC LHR 12GB", null));
        products.add(new Product(9L, "Electronics", "GPU", "Gigabyte GeForce RTX 3090 Ti GAMING OC 24GB", null));
        products.add(new Product(10L, "Electronics", "GPU", "Zotac GeForce RTX 3060 Twin Edge 12GB", null));
    }

    public final int getProductSize() {
        return products.size();
    }

    public Flux<Product> getAll() {
        return Flux.fromIterable(products);
    }

    public Mono<Product> getById(Long id) {
        return getAll()
                .filter(product -> product.getId()==id)
                .next();
    }

}
