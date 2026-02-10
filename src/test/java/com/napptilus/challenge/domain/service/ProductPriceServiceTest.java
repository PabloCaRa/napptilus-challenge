package com.napptilus.challenge.domain.service;

import com.napptilus.challenge.MotherTestBuilder;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.port.ProductPriceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductPriceServiceTest {

    @Mock
    private ProductPriceRepository productPriceRepository;

    @InjectMocks
    private ProductPriceService productPriceService;

    @ParameterizedTest
    @DisplayName("""
            Given a brand id, product id and an application date
            And a repository that returns a list of product prices
            When the service is called
            Then the expected product price is returned
            """)
    @MethodSource("case1Arguments")
    void case1(LocalDateTime applicationDate, Flux<ProductPrice> returnedProductPrices, ProductPrice expectedProductPrice) {
        Integer brandId = 1;
        Integer productId = 35455;

        given(productPriceRepository.getProductPriceByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(returnedProductPrices);

        StepVerifier.create(productPriceService.getBrandProductPriceInDate(brandId, productId, applicationDate))
                .expectNext(expectedProductPrice)
                .verifyComplete();

        then(productPriceRepository).should(times(1)).getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given a brand id, product id and an application date
            And a repository that returns an empty list of product prices
            When the service is called
            Then it throws a ProductPriceNotFoundException
            """)
    void case2() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(productPriceRepository.getProductPriceByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(Flux.empty());

        StepVerifier.create(productPriceService.getBrandProductPriceInDate(brandId, productId, applicationDate))
                .expectErrorMessage("There isn't any saved price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate))
                .verify();

        then(productPriceRepository).should(times(1)).getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given a brand id, product id and an application date
            And a repository that throws an exception
            When the service is called
            Then it throws the same exception
            """)
    void case3() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(productPriceRepository.getProductPriceByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class))).willThrow(new RuntimeException("boom"));

        StepVerifier.create(productPriceService.getBrandProductPriceInDate(brandId, productId, applicationDate))
                .expectErrorMessage("boom")
                .verify();

        then(productPriceRepository).should(times(1)).getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given a brand id, product id and an application date
            And a repository that returns error
            When the service is called
            Then it throws the same exception
            """)
    void case4() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(productPriceRepository.getProductPriceByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(Flux.error(new RuntimeException("boom")));

        StepVerifier.create(productPriceService.getBrandProductPriceInDate(brandId, productId, applicationDate))
                .expectErrorMessage("boom")
                .verify();

        then(productPriceRepository).should(times(1)).getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceRepository).shouldHaveNoMoreInteractions();
    }

    private static Stream<Arguments> case1Arguments() {
        ProductPrice productPricePriceList1 = MotherTestBuilder.productPrice(
                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                1,
                0,
                BigDecimal.valueOf(35.50)
        );

        ProductPrice productPricePriceList2 = MotherTestBuilder.productPrice(
                LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30, 0),
                2,
                1,
                BigDecimal.valueOf(25.45)
        );

        ProductPrice productPricePriceList3 = MotherTestBuilder.productPrice(
                LocalDateTime.of(2020, 6, 15, 0, 0, 0),
                LocalDateTime.of(2020, 6, 15, 11, 0, 0),
                3,
                1,
                BigDecimal.valueOf(30.50)
        );

        ProductPrice productPricePriceList4 = MotherTestBuilder.productPrice(
                LocalDateTime.of(2020, 6, 15, 16, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                4,
                1,
                BigDecimal.valueOf(38.95)
        );

        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                        Flux.fromIterable(List.of(productPricePriceList1)),
                        productPricePriceList1
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 16, 0, 0),
                        Flux.fromIterable(List.of(productPricePriceList1, productPricePriceList2)),
                        productPricePriceList2
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 21, 0, 0),
                        Flux.fromIterable(List.of(productPricePriceList1)),
                        productPricePriceList1
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 15, 10, 0, 0),
                        Flux.fromIterable(List.of(productPricePriceList1, productPricePriceList3)),
                        productPricePriceList3
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 16, 21, 0, 0),
                        Flux.fromIterable(List.of(productPricePriceList1, productPricePriceList4)),
                        productPricePriceList4
                )
        );
    }

}
