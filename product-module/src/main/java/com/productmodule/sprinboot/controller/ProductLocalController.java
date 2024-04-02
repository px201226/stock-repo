package com.productmodule.sprinboot.controller;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.commonmodule.dto.product.DecreaseStockCommand;
import com.commonmodule.dto.product.ProductViewModel;
import com.productmodule.domain.idempotency.IdempotencyService;
import com.productmodule.domain.product.Product;
import com.productmodule.domain.product.ProductCommand.ProductCreateCommand;
import com.productmodule.domain.product.ProductCommand.ProductUpdateCommand;
import com.productmodule.domain.product.ProductRepository;
import com.productmodule.sprinboot.facade.ProductFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/local")
public class ProductLocalController {

	private final ProductFacade productFacade;
	private final IdempotencyService idempotencyService;


	private final ProductRepository productRepository;

	@GetMapping("/getProducts/{productIds}")
	public ResponseEntity<List<ProductViewModel>> getProducts(@PathVariable Set<Long> productIds) {
		var productViewModel = productFacade.getProducts(productIds);
		return ResponseEntity.ok(productViewModel);
	}

	@PutMapping("/adjustStock")
	public ResponseEntity<Void> adjustStock(
			@RequestHeader(value = "Idempotency-Key", defaultValue = "") String idempotencyKey,
			@Valid @RequestBody AdjustStockCommand command) {

		var idempotency = idempotencyService.get(idempotencyKey, String.class);
		if (idempotency.isPresent()) {
			return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
		}

		productFacade.adjustStock(command);

		idempotencyService.put(idempotencyKey, "");

		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

	@GetMapping("/lock/{productIds}")
	@Transactional
	public void lock(@PathVariable Set<Long> productIds, @RequestParam("time") Long timeSec) throws InterruptedException {
		var allByIdForUpdate = productRepository.findAllByIdForUpdate(productIds);

		Thread.sleep(timeSec * 1000L);
	}

}