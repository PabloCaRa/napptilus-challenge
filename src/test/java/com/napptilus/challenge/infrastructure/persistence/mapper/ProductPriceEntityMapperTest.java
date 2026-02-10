package com.napptilus.challenge.infrastructure.persistence.mapper;

import com.napptilus.challenge.MotherTestBuilder;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.persistence.entity.ProductPriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductPriceEntityMapperTest {

    private final ProductPriceEntityMapper productPriceEntityMapper = new ProductPriceEntityMapperImpl();

    @Test
    @DisplayName("""
            Given a valid product price entity
            When map toProductPrice
            Then it returns the expected value
            """)
    void case1() {
        LocalDateTime now = LocalDateTime.now();
        ProductPriceEntity productPriceEntity = MotherTestBuilder.productPriceEntity(now);
        ProductPrice productPrice = productPriceEntityMapper.toProductPrice(productPriceEntity);

        ProductPrice expected = MotherTestBuilder.productPrice(now);
        assertEquals(expected, productPrice);
    }

    @Test
    @DisplayName("""
            Given a null product price entity
            When map toProductPrice
            Then it returns the expected value
            """)
    void case2() {
        ProductPrice productPrice = productPriceEntityMapper.toProductPrice(null);

        assertNull(productPrice);
    }
}
