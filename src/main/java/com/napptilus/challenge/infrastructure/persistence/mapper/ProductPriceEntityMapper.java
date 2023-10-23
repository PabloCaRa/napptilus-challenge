package com.napptilus.challenge.infrastructure.persistence.mapper;

import com.napptilus.challenge.domain.model.ProductPrice;
import com.napptilus.challenge.infrastructure.persistence.jpa.entity.ProductPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductPriceEntityMapper {

    List<ProductPrice> toDomain(List<ProductPriceEntity> productPriceEntities);
}
