package com.napptilus.challenge.infrastructure.persistence;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.domain.port.ProductPriceRepository;
import com.napptilus.challenge.infrastructure.persistence.jpa.ProductPriceJpaRepository;
import com.napptilus.challenge.infrastructure.persistence.mapper.ProductPriceEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductPriceRepositoryImpl implements ProductPriceRepository {

    private final ProductPriceJpaRepository productPriceJpaRepository;
    private final ProductPriceEntityMapper productPriceEntityMapper;

    @Override
    public List<ProductPrice> findProductPrice(Integer productId, LocalDateTime date, Short brandId) {
        return productPriceEntityMapper.toDomain(productPriceJpaRepository.findProductPriceByProductIdAndBrandIdAndDate(productId, brandId, date));
    }
}
