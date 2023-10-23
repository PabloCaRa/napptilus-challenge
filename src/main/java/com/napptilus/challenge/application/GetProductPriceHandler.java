package com.napptilus.challenge.application;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GetProductPriceHandler {

    private final ProductPriceService productPriceService;

    public ProductPrice getProductPrice(Integer productId, LocalDateTime date, Short brandId) {
        return productPriceService.getProductPrice(productId, date, brandId);
    }
}
