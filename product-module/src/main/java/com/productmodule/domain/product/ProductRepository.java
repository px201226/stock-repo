package com.productmodule.domain.product;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

	Optional<Product> findById(Long id);

	Page<Product> getProductsByPagination(Pageable pageable);

	Product save(Product product);

	void delete(Product product);
}
