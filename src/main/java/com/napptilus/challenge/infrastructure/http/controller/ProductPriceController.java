package com.napptilus.challenge.infrastructure.http.controller;

import com.napptilus.challenge.application.GetProductPriceHandler;
import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProductPriceResponse;
import com.napptilus.challenge.infrastructure.http.controller.mapper.ProductPriceMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/product/{productId}/price")
@RequiredArgsConstructor
public class ProductPriceController {

    private final GetProductPriceHandler getProductPriceHandler;
    private final ProductPriceMapper productPriceMapper;

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = GetProductPriceResponse.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GetProductPriceResponse getProductPrice(
            @Parameter(example = "35455")
            @PathVariable(name = "productId") Integer productId,
            @Parameter(example = "2020-06-14T10:00:00")
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @Parameter(example = "1")
            @RequestParam(name = "brandId") Short brandId
    ) {
        try {
            ProductPrice productPrice = getProductPriceHandler.getProductPrice(productId, date, brandId);
            return productPriceMapper.toResponse(productPrice);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
