package com.productmodule.domain.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductCommand() {

	public record ProductCreateCommand(
			@NotEmpty String name,
			@NotNull @PositiveOrZero Long price,
			@NotNull @PositiveOrZero Long stockQuantity
	){

	}

	public record ProductUpdateCommand(
			@NotEmpty String name,
			@NotNull @PositiveOrZero Long price,
			@NotNull @PositiveOrZero Long stockQuantity
	){

	}
}
