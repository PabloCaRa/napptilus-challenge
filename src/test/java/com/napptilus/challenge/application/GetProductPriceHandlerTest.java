package com.napptilus.challenge.application;

import com.napptilus.challenge.domain.model.Brand;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.service.ProductPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GetProductPriceHandlerTest {

    @Mock
    private ProductPriceService productPriceService;

    private GetProductPriceHandler getProductPriceHandler;

    @BeforeEach
    void setup() {
        getProductPriceHandler = new GetProductPriceHandler(productPriceService);
    }

    @Test
    @DisplayName("""
            Given a product id
            And a date
            And a brand id
            And service returns a product price
            When getProductPrice
            Then the produce price is returned
            """)
    void case1() {
        Integer productId = 1;
        LocalDateTime date = LocalDateTime.now();
        Short brandId = Short.valueOf("1");

        ProductPrice existingProductPrice = ProductPrice.builder()
                .brand(Brand.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Brand one")
                        .build())
                .startDate(LocalDateTime.now().minusHours(1L))
                .endDate(LocalDateTime.now().plusHours(1L))
                .priceList(Short.valueOf("1"))
                .productId(1)
                .priority(Short.valueOf("0"))
                .price(BigDecimal.TEN)
                .currency("EUR")
                .build();

        given(productPriceService.getProductPrice(productId, date, brandId)).willReturn(existingProductPrice);

        ProductPrice productPrice = getProductPriceHandler.getProductPrice(productId, date, brandId);

        then(productPriceService).should(times(1)).getProductPrice(productId, date, brandId);
        assertEquals(existingProductPrice, productPrice);
    }

    @Test
    @DisplayName("""
            Given a product id
            And a date
            And a brand id
            And service throws an exception
            When getProductPrice
            Then an exception is thrown
            """)
    void case2() {
        Integer productId = 1;
        LocalDateTime date = LocalDateTime.now();
        Short brandId = Short.valueOf("1");

        given(productPriceService.getProductPrice(productId, date, brandId)).willThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> getProductPriceHandler.getProductPrice(productId, date, brandId));
        then(productPriceService).should(times(1)).getProductPrice(productId, date, brandId);
    }
}