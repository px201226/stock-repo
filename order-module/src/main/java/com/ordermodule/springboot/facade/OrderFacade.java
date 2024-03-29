package com.ordermodule.springboot.facade;

import com.ordermodule.domain.order.OrderCommand;
import com.ordermodule.domain.order.OrderCommand.OrderCreateCommand;
import com.ordermodule.domain.order.OrderCommand.OrderUpdateCommand;
import com.ordermodule.springboot.dto.OrderViewModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	public List<OrderViewModel> getAllOrders() {
		return null;
	}

	public OrderViewModel getOrderById(Long orderId) {
		return null;
	}

	public OrderViewModel placeOrder(OrderCreateCommand command) {
		return null;
	}

	public OrderViewModel changeOrder(Long orderId, OrderUpdateCommand command) {
		return null;
	}

	public void deleteOrder(Long orderId) {

	}
}
