package com.napptilus.challenge.domain.service;

import com.napptilus.challenge.domain.exception.ProductPriceNotFoundException;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.port.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;

@RequiredArgsConstructor
@Service
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    public Mono<ProductPrice> getBrandProductPriceInDate(Integer brandId, Integer productId, LocalDateTime applicationDate) {
        return Flux.defer(() -> productPriceRepository.getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate))
                .sort(Comparator.comparing(ProductPrice::priority).reversed())
                .next()
                .switchIfEmpty(Mono.error(new ProductPriceNotFoundException(brandId, productId, applicationDate)));
    }
}
