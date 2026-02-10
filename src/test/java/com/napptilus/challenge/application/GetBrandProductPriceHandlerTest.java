package com.napptilus.challenge.application;

import com.napptilus.challenge.MotherTestBuilder;
import com.napptilus.challenge.domain.exception.ProductPriceNotFoundException;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.service.ProductPriceService;
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
public class GetBrandProductPriceHandlerTest {

    @Mock
    private ProductPriceService productPriceService;

    @InjectMocks
    private GetBrandProductPriceHandler getBrandProductPriceHandler;

    @Test
    @DisplayName("""
            Given brandId, productId and applicationDate
            And a service that returns a Product Price
            When handler is called
            Then a Product Price is returned
            """)
    void case1() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();
        ProductPrice productPrice = MotherTestBuilder.productPrice(applicationDate);

        given(productPriceService.getBrandProductPriceInDate(anyInt(), anyInt(), any(LocalDateTime.class))).willReturn(Mono.just(productPrice));

        StepVerifier.create(getBrandProductPriceHandler.handle(brandId, productId, applicationDate))
                .expectNext(productPrice)
                .verifyComplete();

        then(productPriceService).should(times(1)).getBrandProductPriceInDate(brandId, productId, applicationDate);
        then(productPriceService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given brandId, productId and applicationDate
            And a service that returns an error
            When handler is called
            Then it propagates the error
            """)
    void case2() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(productPriceService.getBrandProductPriceInDate(anyInt(), anyInt(), any(LocalDateTime.class)))
                .willReturn(Mono.error(new ProductPriceNotFoundException(brandId, productId, applicationDate)));

        StepVerifier.create(getBrandProductPriceHandler.handle(brandId, productId, applicationDate))
                .expectErrorMessage("There isn't any saved price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate))
                .verify();

        then(productPriceService).should(times(1)).getBrandProductPriceInDate(brandId, productId, applicationDate);
        then(productPriceService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given brandId, productId and applicationDate
            And the service throws synchronously
            When handler is called
            Then the error is propagated
            """)
    void case3() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(productPriceService.getBrandProductPriceInDate(anyInt(), anyInt(), any(LocalDateTime.class)))
                .willThrow(new RuntimeException("boom"));

        StepVerifier.create(Mono.defer(() ->
                        getBrandProductPriceHandler.handle(brandId, productId, applicationDate)))
                .expectErrorMessage("boom")
                .verify();

        then(productPriceService).should(times(1)).getBrandProductPriceInDate(brandId, productId, applicationDate);
        then(productPriceService).shouldHaveNoMoreInteractions();
    }
}
