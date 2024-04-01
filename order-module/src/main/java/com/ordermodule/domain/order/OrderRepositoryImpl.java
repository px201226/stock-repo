package com.ordermodule.domain.order;

import com.ordermodule.springboot.repository.OrderJpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;
	@Override public Order save(Order order) {
		return orderJpaRepository.save(order);
	}

	@Override public void delete(Order order) {
		orderJpaRepository.delete(order);
	}

	@Override public Optional<Order> findById(Long orderId) {
		return orderJpaRepository.findById(orderId);
	}

	@Override public List<Order> findAllById(Collection<Long> orderIds) {
		return orderJpaRepository.findAllById(orderIds);
	}
}
