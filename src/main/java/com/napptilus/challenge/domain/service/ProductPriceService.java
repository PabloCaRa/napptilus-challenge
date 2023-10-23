package com.napptilus.challenge.domain.service;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.port.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    public ProductPrice getProductPrice(Integer productId, LocalDateTime date, Short brandId) {
        return productPriceRepository.findProductPrice(productId, date, brandId).stream()
                .max(Comparator.comparing(ProductPrice::priority))
                .orElseThrow();
    }

}
