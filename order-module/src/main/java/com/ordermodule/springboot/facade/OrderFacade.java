package com.ordermodule.springboot.facade;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.ordermodule.domain.order.Order;
import com.ordermodule.domain.order.OrderCommand.OrderCreateCommand;
import com.ordermodule.domain.order.OrderCommand.OrderUpdateCommand;
import com.ordermodule.domain.order.OrderItem;
import com.ordermodule.domain.order.OrderService;
import com.ordermodule.domain.order.ProductClient;
import com.ordermodule.springboot.dto.OrderViewModel;
import com.ordermodule.springboot.dto.OrderViewModel.OrderItemViewModel;
import com.ordermodule.springboot.mapper.OrderMapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;
	private final OrderMapper orderMapper;
	private final ProductClient productClient;

	public List<OrderViewModel> getOrders(Collection<Long> orderIds) {
		var orders = orderService.getOrders(orderIds);
		return orderMapper.toOrderViewModelList(orders);
	}

	public OrderViewModel getOrderById(Long orderId) {
		var order = orderService.getOrderById(orderId);
		return orderMapper.toOrderViewModel(order);
	}

	public OrderViewModel placeOrder(OrderCreateCommand command) {

		// 상품ID 별 주문 수량 집계
		var productIdToOrderQuantity = command.aggregateOrderQuantitiesByProductId();

		// 상품 정보 가져오기
		var productViewModels = productClient.findAllById(productIdToOrderQuantity.keySet());

		// 주문 검증
		orderService.validateCreateOrder(command, productViewModels);

		// 상품별 주문 수량
		var productToOrderQuantity = productViewModels.stream()
				.collect(Collectors.toMap(
								Function.identity(),
								v -> productIdToOrderQuantity.get(v.productId())
						)
				);

		// 재소 차감
		var adjustStockCommand = new AdjustStockCommand(productIdToOrderQuantity);
		productClient.adjustStock(adjustStockCommand.decrease());

		// 주문 생성
		try {
			var order = orderService.placeOrder(productToOrderQuantity);
			return orderMapper.toOrderViewModel(order);

		} catch (Exception e) {
			productClient.adjustStock(adjustStockCommand.reverse());
			throw new RuntimeException(e);
		}

	}


	public OrderViewModel changeOrder(Long orderId, OrderUpdateCommand command) {

		// 유효성 검증
		orderService.validateOrderUpdateCommand(orderId, command);

		// 기존 주문 상태 조회
		OrderViewModel orderViewModel = this.getOrderById(orderId);

		// 주문 변경
		Order order = orderService.changeOrder(orderId, command);

		// 재고 조정값 계산
		Map<Long, Long> productIdToQuantityDifferences = calculateQuantityDifferences(order, orderViewModel);

		// product 재고 조정 호출
		productClient.adjustStock(new AdjustStockCommand(productIdToQuantityDifferences));

		return orderMapper.toOrderViewModel(order);
	}


	public void deleteOrder(Long orderId) {
		var order = orderService.deleteOrder(orderId);

		var productIdToOrderQuantity = order.getOrderItems()
				.stream()
				.collect(Collectors.groupingBy(
						OrderItem::getProductId,
						Collectors.summingLong(OrderItem::getOrderQuantity))
				);

		// 재고 증가
		productClient.adjustStock(new AdjustStockCommand(productIdToOrderQuantity));

	}


	private Map<Long, Long> calculateQuantityDifferences(Order order, OrderViewModel orderViewModel) {

		Map<Long, Long> existingQuantities = orderViewModel.orderItemViewModels().stream()
				.collect(Collectors.toMap(OrderItemViewModel::productId, OrderItemViewModel::orderQuantity));

		Map<Long, Long> updatedQuantities = order.getOrderItems().stream()
				.collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getOrderQuantity));

		// 최종적으로 계산된 상품별 수량 차이를 저장할 맵
		Map<Long, Long> quantityDifferences = new HashMap<>();

		// 변경된 주문 항목을 기준으로 수량 차이 계산
		updatedQuantities.forEach((itemId, updatedQuantity) -> {
			Long existingQuantity = existingQuantities.getOrDefault(itemId, 0L);
			long quantityDifference = existingQuantity - updatedQuantity;
			quantityDifferences.put(itemId, quantityDifference);
		});

		return quantityDifferences;
	}

}
