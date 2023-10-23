package com.napptilus.challenge.infrastructure.persistence.jpa;

import com.napptilus.challenge.infrastructure.persistence.jpa.entity.ProductPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductPriceJpaRepository extends JpaRepository<ProductPriceEntity, Integer> {

    //List<ProductPriceEntity> findByProductIdAndBrandBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer productId, Short brandId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(
            value = "SELECT * FROM prices p WHERE p.product_id = ?1 AND p.brand_id = ?2 AND ?3 BETWEEN p.start_date AND p.end_date",
            nativeQuery = true)
    List<ProductPriceEntity> findProductPriceByProductIdAndBrandIdAndDate(Integer productId, Short brandId, LocalDateTime date);
}
