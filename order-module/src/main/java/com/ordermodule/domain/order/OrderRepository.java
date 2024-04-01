package com.ordermodule.domain.order;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

	Order save(Order order);

	void delete(Order order);

	Optional<Order> findById(Long orderId);

	List<Order> findAllById(Collection<Long> orderIds);
}
