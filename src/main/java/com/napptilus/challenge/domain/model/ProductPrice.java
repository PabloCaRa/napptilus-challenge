package com.napptilus.challenge.domain.model;

import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductPrice(Brand brand,
                           LocalDateTime startDate,
                           LocalDateTime endDate,
                           Short priceList,
                           Integer productId,
                           Short priority,
                           BigDecimal price,
                           String currency) {
}
