package com.ordermodule.domain.order;

import com.commonmodule.dto.product.ProductViewModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Getter @Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	private Long orderPrice;  // 주문금액

	@Default
	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
	private List<OrderItem> orderItems = new ArrayList<>();

	public static Order createOrder(List<ProductViewModel> products, Map<Long, Long> productsToOrderQuantity) {

		var order = Order.builder()
				.orderPrice(calculateOrderPrice(products, productsToOrderQuantity))
				.build();

		var orderItems = products.stream()
				.filter(product -> productsToOrderQuantity.containsKey(product.id()))
				.map(product -> OrderItem.builder()
						.order(order)
						.itemId(product.id())
						.itemName(product.name())
						.itemUnitPrice(product.price())
						.orderQuantity(productsToOrderQuantity.get(product.id()))
						.build()
				)
				.toList();

		order.setOrderItemEntities(orderItems);

		return order;
	}

	public static Long calculateOrderPrice(List<ProductViewModel> itemEntities, Map<Long, Long> itemIdToOrderQuantity) {
		return itemEntities.stream()
				.mapToLong(item -> item.price() * itemIdToOrderQuantity.getOrDefault(item.id(), 0L))
				.sum();
	}

	public void setOrderItemEntities(List<OrderItem> orderItemEntities) {
		this.orderItems = orderItemEntities;
	}

}