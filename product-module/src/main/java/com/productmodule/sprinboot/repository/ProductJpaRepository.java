package com.productmodule.sprinboot.repository;

import com.productmodule.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

	Page<Product> getProductsByPagination(Pageable pageable);
}
