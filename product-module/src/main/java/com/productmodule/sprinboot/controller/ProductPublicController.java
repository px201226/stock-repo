package com.productmodule.sprinboot.controller;

import com.commonmodule.dto.product.ProductViewModel;
import com.productmodule.domain.product.ProductCommand.ProductCreateCommand;
import com.productmodule.domain.product.ProductCommand.ProductUpdateCommand;
import com.productmodule.sprinboot.facade.ProductFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductPublicController {

	private final ProductFacade productFacade;

	@GetMapping("/getProduct/{id}")
	public ResponseEntity<ProductViewModel> getProduct(@Valid @NotNull @PathVariable Long id) {
		var productViewModel = productFacade.getProduct(id);
		return ResponseEntity.ok(productViewModel);
	}

	@GetMapping("/getProductsByPagination")
	public ResponseEntity<Page<ProductViewModel>> getProductsByPagination(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		var pageable = PageRequest.of(page, size, Sort.by("productId").descending());
		var productViewModelPage = productFacade.getProductsByPagination(pageable);

		return ResponseEntity.ok(productViewModelPage);
	}

	@PostMapping("/addProduct")
	public ResponseEntity<ProductViewModel> addProduct(@Valid @RequestBody ProductCreateCommand productCreateCommand) {
		var productViewModel = productFacade.addProduct(productCreateCommand);
		return ResponseEntity.ok(productViewModel);
	}

	@PutMapping("/updateProduct/{id}")
	public ResponseEntity<ProductViewModel> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateCommand productUpdateCommand) {
		var productViewModel = productFacade.updateProduct(id, productUpdateCommand);
		return ResponseEntity.ok(productViewModel);
	}

	@DeleteMapping("/deleteProduct/{id}")
	public ResponseEntity<Void> deleteProduct(@Valid @NotNull @PathVariable Long id) {
		productFacade.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}