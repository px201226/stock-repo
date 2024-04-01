package com.productmodule.sprinboot.facade;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.commonmodule.dto.product.DecreaseStockCommand;
import com.commonmodule.dto.product.ProductViewModel;
import com.productmodule.domain.product.Product;
import com.productmodule.domain.product.ProductCommand.ProductCreateCommand;
import com.productmodule.domain.product.ProductCommand.ProductUpdateCommand;
import com.productmodule.domain.product.ProductService;
import com.productmodule.sprinboot.mapper.ProductMapper;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacade {

	private final ProductService productService;
	private final ProductMapper productMapper;

	public ProductViewModel getProduct(final Long id) {
		var product = productService.findById(id);
		return productMapper.toProductViewModel(product);
	}

	public Page<ProductViewModel> getProductsByPagination(final Pageable pageable) {
		var productPage = productService.getProductsByPagination(pageable);
		return productPage.map(productMapper::toProductViewModel);
	}

	public ProductViewModel addProduct(final ProductCreateCommand command) {
		Product product = productService.addProduct(command);
		return productMapper.toProductViewModel(product);
	}

	public ProductViewModel updateProduct(final Long id, final ProductUpdateCommand command) {
		Product product = productService.updateProduct(id, command);
		return productMapper.toProductViewModel(product);
	}

	public void deleteProduct(final Long id) {
		productService.deleteProduct(id);
	}

	public List<ProductViewModel> getProducts(Collection<Long> productIds) {
		List<Product> products = productService.getProducts(productIds);
		return productMapper.toProductViewModelList(products);
	}


	public void adjustStock(AdjustStockCommand command) {
		productService.adjustStock(command);
	}
}
