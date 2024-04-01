package com.ordermodule.domain.order;

import com.commonmodule.dto.product.ProductViewModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter @Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	private Long orderPrice;  // 주문금액

	@Default
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	public void setOrderItems(List<OrderItem> orderItemEntities) {
		this.orderItems = orderItemEntities;
	}

	public static Order createOrder(Map<ProductViewModel, Long> productToOderQuantity) {

		var order = Order.builder()
				.orderPrice(calculateOrderPrice(productToOderQuantity))
				.build();

		var orderItems = productToOderQuantity.entrySet().stream()
				.map(entry -> OrderItem.builder()
						.order(order)
						.productId(entry.getKey().productId())
						.itemName(entry.getKey().name())
						.itemUnitPrice(entry.getKey().price())
						.orderQuantity(entry.getValue())
						.build()
				)
				.toList();

		order.setOrderItems(orderItems);

		return order;
	}

	private static Long calculateOrderPrice(Map<ProductViewModel, Long> productToOderQuantity) {
		return productToOderQuantity.entrySet()
				.stream()
				.mapToLong(entry -> entry.getKey().price() * entry.getValue())
				.sum();
	}

	public void applyOrderPrice() {
		this.orderPrice = this.orderItems.stream()
				.mapToLong(item -> item.getItemUnitPrice() * item.getOrderQuantity())
				.sum();
	}
}