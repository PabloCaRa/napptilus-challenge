package com.napptilus.challenge.infrastructure.persistence.mapper;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.persistence.jpa.entity.BrandEntity;
import com.napptilus.challenge.infrastructure.persistence.jpa.entity.ProductPriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductPriceEntityMapperTest {

    private final ProductPriceEntityMapper productPriceEntityMapper = new ProductPriceEntityMapperImpl();

    @Test
    @DisplayName("""
            Given a list of ProductPriceEntities
            When map toDomain
            Then a list of ProductPrices is returned
            """)
    void case1() {
        ProductPriceEntity productPriceEntity = ProductPriceEntity.builder()
                .priceId(1)
                .brand(BrandEntity.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Brand name")
                        .build())
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(Short.valueOf("1"))
                .productId(1)
                .priority(Short.valueOf("1"))
                .price(BigDecimal.TEN)
                .currency("EUR")
                .build();

        List<ProductPrice> productPrices = productPriceEntityMapper.toDomain(List.of(productPriceEntity));

        assertEquals(productPriceEntity.getBrand().getBrandId(), productPrices.getFirst().brand().brandId());
        assertEquals(productPriceEntity.getBrand().getName(), productPrices.getFirst().brand().name());
        assertEquals(productPriceEntity.getStartDate(), productPrices.getFirst().startDate());
        assertEquals(productPriceEntity.getEndDate(), productPrices.getFirst().endDate());
        assertEquals(productPriceEntity.getPriceList(), productPrices.getFirst().priceList());
        assertEquals(productPriceEntity.getProductId(), productPrices.getFirst().productId());
        assertEquals(productPriceEntity.getPriority(), productPrices.getFirst().priority());
        assertEquals(productPriceEntity.getPrice(), productPrices.getFirst().price());
        assertEquals(productPriceEntity.getCurrency(), productPrices.getFirst().currency());
    }

}