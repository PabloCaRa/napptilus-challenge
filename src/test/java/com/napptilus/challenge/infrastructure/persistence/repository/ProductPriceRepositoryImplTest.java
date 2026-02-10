package com.napptilus.challenge.infrastructure.persistence.repository;

import com.napptilus.challenge.MotherTestBuilder;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.persistence.entity.ProductPriceEntity;
import com.napptilus.challenge.infrastructure.persistence.mapper.ProductPriceEntityMapper;
import com.napptilus.challenge.infrastructure.persistence.r2dbc.ProductPriceReactiveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductPriceRepositoryImplTest {

    @Mock
    private ProductPriceReactiveRepository productPriceReactiveRepository;

    @Mock
    private ProductPriceEntityMapper productPriceEntityMapper;

    @InjectMocks
    private ProductPriceRepositoryImpl productPriceRepositoryImpl;

    @Test
    @DisplayName("""
            Given a valid brandId, productId and applicationDate
            And a reactive repository that returns two product prices
            And a mapper that maps product price entities to product prices
            When getProductPriceByBrandIdAndProductIdAndApplicationDate
            Then it returns the expected product prices
            """)
    void case1() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        ProductPriceEntity productPriceEntity1 = MotherTestBuilder.productPriceEntity(1, applicationDate.minusDays(1), applicationDate.plusDays(1), 1, 0, BigDecimal.ONE);
        ProductPriceEntity productPriceEntity2 = MotherTestBuilder.productPriceEntity(2, applicationDate.minusDays(1), applicationDate.plusDays(5), 2, 1, BigDecimal.TWO);
        given(productPriceReactiveRepository.findByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class)))
                .willReturn(Flux.just(productPriceEntity1, productPriceEntity2));

        ProductPrice productPrice1 = MotherTestBuilder.productPrice(applicationDate.minusDays(1), applicationDate.plusDays(1), 1, 0, BigDecimal.ONE);
        given(productPriceEntityMapper.toProductPrice(productPriceEntity1)).willReturn(productPrice1);

        ProductPrice productPrice2 = MotherTestBuilder.productPrice(applicationDate.minusDays(1), applicationDate.plusDays(5), 2, 1, BigDecimal.TWO);
        given(productPriceEntityMapper.toProductPrice(productPriceEntity2)).willReturn(productPrice2);

        StepVerifier.create(productPriceRepositoryImpl.getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate))
                .expectNext(productPrice1, productPrice2)
                .verifyComplete();

        then(productPriceReactiveRepository).should(times(1)).findByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceReactiveRepository).shouldHaveNoMoreInteractions();

        then(productPriceEntityMapper).should(times(2)).toProductPrice(any());
        then(productPriceEntityMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given a valid brandId, productId and applicationDate
            And a reactive repository that returns empty
            When getProductPriceByBrandIdAndProductIdAndApplicationDate
            Then it returns empty
            """)
    void case2() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(productPriceReactiveRepository.findByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class)))
                .willReturn(Flux.empty());

        StepVerifier.create(productPriceRepositoryImpl.getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate))
                .verifyComplete();

        then(productPriceReactiveRepository).should(times(1)).findByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceReactiveRepository).shouldHaveNoMoreInteractions();

        then(productPriceEntityMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("""
            Given a valid brandId, productId and applicationDate
            And a reactive repository that returns an error
            When getProductPriceByBrandIdAndProductIdAndApplicationDate
            Then it returns an InfrastructurException
            """)
    void case3() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        given(productPriceReactiveRepository.findByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class)))
                .willReturn(Flux.error(new RuntimeException("boom")));

        StepVerifier.create(productPriceRepositoryImpl.getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate))
                .expectErrorMessage("Error retrieving product price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate))
                .verify();

        then(productPriceReactiveRepository).should(times(1)).findByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceReactiveRepository).shouldHaveNoMoreInteractions();

        then(productPriceEntityMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("""
            Given a valid brandId, productId and applicationDate
            And a reactive repository that returns two product prices
            And a mapper that throws an exception on the second call
            When getProductPriceByBrandIdAndProductIdAndApplicationDate
            Then it propagates the exception
            """)
    void case4() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        ProductPriceEntity productPriceEntity1 = MotherTestBuilder.productPriceEntity(1, applicationDate.minusDays(1), applicationDate.plusDays(1), 1, 0, BigDecimal.ONE);
        ProductPriceEntity productPriceEntity2 = MotherTestBuilder.productPriceEntity(2, applicationDate.minusDays(1), applicationDate.plusDays(5), 2, 1, BigDecimal.TWO);
        given(productPriceReactiveRepository.findByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class)))
                .willReturn(Flux.just(productPriceEntity1, productPriceEntity2));

        ProductPrice productPrice1 = MotherTestBuilder.productPrice(applicationDate.minusDays(1), applicationDate.plusDays(1), 1, 0, BigDecimal.ONE);
        given(productPriceEntityMapper.toProductPrice(productPriceEntity1)).willReturn(productPrice1);

        given(productPriceEntityMapper.toProductPrice(productPriceEntity2)).willThrow(new RuntimeException("boom"));

        StepVerifier.create(productPriceRepositoryImpl.getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate))
                .expectNext(productPrice1)
                .expectErrorMessage("Error retrieving product price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate))
                .verify();

        then(productPriceReactiveRepository).should(times(1)).findByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceReactiveRepository).shouldHaveNoMoreInteractions();

        then(productPriceEntityMapper).should(times(2)).toProductPrice(any());
        then(productPriceEntityMapper).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("""
            Given a valid brandId, productId and applicationDate
            And a reactive repository that returns two product prices
            And a mapper that throws an exception on the first call
            When getProductPriceByBrandIdAndProductIdAndApplicationDate
            Then it propagates the exception
            """)
    void case5() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.now();

        ProductPriceEntity productPriceEntity1 = MotherTestBuilder.productPriceEntity(1, applicationDate.minusDays(1), applicationDate.plusDays(1), 1, 0, BigDecimal.ONE);
        ProductPriceEntity productPriceEntity2 = MotherTestBuilder.productPriceEntity(2, applicationDate.minusDays(1), applicationDate.plusDays(5), 2, 1, BigDecimal.TWO);
        given(productPriceReactiveRepository.findByBrandIdAndProductIdAndApplicationDate(anyInt(), anyInt(), any(LocalDateTime.class)))
                .willReturn(Flux.just(productPriceEntity1, productPriceEntity2));

        given(productPriceEntityMapper.toProductPrice(productPriceEntity1)).willThrow(new RuntimeException("boom"));

        StepVerifier.create(productPriceRepositoryImpl.getProductPriceByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate))
                .expectErrorMessage("Error retrieving product price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate))
                .verify();

        then(productPriceReactiveRepository).should(times(1)).findByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate);
        then(productPriceReactiveRepository).shouldHaveNoMoreInteractions();

        then(productPriceEntityMapper).should(times(1)).toProductPrice(any());
        then(productPriceEntityMapper).shouldHaveNoMoreInteractions();
    }
}
