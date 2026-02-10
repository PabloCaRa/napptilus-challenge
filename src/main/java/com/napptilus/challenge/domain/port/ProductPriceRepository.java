package com.napptilus.challenge.domain.port;

import com.napptilus.challenge.domain.model.ProductPrice;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ProductPriceRepository {
    Flux<ProductPrice> getProductPriceByBrandIdAndProductIdAndApplicationDate(Integer brandId, Integer productId, LocalDateTime applicationDate);
}
