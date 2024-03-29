package com.productmodule.domain.product;

import com.productmodule.domain.product.ProductCommand.ProductCreateCommand;
import com.productmodule.domain.product.ProductCommand.ProductUpdateCommand;
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

	public void decreaseStockQuatity(Long orderQuantity) {
		this.stockQuantity = this.stockQuantity - orderQuantity;
	}

	public static Product create(ProductCreateCommand command) {
		return new Product(null, command.name(), command.price(), command.stockQuantity());
	}

	public void update(ProductUpdateCommand command) {
		this.name = command.name();
		this.price = command.price();
		this.stockQuantity = command.stockQuantity();
	}
}
