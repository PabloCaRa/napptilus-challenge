package com.napptilus.challenge.domain.port;

import com.napptilus.challenge.domain.model.ProductPrice;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductPriceRepository {

    List<ProductPrice> findProductPrice(Integer productId, LocalDateTime date, Short brandId);
}
