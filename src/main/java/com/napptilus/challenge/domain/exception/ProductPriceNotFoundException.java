package com.napptilus.challenge.domain.exception;

import java.time.LocalDateTime;

public class ProductPriceNotFoundException extends RuntimeException {

    public ProductPriceNotFoundException(Integer brandId, Integer productId, LocalDateTime applicationDate) {
        super("There isn't any saved price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate));
    }
}
