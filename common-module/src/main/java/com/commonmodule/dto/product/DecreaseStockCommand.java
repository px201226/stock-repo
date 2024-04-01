package com.commonmodule.dto.product;

import java.util.Map;

public record DecreaseStockCommand(
		Map<Long, Long> productIdToOrderQuantity
) {

}
