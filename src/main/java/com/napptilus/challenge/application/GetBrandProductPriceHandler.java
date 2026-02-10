package com.napptilus.challenge.application;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class GetBrandProductPriceHandler {

    private final ProductPriceService productPriceService;

    public Mono<ProductPrice> handle(Integer brandId, Integer productId, LocalDateTime applicationDate) {
        return productPriceService.getBrandProductPriceInDate(brandId, productId, applicationDate);
    }
}
