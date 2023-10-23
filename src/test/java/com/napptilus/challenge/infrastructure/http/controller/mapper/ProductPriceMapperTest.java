package com.napptilus.challenge.infrastructure.http.controller.mapper;

import com.napptilus.challenge.domain.model.Brand;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProductPriceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductPriceMapperTest {

    private final ProductPriceMapper mapper = new ProductPriceMapperImpl();

    @Test
    @DisplayName("""
            Given a product price
            When map toResponse
            Then a GetProductPriceResponse is returned
            """)
    void case1() {
        ProductPrice productPrice = ProductPrice.builder()
                .brand(Brand.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Brand 1")
                        .build())
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(Short.valueOf("1"))
                .productId(1)
                .priority(Short.valueOf("1"))
                .price(BigDecimal.TEN)
                .currency("EUR")
                .build();

        GetProductPriceResponse getProductPriceResponse = mapper.toResponse(productPrice);

        assertEquals(productPrice.productId(), getProductPriceResponse.productId());
        assertEquals(productPrice.brand().brandId(), getProductPriceResponse.brandId());
        assertEquals(productPrice.priceList(), getProductPriceResponse.priceList());
        assertEquals(productPrice.startDate(), getProductPriceResponse.startDate());
        assertEquals(productPrice.endDate(), getProductPriceResponse.endDate());
        assertEquals(productPrice.price(), getProductPriceResponse.price());
    }
}