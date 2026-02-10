package com.napptilus.challenge.infrastructure.http.controller;

import com.napptilus.challenge.application.GetBrandProductPriceHandler;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProducePriceResponse;
import com.napptilus.challenge.infrastructure.http.controller.mapper.ProductPriceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/brands/{brand_id}/products/{product_id}/prices")
public class ProductPriceController {

    private final GetBrandProductPriceHandler getBrandProductPriceHandler;
    private final ProductPriceMapper productPriceMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<GetProducePriceResponse> getBrandProductPrice(
            @PathVariable("brand_id") Integer brandId,
            @PathVariable("product_id") Integer productId,
            @RequestParam("application_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate
    ) {
        return getBrandProductPriceHandler.handle(brandId, productId, applicationDate)
                .map(productPriceMapper::toGetProducePriceResponse);
    }
}
