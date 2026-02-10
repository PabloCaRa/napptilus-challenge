package com.napptilus.challenge.infrastructure.persistence.repository;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.port.ProductPriceRepository;
import com.napptilus.challenge.infrastructure.persistence.exception.InfrastructureException;
import com.napptilus.challenge.infrastructure.persistence.mapper.ProductPriceEntityMapper;
import com.napptilus.challenge.infrastructure.persistence.r2dbc.ProductPriceReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class ProductPriceRepositoryImpl implements ProductPriceRepository {

    private final ProductPriceReactiveRepository productPriceReactiveRepository;
    private final ProductPriceEntityMapper productPriceEntityMapper;

    @Override
    public Flux<ProductPrice> getProductPriceByBrandIdAndProductIdAndApplicationDate(Integer brandId, Integer productId, LocalDateTime applicationDate) {
        return Flux.defer(() -> productPriceReactiveRepository.findByBrandIdAndProductIdAndApplicationDate(brandId, productId, applicationDate))
                .map(productPriceEntityMapper::toProductPrice)
                .onErrorMap(ex -> new InfrastructureException("Error retrieving product price for brand %d, product %d in date %s".formatted(brandId, productId, applicationDate), ex));
    }
}
