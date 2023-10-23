package com.napptilus.challenge.infrastructure.http.controller.mapper;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProductPriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductPriceMapper {

    @Mapping(target = "brandId", source = "brand.brandId")
    GetProductPriceResponse toResponse(ProductPrice productPrice);
}
