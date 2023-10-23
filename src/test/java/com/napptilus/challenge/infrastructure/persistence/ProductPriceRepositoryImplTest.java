package com.napptilus.challenge.infrastructure.persistence;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.persistence.jpa.ProductPriceJpaRepository;
import com.napptilus.challenge.infrastructure.persistence.jpa.entity.ProductPriceEntity;
import com.napptilus.challenge.infrastructure.persistence.mapper.ProductPriceEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductPriceRepositoryImplTest {

    @Mock
    private ProductPriceJpaRepository productPriceJpaRepository;

    @Mock
    private ProductPriceEntityMapper productPriceEntityMapper;

    private ProductPriceRepositoryImpl productPriceRepository;

    @BeforeEach
    void setup() {
        productPriceRepository = new ProductPriceRepositoryImpl(productPriceJpaRepository, productPriceEntityMapper);
    }

    @ParameterizedTest(name = """
            Given a product id
            And a date
            And a brand id
            And jpa repository returns a list of entities
            When findProductPrice
            Then a mapped objects to domain list is returned
            """)
    @MethodSource("provideListOfProductPriceEntities")
    void case1(List<ProductPriceEntity> productPriceEntities, List<ProductPrice> productPrices) {
        Integer productId = 1;
        LocalDateTime date = LocalDateTime.now();
        Short brandId = Short.valueOf("1");

        given(productPriceJpaRepository.findProductPriceByProductIdAndBrandIdAndDate(productId, brandId, date)).willReturn(productPriceEntities);
        given(productPriceEntityMapper.toDomain(productPriceEntities)).willReturn(productPrices);

        List<ProductPrice> productPricesResult = productPriceRepository.findProductPrice(productId, date, brandId);

        assertThat(productPricesResult)
                .hasSameElementsAs(productPrices);
    }

    private static Stream<Arguments> provideListOfProductPriceEntities() {
        ProductPriceEntity productPriceEntity = ProductPriceEntity.builder().build();
        ProductPrice productPrice = ProductPrice.builder().build();

        return Stream.of(
                Arguments.of(List.of(productPriceEntity), List.of(productPrice)),
                Arguments.of(List.of(), List.of())
        );
    }
}