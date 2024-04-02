package com.ordermodule.springboot.facade;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.ordermodule.domain.order.Order;
import com.ordermodule.domain.order.OrderCommand.OrderCreateCommand;
import com.ordermodule.domain.order.OrderCommand.OrderItemUpdate;
import com.ordermodule.domain.order.OrderCommand.OrderUpdateCommand;
import com.ordermodule.domain.order.OrderItem;
import com.ordermodule.domain.order.OrderService;
import com.ordermodule.domain.order.ProductClient;
import com.ordermodule.springboot.client.ProductMessageProducer;
import com.ordermodule.springboot.dto.OrderViewModel;
import com.ordermodule.springboot.dto.OrderViewModel.OrderItemViewModel;
import com.ordermodule.springboot.mapper.OrderMapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;
	private final OrderMapper orderMapper;
	private final ProductClient productClient;
	private final ProductMessageProducer productMessageProducer;

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
		productClient.adjustStock(UUID.randomUUID(), adjustStockCommand.decrease());

		// 주문 생성
		try {
			var order = orderService.placeOrder(productToOrderQuantity);
			return orderMapper.toOrderViewModel(order);

		} catch (Exception e) {
			log.error("failed placeOrder");
			productMessageProducer.sendMessage(UUID.randomUUID(), adjustStockCommand.reverse());
			throw e;
		}

	}


	public OrderViewModel changeOrder(Long orderId, OrderUpdateCommand command) {

		// 유효성 검증
		orderService.validateOrderUpdateCommand(orderId, command);

		// 기존 주문 상태 조회
		OrderViewModel orderViewModel = this.getOrderById(orderId);

		// 재고 조정값 계산
		Map<Long, Long> productIdToQuantityDifferences = calculateQuantityDifferences(command, orderViewModel);

		// product 재고 조정 호출
		var adjustStockCommand = new AdjustStockCommand(productIdToQuantityDifferences);
		productClient.adjustStock(UUID.randomUUID(), adjustStockCommand);

		// 상품 정보 가져오기
		var productViewModels = productClient.findAllById(productIdToQuantityDifferences.keySet());

		// 주문 변경
		try {
			Order order = orderService.changeOrder(orderId, command, productViewModels);
			return orderMapper.toOrderViewModel(order);

		} catch (Exception e){
			log.error("failed changeOrder");
			productMessageProducer.sendMessage(UUID.randomUUID(), adjustStockCommand.reverse());
			throw e;
		}
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
		productMessageProducer.sendMessage(UUID.randomUUID(), new AdjustStockCommand(productIdToOrderQuantity));

	}


	private Map<Long, Long> calculateQuantityDifferences(OrderUpdateCommand command, OrderViewModel orderViewModel) {

		Map<Long, Long> existingQuantities = orderViewModel.orderItemViewModels().stream()
				.collect(Collectors.toMap(OrderItemViewModel::productId, OrderItemViewModel::orderQuantity));

		Map<Long, Long> updatedQuantities = command.orderItemUpdates().stream()
				.collect(Collectors.toMap(OrderItemUpdate::productId, OrderItemUpdate::orderQuantity));

		// 최종적으로 계산된 상품별 수량 차이를 저장할 맵
		Map<Long, Long> quantityDifferences = new HashMap<>();

		// 변경된 주문 항목을 기준으로 수량 차이 계산
		updatedQuantities.forEach((productId, updatedQuantity) -> {
			Long existingQuantity = existingQuantities.getOrDefault(productId, 0L);
			long quantityDifference = existingQuantity - updatedQuantity;
			quantityDifferences.put(productId, quantityDifference);
		});

		return quantityDifferences;
	}

}
