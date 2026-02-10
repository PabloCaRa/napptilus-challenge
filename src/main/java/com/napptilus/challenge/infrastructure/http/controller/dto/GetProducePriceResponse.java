package com.napptilus.challenge.infrastructure.http.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record GetProducePriceResponse(
        @JsonProperty("product_id")
        Integer productId,
        @JsonProperty("brand_id")
        Integer brandId,
        @JsonProperty("price_list")
        Integer priceList,
        @JsonProperty("start_date")
        LocalDateTime startDate,
        @JsonProperty("end_date")
        LocalDateTime endDate,
        BigDecimal price,
        String currency
) {
}
