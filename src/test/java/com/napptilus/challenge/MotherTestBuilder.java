package com.napptilus.challenge;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProducePriceResponse;
import com.napptilus.challenge.infrastructure.persistence.entity.ProductPriceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MotherTestBuilder {

    private static final Integer BRAND_ID = 1;
    private static final Integer PRODUCT_ID = 35455;
    private static final Integer PRICE_LIST = 1;
    private static final String CURRENCY = "EUR";

    private MotherTestBuilder() {
    }

    public static ProductPrice productPrice(LocalDateTime now) {
        return productPrice(now.minusDays(1), now.plusDays(1), PRICE_LIST, 1, BigDecimal.TEN);
    }

    public static ProductPrice productPrice(LocalDateTime startDate, LocalDateTime endDate, Integer priceList, Integer priority, BigDecimal price) {
        return ProductPrice.builder()
                .brandId(BRAND_ID)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(priceList)
                .productId(PRODUCT_ID)
                .priority(priority)
                .price(price)
                .currency(CURRENCY)
                .build();
    }

    public static GetProducePriceResponse productPriceResponse(LocalDateTime now) {
        return GetProducePriceResponse.builder()
                .productId(PRODUCT_ID)
                .brandId(BRAND_ID)
                .priceList(PRICE_LIST)
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .price(BigDecimal.TEN)
                .currency(CURRENCY)
                .build();
    }

    public static ProductPriceEntity productPriceEntity(LocalDateTime now) {
        return productPriceEntity(1, now.minusDays(1), now.plusDays(1), PRICE_LIST, 1, BigDecimal.TEN);
    }

    public static ProductPriceEntity productPriceEntity(Integer id, LocalDateTime startDate, LocalDateTime endDate, Integer priceList, Integer priority, BigDecimal price) {
        return ProductPriceEntity.builder()
                .id(1)
                .brandId(BRAND_ID)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(priceList)
                .productId(PRODUCT_ID)
                .priority(priority)
                .price(price)
                .currency(CURRENCY)
                .build();
    }
}
