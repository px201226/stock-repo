package com.ordermodule.springboot.dto;

import java.util.List;

public record OrderViewModel(
		Long orderId,
		Long orderPrice,
		List<OrderItemViewModel> orderItemViewModels
) {


	public record OrderItemViewModel(
			Long orderItemId,
			Long productId,
			String itemName,
			Long itemUnitPrice,
			Long orderQuantity
	) {

	}
}
