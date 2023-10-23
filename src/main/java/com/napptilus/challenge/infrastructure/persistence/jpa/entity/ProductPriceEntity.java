package com.napptilus.challenge.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceEntity {

    @Id
    private Integer priceId;

    @OneToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brandId")
    private BrandEntity brand;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Short priceList;

    private Integer productId;

    private Short priority;

    private BigDecimal price;

    @Column(name = "curr")
    private String currency;
}
