package com.napptilus.challenge.infrastructure.http.controller;

import com.napptilus.challenge.MotherTestBuilder;
import com.napptilus.challenge.application.GetBrandProductPriceHandler;
import com.napptilus.challenge.domain.exception.ProductPriceNotFoundException;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.mapper.ProductPriceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductPriceControllerTest {

    @Mock
    private GetBrandProductPriceHandler getBrandProductPriceHandler;

    @Mock
    private ProductPriceMapper productPriceMapper;

    @InjectMocks
    private ProductPriceController productPriceController;

    @Test
    @DisplayName("""
            Given brandId, productId and applicationDate
            And a handler that returns a valid product price
            And a mapper that returns a valid product price response
            When controller is called
            Then the expected product price is returned
            """)
    void case1() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(getBrandProductPriceHandler.handle(anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(Mono.just(MotherTestBuilder.productPrice(applicationDate)));
        given(productPriceMapper.toGetProducePriceResponse(any(ProductPrice.class))).willReturn(MotherTestBuilder.productPriceResponse(applicationDate));

        StepVerifier.create(productPriceController.getBrandProductPrice(brandId, productId, applicationDate))
                .expectNext(MotherTestBuilder.productPriceResponse(applicationDate))
                .verifyComplete();

        then(getBrandProductPriceHandler).should(times(1)).handle(brandId, productId, applicationDate);
        then(getBrandProductPriceHandler).shouldHaveNoMoreInteractions();

        then(productPriceMapper).should(times(1)).toGetProducePriceResponse(any(ProductPrice.class));
        then(productPriceMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given brandId, productId and applicationDate
            And a handler that returns an error
            When controller is called
            Then the expected error is returned
            """)
    void case2() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(getBrandProductPriceHandler.handle(anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(Mono.error(new ProductPriceNotFoundException(brandId, productId, applicationDate)));

        StepVerifier.create(productPriceController.getBrandProductPrice(brandId, productId, applicationDate))
                .expectErrorMessage("There isn't any saved price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate))
                .verify();

        then(getBrandProductPriceHandler).should(times(1)).handle(brandId, productId, applicationDate);
        then(getBrandProductPriceHandler).shouldHaveNoMoreInteractions();

        then(productPriceMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("""
            Given brandId, productId and applicationDate
            And a handler that throws an exception
            When controller is called
            Then the exception is propagated
            """)
    void case3() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(getBrandProductPriceHandler.handle(anyInt(), anyInt(), any(LocalDateTime.class))).willThrow(new RuntimeException("boom"));

        StepVerifier.create(Mono.defer(() ->
                        productPriceController.getBrandProductPrice(brandId, productId, applicationDate)))
                .expectErrorMessage("boom")
                .verify();

        then(getBrandProductPriceHandler).should(times(1)).handle(brandId, productId, applicationDate);
        then(getBrandProductPriceHandler).shouldHaveNoMoreInteractions();

        then(productPriceMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("""
            Given brandId, productId and applicationDate
            And a handler that returns a valid product price
            And a mapper that returns an exception
            When controller is called
            Then the exception is propagated
            """)
    void case4() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(getBrandProductPriceHandler.handle(anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(Mono.just(MotherTestBuilder.productPrice(applicationDate)));
        given(productPriceMapper.toGetProducePriceResponse(any(ProductPrice.class))).willThrow(new RuntimeException("boom"));

        StepVerifier.create(Mono.defer(() ->
                        productPriceController.getBrandProductPrice(brandId, productId, applicationDate)))
                .expectErrorMessage("boom")
                .verify();

        then(getBrandProductPriceHandler).should(times(1)).handle(brandId, productId, applicationDate);
        then(getBrandProductPriceHandler).shouldHaveNoMoreInteractions();

        then(productPriceMapper).should(times(1)).toGetProducePriceResponse(any(ProductPrice.class));
        then(productPriceMapper).shouldHaveNoMoreInteractions();
    }
}
