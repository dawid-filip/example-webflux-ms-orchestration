package com.pl.df.lib;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private Long id;
    private String category;
    private String group;
    private String name;
    private ProductDetails productDetails;

}
