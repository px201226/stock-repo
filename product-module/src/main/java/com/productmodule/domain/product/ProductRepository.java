package com.productmodule.domain.product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

	Optional<Product> findById(Long id);

	Page<Product> getProductsByPagination(Pageable pageable);

	Product save(Product product);

	void delete(Product product);

	List<Product> findAllById(Collection<Long> productIds);

	List<Product> findAllByIdLock(Collection<Long> productIds);
}
