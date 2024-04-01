package com.productmodule.sprinboot.repository;

import com.productmodule.domain.product.Product;
import com.productmodule.domain.product.ProductRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;


	@Override public Optional<Product> findById(Long id) {
		return productJpaRepository.findById(id);
	}

	@Override public Page<Product> getProductsByPagination(Pageable pageable) {
		return productJpaRepository.findAll(pageable);
	}

	@Override public Product save(Product product) {
		return productJpaRepository.save(product);
	}

	@Override public void delete(Product product) {
		productJpaRepository.delete(product);
	}

	@Override public List<Product> findAllById(Collection<Long> productIds) {
		return productJpaRepository.findAllById(productIds);
	}

	@Override public List<Product> findAllByIdForShared(Collection<Long> productIds) {
		return productJpaRepository.findByIdForShared(productIds);
	}

	@Override public List<Product> findAllByIdForUpdate(Collection<Long> productIds) {
		return productJpaRepository.findAllByIdForUpdate(productIds);
	}

	@Override public Product findByIdForUpdate(Long productId) {
		return productJpaRepository.findByIdForUpdate(productId);
	}
}
