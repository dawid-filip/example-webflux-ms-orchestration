package com.pl.df.lib;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetails {

    private Long id;
    private Long productId;
    private int yearsOfWarranty;
    private double price;
    private String description;

}
