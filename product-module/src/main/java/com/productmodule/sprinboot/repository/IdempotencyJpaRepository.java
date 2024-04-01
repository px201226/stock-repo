package com.productmodule.sprinboot.repository;

import com.productmodule.domain.idempotency.Idempotency;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IdempotencyJpaRepository extends JpaRepository<Idempotency, String> {

}
