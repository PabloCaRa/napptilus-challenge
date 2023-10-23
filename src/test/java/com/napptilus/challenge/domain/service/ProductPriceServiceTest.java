package com.napptilus.challenge.domain.service;

import com.napptilus.challenge.domain.model.Brand;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.port.ProductPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductPriceServiceTest {

    @Mock
    private ProductPriceRepository productPriceRepository;

    private ProductPriceService productPriceService;

    @BeforeEach
    void setup() {
        productPriceService = new ProductPriceService(productPriceRepository);
    }

    @Test
    @DisplayName("""
            Given a product id
            And a brand id
            And a date
            And repository returns one product price entity
            When getProductPrice
            Then the product price is returned
            """)
    void case1() {
        Integer productId = 1;
        Short brandId = Short.valueOf("1");
        LocalDateTime date = LocalDateTime.now();

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

        given(productPriceRepository.findProductPrice(productId, date, brandId)).willReturn(List.of(existingProductPrice));

        ProductPrice productPrice = productPriceService.getProductPrice(productId, date, brandId);

        then(productPriceRepository).should(times(1)).findProductPrice(productId, date, brandId);
        assertEquals(existingProductPrice, productPrice);
    }

    @Test
    @DisplayName("""
            Given a product id
            And a brand id
            And a date
            And repository returns more than one product price entities
            When getProductPrice
            Then the product price with the highest priority is returned
            """)
    void case2() {
        Integer productId = 1;
        Short brandId = Short.valueOf("1");
        LocalDateTime date = LocalDateTime.now();

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

        ProductPrice existingProductPriceWithHighestPriority = ProductPrice.builder()
                .brand(Brand.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Brand one")
                        .build())
                .startDate(LocalDateTime.now().minusHours(2L))
                .endDate(LocalDateTime.now().plusHours(2L))
                .priceList(Short.valueOf("2"))
                .productId(1)
                .priority(Short.valueOf("1"))
                .price(BigDecimal.ONE)
                .currency("EUR")
                .build();

        given(productPriceRepository.findProductPrice(productId, date, brandId))
                .willReturn(List.of(existingProductPrice, existingProductPriceWithHighestPriority));

        ProductPrice productPrice = productPriceService.getProductPrice(productId, date, brandId);

        then(productPriceRepository).should(times(1)).findProductPrice(productId, date, brandId);
        assertEquals(existingProductPriceWithHighestPriority, productPrice);
    }

    @Test
    @DisplayName("""
            Given a product id
            And a brand id
            And a date
            And repository doesn't return anything
            When getProductPrice
            Then an exception is thrown
            """)
    void case3() {
        Integer productId = 1;
        Short brandId = Short.valueOf("1");
        LocalDateTime date = LocalDateTime.now();

        given(productPriceRepository.findProductPrice(productId, date, brandId)).willReturn(List.of());

        assertThrows(NoSuchElementException.class, () -> productPriceService.getProductPrice(productId, date, brandId));

        then(productPriceRepository).should(times(1)).findProductPrice(productId, date, brandId);
    }
}