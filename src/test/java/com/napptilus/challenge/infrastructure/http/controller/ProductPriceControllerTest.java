package com.napptilus.challenge.infrastructure.http.controller;

import com.napptilus.challenge.application.GetProductPriceHandler;
import com.napptilus.challenge.domain.model.Brand;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProductPriceResponse;
import com.napptilus.challenge.infrastructure.http.controller.mapper.ProductPriceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductPriceControllerTest {

    @Mock
    private GetProductPriceHandler getProductPriceHandler;

    @Mock
    private ProductPriceMapper productPriceMapper;

    private ProductPriceController productPriceController;

    @BeforeEach
    void setup() {
        productPriceController = new ProductPriceController(getProductPriceHandler, productPriceMapper);
    }

    @Test
    @DisplayName("""
            Given a product id
            And a date
            And a brand id
            And the handler returns a product price
            When getProductPrice
            Then the product price is returned
            """)
    void case1() {
        Integer productId = 1;
        LocalDateTime date = LocalDateTime.now();
        Short brandId = Short.valueOf("1");

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

        GetProductPriceResponse expectedGetProductPriceResponse = GetProductPriceResponse.builder()
                .productId(productPrice.productId())
                .brandId(productPrice.brand().brandId())
                .priceList(productPrice.priceList())
                .startDate(productPrice.startDate())
                .endDate(productPrice.endDate())
                .price(productPrice.price())
                .build();

        given(getProductPriceHandler.getProductPrice(productId, date, brandId)).willReturn(productPrice);
        given(productPriceMapper.toResponse(productPrice)).willReturn(expectedGetProductPriceResponse);

        GetProductPriceResponse getProductPriceResponse = productPriceController.getProductPrice(productId, date, brandId);

        then(getProductPriceHandler).should(times(1)).getProductPrice(productId, date, brandId);
        then(productPriceMapper).should(times(1)).toResponse(productPrice);

        assertEquals(expectedGetProductPriceResponse, getProductPriceResponse);
    }

    @Test
    @DisplayName("""
            Given a product id
            And a date
            And a brand id
            And the handler throws an exception
            When getProductPrice
            Then a ResponseStatusException is thrown
            """)
    void case2() {
        Integer productId = 1;
        LocalDateTime date = LocalDateTime.now();
        Short brandId = Short.valueOf("1");

        given(getProductPriceHandler.getProductPrice(productId, date, brandId)).willThrow(NoSuchElementException.class);

        assertThrows(ResponseStatusException.class, () -> productPriceController.getProductPrice(productId, date, brandId));

        then(getProductPriceHandler).should(times(1)).getProductPrice(productId, date, brandId);
        then(productPriceMapper).shouldHaveNoInteractions();
    }
}