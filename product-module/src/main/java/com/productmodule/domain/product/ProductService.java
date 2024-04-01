package com.productmodule.domain.product;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.productmodule.domain.exception.BusinessException;
import com.productmodule.domain.exception.Error;
import com.productmodule.domain.product.ProductCommand.ProductCreateCommand;
import com.productmodule.domain.product.ProductCommand.ProductUpdateCommand;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		return productRepository.findById(id).orElseThrow(() -> BusinessException.of(Error.NOT_FOUND_ENTITY, Map.of("productId", id)));
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
		var product = productRepository.findByIdForUpdate(id);
		product.update(command);
		return product;
	}

	@Transactional
	public void deleteProduct(Long id) {
		var product = this.findById(id);
		productRepository.delete(product);
	}

	public List<Product> getProducts(Collection<Long> productIds) {
		return productRepository.findAllByIdForShared(productIds);
	}


	@Transactional
	public void adjustStock(AdjustStockCommand command) {

		Set<Long> soldOutProductId = new HashSet<>();

		var products = productRepository.findAllByIdForUpdate(command.productIdToAdjustStock().keySet());

		for (Product product : products) {
			var adjustQty = command.productIdToAdjustStock().getOrDefault(product.getProductId(), 0L);
			product.adjustStockQuantity(adjustQty);

			if (product.getStockQuantity() < 0) {
				soldOutProductId.add(product.getProductId());
			}
		}

		if (!soldOutProductId.isEmpty()) {
			throw BusinessException.of(Error.SOLDOUT_STOCK, Map.of("productId", soldOutProductId));
		}
	}

}
