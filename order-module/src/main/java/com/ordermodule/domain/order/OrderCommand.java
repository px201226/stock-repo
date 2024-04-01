package com.ordermodule.domain.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record OrderCommand() {

	public record OrderCreateCommand(
			@Valid List<OrderItemCreate> orderItemCreates
	) {

		public Map<Long, Long> aggregateOrderQuantitiesByProductId() {
			return orderItemCreates
					.stream()
					.collect(Collectors.groupingBy(
							OrderItemCreate::productId,
							Collectors.summingLong(OrderItemCreate::orderQuantity))
					);
		}
	}

	public record OrderItemCreate(
			@NotNull Long productId,
			@NotNull Long orderQuantity
	) {

	}

	public record OrderUpdateCommand(
			@Valid List<OrderItemUpdate> orderItemUpdates
	) {

	}

	public record OrderItemUpdate(
			@NotNull Long orderItemId,
			@NotNull Long orderQuantity
	) {

	}

}
