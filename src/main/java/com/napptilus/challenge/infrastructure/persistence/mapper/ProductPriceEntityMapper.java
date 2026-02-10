package com.napptilus.challenge.infrastructure.persistence.mapper;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.persistence.entity.ProductPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductPriceEntityMapper {

    ProductPrice toProductPrice(ProductPriceEntity productPriceEntity);
}
