package com.commonmodule.dto.product;

import java.util.Map;
import java.util.stream.Collectors;

public record AdjustStockCommand(
		Map<Long, Long> productIdToAdjustStock
) {

	public AdjustStockCommand reverse() {
		Map<Long, Long> reversedDifferences = this.productIdToAdjustStock.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> entry.getValue() * -1
				));

		return new AdjustStockCommand(reversedDifferences);
	}

	public AdjustStockCommand decrease() {
		Map<Long, Long> decreaseStock = this.productIdToAdjustStock.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						entry -> Math.abs(entry.getValue()) * -1
				));

		return new AdjustStockCommand(decreaseStock);
	}
}
