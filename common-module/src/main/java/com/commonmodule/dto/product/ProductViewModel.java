package com.commonmodule.dto.product;

public record ProductViewModel(
		Long productId,
		String name,
		Long price,
		Long stockQuantity
) {

}
