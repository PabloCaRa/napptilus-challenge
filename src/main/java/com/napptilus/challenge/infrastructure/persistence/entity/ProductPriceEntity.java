package com.napptilus.challenge.infrastructure.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "product_price")
@Getter
@Builder
public class ProductPriceEntity {

    @Id
    Integer id;
    @Column("brand_id")
    Integer brandId;
    @Column("start_date")
    LocalDateTime startDate;
    @Column("end_date")
    LocalDateTime endDate;
    @Column("price_list")
    Integer priceList;
    @Column("product_id")
    Integer productId;
    @Column("priority")
    Integer priority;
    @Column("price")
    BigDecimal price;
    @Column("currency")
    String currency;
}
