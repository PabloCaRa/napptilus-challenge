package com.napptilus.challenge.infrastructure.persistence.jpa;

import com.napptilus.challenge.infrastructure.persistence.jpa.entity.BrandEntity;
import com.napptilus.challenge.infrastructure.persistence.jpa.entity.ProductPriceEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductPriceJpaRepositoryTest {

    @Autowired
    private ProductPriceJpaRepository productPriceJpaRepository;

    @ParameterizedTest(name = """
            Given a product id
            And a brand id
            And a date {0}
            When findProductPriceByProductIdAndBrandIdAndDate
            Then the result is the expected
            """)
    @MethodSource("arguments")
    void case1(LocalDateTime date, List<ProductPriceEntity> expectedProductPriceEntities) {
        Integer productId = 35455;
        Short brandId = 1;

        List<ProductPriceEntity> productPriceEntities = productPriceJpaRepository.findProductPriceByProductIdAndBrandIdAndDate(productId, brandId, date);

        assertThat(productPriceEntities).hasSameElementsAs(expectedProductPriceEntities);
    }

    private static Stream<Arguments> arguments() {
        ProductPriceEntity productPriceEntityIdOne = ProductPriceEntity.builder()
                .priceId(1)
                .brand(BrandEntity.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Napptilus")
                        .build())
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .priceList(Short.valueOf("1"))
                .productId(35455)
                .priority(Short.valueOf("0"))
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        ProductPriceEntity productPriceEntityIdTwo = ProductPriceEntity.builder()
                .priceId(2)
                .brand(BrandEntity.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Napptilus")
                        .build())
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30, 0))
                .priceList(Short.valueOf("2"))
                .productId(35455)
                .priority(Short.valueOf("1"))
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        ProductPriceEntity productPriceEntityIdThree = ProductPriceEntity.builder()
                .priceId(3)
                .brand(BrandEntity.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Napptilus")
                        .build())
                .startDate(LocalDateTime.of(2020, 6, 15, 0, 0, 0))
                .endDate(LocalDateTime.of(2020, 6, 15, 11, 0, 0))
                .priceList(Short.valueOf("3"))
                .productId(35455)
                .priority(Short.valueOf("1"))
                .price(new BigDecimal("30.50"))
                .currency("EUR")
                .build();

        ProductPriceEntity productPriceEntityIdFour = ProductPriceEntity.builder()
                .priceId(4)
                .brand(BrandEntity.builder()
                        .brandId(Short.valueOf("1"))
                        .name("Napptilus")
                        .build())
                .startDate(LocalDateTime.of(2020, 6, 15, 16, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .priceList(Short.valueOf("4"))
                .productId(35455)
                .priority(Short.valueOf("1"))
                .price(new BigDecimal("38.95"))
                .currency("EUR")
                .build();

        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                        List.of(productPriceEntityIdOne)
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 16, 0, 0),
                        List.of(productPriceEntityIdOne, productPriceEntityIdTwo)
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 14, 21, 0, 0),
                        List.of(productPriceEntityIdOne)
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 15, 10, 0, 0),
                        List.of(productPriceEntityIdOne, productPriceEntityIdThree)
                ),
                Arguments.of(
                        LocalDateTime.of(2020, 6, 16, 21, 0, 0),
                        List.of(productPriceEntityIdOne, productPriceEntityIdFour)
                )
        );
    }
}