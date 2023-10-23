package com.napptilus.challenge.infrastructure.http.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record GetProductPriceResponse(Integer productId,
                                      Short brandId,
                                      Short priceList,
                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime startDate,
                                      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime endDate,
                                      BigDecimal price) {
}
