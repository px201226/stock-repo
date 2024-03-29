package com.productmodule.domain.product;

import com.productmodule.domain.product.ProductCommand.ProductCreateCommand;
import com.productmodule.domain.product.ProductCommand.ProductUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public Product findById(final Long id) {
		return productRepository.findById(id).orElseThrow();
	}

	public Page<Product> getProductsByPagination(final Pageable pageable) {
		return productRepository.getProductsByPagination(pageable);
	}

	public Product addProduct(final ProductCreateCommand command) {
		Product product = Product.create(command);
		return productRepository.save(product);
	}

	@Transactional
	public Product updateProduct(final Long id, final ProductUpdateCommand command) {
		var product = this.findById(id);
		product.update(command);
		return product;
	}

	@Transactional
	public void deleteProduct(Long id) {
		var product = this.findById(id);
		productRepository.delete(product);
	}

}
