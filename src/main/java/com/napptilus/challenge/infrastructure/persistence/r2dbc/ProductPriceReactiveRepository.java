package com.napptilus.challenge.infrastructure.persistence.r2dbc;

import com.napptilus.challenge.infrastructure.persistence.entity.ProductPriceEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ProductPriceReactiveRepository extends ReactiveCrudRepository<ProductPriceEntity, Integer> {

    @Query("""
            SELECT brand_id, start_date, end_date, price_list, product_id, priority, price, currency
             FROM product_price p
            WHERE p.brand_id = :brandId
              AND p.product_id = :productId
              AND :applicationDate BETWEEN p.start_date AND p.end_date
            """
    )
    Flux<ProductPriceEntity> findByBrandIdAndProductIdAndApplicationDate(Integer brandId, Integer productId, LocalDateTime applicationDate);
}
