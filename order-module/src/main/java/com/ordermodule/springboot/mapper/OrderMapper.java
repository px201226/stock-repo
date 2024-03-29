package com.ordermodule.springboot.mapper;

import com.ordermodule.domain.order.Order;
import com.ordermodule.springboot.dto.OrderViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

	OrderViewModel toOrderViewModel(Order order);
}
