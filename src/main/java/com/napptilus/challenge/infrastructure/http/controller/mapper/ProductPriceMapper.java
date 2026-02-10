package com.napptilus.challenge.infrastructure.http.controller.mapper;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.http.controller.dto.GetProducePriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductPriceMapper {

    GetProducePriceResponse toGetProducePriceResponse(ProductPrice productPrice);
}
