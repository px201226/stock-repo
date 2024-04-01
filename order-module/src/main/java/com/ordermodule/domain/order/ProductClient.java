package com.ordermodule.domain.order;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.commonmodule.dto.product.ProductViewModel;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductClient {

	List<ProductViewModel> findAllById(Set<Long> productIds);

	void adjustStock(UUID uuid, AdjustStockCommand command);
}
