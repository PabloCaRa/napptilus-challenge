package com.napptilus.challenge.infrastructure.http.controller.mapper;

import com.napptilus.challenge.MotherTestBuilder;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProducePriceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductPriceMapperTest {

    private final ProductPriceMapper productPriceMapper = new ProductPriceMapperImpl();

    @Test
    @DisplayName("""
            Given a valid product price
            When map toGetProducePriceResponse
            Then it returns the expected value
            """)
    void case1() {
        LocalDateTime now = LocalDateTime.now();
        ProductPrice productPrice = MotherTestBuilder.productPrice(now);

        GetProducePriceResponse getProducePriceResponse = productPriceMapper.toGetProducePriceResponse(productPrice);

        GetProducePriceResponse expected = MotherTestBuilder.productPriceResponse(now);
        assertEquals(expected, getProducePriceResponse);
    }

    @Test
    @DisplayName("""
            Given a null product price
            When map toGetProducePriceResponse
            Then it returns the expected value
            """)
    void case2() {
        GetProducePriceResponse getProducePriceResponse = productPriceMapper.toGetProducePriceResponse(null);

        assertNull(getProducePriceResponse);
    }
}
