package com.productmodule.sprinboot.repository;

import com.productmodule.domain.idempotency.Idempotency;
import com.productmodule.domain.idempotency.IdempotencyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IdempotencyRepositoryImpl implements IdempotencyRepository {

	private final IdempotencyJpaRepository idempotencyJpaRepository;

	@Override public Optional<Idempotency> findById(String idempotencyKey) {
		return idempotencyJpaRepository.findById(idempotencyKey);
	}

	@Override public void save(Idempotency saveIdempotency) {
		idempotencyJpaRepository.save(saveIdempotency);
	}
}
