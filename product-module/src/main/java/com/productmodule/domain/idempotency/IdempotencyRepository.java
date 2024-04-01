package com.productmodule.domain.idempotency;

import java.util.Optional;

public interface IdempotencyRepository {

	Optional<Idempotency> findById(String idempotencyKey);

	void save(Idempotency saveIdempotency);
}
