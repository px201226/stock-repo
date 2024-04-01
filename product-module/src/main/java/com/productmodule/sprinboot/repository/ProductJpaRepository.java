package com.productmodule.sprinboot.repository;

import com.productmodule.domain.product.Product;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = "select p from Product p where p.productId in (:productIds)")
	List<Product> findAllByIdForUpdate(@Param("productIds") Collection<Long> productIds);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = "select p from Product p where p.productId = (:productId)")
	Product findByIdForUpdate(@Param("productId") Long productId);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query(value = "select p from Product p where p.productId in (:productIds)")
	List<Product> findByIdForShared(@Param("productIds") Collection<Long> productIds);
}
