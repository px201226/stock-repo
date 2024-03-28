package com.productmodule.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

	@Id
	private Long id;

	private String name;

	private Long price;

	private Long stockQuantity;

	public static Product create(Long id, String name, Long price, Long stockQuantity) {
		return new Product(id, name, price, stockQuantity);
	}

	public void decreaseStockQuatity(Long orderQuantity) {
		this.stockQuantity = this.stockQuantity - orderQuantity;
	}
}
