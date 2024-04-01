package com.ordermodule.domain.order;


import com.commonmodule.dto.product.ProductViewModel;
import com.ordermodule.domain.exception.BusinessException;
import com.ordermodule.domain.exception.Error;
import com.ordermodule.domain.order.OrderCommand.OrderCreateCommand;
import com.ordermodule.domain.order.OrderCommand.OrderItemCreate;
import com.ordermodule.domain.order.OrderCommand.OrderItemUpdate;
import com.ordermodule.domain.order.OrderCommand.OrderUpdateCommand;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public List<Order> getOrders(Collection<Long> orderIds) {
		return orderRepository.findAllById(orderIds);
	}

	public Order getOrderById(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> BusinessException.of(Error.NOT_FOUND_ENTITY, Map.of("orderId", orderId)));
	}


	@Transactional
	public Order deleteOrder(Long orderId) {
		var order = getOrderById(orderId);
		orderRepository.delete(order);
		order.getOrderItems().size(); // lazy load 초기화
		return order;
	}

	public Order placeOrder(Map<ProductViewModel, Long> productToOderQuantity) {
		Order order = Order.createOrder(productToOderQuantity);
		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public void validateOrderUpdateCommand(Long orderId, OrderUpdateCommand command) {

		var order = orderRepository.findById(orderId)
				.orElseThrow();

		// 요청된 모든 주문 항목 ID를 추출
		Set<Long> requestedOrderItemIds = command.orderItemUpdates().stream()
				.map(OrderItemUpdate::orderItemId)
				.collect(Collectors.toSet());

		// 주문에 실제로 존재하는 주문 항목 ID 추출
		Set<Long> existingOrderItemIds = order.getOrderItems().stream()
				.map(OrderItem::getOrderItemId)
				.collect(Collectors.toSet());

		// 존재하지 않는 주문 항목 ID를 찾음
		Set<Long> notFoundOrderItemIds = requestedOrderItemIds.stream()
				.filter(id -> !existingOrderItemIds.contains(id))
				.collect(Collectors.toSet());

		if (!notFoundOrderItemIds.isEmpty()) {
			throw BusinessException.of(Error.NOT_FOUND_ENTITY, Map.of("orderItemId", notFoundOrderItemIds));
		}

	}

	@Transactional
	public Order changeOrder(Long orderId, OrderUpdateCommand command) {

		var order = getOrderById(orderId);

		Map<Long, Long> orderItemIdToNewQuantity = command.orderItemUpdates().stream()
				.collect(Collectors.toMap(OrderItemUpdate::orderItemId, OrderItemUpdate::orderQuantity));

		order.getOrderItems().forEach(orderItem -> {
			Long newQuantity = orderItemIdToNewQuantity.get(orderItem.getOrderItemId());
			if (newQuantity != null) {
				orderItem.changeOrderQuantity(newQuantity);
			}
		});

		// 총 주문 금액 재계산
		order.applyOrderPrice();
		return order;
	}

	public void validateCreateOrder(OrderCreateCommand command, List<ProductViewModel> productViewModels) {

		Set<Long> notFoundProductIds = getNotFoundProductIds(command, productViewModels);

		// 존재하지 않는 상품을 주문한 경우
		if (!notFoundProductIds.isEmpty()) {
			throw BusinessException.of(Error.NOT_FOUND_ENTITY, Map.of("productId", notFoundProductIds));
		}
	}

	private Set<Long> getNotFoundProductIds(OrderCreateCommand command, List<ProductViewModel> productViewModels) {
		// 조회된 상품 ID들
		Set<Long> foundProductIds = productViewModels.stream()
				.map(ProductViewModel::productId)
				.collect(Collectors.toSet());

		// 요청된 상품 ID들
		Set<Long> requestedProductIds = command.orderItemCreates()
				.stream()
				.map(OrderItemCreate::productId)
				.collect(Collectors.toSet());
		;

		// 존재하지 않는 상품 ID들 찾기
		return requestedProductIds.stream()
				.filter(id -> !foundProductIds.contains(id))
				.collect(Collectors.toSet());
	}
}
