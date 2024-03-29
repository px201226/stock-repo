package com.commonmodule.dto.product;

public record ProductViewModel(
		Long id,
		String name,
		Long price,
		Long stockQuantity
) {

}
