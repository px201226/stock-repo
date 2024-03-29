package com.productmodule.domain.product;

public record ProductCommand() {

	public record ProductCreateCommand(
			String name,
			Long price,
			Long stockQuantity
	){

	}

	public record ProductUpdateCommand(
			String name,
			Long price,
			Long stockQuantity
	){

	}
}
