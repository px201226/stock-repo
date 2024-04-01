package com.ordermodule.springboot.mapper;

import com.ordermodule.domain.order.Order;
import com.ordermodule.domain.order.OrderItem;
import com.ordermodule.springboot.dto.OrderViewModel;
import com.ordermodule.springboot.dto.OrderViewModel.OrderItemViewModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

	@Mapping(target = "orderItemViewModels", source = "orderItems")
	OrderViewModel toOrderViewModel(Order order);

	OrderItemViewModel toOrderItemViewModel(OrderItem orderItem);

	List<OrderItemViewModel> toOrderItemViewModelList(List<OrderItem> orderItems);

	List<OrderViewModel> toOrderViewModelList(List<Order> orders);
}
